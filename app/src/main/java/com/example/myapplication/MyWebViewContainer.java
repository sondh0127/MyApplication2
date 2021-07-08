package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MyWebViewContainer extends FrameLayout {
    boolean _isPrevent = false;
    public MyWebViewContainer(Context context) {
        super(context);
    }

    public MyWebViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("MyWebViewContainer", "_isPrevent" + _isPrevent);
        if (!_isPrevent) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
//        return false;
    }

    public void setPreventTouch(boolean isPrevent) {
        Log.d("WebView", "isPrevent" + isPrevent);

        _isPrevent = isPrevent;
    }



}
