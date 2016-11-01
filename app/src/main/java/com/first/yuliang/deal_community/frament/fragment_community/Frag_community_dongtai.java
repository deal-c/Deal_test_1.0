package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.Community_Activity.ContentAdapter;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
import com.first.yuliang.deal_community.model.ComMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by yuliang on 2016/9/22.
 */
public class Frag_community_dongtai extends Fragment{
    private PullToRefreshListView pull_to_refresh_listview;
    private ListView lv_community_dongtai;
    private BaseAdapter mAdapter;
    private LinkedList<String> mListItems;
    public ArrayList<Dynamic> dynamicArrayList=new ArrayList<>();
    private String []eg=new String[]{"","","","","","","","","","",""};
    private String  jsondynamiclist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_community_dontai,null);
//         final int []imgs={R.drawable.dongtai1,R.drawable.dongtai2,R.drawable.dongtai3,R.drawable.dongtai4,R.drawable.dongtai5,};
        pull_to_refresh_listview = ((PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview));
        pull_to_refresh_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                new GetDataTask().execute();
            }

        });
        mAdapter=new BaseAdapter() {
            private TextView textView;
            private TextView tv_dongtai_content;

            private TextView tv_dongtai_usename;
            private ImageView iv_dongtai_userphoto;
            private ImageView iv_dongtai_dongtaiphoto;

            @Override
            public int getCount() {

                return dynamicArrayList.size();
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
                View view =View.inflate(getActivity().getApplicationContext(),R.layout.item_dongtai,null );

                iv_dongtai_userphoto = ((ImageView) view.findViewById(R.id.iv_dongtai_userphoto));
                tv_dongtai_usename = ((TextView) view.findViewById(R.id.tv_dongtai_usename));
                iv_dongtai_dongtaiphoto = ((ImageView) view.findViewById(R.id.iv_dongtai_dongtaiphoto));
                tv_dongtai_content = ((TextView) view.findViewById(R.id.tv_dongtai_content));
                textView = ((TextView) view.findViewById(R.id.textView));

                tv_dongtai_usename.setText(dynamicArrayList.get(position).getUserId().getUserName());
                tv_dongtai_content.setText(dynamicArrayList.get(position).getContent());
                textView.setText(dynamicArrayList.get(position).getPublishTime());
                x.image().bind(iv_dongtai_dongtaiphoto,HttpUtils.hostLuoqingshanSchool+"/usys/imgs/"+dynamicArrayList.get(position).getPic()+".png");
                x.image().bind(iv_dongtai_userphoto,HttpUtils.hostLuoqingshanSchool+"/usys/imgs/"+dynamicArrayList.get(position).getUserId().getUserImg()+".png");

                return view;


            }
        };

        lv_community_dongtai =pull_to_refresh_listview.getRefreshableView();

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(eg));
        getAlldynamic();
        lv_community_dongtai.setAdapter(mAdapter);
        lv_community_dongtai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  intent=new Intent(getActivity(), ComMainActivity.class);
                intent.putExtra("dynamicList", jsondynamiclist);
               startActivity(intent);



            }
        });


        return view;
    }

    private void getAlldynamic() {
        RequestParams params = new RequestParams
                (HttpUtils.hostLuoqingshanSchool+"/usys/Radomdynamic");//网络请求
        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {//使用xutils开启网络线程
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Dynamic>>() {
                }.getType();


                List<Dynamic>   communityList1=new ArrayList<>();
                communityList1  = gson.fromJson(result, type);

                if (result!=null){
                    jsondynamiclist=result;
                    dynamicArrayList.addAll(communityList1);
                    mAdapter.notifyDataSetChanged();
                    Log.e("dynamic看看数据====", dynamicArrayList.toString());
                    Log.e("dynamic看看数据====", gson.toString());
                }








            }

            @Override
            public void onError(Throwable ex, boolean
                    isOnCallback) {
                //Toast.makeText(Frag_community_guanzhu.this, ex.toString(), Toast.LENGTH_LONG).show();
                System.out.println(ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });





    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            return eg;
        }
        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            pull_to_refresh_listview.onRefreshComplete();
            super.onPostExecute(result);
        }
    }
    public static class myViewHolder{
        TextView    tv_dongtai_usename;
        TextView     tv_dongtai_content;
        TextView     textView;
        ImageView   iv_dongtai_userphoto;
        ImageView     iv_dongtai_dongtaiphoto;

    }

}