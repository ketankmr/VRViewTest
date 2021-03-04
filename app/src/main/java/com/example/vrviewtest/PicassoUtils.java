package com.example.vrviewtest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Use this to get at the Picasso object.
 */

public class PicassoUtils {
    private static Picasso picasso = null;
    private static OkHttpClient httpClient;

    public static synchronized Picasso getPicasso() {
        if(picasso != null) {
            return picasso;
        }

        Cache cache = new Cache(VrApplication.getContext().getCacheDir(), 64 * 1024 * 1024);

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .cache(cache)
                .build();
        picasso = new Picasso.Builder(VrApplication.getContext())
                .addRequestHandler(new AppIconRequestHandler(VrApplication.getContext()))
                .downloader(new OkHttp3Downloader(httpClient))
                .memoryCache(new LruCache(50000000))
                .build();
        return picasso;
    }

    public static Uri getAppIconUri(String packageName) {
        return AppIconRequestHandler.getUri(packageName);
    }

    private static class AppIconRequestHandler extends RequestHandler {
        /** Uri scheme for app icons */
        public static final String SCHEME_APP_ICON = "app-icon";

        private PackageManager mPackageManager;

        public AppIconRequestHandler(Context context) {
            mPackageManager = context.getPackageManager();
        }

        /**
         * Create an Uri that can be handled by this RequestHandler based on the package name
         */
        public static Uri getUri(String packageName) {
            return Uri.fromParts(SCHEME_APP_ICON, packageName, null);
        }

        @Override
        public boolean canHandleRequest(Request data) {
            // only handle Uris matching our scheme
            return (SCHEME_APP_ICON.equals(data.uri.getScheme()));
        }

        @Override
        public Result load(Request request, int networkPolicy) throws IOException {
            String packageName = request.uri.getSchemeSpecificPart();
            Drawable drawable = null;

            Log.d("I36", "App-"+packageName);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                try {
                    drawable = mPackageManager.getApplicationBanner(packageName);
                    Log.d("I36","getApplicationBanner()");
                } catch (PackageManager.NameNotFoundException ignored) {
                    Log.d("I36","getApplicationBanner() Not Found");
                    return null;
                }
            }

            if(drawable == null || !(drawable instanceof BitmapDrawable)) {
                try {
                    drawable = mPackageManager.getApplicationIcon(packageName);
                    Log.d("I36","getApplicationIcon()");
                } catch (PackageManager.NameNotFoundException ignored) {
                    Log.d("I36","getApplicationIcon() Not Found");
                    return null;
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(drawable != null && drawable instanceof AdaptiveIconDrawable) {
                    //FIXME: Blend the bitmaps.
                    drawable = ((AdaptiveIconDrawable) drawable).getForeground();
                }
            }

            if(drawable == null || !(drawable instanceof BitmapDrawable)) {
                Log.d("I36","Bad drawable");
                if(!(drawable instanceof BitmapDrawable)) {
                    Log.d("I36","Drawable Type: " + drawable.getClass().getCanonicalName());
                }
                return null;
            }

            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if(bitmap!=null
                    && bitmap.isRecycled()){
                if(BuildConfig.DEBUG) {
                    Log.d("I36", "BITMAP_IS_RECYCLED - package: " + packageName);
                }
                return null;
            }
            return new Result(bitmap, Picasso.LoadedFrom.DISK);
        }
    }
}
