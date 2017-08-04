package wsy.org.mytestapplication.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2017/8/4.
 */

public class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void resize() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height -= (int) getResources().getDimension(R.dimen.location_in_window_scroll_velocity);
        setLayoutParams(layoutParams);
//        forceLayout();
    }
}
