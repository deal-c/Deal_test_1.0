package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.SearchCommodityActivity;
import com.first.yuliang.deal_community.TypeActivity;
import com.first.yuliang.deal_community.frament.pojo.Adbean;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuliang on 2016/9/21.
 */
public class Fragment_home extends Fragment {
    private Handler handler=new Handler(){


        private  int currentItem=0;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(currentItem>3){
                        currentItem=0;
                    }else {
                        currentItem++;
                    }
                    vp_ad.setCurrentItem(currentItem);
//                   adapter.notifyDataSetChanged();
                    handler.sendEmptyMessageDelayed(1,2500);
                    break;
                case 2:
                    break;
            }

        }
    };
    private ViewPager vp_ad;
    private PagerAdapter adapter;
    private int previousposition=0;
    final List<Adbean.Ad> adlist=new ArrayList<Adbean.Ad>();

    private ImageButton ib_type;
    private EditText query1;
    private ImageButton ib_search1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_home,null);


        ib_type=((ImageButton) view.findViewById(R.id.ib_type));

        ib_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getActivity(), TypeActivity.class);
                startActivity(intent);
            }
        });
//        搜索键点击跳转
        ib_search1 = ((ImageButton) view.findViewById(R.id.ib_search1));
        ib_search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchCommodityActivity.class);
                startActivity(intent);
            }
        });
//        EditText获得焦点后跳转
        query1 = ((EditText) view.findViewById(R.id.query1));

        query1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    query1.setFocusable(false);
                    query1.setFocusableInTouchMode(true);
//                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    Intent intent = new Intent(getActivity(), SearchCommodityActivity.class);
                    intent.putExtra("search",query1.getText().toString());
                    startActivity(intent);
                }
            }
        });

        //相关数据
        final  int []imgs=new int[]{R.drawable.ad1,R.drawable.ad2,R.drawable.ad3,R.drawable.ad4};
        final int[]ivs={R.id.iv_iv1,R.id.iv_iv2,R.id.iv_iv3,R.id.iv_iv4};

        //获得ViewPager控件
        vp_ad = ((ViewPager)view.findViewById(R.id.vp_ad));

        //setAdapter(PagerAdapter adapter)
        adapter=new PagerAdapter() {


            private TextView tv_content;
            private TextView tv_title;
            private ImageView iv_adphoto;

            @Override
            public int getCount() {
                return adlist.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }


            //加载对应的页面
            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                View view =View.inflate(getActivity(),R.layout.home_advp_item,null);
                iv_adphoto = ((ImageView) view.findViewById(R.id.iv_adphoto));
                tv_title = ((TextView) view.findViewById(R.id.tv_title));
                tv_content = ((TextView) view.findViewById(R.id.tv_content));


                ImageOptions imageOptions=new ImageOptions.Builder()
                        .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                        .build();
                int key=-1;
                switch (position){
                    case 0:
                        key=3;break;
                    case 1:
                        key=2;break;
                    case 2:
                        key=1;break;
                    case 3:
                        key=0;break;
                }if (adlist.size()!=0){
                    for (int i=0;i<4;i++) {
                        //拿不到数据就不显示
                        ((ImageView) (getActivity()).findViewById(ivs[position])).setVisibility(View.VISIBLE);
                    }

               x.image().bind(iv_adphoto,HttpUtile.host+adlist.get(key).getAdphoto(),imageOptions);
//                iv_adphoto.setImageResource(imgs[position]);
                tv_title.setText(adlist.get(key).getAdtitle());
                tv_content.setText("    "+adlist.get(key).getAdcontent());
                final int p;
                p= position;
                iv_adphoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(adlist.get(p).getAdhttp());
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    }
                });
                }else {
                  Toast.makeText(getActivity(),"加载出错",Toast.LENGTH_SHORT).show();
                }

                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(((View) object));
            }
        };
        vp_ad.setAdapter(adapter);

        //页面改变对应的操作
        vp_ad.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private ImageView iv_ivs;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(getActivity()!=null){
                iv_ivs = ((ImageView) (getActivity()).findViewById(ivs[position]));
                iv_ivs.setImageResource(R.drawable.red);
                ImageView iv_ivp = ((ImageView) (getActivity()).findViewById(ivs[previousposition]));
                iv_ivp.setImageResource(R.drawable.point);
                previousposition=position;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Message msg=new Message();
//               handler.sendEmptyMessage(1);


                }
        });


        //调用方法从数据库中获得广告数据
        getAdList();


        //发送空信息实现循环播放
        handler.sendEmptyMessage(1);

        return view;
    }
    private void getAdList() {

        RequestParams params=new RequestParams(HttpUtile.host+"deal_ad/getad");
        params.setCacheMaxAge(1000 * 10);

            x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                getActivity().findViewById(R.id.pb_load).setVisibility(View.GONE);
                System.out.println(result);

                Gson gson=new Gson();
                Adbean bean= gson.fromJson(result,Adbean.class);

                adlist.addAll(bean.adlist);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"访问成功",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(getActivity()!=null) {

                    Toast.makeText(getActivity().getApplication(), "访问出错", Toast.LENGTH_LONG).show();
                    getActivity().findViewById(R.id.pb_load).setVisibility(View.GONE);
                }
//                getActivity().findViewById(R.id.pb_load).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                getActivity().findViewById(R.id.pb_load).setVisibility(View.GONE);
            }
        });


    }
}
