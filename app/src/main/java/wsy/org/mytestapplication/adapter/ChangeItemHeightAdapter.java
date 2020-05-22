package wsy.org.mytestapplication.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.view.ExpandableTextView;

/**
 * Created by wsy on 2016/11/21.
 */
public class ChangeItemHeightAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;

    public ChangeItemHeightAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_view_change_item_height, parent, false);
        ChangeHeightViewHolder viewHolder = new ChangeHeightViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChangeHeightViewHolder holder1 = (ChangeHeightViewHolder) holder;
        holder1.textView.setContent("内内容内内内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容",
                "标题1", "标题2",
                "内内容内内容内内内容内容内容内容内容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容",
                "内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容");

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ChangeHeightViewHolder extends RecyclerView.ViewHolder {

        ExpandableTextView textView;

        public ChangeHeightViewHolder(View itemView) {
            super(itemView);
            textView = (ExpandableTextView) itemView.findViewById(R.id.change_item_height_tv);
        }
    }
}
