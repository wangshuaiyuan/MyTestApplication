package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.spec.PSource;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 26/06/2018
 */
public class RecyclerViewInScrollViewActivity extends Activity {

    private RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_in_scrollview);
        mRv = findViewById(R.id.rv_recycler_in_scroll_view);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new RecycleInScrollAdapter());
//        findViewById(R.id.tv_rv_in_cv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(RecyclerViewInScrollViewActivity.this,"点击测试文字",Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.e("-----", "onDestroy");
        super.onDestroy();
    }

    class RecycleInScrollAdapter extends RecyclerView.Adapter<RecycleInScrollAdapter.RvInSvViewHolder> {

        @Override
        public RvInSvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RvInSvViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_rv_sv, parent, false));
        }

        @Override
        public void onBindViewHolder(RvInSvViewHolder holder, int position) {
            holder.tvTest.setText(String.format("test%d", position));
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class RvInSvViewHolder extends RecyclerView.ViewHolder {

            TextView tvTest;

            public RvInSvViewHolder(View itemView) {
                super(itemView);
                tvTest = itemView.findViewById(R.id.tv_rv_in_sv);
            }
        }
    }
}
