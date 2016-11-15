package com.example.chenjunmei.myscrollview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by ChenJunMei on 2016/11/15.
 * 自定义ui-MyScrollView，实现头尾部的下拉、上拉
 */

//1.自定义 MyScrollView 类继承 ScrollView
public class MyScrollView extends ScrollView {
    private static Context mContext;

    private View childView;  //子视图

    private Rect normal=new Rect();//用于记录临界状态时的left,top,right,bottom的坐标

    private int lastX;//记录上一次x坐标的位置
    private int downX,downY;//记录down事件时，x轴和y轴的坐标位置

    private int lastY;  //记录上一次y坐标的位置
    private boolean isFinishAnimation=true;

    //2.构造器（ 注意!这里不能用this来连写,构造 ）
    public MyScrollView(Context context) {
        super(context);
        this.mContext=context;
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
    }


    //2.获取子视图（ScrollView只能有一个孩子,所以这里获取的就是ScrollView）
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount()>0){
            childView= getChildAt(0);
        }
    }

    //3.
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (childView == null || !isFinishAnimation) {
            return super.onTouchEvent(ev);
        }

        //获取当前y轴方向的坐标(相较于当前视图)
        int eventY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = eventY;

                break;

            case MotionEvent.ACTION_MOVE:
                //获取y轴移动的位移
                int dy = eventY - lastY;

                if (isNeedMove()) {//如果没有记录过left,top,right,bottom，返回true.
                    if (normal.isEmpty()) {
                        //记录临界状态的left,top,right,bottom
                        normal.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
                    }
                    //给视图重新布局
                    childView.layout(childView.getLeft(), childView.getTop() + dy / 2, childView.getRight(), childView.getBottom());

                    //记得要重新赋值，即把上一次的结束，作为下一次的开始
                    lastY = eventY;
                }

                break;

            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, normal.bottom - childView.getBottom());
                    translateAnimation.setDuration(300);
                    childView.startAnimation(translateAnimation);

                    //设置动画的监听
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isFinishAnimation = true;
                            //先清除动画
                            childView.clearAnimation();
                            childView.layout(normal.left, normal.top, normal.right, normal.bottom);//重新布局
                            normal.setEmpty();//清空normal中left、top、right、bottom数据
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 4.
     * @param ev
     * @return 如果返回值为true，表示拦截子视图的处理。如果返回false，表示不拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercet=false;
        int eventX= (int) ev.getX();
        int eventY= (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录水平方向和垂直方向移动的距离
                downY= lastY=eventY;
                downX= lastX=eventX;

                break;
            case MotionEvent.ACTION_MOVE:
                int disX=Math.abs(eventX-downX);
                int disY=Math.abs(eventY-downY);
                if (disX<disY  || disY> dp2px(10)){
                    isIntercet=true;
                }
                lastY=eventY;
                lastX=eventX;
                break;
        }

        return isIntercet;

    }


    /**
     * 是否需要使用动画
     * @return
     */
    private boolean isNeedAnimation() {
        return !normal.isEmpty();  //当其不为空，即还没有重新定位前，需要动画效果
    }

    /**
     * 判断是否需要按照我们自定义的方式重新布局
     * @return
     */
    private boolean isNeedMove() {
        //获取子视图测量的高度
        int measureHeight=getMeasuredHeight();
        //获取屏幕的高
        int screenHeight= this.getHeight();
        //获取二者的距离差
        int dy=measureHeight-screenHeight;

        //获取当前视图在y轴方向上移动的位移 (最初：0.上移：+，下移：-)
        int scrollY= this.getScrollY();

        if (scrollY<=0 ||scrollY>=dy){
            return true;
        }

        return false;
    }


    //dp-->px
    public static int dp2px(int dp){
        //先获取手机的密度
        float desity=mContext.getResources().getDisplayMetrics().density;
        //通过+0.5来实现四舍五入
        return (int) (dp*desity+0.5);
    }

    }
