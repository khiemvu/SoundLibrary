package com.oman.allinone.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Khiemvx on 5/19/2015.
 */
public class DialogUtils {
    protected String message;
    protected String nameNegativeButton;
    protected Context context;

    public DialogUtils(Context context, String message, String nameNegativeButton) {
        this.context = context;
        this.message = message;
        this.nameNegativeButton = nameNegativeButton;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.create().show();
    }
}
