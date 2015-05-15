package com.oman.allinone.utils;

/**
 * User: khiemvx
 * Date: 10/02/14
 */
public class StringUtils
{
    public static final String EMPTY = "";

    public static boolean isEmpty(CharSequence cs)
    {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs)
    {
        return !StringUtils.isEmpty(cs);
    }

    public static String trimToEmpty(String str)
    {
        return str == null ? EMPTY : str.trim();
    }

    public static boolean isBlank(CharSequence cs)
    {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0)
        {
            return true;
        }
        for (int i = 0; i < strLen; i++)
        {
            if ((Character.isWhitespace(cs.charAt(i)) == false))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs)
    {
        return !StringUtils.isBlank(cs);
    }

    public static String trim(String str)
    {
        return str == null ? null : str.trim();
    }

    public static StringBuffer appendTo(StringBuffer head, String separator, String tail)
    {

        if (StringUtils.isEmpty(tail))
        {
            return head;
        }
        else if (head.length() == 0)
        {
            return head.append(tail);
        }
        else
        {
            return head.append(separator).append(tail);
        }
    }

    public static StringBuffer insertInFront(String head, String separator, StringBuffer tail)
    {

        if (StringUtils.isEmpty(head))
        {
            return tail;
        }
        else if (tail.length() == 0)
        {
            return tail.insert(0, head);
        }
        else
        {
            return tail.insert(0, separator).insert(0, head);
        }
    }

    public static String formatStringReplaceAllSpace(String editable)
    {
        String enter = String.valueOf(editable);
        return enter.replaceAll(" +", " ").trim();
    }
}
