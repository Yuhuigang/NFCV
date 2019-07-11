package cn.ident.nas.tester;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import cn.ident.nas.tester.adapter.MyExpandableListAdapter;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private String[] groups={"标准标签","NAS标签"};
    private String[] childs={"14443","15693"};
    private ActionBar actionBar;
    private TextView titleView;
    //二级列表
    private ExpandableListView expandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        expandableListView=findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new MyExpandableListAdapter(this));
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (groups[i].equals("标准标签")){
                    if (childs[i1].equals("14443")){
                        Intent intent=new Intent(MainActivity.this, Normal_14443Activity.class);
                        startActivity(intent);
                    }else if (childs[i1].equals("15693")){
                        Intent intent=new Intent(MainActivity.this, Normal_15693Activity.class);
                        startActivity(intent);
                    }
                }else if (groups[i].equals("NAS标签")){
                    if (childs[i1].equals("14443")){
                        Intent intent=new Intent(MainActivity.this, NAS_14443Activity.class);
                        startActivity(intent);
                    }else if (childs[i1].equals("15693")){
                        Intent intent=new Intent(MainActivity.this, NAS_15693Activity.class);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
    }
}
