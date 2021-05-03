package com.hearts.customer;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.view.WindowManager.LayoutParams.*;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.hearts.customer.R.color.hearts_back;

public class MainActivity extends AppCompatActivity{
    private TextView back;
    private WebView webView;
    private ImageView noInternet;
    private ImageView splashMain;
    String url ="https://hearts.com.bd";
    public String currentUrl;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    public final static int ALL_PERMISSIONS_RESULT = 102;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean canGetLocation = true;
    String type="";
    private final static int file_perm = 2;





    private static final String TAG = MainActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;
    private boolean doubleBackToExitPressedOnce= false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }

        checkMediaPer();
    }







    @SuppressLint({"SetJavaScriptEnabled", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.P)



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);


        noInternet = findViewById(R.id.noInternetImg);
        back = findViewById(R.id.btn_back);

        splashMain = findViewById(R.id.splashMain);






        back.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           webView.loadUrl(url);
                                         back.setVisibility(View.GONE);

                                       }
                                   }


        );





        if (Build.VERSION.SDK_INT < 18) {
            //speed webview
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        }









        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().getAllowFileAccessFromFileURLs();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new myWebViewClient()
                                 {

                                     @Override
                                     public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                                         currentUrl = webView.getUrl();
//                                         Log.d("urlCheck",currentUrl);
//                                         URL myUrl = null;
//                                         try {
//                                             myUrl = new URL(currentUrl);
//                                         } catch (MalformedURLException e) {
//                                             e.printStackTrace();
//                                         }


                                         if (currentUrl.contains("hearts") && !url.contains("hearts")){

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                    builder.setIcon(R.drawable.ic_dialog_alert);
                                                    builder.setTitle("Will you go to this url?");
                                                    builder.setPositiveButton("Yes",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    back.setVisibility(View.VISIBLE);




                                                                        view.loadUrl(url);

                                                                        //flag=false;


//




                                                                    // it will load in app
                                                //                                                             Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); //it will load in browser
                                                //                                                             startActivity(intent);
                                                                    //flag=true;

                                                                }
                                                            });
                                                    builder.setNeutralButton("No", new DialogInterface.OnClickListener()
                                                    {
                                                        public void onClick(DialogInterface dialog, int which)
                                                        {
                                                            webView.stopLoading();
                                                        }
                                                    });
                                                    AlertDialog alert = builder.create();

                                                    alert.show();





                                                }else {

                                                    webView.loadUrl(url);
                                                }


        /*
            here you have to include the your keywords instead of tags [hardcoded string]
         */









//
//                                         if (url.contains("hearts.com")) {
//                                             webView.loadUrl(url);
//                                         } else {
//                                             webView.stopLoading();
//                                              Toast.makeText(getApplicationContext(), "You Will no
//                                              t be redirected", Toast.LENGTH_LONG).show();
//
//
                                         return true;


                                     }


                                     //for removing div from web page
//                                     @Override
//                                     public void onPageFinished(WebView view, String url)
//                                     {
//                                         webView.loadUrl("javascript:(function() { " +
//                                                 "document.getElementById('a')[0].style.display='none'; " +
//                                                 "})()");
//                                     }


                                 }
                                 );
        webView.setWebChromeClient(new MyChrome());
        webView.setWebChromeClient(new WebChromeClient()


                                   {
                                       private View mCustomView;
                                       private WebChromeClient.CustomViewCallback mCustomViewCallback;
                                       protected FrameLayout mFullscreenContainer;
                                       private int mOriginalOrientation;
                                       private int mOriginalSystemUiVisibility;

                                       public void onGeolocationPermissionsShowPrompt(String origin,
                                                                                      GeolocationPermissions.Callback callback) {
                                           callback.invoke(origin, true, false);
                                       }

                                       public boolean onShowFileChooser(
                                               WebView webView, ValueCallback<Uri[]> filePathCallback,
                                               WebChromeClient.FileChooserParams fileChooserParams){
                                           if(mUMA != null){
                                               mUMA.onReceiveValue(null);
                                           }
                                           mUMA = filePathCallback;
                                           Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                           if(takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null){
                                               File photoFile = null;
                                               try{
                                                   photoFile = createImageFile();
                                                   takePictureIntent.putExtra("PhotoPath", mCM);
                                               }catch(IOException ex){
                                                   Log.e(TAG, "Image file creation failed", ex);
                                               }
                                               if(photoFile != null){
                                                   mCM = "file:" + photoFile.getAbsolutePath();
                                                   takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                               }else{
                                                   takePictureIntent = null;
                                               }
                                           }
                                           Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                           contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                           contentSelectionIntent.setType("*/*");
                                           Intent[] intentArray;
                                           if(takePictureIntent != null){
                                               intentArray = new Intent[]{takePictureIntent};
                                           }else{
                                               intentArray = new Intent[0];
                                           }

                                           Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                                           chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                                           chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                                           chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                                           startActivityForResult(chooserIntent, FCR);
                                           return true;
                                       }



                                       public Bitmap getDefaultVideoPoster()
                                       {
                                           if (mCustomView == null) {
                                               return null;
                                           }
                                           return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
                                       }

                                       public void onHideCustomView()
                                       {
                                           ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
                                           this.mCustomView = null;
                                           getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
                                           setRequestedOrientation(this.mOriginalOrientation);
                                           this.mCustomViewCallback.onCustomViewHidden();
                                           this.mCustomViewCallback = null;
                                       }

                                       public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
                                       {
                                           if (this.mCustomView != null)
                                           {
                                               onHideCustomView();
                                               return;
                                           }
                                           this.mCustomView = paramView;
                                           this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                                           this.mOriginalOrientation = getRequestedOrientation();
                                           this.mCustomViewCallback = paramCustomViewCallback;
                                           ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
                                           getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                                       }


                                   }



        );



       webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                if(!check_permission(2)){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, file_perm);
                }else {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.setMimeType(mimeType);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription(getString(R.string.dl_downloading));
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    assert dm != null;
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), getString(R.string.dl_downloading2), Toast.LENGTH_LONG).show();
                }
            }
        });




        checkLocationPer();
        loadWebsite();







        if(savedInstanceState==null && url.contains("movies/watch")) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                   loadWebsite();
                }
            });
        }


    }


    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {

            if (currentUrl.contains("hearts")) {
                back.setVisibility(View.GONE);
            } else {
                back.setVisibility(View.VISIBLE);

            }


            webView.goBack();
        }else if (this.doubleBackToExitPressedOnce) {
            new AlertDialog.Builder(this).setTitle("Really Exit?").setMessage("Are you sure you want to exit?")

                    .setNegativeButton(android.R.string.cancel, null)

                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.super.onBackPressed();
                }
            }).create().show();
        }


        else {

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit",Toast.LENGTH_LONG ).show();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    boolean unused = MainActivity.this.doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }



//        if (this.webView.canGoBack()) {
//            if (this.currentUrl.contains("hearts")) {
//                this.back.setVisibility(View.VISIBLE);
//            } else {
//                this.back.setVisibility(0);
//            }
//            this.webView.goBack();
//        } else if (this.doubleBackToExitPressedOnce) {
//            new AlertDialog.Builder(this).setTitle("Really Exit?").setMessage("Are you sure you want to exit?").setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    MainActivity.super.onBackPressed();
//                }
//            }).create().show();
//        } else {
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click BACK again to exit", 0).show();
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    boolean unused = MainActivity.this.doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
//        }


    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {

            splashMain.setVisibility(View.GONE);

            webView.setVisibility(View.VISIBLE);

        super.onPageFinished(view, url);
        }
    }





    public void checkLocationPer() {

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
                //Log.d(TAG, "Permission requests");
                canGetLocation = false;
            }
        }
    }


    public void checkMediaPer() {
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);

        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
                //Log.d(TAG, "Permission requests");
                canGetLocation = false;
            }
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                try {
                    //Log.d(TAG, "onRequestPermissionsResult");
                    for (String perms : permissionsToRequest) {
                        if (!hasPermission(perms)) {
                            permissionsRejected.add(perms);
                        }
                    }

                    if (permissionsRejected.size() > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                                showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(permissionsRejected.toArray(
                                                            new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
                break;
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }
                if (!canUseExternalStorage) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cannot_use_save_permission), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("cancel", null)
                .create()
                .show();
    }





    public boolean check_permission(int permission){
        switch(permission){
            case 1:
                return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            case 2:
                return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            case 3:
                return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        }
        return false;
    }
    public void loadWebsite(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected()|| mobile.isConnected()){
            webView.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.INVISIBLE);
            webView.loadUrl(url);

        }else{
            webView.setVisibility(View.INVISIBLE);
            noInternet.setVisibility(View.VISIBLE);
            splashMain.setVisibility(View.INVISIBLE);


        }

    }



    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);

    }


    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
    }
    public static class RotatedVerticalFrameLayout extends FrameLayout{

        public RotatedVerticalFrameLayout(Context context, AttributeSet attrs){
            super(context, attrs);
        }

        @SuppressWarnings("SuspiciousNameCombination")
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
            int w = getWidth();
            int h = getHeight();

            setRotation(90.0f);
            setTranslationX((h - w) >> 1);
            setTranslationY((w - h) >> 1);

            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.height = w;
            lp.width = h;
            requestLayout();
        }
    }



}










