package com.first.yuliang.deal_community;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class CommodityActivity extends AppCompatActivity {

    private ListView lv_commodity;
    private BaseAdapter adapter_l;
    private BaseAdapter adapter_g;
    final List<CommodityBean.Commodity> commodityList = new ArrayList<CommodityBean.Commodity>();
    private GridView gv_commodity;
    private String[] imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);

        getCommodityList("7");

        View view = View.inflate(CommodityActivity.this,R.layout.commodity_head,null);

        View view_g = View.inflate(CommodityActivity.this,R.layout.commodity_footer,null);

        View view_like = View.inflate(CommodityActivity.this,R.layout.commodity_like,null);

        gv_commodity = ((GridView) view_g.findViewById(R.id.gv_commodity));

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
                View view = View.inflate(CommodityActivity.this,R.layout.item_commodity_list_g,null);

                iv_cg = ((ImageView) view.findViewById(R.id.iv_cg));
                tv_cg = ((TextView) view.findViewById(R.id.tv_cg));
                tv_price = ((TextView) view.findViewById(R.id.tv_price));
                tv_local = ((TextView) view.findViewById(R.id.tv_local));
                CommodityBean.Commodity commodity = commodityList.get(position);

                x.image().bind(iv_cg, "http://10.40.5.62:8080"+(commodity.commodityImg.split(","))[0]);
                tv_cg.setText(commodity.commodityTitle);
                tv_price.setText(commodity.price+"");
                tv_local.setText(commodity.location);

                return view;
            }
        };
        gv_commodity.setAdapter(adapter_g);

        lv_commodity = ((ListView) findViewById(R.id.lv_commodity));
        lv_commodity.addHeaderView(view);
        lv_commodity.addFooterView(view_like);
        lv_commodity.addFooterView(view_g);

        adapter_l = new BaseAdapter() {

            private ImageView iv_cg_c;

            @Override
            public int getCount() {
                return (imgs == null) ? 0 : imgs.length;
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
                View view = View.inflate(CommodityActivity.this, R.layout.commodity_img, null);

                iv_cg_c = ((ImageView) view.findViewById(R.id.iv_cg_c));

                String img = imgs[position];

                x.image().bind(iv_cg_c, "http://10.40.5.62:8080" + img);

                return view;
            }
        };
        lv_commodity.setAdapter(adapter_l);
    }
    public void getCommodityList(String search) {
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
                imgs = commodityList.get(0).commodityImg.split(",");
                Log.e("看图片",imgs.length+"");
                adapter_l.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(CommodityActivity.this,"是不是我的无法连接服务器",Toast.LENGTH_LONG).show();
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
