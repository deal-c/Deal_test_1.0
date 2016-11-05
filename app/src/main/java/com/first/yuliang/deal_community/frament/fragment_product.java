package com.first.yuliang.deal_community.frament;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.Util.DateUtils;
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

/**
 * Created by Administrator on 2016/11/1.
 */

public class fragment_product extends Fragment {

    private ListView lv_recent_product;
    BaseAdapter adapter;

    int id=0;


    Dialog progressDialog;
    List<product_h_tb> product_h_tbList=new ArrayList<>();
    List<Product> productList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recent_product, null);

        lv_recent_product = ((ListView) view.findViewById(R.id.lv_recent_product));

        id = getActivity().getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);


        if (id == 0) {
            Toast.makeText(getActivity(), "您尚未登录呦", Toast.LENGTH_SHORT).show();
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


                    View view = View.inflate(getActivity(), R.layout.activity_publish_lv_item, null);

                    TextView tv_publish_product_name = ((TextView) view.findViewById(R.id.tv_publish_product_name));
                    TextView tv_publish_product_price = ((TextView) view.findViewById(R.id.tv_publish_product_price));
                    TextView tv_publish_product_class = ((TextView) view.findViewById(R.id.tv_publish_product_class));
                    TextView tv_publish_product_desc = ((TextView) view.findViewById(R.id.tv_publish_product_desc));
                    TextView tv_publish_product_time = ((TextView) view.findViewById(R.id.tv_publish_product_time));
                    ImageView iv_publish_product = ((ImageView) view.findViewById(R.id.iv_publish_product));
                    Product product = productList.get(position);

                    tv_publish_product_name.setText(product.getProductTitle());
                    tv_publish_product_price.setText(product.getPrice() + "");
                    tv_publish_product_class.setText(product.getProductClass());
                    tv_publish_product_time.setText(DateUtils.dateToString(product.getReleaseTime()));
                    tv_publish_product_desc.setText(product.getProductDescribe());
                    x.image().bind(iv_publish_product, HttpUtile.szj + product.getProductImg());


                    return view;


                }
            };


            lv_recent_product.setAdapter(adapter);
            getAllProduct();


        }
        return view;
    }


    private void getAllProduct() {


        progressDialog = ToolsClass.createLoadingDialog(getActivity(),"加载中...", true,
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

                Toast.makeText(getActivity(),"网络访问失败",Toast.LENGTH_LONG).show();
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
