package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.CommodityActivity;
import com.first.yuliang.deal_community.MessageActivity;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.SearchCommodityActivity;
import com.first.yuliang.deal_community.SearchResultActivity;
import com.first.yuliang.deal_community.TypeActivity;
import com.first.yuliang.deal_community.Util.NoScrollGridView;
import com.first.yuliang.deal_community.frament.pojo.Adbean;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.first.yuliang.deal_community.home_button_activity.huan_magic;
import com.first.yuliang.deal_community.home_button_activity.juan_dongtai;
import com.first.yuliang.deal_community.home_button_activity.old_tonew;
import com.first.yuliang.deal_community.home_button_activity.re_use;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by yuliang on 2016/9/21.
 */
public class Fragment_home extends Fragment implements View.OnClickListener {


    private Handler handler = new Handler() {


        private int currentItem = 0;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (currentItem > 3) {
                        currentItem = 0;
                    } else {
                        currentItem++;
                    }
                    vp_ad.setCurrentItem(currentItem);
//                   adapter.notifyDataSetChanged();
                    handler.sendEmptyMessageDelayed(1, 2500);
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
    private CommodityBean.Commodity bean;

    private ImageButton ib_type;
    private EditText query1;
    private ImageButton ib_search1;
    private Button btn_message;

    int id=0;
    private Button qiji_huan;
    private Button zai_liyong;
    private Button dongtai_juan;
    private Button jiu_huanxin;
    private NoScrollGridView gv_song;
    private BaseAdapter madapter;
    private BaseAdapter gadapter;
    private List <CommodityBean.Commodity> prolist=new ArrayList<>();
    private NoScrollGridView guessyoulike;
    private Button btn_song;
    private NoScrollGridView gv_hot;
    private BaseAdapter hotadapter;
    private ScrollView mScrollView;
    private int hotcount=5;
    private ProgressBar jiazaimore;
    private List <CommodityBean.Commodity> hotlist=new ArrayList<>();
    private View tv_bootom;
    private ImageView v_guess;
    private TextView textView2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_home,null);

        id=getActivity().getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);

        ib_type=((ImageButton) view.findViewById(R.id.ib_type));
        btn_message = ((Button) view.findViewById(R.id.btn_message));

        //对应主页的四个按钮
        qiji_huan = ((Button) view.findViewById(R.id.qiji_huan));
        zai_liyong = ((Button) view.findViewById(R.id.zaili_yong));
        dongtai_juan = ((Button) view.findViewById(R.id.jun_dongtai));
        jiu_huanxin = ((Button) view.findViewById(R.id.jiu_huanxin));
        btn_song = ((Button) view.findViewById(R.id.btn_songpro));
        btn_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("search","");
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,0);
            }
        });
        gv_song = ((NoScrollGridView) view.findViewById(R.id.gv_song));
        guessyoulike = ((NoScrollGridView) view.findViewById(R.id.gv_guessyoulike));
        guessyoulike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CommodityActivity.class);
                CommodityBean.Commodity commodity = prolist.get(position);
                intent.putExtra("search",commodity.commodityTitle);
                intent.putExtra("bundle", commodity);
                startActivity(intent);
            }
        });
        gv_hot = ((NoScrollGridView) view.findViewById(R.id.gv_hot));
        mScrollView = ((ScrollView) view.findViewById(R.id.sv_home));
        jiazaimore = ((ProgressBar) view.findViewById(R.id.jiazaimore));
        tv_bootom = view.findViewById(R.id.tv_bottom);

        v_guess = ((ImageView) view.findViewById(R.id.roundCornerImageView));
        v_guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommodityActivity.class);
                intent.putExtra("search",bean.commodityTitle);
                intent.putExtra("bundle", bean);
                startActivity(intent);
            }
        });
        textView2 = ((TextView) view.findViewById(R.id.textView2));

        getRandomCommodity();

        mScrollView.setOnTouchListener(new TouchListenerImpl());

        hotadapter=new BaseAdapter() {
            private TextView locate;
            private TextView pro_price;
            private TextView pri_title;
            private ImageView pro_img;

            @Override
            public int getCount() {

                return hotcount;
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
                View v=View.inflate(getActivity(),R.layout.item_hot,null);

                if (hotlist.size()!=0){


                CommodityBean.Commodity com=hotlist.get(position);


                pro_img = ((ImageView) v.findViewById(R.id.iv_cg_l));
                pri_title = ((TextView) v.findViewById(R.id.tv_cg_l));
                pro_price = ((TextView) v.findViewById(R.id.tv_price_l));
                locate = ((TextView) v.findViewById(R.id.tv_local_l));
                ImageOptions options=new ImageOptions.Builder()
                        .setImageScaleType(CENTER_CROP)
                        .setFailureDrawableId(R.drawable.loadfailed)
                        .build();

                if (com.commodityImg.split("upload")[0].equals("/csys/")){
                    x.image().bind(pro_img,HttpUtile.szj+com.commodityImg,options);
                }else{

                    x.image().bind(pro_img,HttpUtile.yu+com.commodityImg,options);
                }


                pri_title.setText(com.commodityTitle);
                pro_price.setText(com.price+"");
                locate.setText(com.location);

                }

                return v;
            }
        };


        gv_hot.setAdapter(hotadapter);

        gv_hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "跳转的商品详情", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), CommodityActivity.class);
                CommodityBean.Commodity temp = hotlist.get(position);
                intent.putExtra("search", temp.commodityTitle);
                intent.putExtra("bundle", temp);
                startActivity(intent);
            }
        });

        gethotlist();
        hotadapter.notifyDataSetChanged();

        gadapter=new BaseAdapter() {
            private ImageView pro_img;
            private TextView pro_desc;
            @Override
            public int getCount() {
                return 2;
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
                View  v=View.inflate(getActivity(),R.layout.gridview_song_item,null);
                pro_desc = ((TextView) v.findViewById(R.id.pro_desc));
                pro_img = ((ImageView) v.findViewById(R.id.pro_img));
                if (prolist.size()!=0){
                    CommodityBean.Commodity com=prolist.get(position);
                    pro_desc.setText(com.commodityTitle);
                    ImageOptions options=new ImageOptions.Builder()
                            .setImageScaleType(CENTER_CROP)
                            .setFailureDrawableId(R.drawable.loadfailed)
                            .setLoadingDrawableId(R.drawable.shalou)
                            .build();

                    x.image().bind(pro_img,HttpUtile.yu+com.commodityImg,options);
                }
                return v;
            }
        };

        guessyoulike.setAdapter(gadapter);


        madapter=new BaseAdapter() {
            private ImageView pro_img;
            private TextView pro_desc;

            @Override
            public int getCount() {
                return 4;
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

                View  v=View.inflate(getActivity(),R.layout.gridview_song_item,null);

                pro_desc = ((TextView) v.findViewById(R.id.pro_desc));
                pro_img = ((ImageView) v.findViewById(R.id.pro_img));
                if (prolist.size()!=0){
                CommodityBean.Commodity com=prolist.get(position);
                pro_desc.setText(com.commodityTitle);
                ImageOptions options=new ImageOptions.Builder()
                .setImageScaleType(CENTER_CROP)
                .setFailureDrawableId(R.drawable.loadfailed)
                .setLoadingDrawableId(R.drawable.shalou)
                .build();

                x.image().bind(pro_img,HttpUtile.yu+com.commodityImg,options);
                }
                return v;
            }
        };


        gv_song.setAdapter(madapter);
        gv_song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //prolist

                if (prolist.size()!=0){
                Toast.makeText(getActivity(), "跳转的商品详情", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), CommodityActivity.class);
                CommodityBean.Commodity temp = prolist.get(position);
                intent.putExtra("search", temp.commodityTitle);
                intent.putExtra("bundle", temp);
                startActivity(intent);
                }else {
                    ToastUtil.show(getActivity(),"请检查网络连接");
                }
            }
        });


        getSongProduct();



        qiji_huan.setOnClickListener(this);
        zai_liyong.setOnClickListener(this);
        dongtai_juan.setOnClickListener(this);
        jiu_huanxin.setOnClickListener(this);


        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MessageActivity.class);
                intent.putExtra("id",id+"");
                startActivity(intent);
            }
        });

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
                        .setImageScaleType(CENTER_CROP)
                        .build();
                if (adlist.size() != 0) {
                    final int a=position;
                    iv_adphoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(getActivity(),ad_page.class);
                            intent.putExtra("ad",adlist.get(a));
                            startActivity(intent);
                        }
                    });

                    x.image().bind(iv_adphoto, HttpUtile.yu + adlist.get(position).getAdphoto(), imageOptions);
//                iv_adphoto.setImageResource(imgs[position]);
                    tv_title.setText(adlist.get(position).getAdtitle());
                    tv_content.setText("    " + adlist.get(position).getAdcontent());
                } else {
                    Toast.makeText(getActivity(), "加载出错", Toast.LENGTH_SHORT).show();
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
    //获得推荐赠送的商品列表
    private void getSongProduct() {
        RequestParams params=new RequestParams(HttpUtile.yu+"/community/togetjuan");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                Type type=new TypeToken<List<CommodityBean.Commodity>>(){}.getType();
                prolist=gson.fromJson(result,type);


                madapter.notifyDataSetChanged();
                gadapter.notifyDataSetChanged();
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



   // 获得广告list
    private void getAdList() {

        RequestParams params=new RequestParams(HttpUtile.yu+"deal_ad/getad");
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
                Toast.makeText(getActivity(), "访问成功", Toast.LENGTH_LONG).show();
                handler.sendEmptyMessage(1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qiji_huan:
                Intent intent1=new Intent(getActivity(), huan_magic.class);
                startActivity(intent1);
                break;
            case R.id.zaili_yong:
                Intent intent2=new Intent(getActivity(), re_use.class);
                startActivity(intent2);
                break;
            case R.id.jun_dongtai:
                Intent intent3=new Intent(getActivity(),juan_dongtai.class);
                startActivity(intent3);
                break;
            case R.id.jiu_huanxin:
                Intent intent4=new Intent(getActivity(), old_tonew.class);
                startActivity(intent4);
                break;



        }

    }
//获得热门商品list
    private void gethotlist(){
        RequestParams params =new RequestParams(HttpUtile.szj+"/csys/getallbrowse");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommodityBean bean = gson.fromJson(result, CommodityBean.class);
//                Type type=new TypeToken<List<CommodityBean.Commodity>>(){}.getType();
                hotlist.addAll(bean.commodities);
                Log.e("onSuccess",hotlist.size()+"" );
                hotadapter.notifyDataSetChanged();
                jiazaimore.setVisibility(View.GONE);
                 ToastUtil.show(getActivity(),"success");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                ToastUtil.show(getActivity(),"error");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    //下拉加载更多
    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:

                    int scrollY=view.getScrollY();
                    int height=view.getHeight();
                    int scrollViewMeasuredHeight=mScrollView.getChildAt(0).getMeasuredHeight();
                    if(scrollY==0){
                        Log.e("滑动到了顶端 ", scrollY+"");
                    }
                    if((scrollY+height)==scrollViewMeasuredHeight){
//                        Log.e("滑动到了底部 scrollY=", scrollY+"");
                        hotcount+=5;
                          if (hotlist.size()<hotcount){
                              jiazaimore.setVisibility(View.GONE);
                              tv_bootom.setVisibility(View.VISIBLE);

                          }else {

                              hotadapter.notifyDataSetChanged();
                              jiazaimore.setVisibility(View.GONE);
                          }





//                        Log.e("滑动到了底部 height=", height+"");
                    }else if (scrollY>0 &&(scrollY+height)<scrollViewMeasuredHeight){
                        jiazaimore.setVisibility(View.VISIBLE);
                        tv_bootom.setVisibility(View.GONE);
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    };

    private void getRandomCommodity(){
        RequestParams params =new RequestParams(HttpUtile.szj+"/csys/getrandomcommodity");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                bean = gson.fromJson(result, CommodityBean.Commodity.class);
                x.image().bind(v_guess,HttpUtile.yu+bean.commodityImg);
                textView2.setText(bean.commodityTitle);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                ToastUtil.show(getActivity(),"error");
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
