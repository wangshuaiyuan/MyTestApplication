package wsy.org.timeshaft;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import wsy.org.mytestapplication.R;

/**
 * Created by wsy on 15/03/2019
 */
public class TimerShaftItemActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_shaft);
        init();
    }

    private void init() {
        RecyclerView rv = findViewById(R.id.rv_timer_shaft);
        rv.setWillNotDraw(false);
        rv.addItemDecoration(new TimerShaftItemDecoration());
        rv.setLayoutManager(new LinearLayoutManager(this));
        TimerShaftAdapter adapter = new TimerShaftAdapter();
        rv.setAdapter(adapter);
    }
}
