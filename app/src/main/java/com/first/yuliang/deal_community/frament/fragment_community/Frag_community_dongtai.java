package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.frament.Community_Activity.tieziactivity;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by yuliang on 2016/9/22.
 */
public class Frag_community_dongtai extends Fragment {
    private PullToRefreshListView pull_to_refresh_listview;
    private ListView lv_community_dongtai;
    private BaseAdapter mAdapter;
    private LinkedList<String> mListItems;

    private List<Post> postList=new ArrayList<>();

    public ArrayList<Dynamic> dynamicArrayList = new ArrayList<>();
    private String[] eg = new String[]{"", "", "", "", "", "", "", "", "", "", ""};
    private String jsondynamiclist;
    Dialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_community_dontai, null);
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

        mAdapter = new BaseAdapter() {
            private TextView ping_num;
            private TextView dongtai_time;
            private TextView dongtai_title;
            private TextView textView;
            private TextView tv_dongtai_content;

            private TextView tv_dongtai_usename;
            private ImageView iv_dongtai_userphoto;
            private ImageView iv_dongtai_dongtaiphoto;

            @Override
            public int getCount() {

                return postList.size();
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
                View view = View.inflate(getActivity(), R.layout.item_dongtai, null);

                iv_dongtai_userphoto = ((ImageView) view.findViewById(R.id.iv_dongtai_userphoto));
                tv_dongtai_usename = ((TextView) view.findViewById(R.id.tv_dongtai_usename));
                iv_dongtai_dongtaiphoto = ((ImageView) view.findViewById(R.id.iv_dongtai_dongtaiphoto));
                tv_dongtai_content = ((TextView) view.findViewById(R.id.tv_dongtai_content));
                dongtai_title = ((TextView) view.findViewById(R.id.tv_dongtai_title));
                dongtai_time = ((TextView) view.findViewById(R.id.dongtai_time));

                Post post=postList.get(position);
                tv_dongtai_usename.setText(post.getUser().getUserName());
                tv_dongtai_content.setText(post.getPostInfo());
                String time=  com.first.yuliang.deal_community.Util.DateUtils.dateToString(com.first.yuliang.deal_community.Util.DateUtils.stringToDate(post.getPostTime(),"yyyy-MM-dd hh:mm:ss"),"MM月dd日 hh:mm");

                dongtai_time.setText(time);
                dongtai_title.setText(post.getPostTitle());
                ImageOptions options=new ImageOptions.Builder()
                        .setImageScaleType(CENTER_CROP)
                        .build();
                ImageOptions options1=new ImageOptions.Builder()
                        .setImageScaleType(CENTER_CROP)
                        .setFailureDrawableId(R.drawable.head_)
                        .setLoadingDrawableId(R.drawable.head_)
                        .setCircular(true)
                        .build();

                x.image().bind(iv_dongtai_dongtaiphoto,HttpUtile.yu+post.getImgs(),options);
                x.image().bind(iv_dongtai_userphoto,HttpUtile.zy1+post.getUser().getUserImg(),options1);

                return view;


            }
        };





        lv_community_dongtai = pull_to_refresh_listview.getRefreshableView();

        mListItems = new LinkedList<String>();

        getAlldynamic();
        lv_community_dongtai.setAdapter(mAdapter);
        lv_community_dongtai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), tieziactivity.class);

                intent.putExtra("post",postList.get(position-1));

                startActivity(intent);


            }
        });


        return view;
    }

    private void getAlldynamic() {
        progressDialog = ToolsClass.createLoadingDialog(getActivity(), "加载中...", true,
                0);
        progressDialog.show();
        RequestParams params = new RequestParams
                (HttpUtile.yu + "/community/togetallpost");//网络请求
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Type type=new TypeToken<List<Post>>(){}.getType();
                Gson gson =new Gson();
               postList=gson.fromJson(result,type);
                mAdapter.notifyDataSetChanged();
                ToastUtil.show(getActivity(),"访问成功");

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(getActivity(),"访问失败");
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
            getAlldynamic();
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            pull_to_refresh_listview.onRefreshComplete();
            super.onPostExecute(result);
        }
    }



}