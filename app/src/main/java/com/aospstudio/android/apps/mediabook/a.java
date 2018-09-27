package com.aospstudio.android.apps.mediabook;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class a extends AppCompatActivity {

    final Context context = this;

    public boolean networkConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Thread splashThread = new Thread() {
                public void run() {
                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(getApplicationContext(), ac.class);
                        startActivity(intent);
                    }
                }
            };
            splashThread.start();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            StyleableToast.makeText(getApplicationContext(), getString(R.string.permission_denied_toast), Toast.LENGTH_LONG, R.style.MD2toast).show();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        PowerManager powerManager = (PowerManager)
                getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            powerManager.isPowerSaveMode();
        }

        if (networkConnection()) {
            setContentView(R.layout.mera_splash_screen);

            TedPermission.with(getApplicationContext())
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage(R.string.permission_denied_title)
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.GET_ACCOUNTS)
                    .setGotoSettingButtonText(R.string.settings_btn)
                    .setDeniedCloseButtonText(R.string.close_btn)
                    .check();
            } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(R.string.app_error_dialog_title);
            alertDialogBuilder
                    .setMessage(R.string.app_network_connection_error)
                    .setCancelable(false)
                    .setNegativeButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
