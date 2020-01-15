package com.tabibe.app.util;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.tabibe.app.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

public class DialogUtils {

    private DialogUtils() {
        //intentionally left empty
    }

    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showDialogMessage(Context context, String message) {
        if (context != null) {
            AppCompatDialog alert = new AlertDialog.Builder(context, R.style.customDialogCenteredTitle)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }
    }


    public static void showDialogWithCallBack(final Context context,
                                              final String title, final String msg, final
                                              IEventlistener listener,
                                              final String yesBtnText, final String noBtnText) {

        AlertDialog alertDialog = new AlertDialog.Builder(context,R.style.customDialogCenteredTitle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, yesBtnText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                                listener.onSuccess();
                        }
                    }

                });

        if (noBtnText != null) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, noBtnText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onCancel();
                            }
                        }
                    });
        }
        alertDialog.show();
    }

}
