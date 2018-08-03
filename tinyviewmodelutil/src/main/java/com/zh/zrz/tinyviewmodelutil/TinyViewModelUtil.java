package com.zh.zrz.tinyviewmodelutil;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

public class TinyViewModelUtil {
    private static Map<String, Resources> mCachedResources = new HashMap<>();
    private static Map<String, AssetManager> mCachedAssetManager = new HashMap<>();
    private static File dexOutputDir;
    private static boolean isInited = false;


    public static View load(String apkPath, String smallViewModelClass, Context context) {
        if (!isInited) {
            init(context);
            isInited = true;
        }
        prepareResource(apkPath, context);
        return loadSmallViewModel(apkPath, smallViewModelClass, context);
    }

    private static void init(Context context) {
        dexOutputDir = context.getDir("dex", 0);
    }

    @SuppressWarnings("unchecked")
    private static View loadSmallViewModel(String apkPath, String viewModelClass, Context context) {
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, dexOutputDir.getAbsolutePath(), "data/local/tmp/natives/", ClassLoader.getSystemClassLoader());
        try {
            Class c = dexClassLoader.loadClass(viewModelClass);
            Object instance = c.newInstance();
            View generatedView = (View) c.getMethod("getView", Context.class).invoke(instance, context);
            if (generatedView == null) {
                int layoutId = (int) c.getMethod("getLayoutId").invoke(instance);
                Resources res = mCachedResources.get(apkPath);
                if (res != null) {
                    generatedView = LayoutInflater.from(context).inflate(res.getLayout(layoutId), null);
                }
            }
            c.getMethod("init", String.class, Class.class).invoke(instance, apkPath, Host.class);
            c.getMethod("initView", View.class).invoke(instance, generatedView);
            return generatedView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void prepareResource(String apkPath, Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            mCachedAssetManager.put(apkPath, assetManager);
            Resources superRes = context.getResources();
            mCachedResources.put(apkPath, new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showClasses(String path) {
        try {
            DexFile dexFile = new DexFile(path);
            Enumeration<String> enumeration = dexFile.entries();
            while (enumeration.hasMoreElements()) {
                System.out.println("=====    " + enumeration.nextElement());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Host {
        public static View createView(int layoutId, Context context, String token) {
            Resources res = mCachedResources.get(token);
            if (res != null) {
                return LayoutInflater.from(context).inflate(res.getLayout(layoutId), null);
            }
            return null;
        }
    }
}
