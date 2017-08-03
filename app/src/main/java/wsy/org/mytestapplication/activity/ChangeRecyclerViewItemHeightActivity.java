package wsy.org.mytestapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import wsy.org.mytestapplication.R;
import wsy.org.mytestapplication.adapter.ChangeItemHeightAdapter;

/**
 * Created by wsy on 2016/11/21.
 */
public class ChangeRecyclerViewItemHeightActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_item_height);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.change_item_height_rv);
        ChangeItemHeightAdapter adapter = new ChangeItemHeightAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //顺序不一样 不相等
        ArrayList<String> testList1 = new ArrayList<>();
        testList1.add("哈哈哈");
        testList1.add("哈哈哈2");
        testList1.add("哈哈哈3");
        testList1.add("哈哈哈4");

        ArrayList<String> testList2 = new ArrayList<>();
        testList2.add("哈哈哈");
        testList2.add("哈哈哈2");
        testList2.add("哈哈哈3");
        testList2.add("哈哈哈4");

        String aaaa = "aaa";
        Log.e("aaaaaa",aaaa.contains(null)+"");


        testList1 = (ArrayList<String>) testList2.clone();

        A a = new A();
        A b = (A) a.clone();
        ArrayList<String> aaa = b.a;

        if (testList1.equals(testList2)) {
            Toast.makeText(this, "相等", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "不相等", Toast.LENGTH_LONG).show();
        }
    }

    class A implements Cloneable {
        ArrayList<String> a = new ArrayList<>();

        public Object clone() {
            //TODO 弄清子对象如keyword，clone是否是创建新对象
            Object o = null;
            try {
                o = super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return o;
        }
    }
}
