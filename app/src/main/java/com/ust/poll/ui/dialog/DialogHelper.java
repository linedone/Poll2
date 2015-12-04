package com.ust.poll.ui.dialog;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogHelper {
    private static final String TAG = "DialogHelper";
    private static ProgressDialog pdialog = null;

    public DialogHelper(){}

    public static AlertDialog getOkAlertDialog(Context ctx, String title,
                                               String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public static AlertDialog getOkAlertDialog(Context ctx, String title,
                                               String message, final YesAction action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                action.fnDoYesAction();
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public static AlertDialog getYesNoDialog(Context ctx, String title,
                                             String message, final YesAction action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton("No", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                action.fnDoYesAction();
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public static AlertDialog getSingleOptionDialog(Context ctx, String title,
                                                    String[] options, final OnClickListener action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(options, -1, action);
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public static void fnShowDialog(Context ctx) {
        if (pdialog != null) {
            pdialog.dismiss();
        }
        pdialog = new ProgressDialog(ctx);
        pdialog.setCancelable(true);
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Connecting to the server...");
        pdialog.setCancelable(false);
        pdialog.show();
    }

    public static void fnCloseDialog() {
        if (pdialog != null) {
            pdialog.cancel();
            pdialog.dismiss();
        }
    }

}