package com.oman.allinone.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * User: khiemvx
 * Date: 6/3/14
 */
public class CommonUtils
{
    public static void processPrintFile(Context context, File file)
    {
        LogUtils.errorLog("File info", "===> File size: " + file.length());
        Intent target = new Intent(Intent.ACTION_SEND);
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        target.setType("application/pdf");
        target.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        try
        {
            context.startActivity(target);
        }
        catch (ActivityNotFoundException e)
        {
            LogUtils.errorLog("Utils", "processPrintFile", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void showKeyboard(Activity activity, EditText editText)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void hideKeyboard(Activity activity)
    {
        try
        {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), 0);
        }
        catch (Exception e)
        {
            LogUtils.errorLog(CommonUtils.class, "can't hide keyboard " + activity + " message: " + e.getMessage());
        }
    }


    public static void resizePopupHeight(View view, ListView listView)
    {
        if (null != listView.getAdapter() && listView.getAdapter().getCount() <= 5)
        {
            listView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public static void disableAllView(Activity activity, int layoutId)
    {
        RelativeLayout layout = (RelativeLayout) activity.findViewById(layoutId);
        for (int i = 0; i < layout.getChildCount(); i++)
        {
            View child = layout.getChildAt(i);
            child.setEnabled(false);
        }
    }

    public static void enableAllView(Activity activity, int layoutId)
    {
        RelativeLayout layout = (RelativeLayout) activity.findViewById(layoutId);
        for (int i = 0; i < layout.getChildCount(); i++)
        {
            View child = layout.getChildAt(i);
            child.setEnabled(true);
        }
    }


    public static int getPixels(Context context, float dipValue)
    {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
                r.getDisplayMetrics());
        return px;
    }


    public static String upperCaseFirstLetter(String s)
    {
        final StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for (int i = 0, l = words.length; i < l; ++i)
        {
            if (i > 0)
            {
                result.append(" ");
            }
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));
        }
        return result.toString();
    }

    public static String htmlTextFormat(String value, String color)
    {
        return "&nbsp;<font color=" + color + " >" + value + "</font>";
    }

    public static String roundNumber(String format, String value)
    {
        try
        {
            BigDecimal bigDecimal = new BigDecimal(value);
            return String.format(format, bigDecimal);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static int compare(BigDecimal b1, BigDecimal b2)
    {
        if (b1 == null)
        {
            return b2 == null ? 0 : -1;
        }
        if (b2 == null)
        {
            return 1;
        }
        return b1.compareTo(b2);
    }

    public static void focusMouse(EditText etStart, final EditText etEffect)
    {
        etStart.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    etEffect.requestFocus();
                    etEffect.setSelection(etEffect.getText().length());
                }
                return false;
            }
        });
    }

    public static boolean isListEmpty(Collection collection)
    {
        return collection == null || collection.size() == 0;
    }

}
