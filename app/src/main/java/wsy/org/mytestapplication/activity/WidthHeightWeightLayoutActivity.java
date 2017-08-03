package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2016/10/27.
 */
public class WidthHeightWeightLayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_width_height_weigth);
        final View a = findViewById(R.id.aaa);
        final View b = findViewById(R.id.bbb);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a.getVisibility() == View.VISIBLE) {
                    a.setVisibility(View.GONE);
                    b.setVisibility(View.VISIBLE);
                } else {
                    a.setVisibility(View.VISIBLE);
                    b.setVisibility(View.GONE);
                }
            }
        };
        a.setOnClickListener(onClickListener);
        b.setOnClickListener(onClickListener);
        ImageView iv = (ImageView) findViewById(R.id.iv_aaa);
        Bitmap bitmap = null;
        iv.setImageBitmap(bitmap);

    }

    //获取百度地图截图
//    private String getBaiduMaoPicUrl(int width, int height, String longitude, String latitude, HashMap<LatLng, String> marker) {
//
//        if (marker == null || marker.size() == 0) {
//            return "";
//        }
//        StringBuffer picUrl = new StringBuffer();
//        StringBuffer location = new StringBuffer();
//        Iterator iterator = marker.entrySet().iterator();
//        int i = 0;
//        while (iterator.hasNext()) {
//            Map.Entry entry = (Map.Entry) iterator.next();
//            LatLng latLng = (LatLng) entry.getKey();
//            String url = (String) entry.getValue();
//            if (i == 0) {
//                picUrl.append("-1," + url);
//                location.append(latLng.longitude + "," + latLng.latitude);
//            } else {
//                location.append("|" + latLng.longitude + "," + latLng.latitude);
//                picUrl.append("|-1," + url);
//            }
//            i++;
//        }
//        String mapUrl = "http://api.map.baidu.com/staticimage/v2?" +
//                "ak=wjvdMClCe7uoGra8vE6eoD7X" +
//                "&mcode=A1:2F:59:B5:45:B6:0A:0C:A7:C9:18:00:05:88:2F:40:1B:C5:B0:3B;xiaode.com" +
//                "&width=" + width +
//                "&height=" + height +
//                "&center=" + longitude + "," + latitude +
//                "&zoom=18" +
//                "&markers=" + location.toString() +
//                "&markerStyles=" + picUrl.toString();
//
//        return mapUrl;
//    }



    //view扒开转场动画
    //    private void playMapBackAnimation() {
//        boolean playTopAnim = true;
//        boolean playBottomAnim = true;
//        //状态栏高度
//        int statusBarHeight = Util.getStatusBarHeight();
//        //地图所在屏幕位置
//        int[] mapLocation = new int[2];
//        mRlMapWrap.getLocationInWindow(mapLocation);
//        //地图所在位置y值
//        int mapTop = mapLocation[1] - statusBarHeight;
//        //地图高度
//        int mapHeight = mRlMapWrap.getHeight();
//
//        int[] mapBgLocation = new int[2];
//        mMapView.getLocationInWindow(mapBgLocation);
//        int mapBgTop = mapBgLocation[1] - statusBarHeight;
//
//        final View vWrap = findViewById(R.id.lodge_detail_animation_rl);
//        vWrap.setVisibility(View.VISIBLE);
//
//        if (mapTop <= 0) {
//            playTopAnim = false;
//        }
//        if ((mapTop + mapHeight) >= Util.getAndroidContentHeight()) {
//            playBottomAnim = false;
//        }
//        TranslateAnimation bgAnimation = new TranslateAnimation(0, 0, 0, mapBgTop);
//        bgAnimation.setFillAfter(true);
//        bgAnimation.setDuration(500);
//        bgAnimation.setRepeatCount(0);
//        mIvAnimation1.clearAnimation();
//        mIvAnimation1.setAnimation(bgAnimation);
//        bgAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                vWrap.setBackgroundColor(Color.parseColor("#000000"));
//                vWrap.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        if (!playTopAnim) {
//            TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, Util.getAndroidContentHeight(), mapTop + mapHeight);
//            bottomAnimation.setFillAfter(true);
//            bottomAnimation.setDuration(500);
//            bottomAnimation.setRepeatCount(0);
//            mIvAnimation2.setVisibility(View.INVISIBLE);
//            mIvAnimation2.clearAnimation();
//            mIvAnimation3.setVisibility(View.VISIBLE);
//            mIvAnimation3.clearAnimation();
//            mIvAnimation3.setAnimation(bottomAnimation);
//            bottomAnimation.start();
//            bgAnimation.start();
//        } else if (!playBottomAnim) {
//            TranslateAnimation topAnimation = new TranslateAnimation(0, 0, -mapTop, 0);
//            topAnimation.setFillAfter(true);
//            topAnimation.setDuration(500);
//            topAnimation.setRepeatCount(0);
//            mIvAnimation2.clearAnimation();
//            mIvAnimation2.setAnimation(topAnimation);
//            mIvAnimation3.setVisibility(View.INVISIBLE);
//            mIvAnimation3.clearAnimation();
//            topAnimation.start();
//            bgAnimation.start();
//        } else {
//            TranslateAnimation topAnimation = new TranslateAnimation(0, 0, -mapTop, 0);
//            TranslateAnimation bottomAnimation = new TranslateAnimation(0, 0, Util.getAndroidContentHeight(), mapTop + mapHeight);
//
//            topAnimation.setFillAfter(true);
//            topAnimation.setDuration(500);
//            topAnimation.setRepeatCount(0);
//
//            bottomAnimation.setFillAfter(true);
//            bottomAnimation.setDuration(500);
//            bottomAnimation.setRepeatCount(0);
//            mIvAnimation2.setVisibility(View.VISIBLE);
//            mIvAnimation2.clearAnimation();
//            mIvAnimation2.setAnimation(topAnimation);
//            mIvAnimation3.setVisibility(View.VISIBLE);
//            mIvAnimation3.clearAnimation();
//            mIvAnimation3.setAnimation(bottomAnimation);
//
//            topAnimation.start();
//            bottomAnimation.start();
//            bgAnimation.start();
//        }
//    }
}
