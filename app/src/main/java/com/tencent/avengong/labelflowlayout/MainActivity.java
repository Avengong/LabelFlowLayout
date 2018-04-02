package com.tencent.avengong.labelflowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tencent.avengong.labelflowlayout.viewgroup.LabelAdapter;
import com.tencent.avengong.labelflowlayout.viewgroup.LabelFlowlayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String tag = MainActivity.class.getSimpleName();

    private List<String> labels = new ArrayList<>();
    private LabelAdapter<String> mLabelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LabelFlowlayout labelFlowlayout = findViewById(R.id.lfl_flowlayout);

        labels.clear();

        for (int i = 0; i < 20; i++) {
            labels.add("android" + i);
            labels.add("java" + i);
            labels.add("php" + i);
        }

        mLabelAdapter = new LabelAdapter<String>(labels) {
            @Override
            public View getView(LabelFlowlayout parent, int i, String item) {

                TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R
                        .layout.label_item, null);
                textView.setText(item);
                return textView;
            }
        };
//        ArrayList<Integer> integers = new ArrayList<>();
//        integers.add(2);
//        integers.add(3);
//        integers.add(4);
//        labelAdapter.setPreSelected(integers);

        labelFlowlayout.setMaxSelectNum(1);
        labelFlowlayout.setAdapter(mLabelAdapter);
        labelFlowlayout.setLabelSelectedListener(new LabelFlowlayout.OnLabelSelectedListener() {
            @Override
            public void selected(List<Integer> selectedList) {

                for (Integer index : selectedList) {
                    Log.d(tag, "selected_index: " + index);
                }
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> integers = new ArrayList<>();
                integers.add(2);
                mLabelAdapter.setPreSelected(integers);
            }
        });


    }

}
