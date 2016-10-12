package com.first.yuliang.deal_community.frament.fragment_community;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;
import com.first.yuliang.deal_community.frament.pojo.MyComminity;
import com.google.gson.Gson;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuliang on 2016/9/22.
 */
public class Frag_community_guanzhu extends Fragment {
    private ListView lv_community_guanzhu;
    private Button btn_del_guanzhu;
    MyComminity bean = null;
    List<MyComminity.myDongtai> comlist = new ArrayList<MyComminity.myDongtai>();
    private BaseAdapter adapter;
    ViewHolder viewhoder = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_community_guanzhu, null);
        lv_community_guanzhu = ((ListView) view.findViewById(R.id.lv_community_guanzhu));

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return comlist.size();
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


                if (convertView == null) {
                    viewhoder = new ViewHolder();//使用viewholder获取各个控件
                    convertView = View.inflate(getActivity(), R.layout.item_guanzhu, null);//打入listview
                    viewhoder.comImg = ((ImageView) convertView.findViewById(R.id.comImg));
                    viewhoder.communityInfo = ((TextView) convertView.findViewById(R.id.communityInfo));
                    viewhoder.communityName = ((TextView) convertView.findViewById(R.id.communityName));
                    viewhoder.comCreateTime = ((TextView) convertView.findViewById(R.id.comCreateTime));
                    x.view().inject(viewhoder, convertView);
                    convertView.setTag(viewhoder);
                } else {

                    viewhoder = (ViewHolder) convertView.getTag();
                }

                MyComminity.myDongtai dongtai = comlist.get(position);//获取数据打入控件
                viewhoder.communityName.setText(dongtai.communityName);
                viewhoder.communityInfo.setText(dongtai.communityInfo);
                viewhoder.comCreateTime.setText(dongtai.comCreateTime);
                x.image().bind((viewhoder.comImg), "http://10.40.5.61:8080/usys/imgs/" + dongtai.comImg + ".png");

                return convertView;
            }

        };

        getAllCommunity();
        lv_community_guanzhu.setAdapter(adapter);
        return view;


    }

    public static class ViewHolder {
        ImageView comImg;
        TextView communityInfo;
        TextView communityName;
        TextView comCreateTime;

    }

    private void getAllCommunity() {//获得社区动态的方法
        RequestParams params = new RequestParams
                ("http://10.40.5.61:8080/usys/MyCom");//网络请求
        x.http().get(params, new Callback.CommonCallback<String>() {//使用xutils开启网络线程
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();

                bean = gson.fromJson(result, MyComminity.class);//将获得的数据封装进入社区动态bean
                // Toast.makeText(DongtaiActivity.this,result,Toast.LENGTH_LONG).show();
                System.out.print(result);

                System.out.println(bean.status + "????");

                comlist.addAll(bean.comlist);
                adapter.notifyDataSetChanged();//提醒adpter更新数据
                System.out.print(result);

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


}
