package com.oman.allinone.utils;

import android.util.Log;

/**
 * Central log utils, intend to replace all normal log.* in our application.
 * This support content empty as default.
 * <p/>
 * User: lent
 * Date: 10/16/13
 */
public class LogUtils
{
// ------------------------------ FIELDS ------------------------------

    public static final String PREFIX_TAG = "PRISM_";
    public static boolean isDebugLogEnabled = true;

// -------------------------- STATIC METHODS --------------------------

    public static void debugLog(final Object target, final String content)
    {
        if (isDebugLogEnabled)
        {
            Log.d(PREFIX_TAG + target.getClass().getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content);
        }
    }

    public static void debugLog(final Class targetType, final String content)
    {
        if (isDebugLogEnabled)
        {
            Log.d(PREFIX_TAG + targetType.getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content);
        }
    }


    public static void debugLog(final Object target, final String content, Throwable throwable)
    {
        if (isDebugLogEnabled)
        {
            Log.d(PREFIX_TAG + target.getClass().getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content, throwable);
        }
    }

    public static void debugLog(final Class targetType, final String content, final Throwable throwable)
    {
        if (isDebugLogEnabled)
        {
            Log.d(PREFIX_TAG + targetType.getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content, throwable);
        }
    }

    public static void infoLog(final Object target, final String content)
    {
        Log.i(PREFIX_TAG + target.getClass().getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content);
    }

    public static void infoLog(final Class targetType, final String content)
    {
        Log.i(PREFIX_TAG + targetType.getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content);
    }

    public static void infoLog(final Object target, final String content, Throwable throwable)
    {
        Log.i(PREFIX_TAG + target.getClass().getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content, throwable);
    }

    public static void infoLog(final Class targetType, final String content, final Throwable throwable)
    {
        Log.i(PREFIX_TAG + targetType.getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content, throwable);
    }

    public static void errorLog(final Object target, final String content)
    {
        Log.e(PREFIX_TAG + target.getClass().getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content);
    }

    public static void errorLog(final Class targetType, final String content)
    {
        Log.e(PREFIX_TAG + targetType.getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content);
    }

    public static void errorLog(final Object target, final String content, Throwable throwable)
    {
        Log.e(PREFIX_TAG + target.getClass().getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content, throwable);
    }

    public static void errorLog(final Class targetType, final String content, final Throwable throwable)
    {
        Log.e(PREFIX_TAG + targetType.getSimpleName(), StringUtils.isEmpty(content) ? "[Log is empty]" : content, throwable);
    }
}
