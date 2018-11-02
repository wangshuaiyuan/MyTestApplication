package com.example.navtablayout;

import android.content.Context;

/**
 * Created by wsy on 01/11/2018
 */
class NavTabUtil {

    /**
     * dp转px
     *
     * @param context 上线文
     * @param dps     dp value
     * @return px value
     */
    public static int dpToPx(Context context, int dps) {
        return Math.round(context.getResources().getDisplayMetrics().density * dps);
    }
}
