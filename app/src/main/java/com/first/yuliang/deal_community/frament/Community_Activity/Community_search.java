package com.first.yuliang.deal_community.frament.Community_Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Community;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Community_search extends AppCompatActivity implements View.OnClickListener {
    private EditText query2;
    private ImageButton ib_search2;
    private ImageButton ib_return;
    private ImageButton clear;
    final List<Community> communityList = new ArrayList<Community>();
    private BaseAdapter adapterSearch;
    private ListView lv_history;
    private ListView lv_search;
    private Button btn_clear_history;
    private ArrayAdapter<String> arr_adapter;
    String[] history_arr;
    String history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_search);
        //设置信息栏颜色
        setColor();

        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        query2 = ((EditText) findViewById(R.id.query2));
        query2.requestFocus();
        keySearch();

        lv_history = ((ListView) findViewById(R.id.lv_history));
        lv_history.addFooterView(new ViewStub(this));
        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                query2.setText(history_arr[position]);
                query2.setSelection(history_arr[position].length());
                RequestParams params = new RequestParams(HttpUtile.host +"/community/togetcombyname");
                params.addQueryStringParameter("name", history_arr[position]);
                save();
                getHistory();
                btn_clear_history.setVisibility(View.GONE);
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = new Gson();

                        Community temp = gson.fromJson(result, Community.class);
                        Intent intent = new Intent(Community_search.this, Community_model.class);
                        intent.putExtra("bundle", temp);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(Community_search.this, "访问出错", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

            }
        });
        lv_search = ((ListView) findViewById(R.id.lv_search));
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Community temp = communityList.get(position);
                query2.getText().clear();
                query2.setText(temp.getCommunityName());
                query2.setSelection(temp.getCommunityName().length());
                Intent intent = new Intent(Community_search.this, Community_model.class);
                save();
                getHistory();
                btn_clear_history.setVisibility(View.GONE);
                intent.putExtra("bundle", temp);
                startActivity(intent);
            }
        });

        ib_search2 = ((ImageButton) findViewById(R.id.ib_search2));
        ib_search2.setOnClickListener(this);
        ib_return = ((ImageButton) findViewById(R.id.ib_return));
        ib_return.setOnClickListener(this);
        clear = ((ImageButton) findViewById(R.id.search_clear));
        clear.setOnClickListener(this);
        btn_clear_history = ((Button) findViewById(R.id.btn_clear_history));
        btn_clear_history.setOnClickListener(this);

        getHistory();

//        添加EditText监听事件
        query2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //         当EditText内容改变时，显隐藏清空按钮
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    if (clear != null) {

                        lv_history.setVisibility(View.GONE);
                        btn_clear_history.setVisibility(View.GONE);
                        clear.setVisibility(View.VISIBLE);
                        clear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (query2 != null) {
                                    query2.getText().clear();
                                    clear.setVisibility(View.GONE);
                                    getHistory();
                                }
                            }
                        });
                    }
                } else {
                    if (clear != null) {
                        clear.setVisibility(View.GONE);
                        lv_history.setVisibility(View.VISIBLE);
                    }
                }
            }

            //         得到EditText的输入内容，并实时搜索
            @Override
            public void afterTextChanged(Editable s) {
                communityList.clear();
                if (!s.toString().trim().equals("") && s.toString().trim() != null) {
                    lv_search.setVisibility(View.VISIBLE);
                    adapterSearch = new BaseAdapter() {

                        @Override
                        public int getCount() {
                            return communityList.size();
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
                            View view = View.inflate(Community_search.this, R.layout.item_search, null);
                            TextView tv_search = ((TextView) view.findViewById(R.id.tv_search));
                            Community community = communityList.get(position);

                            tv_search.setText(community.getCommunityName());

                            return view;
                        }
                    };
                    lv_search.setAdapter(adapterSearch);
                    getCommunityList(s.toString().trim());
                    keySearch();
                } else {
                    lv_search.setVisibility(View.GONE);
                    if (!history.equals("")) {
                        btn_clear_history.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    //设置信息栏颜色
    private void setColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.xinxilan);
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
        query2.setFocusableInTouchMode(true);
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_search2:
                if (!query2.getText().toString().trim().equals("") && query2.getText().toString().trim() != null) {
                    query2.setFocusable(false);
                    query2.setFocusableInTouchMode(true);

                    Intent intent = new Intent(Community_search.this, Community_model.class);

                    if (communityList.size()!=0){
                        save();
                        getHistory();
                        btn_clear_history.setVisibility(View.GONE);
                        Community temp = communityList.get(0);

                        intent.putExtra("bundle", temp);
                        startActivity(intent);
                    }else {
                        Toast.makeText(Community_search.this,"暂无该社区",Toast.LENGTH_LONG).show();
                    }
                } else {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Community_search.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    Toast.makeText(Community_search.this, "搜索类容不能为空", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ib_return:
                finish();
                break;
            case R.id.btn_clear_history:
                cleanHistory(lv_history);
                break;
        }
    }

    public void getCommunityList(String search) {
        search = search.replace(" ", "%");
        RequestParams params = new RequestParams(HttpUtile.host+"/community/togetcommunity");
        params.addQueryStringParameter("name", search);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Log.i("onSuccess", result);
                Type type = new TypeToken<List<Community>>() {
                }.getType();
                List<Community> communityList1 = gson.fromJson(result, type);
                Log.e("看看数据====", gson.toString());
                communityList.addAll(communityList1);
                adapterSearch.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(Community_search.this, "无法连接服务器", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void getHistory() {
        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences("search_history1", 0);
        history = sp.getString("history", "");
        if (!history.equals("")) {
            btn_clear_history.setVisibility(View.VISIBLE);
            // 用逗号分割内容返回数组
            history_arr = history.split(",");
            for (int start = 0, end = history_arr.length - 1; start < end; start++, end--) {
                String temp = history_arr[end];
                history_arr[end] = history_arr[start];
                history_arr[start] = temp;
            }
            // 新建适配器，适配器数据为搜索历史文件内容
            arr_adapter = new ArrayAdapter<String>(this,
                    R.layout.item_search_history, history_arr);

            // 设置适配器
            lv_history.setAdapter(arr_adapter);
        } else {
            lv_history.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return 0;
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
                    return null;
                }
            });
        }
    }

    public void save() {
        // 获取搜索框信息
        String text = query2.getText().toString().trim();
        SharedPreferences mysp = getSharedPreferences("search_history1", 0);
        String old_text = mysp.getString("history", "");
        history_arr = old_text.split(",");

        if (history_arr.length > 9) {
            old_text = old_text.replace(history_arr[0] + ",", "");
            StringBuilder builder = new StringBuilder(old_text);
            SharedPreferences.Editor myeditor = mysp.edit();
            myeditor.putString("history", builder.toString());
            myeditor.commit();
        }

        // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
        StringBuilder builder = new StringBuilder(old_text);
        builder.append(text + ",");

        // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加

        if (!old_text.contains(text + ",")) {
            SharedPreferences.Editor myeditor = mysp.edit();
            myeditor.putString("history", builder.toString());
            myeditor.commit();
        } else {
            old_text = old_text.replace(text + ",", "");
            builder = new StringBuilder(old_text);
            builder.append(text + ",");
            SharedPreferences.Editor myeditor = mysp.edit();
            myeditor.putString("history", builder.toString());
            myeditor.commit();
        }
    }

    public void cleanHistory(View v) {
        SharedPreferences sp = getSharedPreferences("search_history1", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        getHistory();
        btn_clear_history.setVisibility(View.GONE);
    }

    public void keySearch() {

        query2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!query2.getText().toString().trim().equals("") && query2.getText().toString().trim() != null) {
                        Community temp = communityList.get(0);

                        Intent intent = new Intent(Community_search.this, Community_model.class);
                        if (temp.getCommunityName()!=null){
                        intent.putExtra("bundle", temp);
                        query2.getText().clear();
                        query2.setText(temp.getCommunityName());
                        query2.setSelection(temp.getCommunityName().length());
                        save();
                        getHistory();
                        btn_clear_history.setVisibility(View.GONE);
                        startActivity(intent);
                        }else{
                            startActivity(intent);
                        }
                    } else {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(Community_search.this.getCurrentFocus()
                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        Toast.makeText(Community_search.this, "搜索类容不能为空", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
    }


}