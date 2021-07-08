package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class MyWebView extends WebView {

    MyWebView mMyWebView;

    boolean _isPrevent = false;
    RelativeLayout mainLayout;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d("WebView", "_isPrevent" + _isPrevent);
//        if (_isPrevent) {
//            return false;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.d("WebView", "_isPrevent" + _isPrevent);
//        if (_isPrevent) {
//            return false;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    public void setPreventTouch(boolean isPrevent) {
//        Log.d("WebView", "isPrevent" + isPrevent);

        _isPrevent = isPrevent;
    }

    public void setConfig(RelativeLayout _mainLayout) {
        mainLayout = _mainLayout;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("WebView", "_isPrevent" + _isPrevent);
////
////        if (_isPrevent) {
////            return false;
////        }
//
//        return super.onTouchEvent(event);
//
//    }


}
