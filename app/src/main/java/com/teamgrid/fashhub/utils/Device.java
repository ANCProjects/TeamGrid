package com.teamgrid.fashhub.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.appcompat.BuildConfig;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import java.util.HashMap;

public class Device {
    private static ProgressDialog progressDialog;

    public  static boolean isConnected(Context context) {
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }

    public static void hideKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (context instanceof Activity) {
            View view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void showProgressDialog(Context context, boolean isDismissible, String msg) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setProgressStyle(0);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(isDismissible);
        progressDialog.show();
    }

    public static void messageProgressDialog(String msg){
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.setMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isProgressDialogShowing() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return false;
        }
        return true;
    }

    public static void dismissProgressDialog(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Device.dismissProgressDialog();
            }
        });
    }

    public static void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlertDialog showDialog(final Context context, String title, String content) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(context.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create();
    }

    public static Snackbar showSnackbar(final Context context, String content, Boolean indefinite) {
        Activity activity = (Activity) context;
        int snackbarTime = indefinite ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG;
        return Snackbar.make(activity.findViewById(android.R.id.content), content, snackbarTime);
    }

    public static void showNotification(Context context, String title, String content, int smallIconResourceId) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, context.getPackageManager().getLaunchIntentForPackage(getAppPackageName(context)), PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setSmallIcon(smallIconResourceId)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    public static String getAppName(Context context) {
        return context.getString(context.getApplicationInfo().labelRes);
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public static String getFileType(String string) {
        String extension = getExtensionFromString(string);
        if (extension != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return null;
    }

    public static String getExtensionFromString(String string) {
        if (string != null) {
            int index = string.lastIndexOf(".");
            if (index != 0) {
                return string.substring(index + 1);
            }
        }
        return null;
    }

    public static int dpToPixels(int dp, Resources resources) {
        return dpToPixels((float) dp, resources);
    }

    public static int dpToPixels(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(1, dp, resources.getDisplayMetrics());
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
           return BuildConfig.FLAVOR;
        }
        char first = s.charAt(0);
        return !Character.isUpperCase(first) ? Character.toUpperCase(first) + s.substring(1) : s;
    }

    public static float getScreenDensity(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.density;
    }

    public static Bitmap scaleLargeImage(String url) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);
        options.inSampleSize = calculateInSampleSize(options, 60, 60);
        return bitmap;
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() == radius && bmp.getHeight() == radius) {
            sbmp = bmp;
        } else {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        }
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(((float) (sbmp.getWidth() / 2)) + 0.7f, ((float) (sbmp.getHeight() / 2)) + 0.7f, ((float) (sbmp.getWidth() / 2)) + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }
}