package com.first.yuliang.deal_community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String search=bundle.getString("search");
        Log.e("看看是不是传值过来==========",search);


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

                x.image().bind(iv_cg, "http://10.40.5.62:8080"+commodity.commodityImg);
                tv_cg.setText(commodity.commodityTitle);
                tv_price.setText(commodity.price+"");
                tv_local.setText(commodity.location);

                return view;
            }
        };
        gv_commodity_list.setAdapter(adapter_g);


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

                x.image().bind(iv_cg_l, "http://10.40.5.62:8080" + (commodity.commodityImg.split(","))[0]);
                tv_cg_l.setText(commodity.commodityTitle);
                tv_price_l.setText(commodity.price + "");
                tv_local_l.setText(commodity.location);

                return view;
            }
        };
        lv_commodity_list.setAdapter(adapter_l);

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
            params = new RequestParams("http://10.40.5.62:8080/csys/getcommodity?search="+ URLEncoder.encode(search,"utf-8"));
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