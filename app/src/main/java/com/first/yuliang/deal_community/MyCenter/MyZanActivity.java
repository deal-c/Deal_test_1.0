package com.first.yuliang.deal_community.MyCenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Product;
import com.first.yuliang.deal_community.pojo.ProductCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyZanActivity extends AppCompatActivity {

    private ListView lv_my_store;
    BaseAdapter adapter;

    List<ProductCollection> productCollectionList=new ArrayList<>();
    List<Product> productList=new ArrayList<>();


    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_zan);
        id=getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);

        lv_my_store = ((ListView) findViewById(R.id.lv_my_store));

        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return productCollectionList.size();
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

                View view = View.inflate(MyZanActivity.this, R.layout.activity_mycoolection_item, null);
                TextView tv_collect_product_name = ((TextView) view.findViewById(R.id.tv_collect_product_name));
                ImageView iv_collect_product = ((ImageView) view.findViewById(R.id.iv_collect_product));
                TextView tv_collect_product_price = ((TextView) view.findViewById(R.id.tv_collect_product_price));
                TextView tv_collect_product_class = ((TextView) view.findViewById(R.id.tv_collect_product_class));
                TextView tv_collect_product_desc = ((TextView) view.findViewById(R.id.tv_collect_product_desc));
                TextView tv_collect_product_time = ((TextView) view.findViewById(R.id.tv_collect_product_time));

                Product product=productList.get(position);
                tv_collect_product_name.setText(product.getProductTitle());
                tv_collect_product_price.setText(product.getPrice()+"");
                tv_collect_product_class.setText(product.getProductClass());
                tv_collect_product_time.setText(DateUtils.dateToString(product.getReleaseTime()));
                tv_collect_product_desc.setText(product.getProductDescribe());
                x.image().bind(iv_collect_product,HttpUtile.szj+product.getProductImg());

                return view;
            }
        };
        lv_my_store.setAdapter(adapter);

        getAllCollections();

    }



    private void getAllCollections() {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectCollectionServlet");

        params.addBodyParameter("id",id+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<ProductCollection>>(){}.getType();
                List<ProductCollection> newproductCollectionList=new ArrayList<ProductCollection>();
                newproductCollectionList= gson.fromJson(result,type);

                for(int i=0;i<newproductCollectionList.size()-1;i++)
                {
                    for(int j=0;j<newproductCollectionList.size()-i-1;j++)
                    {
                        if( newproductCollectionList.get(i).getCommodityCollectionId()>newproductCollectionList.get(j).getCommodityCollectionId())
                        {
                            int flag=0;
                            flag=newproductCollectionList.get(i).getCommodityCollectionId();
                            newproductCollectionList.get(i).setCommodityCollectionId(newproductCollectionList.get(j).getCommodityCollectionId());
                            newproductCollectionList.get(j).setCommodityCollectionId(flag);

                        }
                    }

                }
                productCollectionList.clear();
                productCollectionList.addAll(newproductCollectionList);
                productList.clear();
                for(int n=0;n<newproductCollectionList.size();n++)
                {
                    Product product=newproductCollectionList.get(n).getProduct();

                    productList.add(product);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MyZanActivity.this,"是不是无法插入收藏",Toast.LENGTH_LONG).show();
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
