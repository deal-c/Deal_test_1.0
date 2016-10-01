package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.Community_Activity.ContentAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by yuliang on 2016/9/22.
 */
public class Frag_community_dongtai extends Fragment{
    private PullToRefreshListView pull_to_refresh_listview;
    private ListView lv_community_dongtai;
    private BaseAdapter mAdapter;
    private LinkedList<String> mListItems;

    private String []eg=new String[]{"","","","","","","","","","",""};


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
            private ImageView iv_dongtai_dongtaiphoto;

            @Override
            public int getCount() {

                return mListItems.size();
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
              /*  myViewHolder   holder=new myViewHolder();


               if (convertView==null){
                holder.iv_dongtai_dongtaiphoto=(ImageView) convertView.findViewById(R.id.iv_dongtai_dongtaiphoto);
                holder.iv_dongtai_userphoto=(ImageView)convertView.findViewById(R.id.iv_dongtai_userphoto);
                holder.textView=(TextView)convertView.findViewById(R.id.textView);
                holder.tv_dongtai_usename=(TextView)convertView.findViewById(R.id.tv_dongtai_usename);
                holder.tv_dongtai_content=(TextView)convertView.findViewById(R.id.tv_dongtai_content);
                convertView.setTag(holder);}
            else {
                   holder = (myViewHolder) convertView.getTag();
                }

*/


                return view;


            }
        };

        lv_community_dongtai =pull_to_refresh_listview.getRefreshableView();

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(eg));
        lv_community_dongtai.setAdapter(mAdapter);
        return view;
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
