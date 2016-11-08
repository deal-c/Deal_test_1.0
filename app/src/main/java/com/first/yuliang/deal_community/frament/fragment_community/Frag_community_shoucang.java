package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.frament.Community_Activity.tieziactivity;
import com.first.yuliang.deal_community.frament.pojo.Post;
import com.first.yuliang.deal_community.frament.pojo.PostCollection;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
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
public class Frag_community_shoucang extends Fragment {
    List<PostCollection>   communityList=new ArrayList<>();
    private Button btn_del_guanzhu;
    private BaseAdapter adapter;
    SwipeMenuListView lv_community_shoucang;
    Dialog progressDialog;
    private int userId;
    String  dynamicList=null;
    List<PostCollection>   communityList1=new ArrayList<>();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userId = getActivity().getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);


        lv_community_shoucang = (SwipeMenuListView) getActivity().findViewById(R.id.lv_community_shoucang);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return communityList.size();
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

                ViewHolder viewhoder = null;
                if (convertView == null) {
                    viewhoder = new ViewHolder();//使用viewholder获取各个控件
                    convertView = View.inflate(getActivity(), R.layout.item_guanzhu, null);//打入listview

                    viewhoder.communityInfo = ((TextView) convertView.findViewById(R.id.communityInfo));
                    viewhoder.communityName = ((TextView) convertView.findViewById(R.id.communityName));
                    viewhoder.comCreateTime = ((TextView) convertView.findViewById(R.id.comCreateTime));

                    convertView.setTag(viewhoder);
                } else {

                    viewhoder = (ViewHolder) convertView.getTag();
                }

                PostCollection dongtai = communityList.get(position);//获取数据打入控件
                viewhoder.communityName.setText(dongtai.getPost().getPostTitle());
                viewhoder.communityInfo.setText(dongtai.getCommunity().getCommunityName());
                viewhoder.comCreateTime.setText(dongtai.getPost().getPostTime());
                // x.image().bind(viewhoder.comImg, HttpUtile.zy1+dongtai.getUserId().getUserImg());

                return convertView;
            }

        };
        getAllCommunity(userId);
        System.out.print("为什么会空指针");
        lv_community_shoucang.setAdapter(adapter);
    }

    private void getAllCommunity(int  userId) {

        Log.e("看看数据!!!!", userId+"");
        RequestParams params = new RequestParams
                (HttpUtils.hostLuoqingshanSchool+"/csys/Getshoucang?userId="+userId);//网络请求
        x.http().get(params, new Callback.CommonCallback<String>() {//使用xutils开启网络线程
            @Override
            public void onSuccess(String result) {
                Gson    gson=new Gson();
                Type type = new TypeToken<List<PostCollection>>() {
                }.getType();

                communityList1  = gson.fromJson(result, type);
                Log.e("看看成功数据!!!!", communityList.toString());
                Log.e("看看成功数据!!!!", gson.toString());

                if (result!=null){
                    communityList.addAll(communityList1);
                    dynamicList=result;
                }

                adapter.notifyDataSetChanged();//提醒adpter更新数据


            }

            @Override
            public void onError(Throwable ex, boolean
                    isOnCallback) {
                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
                Log.e("看看错误数据!!!!", ex.toString());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });





    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_community_shoucang, null);
        System.out.print("这是为什么");
        System.out.print("这是为什么");
        System.out.print("这是为什么");
        lv_community_shoucang = (SwipeMenuListView) view.findViewById(R.id.lv_community_shoucang);


        SwipeMenuCreator creator = new SwipeMenuCreator() {


            @Override

            public void create(SwipeMenu menu) {

// 生成 "open" item

                SwipeMenuItem openItem = new SwipeMenuItem(

                        getActivity().getApplicationContext());

// 设置删除背景

                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,

                        0xCE)));

// 设置删除长度

                openItem.setWidth(dp2px(90));

// 设置打开标题

                openItem.setTitle("Open");

// set item title fontsize

                openItem.setTitleSize(18);

// set item title font color

                openItem.setTitleColor(Color.WHITE);

// add to menu

                menu.addMenuItem(openItem);

// create "delete" item

                SwipeMenuItem deleteItem = new SwipeMenuItem(

                        getActivity().getApplicationContext());

// set item background

                deleteItem.setBackground(R.color.bgcolor);

// set item width

                deleteItem.setWidth(dp2px(90));

// set a icon

                deleteItem.setIcon(R.drawable.ic_delete);

// add to menu

                menu.addMenuItem(deleteItem);

            }

        };

// set creator

        lv_community_shoucang.setMenuCreator(creator);

// step 2. listener item click event

        lv_community_shoucang.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override

            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                PostCollection dongtai = communityList.get(position);

                switch (index) {

                    case 0:

                        getpost(dongtai.getPostId());

// open
                        break;

                    case 1:
                     int item=dongtai.getPostId();
                        Log.e("看我滑动删除的数据",item+"123" );
                        delete(item);
                        System.out.print("删除成功");
                        Log.v("tag","message");
                        communityList.remove(position);

                        adapter.notifyDataSetChanged();


                        break;

                }

                return false;

            }

            private void delete(int item) {

                RequestParams request=new RequestParams(HttpUtils.hostLuoqingshanSchool+"/csys/DeleteSwipe?communityId="+item);
                x.http().get(request, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getActivity(),result+"",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e("看我得到的数据呢", ex+"出现了");
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });

            }


        });





// set SwipeListener

        lv_community_shoucang.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override

            public void onSwipeStart(int position) {

// swipe start

            }

            @Override

            public void onSwipeEnd(int position) {

// swipe end

            }

        });

// other setting

//      listView.setCloseInterpolator(new BounceInterpolator());

// test item long click

        lv_community_shoucang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override

            public boolean onItemLongClick(AdapterView<?> parent, View view,

                                           int position, long id) {

                // Toast.makeText(getApplicationContext(), position + " long click", 0).show();

                return false;

            }

        });
        //点击进入社区动态界面
        lv_community_shoucang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



             /* Dynamic temp = communityList.get(0);
                Intent intent = new Intent(getActivity(), Community_model.class);
                intent.putExtra("bundle", temp);
                startActivity(intent);*/


            }
        });


        return view;
    }

    private void getpost(int postid) {
        progressDialog = ToolsClass.createLoadingDialog(getActivity(), "加载中...", true,
                0);
        progressDialog.show();
        RequestParams params = new RequestParams
                (HttpUtile.yu + "/community/togetpostbyid?postid="+postid);//网络请求
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson =new Gson();
                Post post=gson.fromJson(result,Post.class);
                Intent intent=new Intent(getActivity(), tieziactivity.class);

                intent.putExtra("post",post);

                startActivity(intent);

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


    public static class ViewHolder {
        ImageView comImg;
        TextView communityInfo;
        TextView communityName;
        TextView comCreateTime;


    }



    private int dp2px(int dp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,

                getResources().getDisplayMetrics());

    }
}
