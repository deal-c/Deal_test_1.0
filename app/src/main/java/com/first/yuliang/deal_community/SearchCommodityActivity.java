package com.first.yuliang.deal_community;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

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

//        添加EditText监听事件
        query2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
//         当EditText内容改变时，显隐藏清空按钮
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if (clear != null) {
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (query2 != null) {
                                    query2.getText().clear();
                                    clear.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } else {
                    if (clear != null) {
                        clear.setVisibility(View.GONE);
                    }
                }
            }
//         得到EditText的输入内容，并实时搜索
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        }
    }
}
