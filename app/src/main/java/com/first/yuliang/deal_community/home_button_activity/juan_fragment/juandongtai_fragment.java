package com.first.yuliang.deal_community.home_button_activity.juan_fragment;

import android.app.Dialog;
import android.app.Fragment;
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

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.first.yuliang.deal_community.pojo.Product;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuliang on 2016/11/2.
 */

public class juandongtai_fragment extends Fragment {
    private ListView juan_listview;
    private BaseAdapter baseAdapter;
    private List<Product>junlist=new ArrayList<Product>();
    private List<User>userList=new ArrayList<User>();
    Dialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jun_dongtai_fragment, null);
        juan_listview = ((ListView) view.findViewById(R.id.juan_dongtai_listview));

         baseAdapter=new BaseAdapter() {
             private ImageView pic;

             @Override
             public int getCount() {
                 Log.e("getCount", "getCount: "+junlist.size());
                 return junlist.size();
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
                 View v=View.inflate(getActivity(),R.layout.jun_lv_item,null);

                 Log.e("getCount", "userListCount: "+userList.size());
                          Product product=junlist.get(position);
                        ((TextView) v.findViewById(R.id.tv_ganxiename)).setText(product.getReleaseUser().getUserName());
                 ((TextView) v.findViewById(R.id.tv_junproname)).setText(product.getProductTitle());
                 pic = ((ImageView) v.findViewById(R.id.juan_pic));
                 x.image().bind(pic,HttpUtile.szj+product.getProductImg());
                 return v;
             }
         };
        juan_listview.setAdapter(baseAdapter);
        progressDialog = ToolsClass.createLoadingDialog(getActivity(), "加载中...", true,
                0);
        progressDialog.show();
        getdogtai();

        return view;

    }
    private void getdogtai(){
        RequestParams params=new RequestParams(HttpUtile.zy+"/togetjuan");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getCount", "onSuccess: "+result);
                Gson gson=new Gson();

                Type type=new TypeToken<List<Product>>(){}.getType();
                junlist=gson.fromJson(result,type);
                Log.e("getCount", "onSuccess: "+result);
                Log.e("getCount", "onSuccess: "+ junlist.size());
                baseAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(getActivity(),"加载出错");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        });

    }


}
