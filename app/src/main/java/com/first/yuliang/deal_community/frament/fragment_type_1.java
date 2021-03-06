package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.SearchCommodityActivity;
import com.first.yuliang.deal_community.SearchResultActivity;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Sub_Type_Bean;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;



/**
 *
 * Created by Administrator on 2016/9/19.
 */
public class fragment_type_1 extends Fragment {

    //private TextView tv_fragment1;
  // private List<Sub_Type_Bean.Sub_Type> sub_Types;
   //private ListView lv_fragment1;
    private GridView gv_fragment1;
    private ProgressBar pb_fenlei;
    private BaseAdapter adapter;
    private List<Sub_Type_Bean.Sub_Type> sub_typess=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_type_1, null);
        //lv_fragment1 = ((ListView) view.findViewById(R.id.lv_fragment1));
        gv_fragment1=((GridView)view.findViewById(R.id.gv_fragment1));
        //tv_fragment1 = ((TextView) view.findViewById(R.id.tv_fragment1));
        pb_fenlei=((ProgressBar)view.findViewById(R.id.pb_fenlei));

        gv_fragment1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("search",sub_typess.get(position).sub_type_name);
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();

        int Id = bundle.getInt("Id");

        adapter = new BaseAdapter() {


            @Override
            public int getCount() {
                return sub_typess.size();
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
                View view = View.inflate(getActivity(), R.layout.fragment_type_1_1, null);

                TextView tv_type_1_1 = ((TextView) view.findViewById(R.id.tv_cg));

                ImageView iv_type_1_1 = ((ImageView) view.findViewById(R.id.iv_cg));
                String str=sub_typess.get(position).sub_type_name;
                String str2=sub_typess.get(position).sub_type_pic;

                tv_type_1_1.setText(str);
                x.image().bind(iv_type_1_1, HttpUtile.zy1+str2);


                return  view;
            }
        };

        gv_fragment1.setAdapter(adapter);


        getSub_typeList(Id);


    }




    private void getSub_typeList(int Id) {
        pb_fenlei.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/sub_type_servlet?typeId="+Id);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Sub_Type_Bean stb= gson.fromJson(result,Sub_Type_Bean.class);
                sub_typess.addAll(stb.sub_types);

                adapter.notifyDataSetChanged();
                pb_fenlei.setVisibility(View.GONE);
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
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
