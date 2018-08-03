package com.zh.zrz.tinyviewmodel;

import android.content.Context;
import android.view.View;

import java.lang.reflect.InvocationTargetException;

abstract public class TinyViewModel {
    protected String token;
    protected Class host;

    abstract public View getView(Context context);

    abstract public int getLayoutId();

    abstract public void initView(View v);

    final public void init(String token, Class host) {
        this.token = token;
        this.host = host;
    }

    //用于任意时候在主线程请求host创建一个自己的res里的视图。
    protected View createView(Context context, int layoutId) {
        try {
            return (View) host.getMethod("createView", int.class, Context.class, String.class).invoke(host, layoutId, context, token);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
