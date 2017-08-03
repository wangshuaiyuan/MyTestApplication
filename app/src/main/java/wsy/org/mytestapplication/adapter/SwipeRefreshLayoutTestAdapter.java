package wsy.org.mytestapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wsy.org.mytestapplication.tool.Util;

/**
 * Created by wsy on 2017/1/5.
 */
public class SwipeRefreshLayoutTestAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mData;

    public SwipeRefreshLayoutTestAdapter(ArrayList<String> data) {
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(parent.getContext(), 40));
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(params);
        MyViewHolder viewHolder = new MyViewHolder(textView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.tvLabel.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLabel;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvLabel = (TextView) itemView;
        }
    }
}
