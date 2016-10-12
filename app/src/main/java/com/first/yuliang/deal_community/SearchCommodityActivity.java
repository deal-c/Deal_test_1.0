package com.first.yuliang.deal_community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SearchCommodityActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText query2;
    private ImageButton ib_search2;
    private ImageButton ib_return;
    private ImageButton clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_commodity);

        query2 = ((EditText) findViewById(R.id.query2));
        query2.requestFocus();



        ib_search2 = ((ImageButton) findViewById(R.id.ib_search2));
        ib_search2.setOnClickListener(this);
        ib_return = ((ImageButton) findViewById(R.id.ib_return));
        ib_return.setOnClickListener(this);
        clear = ((ImageButton) findViewById(R.id.search_clear));
        clear.setOnClickListener(this);
        if(!query2.getText().toString().equals("") && query2.getText().toString()!=null){
            clear.setVisibility(View.VISIBLE);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput(view);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        int[] l = {0, 0};
        v.getLocationInWindow(l);
        int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                + v.getWidth();
        if (v != null && (v instanceof EditText)) {

            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    // 隐藏软键盘
    private void hideSoftInput(View view) {
        query2.setFocusable(false);
        query2.setFocusableInTouchMode(true);
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_search2:

                break;
            case R.id.ib_return:
                this.finish();
                break;
            case R.id.search_clear:
                query2.setText("");
                break;
        }
    }
}
