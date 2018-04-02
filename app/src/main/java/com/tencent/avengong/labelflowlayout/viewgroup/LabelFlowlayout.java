package com.tencent.avengong.labelflowlayout.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avengong on 2018/3/20.
 * 点击事件的确定 孩子选中状态的逻辑
 * 孩子view容器抽象逻辑
 */

public class LabelFlowlayout extends FlowLayout {


    public LabelFlowlayout(Context context) {
        super(context);
    }

    public LabelFlowlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelFlowlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private LabelAdapter mAdapter;

    public void setMaxSelectNum(int maxSelectNum) {
        if (mAdapter!=null)
            throw new RuntimeException("selectNum must be set before setAdapter");
        this.maxSelectNum = maxSelectNum;
    }

    private int maxSelectNum = -1; //-1表示无限制

    private List<Integer> mSelectedChildren = new ArrayList<>();


    /**
     * 把数据和ui都抽离到adapter层来处理
     *
     * @param adapter
     */
    public void setAdapter(LabelAdapter adapter) {
        mAdapter = adapter;
        mAdapter.registerDataObserver(this);
        showAdapter();

    }
    public void onChange() {

        removeAllViews();
        showAdapter();

    }
    private void showAdapter() {
        int count = mAdapter.getCount();
        List<Integer> preSelectedList = mAdapter.getPreSelectedList();
        mSelectedChildren.clear();
        for (int i = 0; i < count; i++) {

            final View view = mAdapter.getView(this, i, mAdapter.getItem(i));
            final LabelView myFlowContainer = new LabelView(getContext());
            view.setDuplicateParentStateEnabled(true);

            LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null) {
                myFlowContainer.setLayoutParams(layoutParams);
            } else {
                MarginLayoutParams lpChild = new MarginLayoutParams(LayoutParams
                        .WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lpChild.leftMargin = dip2px(5);
                lpChild.topMargin = dip2px(1);
                lpChild.rightMargin = dip2px(5);
                lpChild.bottomMargin = dip2px(1);
                //margin tagcontainer才是需要设置的孩子
                myFlowContainer.setLayoutParams(lpChild);
            }
            MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            //tagview孩子match就好
            view.setLayoutParams(lp);
            myFlowContainer.addView(view);

            if (isPreSelected(i,preSelectedList)){
                myFlowContainer.setChecked(true);
            }else{
                myFlowContainer.setChecked(false);
            }


            final int finalI = i;
            myFlowContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doClickAction(myFlowContainer, finalI);

                    if (mLabelSelectedListener != null) {
                        mLabelSelectedListener.selected(mSelectedChildren);
                    }

//                    if (mChildClickListener != null) {
//                        mChildClickListener.onclick(v, mAdapter.getItem(finalI));
//                    }
                }
            });
            addView(myFlowContainer);

        }

        mSelectedChildren.addAll(preSelectedList);
    }


    private boolean isPreSelected(int i, List<Integer> preSelectedList) {
        for(Integer index:preSelectedList){
            if (i==index){
                return true;
            }
        }

        return false;
    }

    /**
     * 执行点击操作
     *
     * @param view
     * @param finalI
     */
    private void doClickAction(LabelView view, int finalI) {


        if (view.isChecked()) {
            view.setChecked(false);
            mSelectedChildren.remove((Integer) finalI);

        } else {
            if (maxSelectNum == 1 && mSelectedChildren.size() == 1) {

                LabelView childAt = (LabelView) getChildAt(mSelectedChildren.get(0));
                childAt.setChecked(false);
                mSelectedChildren.remove((Integer) mSelectedChildren.get(0));
                view.setChecked(true);
                mSelectedChildren.add(finalI);

            } else {
                if (mSelectedChildren.size() < maxSelectNum) {
                    view.setChecked(true);
                    mSelectedChildren.add(finalI);

                }
            }
        }

    }

    //==========================

    public void setOnChildClickListener(OnChildClickListener childClickListener) {
        mChildClickListener = childClickListener;
    }

    private OnChildClickListener mChildClickListener;


    public interface OnChildClickListener {
        void onclick(View childView, Object bean);
    }

    public void setLabelSelectedListener(OnLabelSelectedListener labelSelectedListener) {
        mLabelSelectedListener = labelSelectedListener;
    }

    private OnLabelSelectedListener mLabelSelectedListener;

    public interface OnLabelSelectedListener {
        void selected(List<Integer> selectedList);
    }


    public int dip2px(int dp) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        return (int) (dp * displayMetrics.density + 0.5f);

    }


}
