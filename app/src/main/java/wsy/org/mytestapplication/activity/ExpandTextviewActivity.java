package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.math.BigDecimal;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.view.ExpandableTextView;

/**
 * Created by wsy on 2016/10/11.
 */
public class ExpandTextviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_text_view);

//        ExpandableTextView expandableTextView1 = (ExpandableTextView) findViewById(R.id.detail_otherfee_des);
//        expandableTextView1.setText("CollapsedCollapsedCollapsedCollapsedCollapsedCollapsedCollapsedCollapsedCollapsedCollaplapsedCollapsedCollapsedCollapsedCollapsedCollapsedCollasedCollapsed");
        final ExpandableTextView expandableTextView = (ExpandableTextView) findViewById(R.id.expand_lodge_detail_desc);
        expandableTextView.setContent("内内容内内内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容",
                "标题1", "标题2",
                "内内容内内容内内内容内容内容内容内容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容",
                "内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容");

        findViewById(R.id.expand_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ExpandTextviewActivity.this, ExpandTextviewActivity.class));
                expandableTextView.setContent("22222内内容内内内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容",
                        "2标题1", "2标题2",
                        "222内内容内内容内内内容内容内容内容内容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容",
                        "222内内容内内容内内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容内内容内容内容内容内容内容内容容容内容内容内容内容内容容内容内容内容内容内容内容容");

            }
        });
    }


    public static String getPrettyNumber(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number)).stripTrailingZeros().toPlainString();
    }


//        expandableTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (expandableTextView.isCollapsed()) {
//                    findViewById(R.id.ll_lodge_detail_desc_expanded).setVisibility(View.VISIBLE);
//                    findViewById(R.id.tv_lodge_detail_desc_collapsed).setVisibility(View.GONE);
//                } else {
//                    findViewById(R.id.ll_lodge_detail_desc_expanded).setVisibility(View.GONE);
//                    findViewById(R.id.tv_lodge_detail_desc_collapsed).setVisibility(View.VISIBLE);
//                }
//
//            }
//        });


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("onNewIntent", "onNewIntent: ----");
    }
}
