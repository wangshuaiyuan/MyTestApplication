package wsy.org.mytestapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

/**
 * Created by xiaozhu on 16/10/9.
 */

public class EaseBackOutInterpolator implements Interpolator {

    public EaseBackOutInterpolator() {}

    public EaseBackOutInterpolator(Context context, AttributeSet attrs) {}

    public float getInterpolation(float input) {
        return ((input = input - 1) * input * ((1.70158f + 1) * input + 1.70158f) + 1);
    }
}