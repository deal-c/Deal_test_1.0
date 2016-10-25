package com.first.yuliang.deal_community;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.MyCenter.modify.ModifyDetail.addAddressDetailActivity;
import com.first.yuliang.deal_community.address.City;
import com.first.yuliang.deal_community.address.County;
import com.first.yuliang.deal_community.address.Province;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener{

    int llHeight;
    private ImageButton ib_return_search;
    private EditText query3;
    private Intent intent;
    private GridView gv_commodity_list;
    private ListView lv_commodity_list;
    private BaseAdapter adapter_g;
    private BaseAdapter adapter_l;
    final List<CommodityBean.Commodity> commodityList = new ArrayList<CommodityBean.Commodity>();
    private ProgressBar pb_load_commodity;
    private TextView tv_null;
    boolean isGrid;
    private ImageButton ib_list;
    private ImageButton ib_search3;
    private TextView tv_total;
    private LinearLayout ll_total;
    private List<Province> provinces = new ArrayList<Province>();
    private Button btn_quyu;
    private LinearLayout ll_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        final String search=bundle.getString("search");
        Log.e("看看是不是传值过来==========",search);

//        ll_ss = findViewById(R.id.ll_ss);
//
//        btn_ss = ((Button) findViewById(R.id.btn_ss));
//        btn_ss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SearchResultActivity.this,"试试",Toast.LENGTH_SHORT);
//            }
//        });

        tv_total = ((TextView) findViewById(R.id.tv_total));
        pb_load_commodity = ((ProgressBar) findViewById(R.id.pb_load_commodity));

        ib_return_search = ((ImageButton) findViewById(R.id.ib_return_search));
        ib_return_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("search"," ");
                Intent intent1 = new Intent();
                intent1.putExtra("bundle",bundle1);
                SearchResultActivity.this.setResult(0,intent1);
                SearchResultActivity.this.finish();
            }
        });
        //        EditText获得焦点后跳转
        query3 = ((EditText)findViewById(R.id.query3));
        query3.setText(search);
        query3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    query3.setFocusable(false);
                    query3.setFocusableInTouchMode(true);
                    setResult(0,intent);
                    SearchResultActivity.this.finish();
                }
            }
        });
        tv_null = ((TextView) findViewById(R.id.tv_null));
        getCommodityList(search);
        gv_commodity_list = ((GridView) findViewById(R.id.gv_commodity_list));

        adapter_g = new BaseAdapter() {
            private TextView tv_local;
            private TextView tv_price;
            private TextView tv_cg;
            private ImageView iv_cg;

            @Override
            public int getCount() {
                return  commodityList.size();
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
                View view = View.inflate(SearchResultActivity.this,R.layout.item_commodity_list_g,null);

                iv_cg = ((ImageView) view.findViewById(R.id.iv_cg));
                tv_cg = ((TextView) view.findViewById(R.id.tv_cg));
                tv_price = ((TextView) view.findViewById(R.id.tv_price));
                tv_local = ((TextView) view.findViewById(R.id.tv_local));
                CommodityBean.Commodity commodity = commodityList.get(position);

                x.image().bind(iv_cg, "http://192.168.191.1:8080"+(commodity.commodityImg.split(","))[0]);
                tv_cg.setText(commodity.commodityTitle);
                tv_price.setText(commodity.price+"");
                tv_local.setText(commodity.location);

                return view;
            }
        };
        gv_commodity_list.setAdapter(adapter_g);

        gv_commodity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchResultActivity.this, CommodityActivity.class);
                CommodityBean.Commodity temp = commodityList.get(position);
                intent.putExtra("search",search);
                intent.putExtra("bundle", temp);
                startActivity(intent);
            }
        });

        lv_commodity_list = ((ListView) findViewById(R.id.lv_commodity_list));
        adapter_l = new BaseAdapter() {
            private TextView tv_local_l;
            private TextView tv_price_l;
            private TextView tv_cg_l;
            private ImageView iv_cg_l;

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
                View view = View.inflate(SearchResultActivity.this, R.layout.item_commodity_list_l, null);

                iv_cg_l = ((ImageView) view.findViewById(R.id.iv_cg_l));
                tv_cg_l = ((TextView) view.findViewById(R.id.tv_cg_l));
                tv_price_l = ((TextView) view.findViewById(R.id.tv_price_l));
                tv_local_l = ((TextView) view.findViewById(R.id.tv_local_l));
                CommodityBean.Commodity commodity = commodityList.get(position);

                x.image().bind(iv_cg_l, "http://192.168.191.1:8080" + (commodity.commodityImg.split(","))[0]);
                tv_cg_l.setText(commodity.commodityTitle);
                tv_price_l.setText(commodity.price + "");
                tv_local_l.setText(commodity.location);

                return view;
            }
        };
        lv_commodity_list.setAdapter(adapter_l);

        lv_commodity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchResultActivity.this, CommodityActivity.class);
                CommodityBean.Commodity temp = commodityList.get(position);
                intent.putExtra("search",search);
                intent.putExtra("bundle", temp);
                startActivity(intent);
            }
        });

        ll_total = ((LinearLayout) findViewById(R.id.ll_total));
        ll_total.measure(0,0);
        llHeight = ll_total.getMeasuredHeight();
        gv_commodity_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollHeight = 0;
                View c = view.getChildAt(0);//获得listview第一个item
                if (c == null) {
                    scrollHeight = 0;
                    return;
                }
                int firstVisiblePosition = view.getFirstVisiblePosition();
                int top = c.getTop();
                scrollHeight =  -top + firstVisiblePosition * c.getHeight() ;
                ll_total.setPadding(0, (int)(0-scrollHeight),0,0);
            }
        });
        lv_commodity_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollHeight = 0;
                View c = view.getChildAt(0);//获得listview第一个item
                if (c == null) {
                    scrollHeight = 0;
                    return;
                }
                int firstVisiblePosition = view.getFirstVisiblePosition();
                int top = c.getTop();
                scrollHeight =  -top + firstVisiblePosition * c.getHeight() ;
                ll_total.setPadding(0, (int)(-scrollHeight),0,0);
            }
        });

        ib_search3 = ((ImageButton) findViewById(R.id.ib_search3));
        ib_search3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("search"," ");
                Intent intent1 = new Intent();
                intent1.putExtra("bundle",bundle1);
                SearchResultActivity.this.setResult(0,intent1);
                SearchResultActivity.this.finish();
            }
        });
        ib_list = ((ImageButton) findViewById(R.id.ib_list));
        getHistory();
        if (isGrid){
            ib_list.setImageResource(R.drawable.type);
            gv_commodity_list.setVisibility(View.GONE);
            lv_commodity_list.setVisibility(View.VISIBLE);
        }else {
            ib_list.setImageResource(R.drawable.line);
            gv_commodity_list.setVisibility(View.VISIBLE);
            lv_commodity_list.setVisibility(View.GONE);
        }
        ib_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHistory();
                if (isGrid){
                    ib_list.setImageResource(R.drawable.line);
                    gv_commodity_list.setVisibility(View.VISIBLE);
                    lv_commodity_list.setVisibility(View.GONE);
                    save(false);
                }else {
                    ib_list.setImageResource(R.drawable.type);
                    gv_commodity_list.setVisibility(View.GONE);
                    lv_commodity_list.setVisibility(View.VISIBLE);
                    save(true);
                }
            }
        });
        ll_select = ((LinearLayout) findViewById(R.id.ll_select));
        btn_quyu = ((Button) findViewById(R.id.btn_quyu));
        btn_quyu.setOnClickListener(this);

    }
    @Override
    public void onBackPressed() {
        Bundle bundle1 = new Bundle();
        bundle1.putString("search"," ");
        Intent intent1 = new Intent();
        intent1.putExtra("bundle",bundle1);
        SearchResultActivity.this.setResult(0,intent1);
        SearchResultActivity.this.finish();
    }

    public void getCommodityList(String search) {
        pb_load_commodity.setVisibility(View.VISIBLE);
        search = search.replace(" ","%");
        RequestParams params = null;
        try {
            params = new RequestParams("http://192.168.191.1:8080/csys/getcommodity?search="+ URLEncoder.encode(search,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommodityBean bean = gson.fromJson(result, CommodityBean.class);
                commodityList.addAll(bean.commodities);
                pb_load_commodity.setVisibility(View.GONE);
                ll_total.setVisibility(View.VISIBLE);
                if(commodityList.toString().equals("[]")){
                    tv_null.setVisibility(View.VISIBLE);
                }
                Log.e("+++++++++++++++", String.valueOf(commodityList.size()));
                tv_total.setText(String.valueOf(commodityList.size()));
                adapter_g.notifyDataSetChanged();
                adapter_l.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SearchResultActivity.this,"是不是我的无法连接服务器",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public void save(boolean flag) {
        SharedPreferences preferences = getSharedPreferences(
                "commodity_show_config", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isGrid", flag);
        editor.commit();
    }
    public void getHistory() {
        SharedPreferences preferences = getSharedPreferences("commodity_show_config",
                MODE_PRIVATE);
        isGrid = preferences.getBoolean("isGrid", true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddressDialog() {

        new MyCity(SearchResultActivity.this, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {

                    @Override
                    public void onPicked(com.first.yuliang.deal_community.address.Province selectProvince, City selectCity, County selectCounty) {

                        StringBuilder address = new StringBuilder();
                        address.append(
                                selectProvince != null ? selectProvince
                                        .getAreaName() : "")
                                .append(selectCity != null ? selectCity
                                        .getAreaName() : "")
                                .append(selectCounty != null ? selectCounty
                                        .getAreaName() : "");
                        String text = selectCounty != null ? selectCounty
                                .getAreaName() : "";
                        Log.e("地址=============",address+"");
                    }
                },261).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_quyu:
//                Log.e("看看筛选======",ll_select.getBottom()+"");
                if (provinces.size() > 0) {
                    showAddressDialog();
                } else {
                    new InitAreaTask(SearchResultActivity.this).execute(0);
                }
                break;
        }
    }

    private class InitAreaTask  extends AsyncTask<Integer, Integer, Boolean> {


        Context mContext;

        Dialog progressDialog;



        public InitAreaTask(Context context) {
            mContext = context;
            progressDialog = ToolsClass.createLoadingDialog(mContext, "请稍等...", true,
                    0);
        }

        @Override
        protected void onPreExecute() {

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (provinces.size() > 0) {
                showAddressDialog();
            } else {
                Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            String address = null;
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("address.txt");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                address = EncodingUtils.getString(arrayOfByte, "UTF-8");
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i),
                                Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

    }
}



//
//                          █ █ █ █ █ █ █
//                    █ █ █ █ █ █ █ █ █ █ █
//                 █ █ █ █ █ █ █ █ █ █ █ █ █
//              █ █ █ █    █    █    █    █ █ █ █
//           █ █ █ █          █    █             █ █ █
//           █ █ █                                     █ █ █
//        █ █ █                                           █ █
//     █ █ █ █                                        █    █ █
//        █ █                                              █ █ █
//     █ █ █ █                                              █ █ █
//     █ █ █                                              █ █ █
//     █ █ █                                                 █ █ █
//  █    █ █    █                                              █ █
//  █    █ █       █ █ █                █ █ █ █ █    █ █ █
//  █    █    █    █ █ █ █          █ █ █    █    █    █ █
//  █             █ █    █    █       █    █ █    █    █    █
//  █             █             █ █ █ █             █          █
//     █ █                      █       █ █       █       █ █
//     █    █          █ █ █          █    █ █             █
//        █    █                         █                   █ █
//        █ █    █                      █                █ █
//           █ █                   █    █ █          █    █
//           █    █                   █ █                █
//           █ █    █                         █       █    █
//              █ █             █ █ █ █ █       █    █
//              █    █    █                            █ █
//                 █    █             █ █    █    █ █ █
//                 █ █    █                      █    █ █ █
//                 █ █ █    █    █    █    █    █ █ █ █ █ █
//                 █ █ █ █    █    █    █    █ █ █ █ █ █ █ █ █
//              █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █
//        █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █
//     █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █