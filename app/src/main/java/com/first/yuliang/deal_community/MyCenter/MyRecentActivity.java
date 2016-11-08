package com.first.yuliang.deal_community.MyCenter;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.fragment_product;
import com.first.yuliang.deal_community.frament.fragment_tiezi;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Product;
import com.first.yuliang.deal_community.pojo.product_h_tb;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyRecentActivity extends AppCompatActivity {

    private ListView lv_recent;
    BaseAdapter adapter;

    int id=0;


    Dialog progressDialog;
    List<product_h_tb> product_h_tbList=new ArrayList<>();
    List<Product> productList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recent);

        lv_recent = ((ListView) findViewById(R.id.lv_recent));

        id = this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);


        if (id == 0) {
            Toast.makeText(this, "您尚未登录呦", Toast.LENGTH_SHORT).show();
        } else {


            adapter = new BaseAdapter() {

                @Override
                public int getCount() {
                    Log.e("ooooo", "+++" + productList.size());
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


                    View view = View.inflate(MyRecentActivity.this, R.layout.activity_publish_lv_item, null);

                    TextView tv_publish_product_name = ((TextView) view.findViewById(R.id.tv_publish_product_name));
                    TextView tv_publish_product_price = ((TextView) view.findViewById(R.id.tv_publish_product_price));
                    TextView tv_publish_product_class = ((TextView) view.findViewById(R.id.tv_publish_product_class));
                    TextView tv_publish_product_time = ((TextView) view.findViewById(R.id.tv_publish_product_time));
                    ImageView iv_publish_product = ((ImageView) view.findViewById(R.id.iv_publish_product));
                    Product product = productList.get(position);

                    tv_publish_product_name.setText(product.getProductTitle());
                    tv_publish_product_price.setText(product.getPrice() + "");
                    tv_publish_product_class.setText(product.getProductClass());
                    tv_publish_product_time.setText(DateUtils.dateToString(product.getReleaseTime()));
                    x.image().bind(iv_publish_product, HttpUtile.yu + product.getProductImg());

                    return view;
                }
            };
            lv_recent.setAdapter(adapter);
            getAllProduct();


        }
    }


    private void getAllProduct() {


        progressDialog = ToolsClass.createLoadingDialog(this,"加载中...", true,
                0);
        progressDialog.show();

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectHistoryServlet");
        params.addBodyParameter("id",id+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<product_h_tb>>(){}.getType();
                List<product_h_tb> newproductList=new ArrayList<product_h_tb>();
                newproductList= gson.fromJson(result,type);

                Log.e("ooooo","**********"+newproductList.size());

                for(int i=0;i<newproductList.size()-1;i++)
                {
                    for(int j=0;j<newproductList.size()-i-1;j++)
                    {
                        if( newproductList.get(i).getCommodity_b_h_id()>newproductList.get(j).getCommodity_b_h_id())
                        {
                            int flag=0;
                            flag=newproductList.get(i).getCommodity_b_h_id();
                            newproductList.get(i).setCommodity_b_h_id(newproductList.get(j).getCommodity_b_h_id());
                            newproductList.get(j).setCommodity_b_h_id(flag);

                        }
                    }

                }


                product_h_tbList.clear();
                product_h_tbList.addAll(newproductList);
                productList.clear();
                for(int n=0;n<newproductList.size();n++)
                {
                    Product product=newproductList.get(n).getProduct();

                    productList.add(product);
                }

                Log.e("ooooo","%%%%%%%%%%%%%"+productList.size());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Toast.makeText(MyRecentActivity.this,"网络访问失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                progressDialog.dismiss();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });

    }


}