package wsy.org.mytestapplication.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2017/1/6.
 * <p/>
 * 类似房客房东中心翻转动画activity,修正打脸效果，加了setCameraDistance
 */
public class ReversalAnimationActivity extends Activity {
    View vBack;
    View vFront;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversal_animation);
        vBack = findViewById(R.id.v_back);
        vFront = findViewById(R.id.v_front);
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLeftInAnimation(vBack);
                playAnimation(vFront);
            }
        });
        vFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playLeftInAnimation(vBack);
                playAnimation(vFront);
            }
        });
        setCameraDistance();

    }


    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        vBack.setCameraDistance(scale);
        vFront.setCameraDistance(scale);
    }

    /**
     * 右出动画
     *
     * @param target
     */
    private void playAnimation(View target) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator rotation = ObjectAnimator.ofFloat(target, View.ROTATION_Y, 0, 180).setDuration(2000);
//        rotation.setInterpolator(new QuintOutInterpolator());
//        rotation.start();


        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 1, 0).setDuration(0);
//        alpha.start();

        animatorSet.play(rotation);
        animatorSet.start();
        alpha.setStartDelay(1000);
        alpha.start();
    }

    private void playLeftInAnimation(View target) {

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 1, 0).setDuration(0);
//        alpha.start();


        ObjectAnimator rotation = ObjectAnimator.ofFloat(target, View.ROTATION_Y, -180, 0).setDuration(2000);
//        rotation.setInterpolator(new QuintOutInterpolator());
//        rotation.start();

        ObjectAnimator alphaShow = ObjectAnimator.ofFloat(target, View.ALPHA, 0, 1).setDuration(0);
//        alpha.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha).after(rotation);
        alphaShow.setStartDelay(1000);
        alphaShow.start();
        animatorSet.start();
    }


}
