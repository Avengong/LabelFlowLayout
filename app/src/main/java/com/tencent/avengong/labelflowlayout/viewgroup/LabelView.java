package com.tencent.avengong.labelflowlayout.viewgroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;


/**
 * Created by avengong on 2018/3/20.
 * 孩子的抽象--实现checkable接口来实现选中与否的效果
 * 如何根据选中与否来更换背景？？
 * 看下checkbox的实现？
 */

public class LabelView extends FrameLayout implements Checkable {

    private boolean isChecked;
    private static final int[] CHECKED_STATE_SET = {android.
            R.attr.state_checked
    };

    public LabelView(@NonNull Context context) {
        this(context, null);
    }

    public LabelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != isChecked) {
            isChecked = checked;
            //这个方法是从checkbox源码中学习到的
            refreshDrawableState();
        }

    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
