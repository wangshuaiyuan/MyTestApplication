package wsy.org.mytestapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by wsy on 25/11/2018
 */
public class ScrollTestView extends LinearLayout {
    private Paint mPaint = new Paint();

    public ScrollTestView(Context context) {
        this(context, null);
    }

    public ScrollTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.translate(0,-300);
        mPaint.setColor(Color.parseColor("#ff4081"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(120);
        canvas.drawText("我是canvas draw出来的文字", 0, 120, mPaint);
        canvas.restore();
    }

    public void scrollToBottom() {
        scrollBy(0, -300);
    }
}
