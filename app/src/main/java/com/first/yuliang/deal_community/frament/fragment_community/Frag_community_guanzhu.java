package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.Community_Activity.Community_model;

import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.HttpUtils;
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
  List<Community>   communityList=new ArrayList<>();
    private Button btn_del_guanzhu;
    private BaseAdapter adapter;
    SwipeMenuListView lv_community_guanzhu;
    private int item=0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getAllCommunity();

       lv_community_guanzhu= (SwipeMenuListView) getActivity().findViewById(R.id.lv_community_guanzhu);
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
                    viewhoder.comImg = ((ImageView) convertView.findViewById(R.id.comImg));
                    viewhoder.communityInfo = ((TextView) convertView.findViewById(R.id.communityInfo));
                    viewhoder.communityName = ((TextView) convertView.findViewById(R.id.communityName));
                    viewhoder.comCreateTime = ((TextView) convertView.findViewById(R.id.comCreateTime));

                    convertView.setTag(viewhoder);
                } else {

                    viewhoder = (ViewHolder) convertView.getTag();
                }

                Community dongtai = communityList.get(position);//获取数据打入控件
                viewhoder.communityName.setText(dongtai.getCommunityName());
                viewhoder.communityInfo.setText(dongtai.getCommunityInfo());
                viewhoder.comCreateTime.setText(dongtai.getComCreatTime());
                x.image().bind((viewhoder.comImg), HttpUtils.hostLuoqingshanWifi+"/usys/imgs/" + dongtai.getComImg() + ".png");
                item=dongtai.getCommunityId();
                return convertView;
            }

        };

        System.out.print("为什么会空指针");
       lv_community_guanzhu.setAdapter(adapter);



    }

    private void getAllCommunity() {


            RequestParams params = new RequestParams
                    (HttpUtils.hostLuoqingshanSchool+"/usys/MyCom");//网络请求
            x.http().get(params, new Callback.CommonCallback<String>() {//使用xutils开启网络线程
                @Override
                public void onSuccess(String result) {
                    Gson    gson=new Gson();
                    Type type = new TypeToken<List<Community>>() {
                    }.getType();
                    List<Community>   communityList1=new ArrayList<>();
                    communityList1  = gson.fromJson(result, type);
                    Log.e("看看数据====", communityList.toString());
                    Log.e("看看数据====", gson.toString());
                    if (result!=null){
                        communityList.addAll(communityList1);
                    }

                    adapter.notifyDataSetChanged();//提醒adpter更新数据


                }

                @Override
                public void onError(Throwable ex, boolean
                        isOnCallback) {
                    Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frag_community_guanzhu, null);
        System.out.print("这是为什么");
        System.out.print("这是为什么");
        System.out.print("这是为什么");
        lv_community_guanzhu = (SwipeMenuListView) view.findViewById(R.id.lv_community_guanzhu);


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

        lv_community_guanzhu.setMenuCreator(creator);

// step 2. listener item click event

        lv_community_guanzhu.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override

            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

               Community dongtai = communityList.get(position);

                switch (index) {

                    case 0:

// open
                        break;

                    case 1:
// delete
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

                RequestParams request=new RequestParams(HttpUtils.hostLuoqingshanSchool+"/usys/deleteComServlt?communityId="+item);
               x.http().get(request, new Callback.CommonCallback<String>() {
                   @Override
                   public void onSuccess(String result) {
                       Toast.makeText(getActivity(),result+"",Toast.LENGTH_LONG).show();
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


        });





// set SwipeListener

        lv_community_guanzhu.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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

        lv_community_guanzhu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override

            public boolean onItemLongClick(AdapterView<?> parent, View view,

                                           int position, long id) {

                // Toast.makeText(getApplicationContext(), position + " long click", 0).show();

                return false;

            }

        });
        //点击进入社区动态界面
        lv_community_guanzhu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Community temp = communityList.get(0);
                Intent intent = new Intent(getActivity(), Community_model.class);
                intent.putExtra("bundle", temp);

                startActivity(intent);


            }
        });


        return view;
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
