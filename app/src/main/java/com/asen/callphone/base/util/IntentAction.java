package com.asen.callphone.base.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by asus on 2017/12/12.
 */

public class IntentAction {

    public static void send(@NonNull Context context, @NonNull String shareText) {
        Intent shareIntent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent, "分享"));
    }
}