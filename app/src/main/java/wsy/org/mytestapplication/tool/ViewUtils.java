package wsy.org.mytestapplication.tool;

import android.view.View;
import android.view.ViewParent;

/**
 * Created by wsy on 2017/1/6.
 */
public class ViewUtils {

    /**
     * 转换一个子view的坐标点在topView中的坐标
     *
     * @param topView
     * @param descendant
     * @param coord
     * @return
     */
    public static float getDescendantCoordRelativeToSelf(View topView, View descendant, int[] coord) {

        float scale = 1.0f;

        //(coord[0],coord[1])分别是子View中所要转换的点的(x,y)坐标
        float[] pt = {coord[0], coord[1]};

        //子View由于旋转缩放等操作改变了子View的坐标系，这些变化反映在子View对应的Matrix上，
        //getMatrix()方法获得子View的Matrix。而mapPoints方法则可以得到在初始的坐标系下pt点的坐标
        descendant.getMatrix().mapPoints(pt);
        //计算子View x轴的缩放值
        scale *= descendant.getScaleX();

        //在对子View变换时，经过我的实验发现子View的Left，Right，Top，Bottom是不变的
        //因此下面两行可以计算出descendant中的点在其父View坐标系下的坐标
        pt[0] += descendant.getLeft();
        pt[1] += descendant.getTop();

        //通过下面的循环，递归的一层一层计算出descendant中的点在最顶层View也就是DragLayer坐标系下的坐标值
        //和x轴的总的缩放值
        ViewParent viewParent = descendant.getParent();
        while (viewParent instanceof View && viewParent != topView) {
            final View view = (View) viewParent;
            view.getMatrix().mapPoints(pt);
            scale *= view.getScaleX();
            pt[0] += view.getLeft() - view.getScrollX();
            pt[1] += view.getTop() - view.getScrollY();
            viewParent = view.getParent();
        }

        //返回结果
        coord[0] = Math.round(pt[0]);
        coord[1] = Math.round(pt[1]);
        return scale;
    }
}
