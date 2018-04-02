package com.tencent.avengong.labelflowlayout.viewgroup;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avengong on 2018/3/20.
 * 用adapter适配器来适配各种具体的孩子view的输入---adapter抽象
 * 类似于listview的adapter
 */

public abstract class LabelAdapter<T> {



    private List<T> mDatas=new ArrayList<>();

    public void setPreSelected(List<Integer> preSelected) {
        mPreSelected.clear();
        mPreSelected.addAll(preSelected);
        notifyDataChange();
    }

    private List<Integer> mPreSelected=new ArrayList<>();
    private FlowLayout mDateObserver;

    public LabelAdapter(List datas) {
        mDatas=datas;
    }

    public int getCount() {
        return mDatas.size();
    }

    public T getItem(int i) {
        return mDatas.get(i);
    }



    public abstract View getView(LabelFlowlayout parent, int i, T item);

    public List<Integer> getPreSelectedList() {
        return mPreSelected;
    }

    public void registerDataObserver(FlowLayout labelFlowlayout) {

        mDateObserver=labelFlowlayout;

    }

    protected  void notifyDataChange(){

        if (mDateObserver!=null){
            mDateObserver.onChange();
        }
    }
}
