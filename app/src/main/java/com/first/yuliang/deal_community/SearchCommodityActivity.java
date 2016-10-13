package com.first.yuliang.deal_community;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class SearchCommodityActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText query2;
    private ImageButton ib_search2;
    private ImageButton ib_return;
    private ImageButton clear;
    final List<CommodityBean.Commodity> commodityList = new ArrayList<CommodityBean.Commodity>();
    private BaseAdapter adapterSearch;
    private ListView lv_history;
    private ListView lv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_commodity);

        query2 = ((EditText) findViewById(R.id.query2));
        query2.requestFocus();

        lv_history = ((ListView) findViewById(R.id.lv_history));
        lv_search = ((ListView) findViewById(R.id.lv_search));

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
                commodityList.clear();
                Log.e("看看数据=============",""+s.toString());
                if (!s.toString().equals("") && s.toString()!=null){
                lv_search.setVisibility(View.VISIBLE);
                adapterSearch = new BaseAdapter() {

                    @Override
                    public int getCount() {
                        return commodityList.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = View.inflate(SearchCommodityActivity.this,R.layout.item_search,null);
                        TextView tv_search = ((TextView) view.findViewById(R.id.tv_search));
                        CommodityBean.Commodity commodity = commodityList.get(position);

                        tv_search.setText(commodity.commodityTitle);

                        return view;
                    }
                };
                lv_search.setAdapter(adapterSearch);
                getCommodityList(s.toString());
                }else {
                    lv_search.setVisibility(View.GONE);
                }
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
    public void getCommodityList(String search) {
        RequestParams params = new RequestParams("http://192.168.191.1:8080/csys/getcommodity?search="+search);
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommodityBean bean = gson.fromJson(result, CommodityBean.class);
                commodityList.addAll(bean.commodities);
                adapterSearch.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SearchCommodityActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
