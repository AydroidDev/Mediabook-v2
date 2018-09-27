package com.aospstudio.android.apps.mediabook.javascript;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.aospstudio.android.apps.mediabook.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        StyleableToast.makeText(mContext, toast, Toast.LENGTH_LONG, R.style.MD2toast).show();
    }
}
