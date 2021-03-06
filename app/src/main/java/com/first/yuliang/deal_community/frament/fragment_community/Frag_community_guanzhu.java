package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.Community_Activity.Community_model;
import com.first.yuliang.deal_community.frament.Community_Activity.Community_search;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Community;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuliang on 2016/9/22.
 */
public class Frag_community_guanzhu extends Fragment {
//哼
    List<Community> comlist = new ArrayList<Community>();
    private GridView comlist_care;
    private BaseAdapter madapter;

    private TextView result1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_community_guanzhu, null);
        comlist_care = ((GridView) view.findViewById(R.id.comlist_care));

        result1 = ((TextView) view.findViewById(R.id.result));




        madapter = new BaseAdapter() {
            private TextView name;

            @Override
            public int getCount() {
                return comlist.size() + 1;
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
                if (position == comlist.size()) {
                    View view = View.inflate(getActivity(), R.layout.gview_com_item, null);
                    name = ((TextView) view.findViewById(R.id.tv_SheQuName));
                    name.setText("+");
                    name.setTextSize(25);
                    name.setGravity(Gravity.CENTER);

                    return view;
                } else {
                    View view = View.inflate(getActivity(), R.layout.gview_com_item, null);
                    Community com = comlist.get(position);
                    name = ((TextView) view.findViewById(R.id.tv_SheQuName));
                    name.setText(com.getCommunityName());

                    return view;
                }
            }
        };

        comlist_care.setAdapter(madapter);

        comlist_care.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == comlist.size()) {
                    Intent intent = new Intent(getActivity(), Community_search.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), Community_model.class);
                    Community temp = comlist.get(position);
                    intent.putExtra("bundle", temp);
                    startActivity(intent);
                }
            }
        });
       comlist_care.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (comlist.size()==position){
                return false;
            }else {
                initDialog(position);
                return true;
            }
           }
       });
        getcomlist();

        return view;
    }
    private void initDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否取消关注？"); //设置内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭dialog
                Toast.makeText(getActivity(), "已取消关注", Toast.LENGTH_SHORT).show();
                cancleCare(position);
                comlist.remove(position);
                madapter.notifyDataSetChanged();


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    void cancleCare(int position){
        int id = getActivity().getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);
        RequestParams params = new RequestParams(HttpUtile.yu + "/community/manegecarecom");
        params.addQueryStringParameter("flag", "delete");
        params.addQueryStringParameter("userId", id + "");
        params.addQueryStringParameter("communityId", + comlist.get(position).getCommunityId()+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "取关失败", Toast.LENGTH_LONG).show();
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
    public void onResume() {
        super.onResume();
        getcomlist();
    }

    //根据userid 获得关注的社区List
    void getcomlist() {
        int id=getActivity().getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);

            if (id!=0) {
                result1.setVisibility(View.GONE);

            RequestParams params = new RequestParams(HttpUtile.yu + "/community/manegecarecom");

            params.addQueryStringParameter("flag", "getall");
            params.addQueryStringParameter("userId",id+ "");

            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.e("onSuccess", result);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Community>>() {
                    }.getType();
                    comlist = gson.fromJson(result, type);
                    if (comlist.size() == 0) {

                        result1.setVisibility(View.VISIBLE);
                        result1.setText("还没有关注哦！");
                        madapter.notifyDataSetChanged();
                    } else {
                        result1.setVisibility(View.GONE);
                        madapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getActivity(), "加载出错", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }else{
            result1.setVisibility(View.VISIBLE);
            result1.setText("还未登录！");

        }
    }

}
