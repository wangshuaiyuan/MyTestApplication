package wsy.org.mytestapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.bean.DemoItemBean;

/**
 * Created by wsy on 2016/10/10.
 */
public class DemoListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DemoItemBean> mData;
    private LayoutInflater mInflater;

    public DemoListAdapter(Context context, ArrayList<DemoItemBean> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.demo_list_item_view, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_demo_name);
        tv.setText(mData.get(position).demoName);

        return convertView;
    }
}
