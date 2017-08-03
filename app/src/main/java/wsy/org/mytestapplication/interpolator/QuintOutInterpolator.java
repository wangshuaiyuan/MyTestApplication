package wsy.org.mytestapplication.interpolator;

import android.animation.TimeInterpolator;

/**
 * Created by wsy on 2017/1/6.
 */
public class QuintOutInterpolator implements TimeInterpolator {

    @Override
    public float getInterpolation(float t) {
        return (t -= 1) * t * t * t * t + 1;
    }
}
