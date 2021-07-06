package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
    PlayerView pvMain;

    boolean isShow = false;
    private void startPlayingVideo(Context ctx, String CONTENT_URL, int playerID, String appNameRes) {

        pvMain = findViewById(playerID);


        TrackSelector trackSelectorDef = new DefaultTrackSelector();

        SimpleExoPlayer absPlayerInternal = ExoPlayerFactory.newSimpleInstance(ctx, trackSelectorDef);

        String userAgent = Util.getUserAgent(ctx, ctx.getString(R.string.app_name));

        DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(ctx, userAgent);
        Uri uriOfContentUrl = Uri.parse(CONTENT_URL);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);

        absPlayerInternal.prepare(mediaSource);
        absPlayerInternal.setPlayWhenReady(true);

        pvMain.setPlayer(absPlayerInternal);

    }

    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean newIsShow) {
        isShow = newIsShow;
    }

    public class JavaScriptInterface {
        private Activity activity;

        public JavaScriptInterface(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void on(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.webview).setVisibility(View.VISIBLE);
                }
            });
        }
        @JavascriptInterface
        public void off(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    findViewById(R.id.webview).setVisibility(View.INVISIBLE);
                }
            });
        }
        @JavascriptInterface
        public void setIsShowTrue(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setIsShow(true);

                }
            });
        }
        @JavascriptInterface
        public void setIsShowFalse(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setIsShow(false);
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://dev-livestream.gviet.vn/ilp-statics/android-interactive_test.html");
        webview.setBackgroundColor(Color.TRANSPARENT);
        View webParent = findViewById(R.id.web_parent);
        webview.setVisibility(View.VISIBLE);
        findViewById(R.id.ena).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webParent.setVisibility(View.VISIBLE);
                System.out.println("enable");
//                webParent.setEnabled(true);
            }
        });
        findViewById(R.id.dis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webParent.setVisibility(View.INVISIBLE);
                System.out.println("disable");
//                webParent.setEnabled(false);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d("WebView", consoleMessage.message());
                return true;
            }
        });
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        webview.addJavascriptInterface(jsInterface, "parentApp");
        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MotionEvent motionEvent = MotionEvent.obtain(event);
                System.out.println(getIsShow());
                //these 3 lines is for bug when target and source has different on screen positions
//                webview.getLocationOnScreen(posTemp1);
//                targetView.getLocationOnScreen(posTemp2);
//                motionEvent.offsetLocation(posTemp1[0]-posTemp2[0], posTemp1[1]-posTemp2[1]);
                if (!getIsShow()) {
                    pvMain.dispatchTouchEvent(motionEvent);
                }


                return false; //true if we want sourceView to not handle it
            }
        });

        String CONTENT_URL = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4";
        int playerID = R.id.videoFullScreenPlayer;
        int appNameStringRes = R.string.app_name;
        startPlayingVideo(this, CONTENT_URL, playerID, getString(R.string.app_name));
    }
}