package com.aospstudio.android.apps.mediabook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aospstudio.android.apps.mediabook.javascript.WebAppInterface;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class db extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final Context context = this;
    private View mCustomView;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private static long back_pressed;
    private static final int TIME_DELAY = 1200;
    private static final String TAG = db.class.getSimpleName();
    public static final int REQUEST_CODE_LOLIPOP = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    MediaPlayer mediaplayer;
    private final static String RADIO_STATION_URL = "";
    private Button buttonPlay;
    private Button buttonStopPlay;
    private WebView meraWeb;
    final String password = "password";
    final String username = "username";
    private CoordinatorLayout coordinatorLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        de.show(this);
        ad.show(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.meraBassBooster();
        mediaplayer.start();
        meraAutoMusicPlayer();

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 0);

        PowerManager powerManager = (PowerManager)
                getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            powerManager.isPowerSaveMode();
        }

        if (savedInstanceState != null)
            ((WebView) findViewById(R.id.meraWeb)).restoreState(savedInstanceState);

        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);

        //getWindow().setStatusBarColor(ContextCompat.getColor(db.this,R.color.facebook));
        mySwipeRefreshLayout = this.findViewById(R.id.swipeContainer);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        meraWeb = findViewById(R.id.meraWeb);
        meraWeb.loadUrl("https://m.facebook.com");
        meraWeb.getSettings().setPluginState(WebSettings.PluginState.ON);
        meraWeb.getSettings().setJavaScriptEnabled(true);
        meraWeb.addJavascriptInterface(new WebAppInterface(this), "Android");
        meraWeb.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        meraWeb.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL + " Build/" + Build.ID + ") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.158 Mobile Safari/537.36 OPR/47.3.2249.130976");
        meraWeb.getSettings().setDefaultFontSize(14);
        meraWeb.getSettings().setUseWideViewPort(true);
        meraWeb.getSettings().setLoadWithOverviewMode(true);
        meraWeb.getSettings().setGeolocationEnabled(true);
        meraWeb.getSettings().setGeolocationDatabasePath(getFilesDir().getPath());
        meraWeb.getSettings().setMediaPlaybackRequiresUserGesture(true);
        meraWeb.getSettings().setSaveFormData(true);
        meraWeb.getSettings().setSavePassword(true);
        meraWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        meraWeb.getSettings().setAppCacheEnabled(true);
        meraWeb.getSettings().setDatabaseEnabled(true);
        meraWeb.getSettings().setDomStorageEnabled(true);
        meraWeb.getSettings().setAllowFileAccess(true);
        meraWeb.getSettings().setAllowContentAccess(true);
        meraWeb.getSettings().setAllowFileAccessFromFileURLs(true);
        meraWeb.getSettings().setAllowUniversalAccessFromFileURLs(true);
        meraWeb.getSettings().setSupportZoom(true);
        meraWeb.getSettings().setBuiltInZoomControls(true);
        meraWeb.getSettings().setDisplayZoomControls(false);
        meraWeb.getSettings().setLightTouchEnabled(true);
        meraWeb.setVerticalScrollBarEnabled(false);
        meraWeb.setHorizontalScrollBarEnabled(false);
        meraWeb.setFocusable(true);
        meraWeb.setFocusableInTouchMode(true);
        meraWeb.requestFocus(View.FOCUS_DOWN);
        meraWeb.requestFocusFromTouch();
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, getString(R.string.welcome_mediabook), Snackbar.LENGTH_LONG);
        snackbar.show();
        meraWeb.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                meraWeb.setVisibility(View.GONE);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(R.string.app_error_dialog_title);
                alertDialogBuilder
                        .setMessage(R.string.app_page_load_error)
                        .setCancelable(false)
                        .setNegativeButton(R.string.retry_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                meraWeb.reload();
                                meraWeb.setVisibility(View.VISIBLE);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) { {
                    if (url.endsWith(".mp3")) {
                        String temp1 = url.replace(".mp3", "");
                        playaudio(temp1);
                        return true;
                    }
                }
                frameLayout.setVisibility(View.VISIBLE);

                boolean value = true;
                String extension = MimeTypeMap.getFileExtensionFromUrl(url);
                if (extension != null) {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String mimeType = mime.getMimeTypeFromExtension(extension);
                    if (mimeType != null) {
                        if (mimeType.toLowerCase().contains("video")
                                || extension.toLowerCase().contains("mov")
                                || extension.toLowerCase().contains("mp3")) {
                            DownloadManager mdDownloadManager = (DownloadManager) db.this
                                    .getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(
                                    Uri.parse(url));
                            File destinationFile = new File(
                                    Environment.getExternalStorageDirectory(),
                                    getFileName(url));
                            request.setDescription(getString(R.string.app_download_file));
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationUri(Uri.fromFile(destinationFile));
                            mdDownloadManager.enqueue(request);
                            value = false;
                        }
                    }
                    if (value) {
                        view.loadUrl(url);
                    }
                }
                return value;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        meraWeb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                frameLayout.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);

                if (progress == 100) {
                    frameLayout.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, progress);
            }

            private Activity mActivity = db.this;
            private CustomViewCallback mCustomViewCallback;
            private FrameLayout mFullscreenContainer;
            private final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
                mFullscreenContainer = new FullscreenHolder(mActivity);
                mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
                decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
                mCustomView = view;
                setFullscreen(true);
                StyleableToast.makeText(getApplicationContext(), getString(R.string.close_fullscreen), Toast.LENGTH_LONG, R.style.MD2VideoToast).show();
                mCustomViewCallback = callback;
                super.onShowCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                if (mCustomView == null) {
                    return;
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                setFullscreen(false);
                FrameLayout decor = (FrameLayout) mActivity.getWindow().getDecorView();
                decor.removeView(mFullscreenContainer);
                mFullscreenContainer = null;
                mCustomView = null;
                mCustomViewCallback.onCustomViewHidden();
            }

            private void setFullscreen(boolean enabled) {
                Window win = mActivity.getWindow();
                WindowManager.LayoutParams winParams = win.getAttributes();
                final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
                if (enabled) {
                    winParams.flags |= bits;
                } else {
                    winParams.flags &= ~bits;
                    if (mCustomView != null) {
                        mCustomView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
                win.setAttributes(winParams);
            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"),
                        RESULT_CODE_ICE_CREAM);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"),
                        RESULT_CODE_ICE_CREAM);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"),
                        RESULT_CODE_ICE_CREAM);

            }

            //For Android5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

                startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);

                return true;
            }

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        meraWeb.reload();
                        meraWeb.clearCache(true);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            startPlaying();
            Context context = getApplicationContext();
            CharSequence text = "In Connection...";
            int duration = android.widget.Toast.LENGTH_LONG;
            android.widget.Toast toast = android.widget.Toast.makeText(context, text, duration);
            toast.show();
            Intent intent = new Intent(this, db.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Mediabook")
                    .setContentText("Music Player")
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build());

        }
        else if (v == buttonStopPlay) {
            stopPlaying();
            Intent svc = new Intent(this, cb.class);
            startService(svc);
        }
    }

    private void startPlaying() {
        if (mediaplayer.isPlaying()) {
            buttonPlay.setVisibility(View.INVISIBLE);
        }
        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);
        buttonPlay.setVisibility(View.INVISIBLE);
        mediaplayer.prepareAsync();

        mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaplayer.start();
            }
        });
    }

    private void stopPlaying() {
        if (mediaplayer.isPlaying()) {
            mediaplayer.stop();
            mediaplayer.release();
            meraAutoMusicPlayer();
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
        }

        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
        buttonPlay.setVisibility(View.VISIBLE);
    }

    private void meraAutoMusicPlayer() {
        mediaplayer = new MediaPlayer();
        try {
            mediaplayer.setDataSource(RADIO_STATION_URL);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaplayer.setLooping(true);
            }
        });

        mediaplayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }

    public void meraBassBooster() {
        BassBoost booster = new BassBoost(0,0);
        booster.setStrength((short) 1000);
        booster.setEnabled(true);
        mediaplayer.attachAuxEffect(booster.getId());
        mediaplayer.setAuxEffectSendLevel(1.0f);
        try {
            mediaplayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playaudio(String url){
        String s = url;
        int i=getResources().getIdentifier(s,"mp3",getPackageName());
        Log.v("name of file",""+s);
        Log.v("id of file",""+i);
        if(i!=0){
            MediaPlayer player = new MediaPlayer().create(getBaseContext(),i);;
            player.setVolume(100, 100);
            player.setLooping(true);
            player.start();
        }
    }

    public void pauseVideo() {
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(meraWeb, (Object[]) null);

        } catch(ClassNotFoundException cnfe) {

        } catch(NoSuchMethodException nsme) {

        } catch(InvocationTargetException ite) {

        } catch (IllegalAccessException iae) {

        }
    }

    @Override
    protected void onResume() {
        if (meraWeb != null) {
            meraWeb.onResume();
            meraWeb.resumeTimers();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (meraWeb != null) {
            meraWeb.onPause();
        }
        pauseVideo();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        meraWeb.destroy();
        meraWeb = null;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public String getFileName(String url) {
        String filenameWithoutExtension = "";
        filenameWithoutExtension = String.valueOf(System.currentTimeMillis()
                + ".mp4");
        return filenameWithoutExtension;
    }

    private static class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.facebook) {
            meraWeb.loadUrl("https://m.facebook.com");

        } else if (id == R.id.instagram) {
            meraWeb.loadUrl("https://instagram.com");

        } else if (id == R.id.soundcloud) {
            meraWeb.loadUrl("https://soundcloud.com");

        } else if (id == R.id.linkedin) {
            meraWeb.loadUrl("https://www.linkedin.com");

        } else if (id == R.id.flicker) {
            meraWeb.loadUrl("https://www.flickr.com");

        } else if (id == R.id.myspace) {
            meraWeb.loadUrl("https://myspace.com");

        } else if (id == R.id.twitter) {
            meraWeb.loadUrl("https://m.twitter.com");

        } else if (id == R.id.periscope) {
            meraWeb.loadUrl("https://www.pscp.tv");

        } else if (id == R.id.medium) {
            meraWeb.loadUrl("https://medium.com");

        } else if (id == R.id.tumblr) {
            meraWeb.loadUrl("https://www.tumblr.com");

        } else if (id == R.id.reddit) {
            meraWeb.loadUrl("https://www.reddit.com");

        } else if (id == R.id.twitch) {
            meraWeb.loadUrl("https://m.twitch.tv");
        } else if (id == R.id.vk) {
            meraWeb.loadUrl("https://m.vk.com");

        } else if (id == R.id.weibo) {
            meraWeb.loadUrl("https://m.weibo.cn");

        } else if (id == R.id.youtube) {
            meraWeb.loadUrl("https://m.youtube.com");
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.app_youtube_opened_title), Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (id == R.id.settings) {
            Intent open = new Intent(getApplicationContext(), cc.class);
            startActivity(open);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_CODE_ICE_CREAM:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
                break;
            case REQUEST_CODE_LOLIPOP:
                Uri[] results = null;
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_warning_title)
                    .setMessage(R.string.app_exit_title)
                    .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            meraWeb.clearCache(true);
                            moveTaskToBack(true);
                        }
                    })
                    .setNegativeButton(R.string.no_btn, null)
                    .show();
        } else {
            if (meraWeb.canGoBack()) {
                meraWeb.goBack();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_warning_title)
                        .setMessage(R.string.app_exit_title)
                        .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                meraWeb.clearCache(true);
                                moveTaskToBack(true);
                            }
                        })
                        .setNegativeButton(R.string.no_btn, null)
                        .show();
            }
        }
        back_pressed = System.currentTimeMillis();
    }
}
