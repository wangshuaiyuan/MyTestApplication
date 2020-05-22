package wsy.org.timeshaft;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wsy on 15/03/2019
 */
public class TimerShaftAdapter extends RecyclerView.Adapter<TimerShaftAdapter.TimerShaftViewHolder> {
    @NonNull
    @Override
    public TimerShaftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimerShaftViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull TimerShaftViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class TimerShaftViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public TimerShaftViewHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            itemView.setLayoutParams(layoutParams);
            tv = (TextView) itemView;
            tv.setBackgroundColor(Color.parseColor("#26a69a"));
        }

        void bind(int position) {
            tv.setText("我是步骤" + position + "");
        }
    }
}
