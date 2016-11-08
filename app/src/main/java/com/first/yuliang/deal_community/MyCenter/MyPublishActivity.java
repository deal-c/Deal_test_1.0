package com.first.yuliang.deal_community.MyCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyPublishActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView lv_publish;
    private BaseAdapter adapter;
    int userId=0;
    List<Product> productList=new ArrayList<>();
    private ImageView iv_publish_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);

        Intent intent=getIntent();
        userId=Integer.parseInt(intent.getStringExtra("userId").toString().trim());
        Log.e("userId","$$$$$$$$$$$$$$$"+userId);

        lv_publish = ((ListView) findViewById(R.id.lv_publish));
        iv_publish_back = ((ImageView) findViewById(R.id.iv_publish_back));
        iv_publish_back.setOnClickListener(this);

        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return productList.size();
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
                View view=View.inflate(getApplicationContext(),R.layout.activity_publish_lv_item,null);

                TextView tv_publish_product_name=((TextView) view.findViewById(R.id.tv_publish_product_name));
                TextView tv_publish_product_price=((TextView) view.findViewById(R.id.tv_publish_product_price));
                TextView tv_publish_product_class=((TextView) view.findViewById(R.id.tv_publish_product_class));
                TextView tv_publish_product_desc=((TextView) view.findViewById(R.id.tv_publish_product_desc));
                TextView tv_publish_product_time=((TextView) view.findViewById(R.id.tv_publish_product_time));
                ImageView iv_publish_product=((ImageView)view.findViewById(R.id.iv_publish_product));
                Product product=productList.get(position);

                tv_publish_product_name.setText(product.getProductTitle());
                tv_publish_product_price.setText(product.getPrice()+"");
                tv_publish_product_class.setText(product.getProductClass());
                tv_publish_product_time.setText(DateUtils.dateToString(product.getReleaseTime()));
                tv_publish_product_desc.setText(product.getProductDescribe());
                x.image().bind(iv_publish_product,HttpUtile.szj+product.getProductImg());


                return view;
            }
        };


        lv_publish.setAdapter(adapter);


        getAllProductInfo();



    }

    private void getAllProductInfo() {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectAllProductServlet");
        params.addBodyParameter("userId",userId+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<Product>>(){}.getType();
                List<Product> newProductList=new ArrayList<Product>();
                newProductList=gson.fromJson(result,type);
                productList.clear();
                productList.addAll(newProductList);

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.iv_publish_back:

                Intent intentData=new Intent();

                Log.e("num","+++++++++++"+productList.size());
                intentData.putExtra("num",productList.size());
                MyPublishActivity.this.setResult(5,intentData);
                MyPublishActivity.this.finish();
                break;
        }
    }
}
