package com.aospstudio.android.apps.mediabook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import androidx.appcompat.app.AlertDialog;

class de {
    private static final String ASSET_TIPS = "de";
    private static final String PREFERENCE_TIPS_ACCEPTED = "tips.accepted";
    private static final String PREFERENCES_TIPS = "tips";

    /**
     * callback to let the activity know when the user has accepted the EULA.
     */
    static interface OnTipsAgreedTo {

        /**
         * Called when the user has accepted the eula and the dialog closes.
         */
        void onTipsAgreedTo();
    }

    /**
     * Displays the EULA if necessary. This method should be called from the onCreate()
     * method of your main Activity.
     *
     * @param activity The Activity to finish if the user rejects the EULA.
     * @return Whether the user has agreed already.
     */
    static boolean show(final Activity activity) {

        final SharedPreferences preferences = activity.getSharedPreferences(PREFERENCES_TIPS,
                Activity.MODE_PRIVATE);
        //to test:  preferences.edit().putBoolean(PREFERENCE_EULA_ACCEPTED, false).commit();
        if (!preferences.getBoolean(PREFERENCE_TIPS_ACCEPTED, false)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.tips_title);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    accept(preferences);
                    if (activity instanceof OnTipsAgreedTo) {
                        ((OnTipsAgreedTo) activity).onTipsAgreedTo();
                    }
                }
            });
            builder.setMessage(R.string.tips_message);
            builder.create().show();
            return false;
        }
        return true;
    }

    private static void accept(SharedPreferences preferences) {
        preferences.edit().putBoolean(PREFERENCE_TIPS_ACCEPTED, true).commit();
    }

    private static void refuse(Activity activity) {
        activity.finish();
    }

    private static CharSequence readEula(Activity activity) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(activity.getAssets().open(ASSET_TIPS)));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = in.readLine()) != null) buffer.append(line).append('\n');
            return buffer;
        } catch (IOException e) {
            return "";
        } finally {
            closeStream(in);
        }
    }

    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
