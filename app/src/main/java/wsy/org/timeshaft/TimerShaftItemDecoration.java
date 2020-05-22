package wsy.org.timeshaft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import wsy.org.mytestapplication.YashonApplication;

/**
 * Created by wsy on 14/03/2019
 */
public class TimerShaftItemDecoration extends RecyclerView.ItemDecoration {

    private final int ITEM_OFFSET_TOP = dip2px(YashonApplication.getInstance(), 10);
    /**
     * item距离上部偏移量
     */
    private final int ITEM_OFFSET_LEFT = dip2px(YashonApplication.getInstance(), 36);
    /**
     * icon和内容间隙
     */
    private final int GAP_CONTENT_ICON = dip2px(YashonApplication.getInstance(), 8);


    private Path mPath = new Path();
    private Paint mPaint = new Paint();

    public TimerShaftItemDecoration() {
        mPaint.setColor(Color.parseColor("#ff4081"));
        mPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(ITEM_OFFSET_LEFT, ITEM_OFFSET_TOP, 0, 0);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //1.上部线
        //2.icon
        //3.下部线
        //4.左部线
        c.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            View child = parent.getChildAt(i);
            float childLeft = child.getLeft();
            float childTop = child.getTop();

            float centerX = childLeft - 14.5f;
            float centerY = childTop + 6.5f;

            drawTimeIconBg(c, mPaint, centerX, centerY);
            drawTopLine(c, mPaint, centerX, centerY, ITEM_OFFSET_TOP);
            drawBottomLine(c, mPaint, centerX, centerY, child.getHeight());
            drawPointer(c, mPaint, centerX, centerY);
        }
    }


    private final int ICON_RADIUS = dip2px(YashonApplication.getInstance(), 6.5f);

    private final int dp05 = dip2px(YashonApplication.getInstance(), 0.5f);
    private final int dp3 = dip2px(YashonApplication.getInstance(), 3f);
    private final int dp1 = dip2px(YashonApplication.getInstance(), 1f);
    private final int dp1f8 = dip2px(YashonApplication.getInstance(), 1.8f);
    private final int dp2 = dip2px(YashonApplication.getInstance(), 2f);
    private final int dp3f5 = dip2px(YashonApplication.getInstance(), 3.5f);

    /**
     * @param canvas
     * @param paint
     * @param centerX 钟表中心位置
     * @param centerY 钟表中心位置
     */
    private void drawTimeIconBg(Canvas canvas, Paint paint, float centerX, float centerY) {
        paint.setColor(Color.parseColor("#ff4081"));
        // 绘制轴点圆
        paint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(centerX - ICON_RADIUS, centerY - ICON_RADIUS, centerX + ICON_RADIUS, centerY + ICON_RADIUS);
        canvas.drawArc(rectF, 0, 360, true, paint);
    }

    /**
     * 画时间指针
     *
     * @param centerX 钟表中心位置
     * @param centerY 钟表中心位置
     */
    private void drawPointer(Canvas canvas, Paint paint, float centerX, float centerY) {
        paint.setColor(Color.parseColor("#ffffff"));
        mPath.reset();
        mPath.moveTo(centerX - dp05, centerY - dp3f5);
        mPath.rLineTo(0, dp3f5);
        mPath.rLineTo(dp3, dp2);

        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(1);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, paint);
    }

    /**
     * @param canvas
     * @param paint
     * @param centerX 钟表中心位置
     * @param centerY 钟表中心位置
     * @param height
     */
    private void drawTopLine(Canvas canvas, Paint paint, float centerX, float centerY, int height) {
        paint.setColor(Color.parseColor("#ff4081"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawLine(centerX, centerY - height, centerX, centerY, paint);
    }

    /**
     * @param canvas
     * @param paint
     * @param centerX 钟表中心位置
     * @param centerY 钟表中心位置
     * @param height
     */
    private void drawBottomLine(Canvas canvas, Paint paint, float centerX, float centerY, int height) {
        paint.setColor(Color.parseColor("#ff4081"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawLine(centerX, centerY, centerX, centerY + height, paint);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
