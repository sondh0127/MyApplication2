package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import java.util.Iterator;
import java.util.Stack;

public class MyWebView extends WebView {

    MyWebView mMyWebView;

    boolean _isPrevent = true;
    RelativeLayout mainLayout;
    View webParent;

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

    public void setConfig(RelativeLayout _mainLayout, View _webParent) {
        mainLayout = _mainLayout;
        webParent = _webParent;
    }

    Stack<MotionEvent> events = new Stack<MotionEvent>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("WebView", "onTouchEvent " + event.getAction());
        MotionEvent motionEvent = MotionEvent.obtain(event);
        if (motionEvent.getAction() == 0 || !events.empty()) {
            events.push(motionEvent);
        }
        if (motionEvent.getAction() == 1) {
            events.clear();
        }
        Log.d("WebView", "events.length " + events.stream().count());

        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            View child = mainLayout.getChildAt(i);
            if (child != webParent) {
                if (!_isPrevent) {
                    Iterator<MotionEvent> eventsIterator = events.iterator();
                    while (eventsIterator.hasNext()) {
                        MotionEvent ev = eventsIterator.next();
                        child.dispatchTouchEvent(ev);
                        Log.d("WebView", "ev.getAction " + ev.getAction());

                    }
                    events.clear();
                    child.dispatchTouchEvent(motionEvent);
                }
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                setPreventTouch(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setPreventTouch(true);
                break;
            default:
                break;
        }



        return super.onTouchEvent(event);

    }


}
