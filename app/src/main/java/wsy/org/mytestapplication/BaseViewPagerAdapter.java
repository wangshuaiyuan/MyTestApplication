package wsy.org.mytestapplication;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gyzhong on 15/2/13.
 */
public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {

    private List<T> mDataList;

    private LayoutInflater mInflater ;

    protected BaseViewPagerAdapter(Context context,List<T> mDataList) {
        this.mDataList = mDataList;
        this.mInflater = LayoutInflater.from(context) ;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = createView(mInflater) ;
        setViewData(view,mDataList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public abstract void setViewData(View view,T position) ;

    public abstract View createView(LayoutInflater layoutInflater) ;
}
