package com.jdr.groupsizecalculator;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

public class Alerter {
    public static void showAlert(Activity activity, int title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message);

        AlertDialog alert = builder.create();
        alert.show();
    }
}
