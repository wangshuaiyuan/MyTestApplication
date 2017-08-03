package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 2016/11/4.
 */
public class CircleImageViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_image_view);
        findViewById(R.id.circle_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "hahhaha", Toast.LENGTH_LONG).show();
            }
        });

        Matrix matrix = new Matrix();
        float[] values = {0, 0, 0, 0, 0, 0, 0, 0, 0};
        RectF rect = new RectF(0, 0, 100, 100);

        matrix.postRotate(90);
        matrix.mapRect(rect);
        matrix.getValues(values);
        for (int i = 0; i < values.length; i++) {
            Log.e("Values", values[i] + "");
        }

        Log.e("left", rect.left + "");
        Log.e("right", rect.right + "");
        Log.e("top", rect.top + "");
        Log.e("bottom", rect.bottom + "");
    }
}
