package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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
    MyWebView webview;
    MyWebViewContainer webParent;
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
        public void on() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.webview).setVisibility(View.VISIBLE);
                }
            });
        }

        @JavascriptInterface
        public void off() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    findViewById(R.id.webview).setVisibility(View.INVISIBLE);
                }
            });
        }

        @JavascriptInterface
        public void setWebViewPrevent() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("WebView", "setWebViewPrevent");
                    webParent.setPreventTouch(false);
                }
            });

        }

        @JavascriptInterface
        public void setWebViewPreventTrue() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("WebView", "setWebViewPrevent");
                    webParent.setPreventTouch(true);
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (MyWebView) findViewById(R.id.webview);

        RelativeLayout mainLayout = findViewById(R.id.rl_video);
        webview.setConfig(mainLayout);

//        WebView webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("https://dev-livestream.gviet.vn/ilp-statics/android-interactive_test.html");
        webview.setBackgroundColor(Color.TRANSPARENT);
        webParent = (MyWebViewContainer) findViewById(R.id.web_parent);
        webview.setVisibility(View.VISIBLE);
        webview.setFocusableInTouchMode(false);
        findViewById(R.id.ena).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webParent.setVisibility(View.VISIBLE);
//                webParent.setEnabled(true);
            }
        });
        findViewById(R.id.dis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webParent.setVisibility(View.INVISIBLE);
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
        webParent.requestDisallowInterceptTouchEvent(true);

        webview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MotionEvent motionEvent = MotionEvent.obtain(event);
                int[] posTemp1 = new int[2];
                int[] posTemp2 = new int[2];
                webview.getLocationOnScreen(posTemp1);
                pvMain.getLocationOnScreen(posTemp2);
                motionEvent.offsetLocation(posTemp1[0]-posTemp2[0], posTemp1[1]-posTemp2[1]);

//                if (motionEvent.getSource() == 4098) {
//                    webview.requestDisallowInterceptTouchEvent(false);
//                }


//                Log.d("WebView", "onTouch_isPrevent" + webview._isPrevent);

//                if (webview._isPrevent) {
//                    mainLayout.dispatchTouchEvent(motionEvent);
//                }
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        setIsShow(false);
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        setIsShow(false);
//                        break;
//                    default:
//                        break;
//                }


                return false;
            }
        });

        String CONTENT_URL = "https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_480_1_5MG.mp4";
        int playerID = R.id.videoFullScreenPlayer;
        int appNameStringRes = R.string.app_name;
        startPlayingVideo(this, CONTENT_URL, playerID, getString(R.string.app_name));
    }
}