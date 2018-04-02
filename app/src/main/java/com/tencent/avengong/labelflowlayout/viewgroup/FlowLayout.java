package com.tencent.avengong.labelflowlayout.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.tencent.avengong.labelflowlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Aven.Gong} on 2018/3/19 0019.
 */

public class FlowLayout extends ViewGroup {
    private String tag = FlowLayout.class.getSimpleName();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyleAttr, 0);
        int anInt = ta.getInt(R.styleable.FlowLayout_layoutGravity, 0);
        switch (anInt){
            case 0:
            layout_gravity=LayoutGravity.GRAVITY_LEFT;
               break;
            case 1:
                layout_gravity=LayoutGravity.GRAVITY_RIGHT;
                break;
            case 2:
                layout_gravity=LayoutGravity.GRAVITY_CENTER;
                break;
            case 3:
                layout_gravity=LayoutGravity.GRAVITY_EQUEL;
                break;
            default:

               break;
        }
        ta.recycle();


    }


    private List<Line> lineList = new ArrayList<>();
    private final int verticalSpace = 15;
    private final int horizontalSpace = 15;


    private LayoutGravity layout_gravity = LayoutGravity.GRAVITY_EQUEL;

    public void onChange() {

    }

    public enum LayoutGravity {

        GRAVITY_LEFT, GRAVITY_RIGHT, GRAVITY_CENTER, GRAVITY_EQUEL;


    }


    class Line {
        public int lineWidth = 0;
        public int lineHeight = 0;

        public List<View> getViewList() {
            return mViewList;
        }

        private List<View> mViewList = new ArrayList<>();

        public void addView(View view) {
            mViewList.add(view);
            Log.d(tag, "addView-----");
            //行宽--赋值
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            if (mViewList.size() == 1) {
                lineWidth += view.getMeasuredWidth();
            } else {
                lineWidth += view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin +
                        horizontalSpace;
            }
            //行高赋值
            lineHeight = Math.max(lineHeight, view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
        }


    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(tag, "onMeasure-----");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();
        Line line = new Line();
        lineList.clear();
        int realWidth = 0;
        int realHeight = getPaddingTop() + getPaddingBottom();

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            //support margin值
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin +
                    horizontalSpace;
            if (line.getViewList().size() == 0) {
                //没有孩子时，不需要水平间距
                line.addView(childView);
            } else if (childWidth + line.lineWidth > width - getPaddingLeft() - getPaddingRight()) {

                lineList.add(line);
                realWidth = Math.max(realWidth, line.lineWidth);
                realHeight += line.lineHeight + verticalSpace;
                line = new Line();
                line.addView(childView);
            } else {
                line.addView(childView);
            }

            //最后一行别落下
            if (i == childCount - 1) {
                lineList.add(line);
                realWidth = Math.max(realWidth, line.lineWidth);
                realHeight += line.lineHeight + verticalSpace;
            }
        }

        //适配wrapcontent
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? width : realWidth
                , heightMode == MeasureSpec.EXACTLY ? height : realHeight);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(tag, "onLayout-----");
        int size = lineList.size();

        int left = getPaddingLeft();
        int top = getPaddingTop();


        for (int i = 0; i < size; i++) {

            Line line = lineList.get(i);

            switch (layout_gravity) {
                case GRAVITY_LEFT:
                    left = getPaddingLeft();
                    break;

                case GRAVITY_RIGHT:
                    left = getMeasuredWidth() - line.lineWidth -getPaddingRight();
                    break;
                case GRAVITY_CENTER:
                    left = (getMeasuredWidth() - line.lineWidth) / 2 ;
                    break;
                case GRAVITY_EQUEL:
                    left = getPaddingLeft();
                    break;

            }
            List<View> viewList = line.getViewList();
            for (int k = 0; k < viewList.size(); k++) {
                View view = viewList.get(k);
                if (view.getVisibility() == GONE) {
                    continue;
                }

                if (layout_gravity == LayoutGravity.GRAVITY_EQUEL) {
                    //重新测量分配孩子的宽度
                    //剩余的宽度
                    int leaveWidh = getMeasuredWidth() - line.lineWidth - getPaddingLeft() - getPaddingRight();
                    //每个人可以增加的宽度
                    int singleWidth = leaveWidh / line.getViewList().size();
                    int widthSpec = MeasureSpec.makeMeasureSpec(view.getMeasuredWidth() + singleWidth,
                            MeasureSpec
                                    .EXACTLY);
                    //只主动分配view的宽度，高度还是直接按照unspcified模式进行(交给系统来测量)
                    view.measure(widthSpec, 0);
                }

                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                view.layout(left, top, left + view.getMeasuredWidth(), top + view
                        .getMeasuredHeight());
                left += view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin +
                        horizontalSpace;

            }
            top += line.lineHeight + verticalSpace;


        }


    }


}
