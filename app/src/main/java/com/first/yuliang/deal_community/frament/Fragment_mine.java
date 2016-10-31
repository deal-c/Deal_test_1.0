package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.first.yuliang.deal_community.MyCenter.MyBuyActivity;
import com.first.yuliang.deal_community.MyCenter.MyCoinActivity;
import com.first.yuliang.deal_community.MyCenter.MyMaiActivity;
import com.first.yuliang.deal_community.MyCenter.MyPublishActivity;
import com.first.yuliang.deal_community.MyCenter.MyRecentActivity;
import com.first.yuliang.deal_community.MyCenter.MyShezhiActivity;
import com.first.yuliang.deal_community.MyCenter.MyZanActivity;
import com.first.yuliang.deal_community.MyCenter.modify.ModifyActivity;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.RegActivity;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.User;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;

import io.rong.imkit.RongIM;

/**
 * Created by yuliang on 2016/9/21.
 */
public class Fragment_mine extends Fragment implements View.OnClickListener{
    private ImageView iv_pic;
    private TextView tv_login;
    private RelativeLayout rl_publish;
    private RelativeLayout rl_mai;
    private RelativeLayout rl_buy;
    private RelativeLayout rl_zan;
    private RelativeLayout rl_coin;
    private RelativeLayout rl_recent;
    private RelativeLayout rl_contact;
    private RelativeLayout rl_shehzhi;


    int id=0;

    private User user=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.activity_mine,null);
        iv_pic = ((ImageView) view.findViewById(R.id.iv_pic));

        rl_publish = ((RelativeLayout) view.findViewById(R.id.rl_publish));




        rl_mai = ((RelativeLayout) view.findViewById(R.id.rl_mai));


        rl_buy = ((RelativeLayout) view.findViewById(R.id.rl_buy));


        rl_zan = ((RelativeLayout) view.findViewById(R.id.rl_zan));
        rl_coin = ((RelativeLayout) view.findViewById(R.id.rl_coin));
        rl_recent = ((RelativeLayout) view.findViewById(R.id.rl_recent));
        rl_contact = ((RelativeLayout) view.findViewById(R.id.rl_contact));
        rl_shehzhi = ((RelativeLayout) view.findViewById(R.id.rl_shehzhi));

        tv_login = ((TextView) view.findViewById(R.id.tv_login));

        id=getActivity().getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id",0);


        int intoflag=getActivity().getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("intoflag",0);




        if(intoflag==1)
        {
            iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), RegActivity.class);
                    startActivity(intent);

                }
            });
        }


//
//        if(loginCount==1)
//        {
//            RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectUserServlet?id="+loginUserId);
//
//            x.http().get(params, new Callback.CommonCallback<String>() {
//
//                @Override
//                public void onSuccess(final String result) {
//
//
//                    Gson gson=new Gson();
//                    final User user=gson.fromJson(result,User.class);
//
//
//
//                    tv_login.setText(user.getUserName());
//                    //xUtilsImageUtils.display(iv_pic,);
//                    iv_pic.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent=new Intent(getActivity(),ModifyActivity.class);
//                            intent.putExtra("user",user);
//                            startActivity(intent);
//                        }
//                    });
//
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//
//                }
//            });
//        }

        if(id!=0)
        {
            getUserData();
        }

        rl_publish.setOnClickListener(this);
        rl_mai.setOnClickListener(this);
        rl_buy.setOnClickListener(this);
        rl_zan.setOnClickListener(this);
        rl_coin.setOnClickListener(this);
        rl_recent.setOnClickListener(this);
        rl_contact.setOnClickListener(this);
        rl_shehzhi.setOnClickListener(this);
        return  view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.rl_publish:
                Intent intenPublisht=new Intent(getActivity(),MyPublishActivity.class);
                intenPublisht.putExtra("userId",id+"");
                startActivity(intenPublisht);
                break;
            case R.id.rl_mai:
                Intent intentMai=new Intent(getActivity(),MyMaiActivity.class);
                startActivity(intentMai);
                break;
            case R.id.rl_buy:
                Intent intentBuy=new Intent(getActivity(),MyBuyActivity.class);
                startActivity(intentBuy);
                break;
            case R.id.rl_zan:
                Intent intentZan=new Intent(getActivity(),MyZanActivity.class);
                startActivity(intentZan);
                break;
            case R.id.rl_coin:
                Intent intentCoin=new Intent(getActivity(),MyCoinActivity.class);
                startActivity(intentCoin);
                break;
            case R.id.rl_recent:
                Intent intentRecent=new Intent(getActivity(),MyRecentActivity.class);
                startActivity(intentRecent);
                break;
            case R.id.rl_contact:
//                Intent intentContact=new Intent(getActivity(),MyContactActivity.class);
//                //intentContact.putExtra("userId",id+"");
//                startActivity(intentContact);

                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startConversationList(getActivity());
                //start();

                break;
            case R.id.rl_shehzhi:
                Intent intentShezhi=new Intent(getActivity(),MyShezhiActivity.class);
                startActivity(intentShezhi);
                break;

        }
    }

//    private void start() {
//
//
//        if(RongIM.getInstance()!=null)
//        {
//            RongIM.getInstance().startPrivateChat(getActivity(),"48"," ");
//            //getUserData();
//            //getContactorData(48);
//        }
//
//    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                getUserData();
                break;
        }
    }




    private void getUserData()
    {
        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/SelectUserServlet?id="+id);

        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                user=gson.fromJson(result,User.class);



                tv_login.setText(user.getUserName());



                x.image().bind(iv_pic,HttpUtile.zy1+user.getUserImg());

                //Bitmap image = ((BitmapDrawable)iv_pic.getDrawable()).getBitmap();
               // Bitmap image=iv_pic.getDrawingCache();
                //toRoundBitmap(image);

                //String token=user.getToken();
                //connect(token);
                //toRoundBitmap(HttpUtile.zy1+user.getUserImg().);

                iv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),ModifyActivity.class);

                        intent.putExtra("user",user);
                        startActivityForResult(intent,1);
                    }
                });

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

    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
//      canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
        boolean res = output.compress(Bitmap.CompressFormat.PNG, 100, logoStream);
        byte[] logoBuf = logoStream.toByteArray();//将图像保存到byte[]中
        Bitmap temp = BitmapFactory.decodeByteArray(logoBuf, 0, logoBuf.length);//将图像从byte[]中读取生成Bitmap 对象 temp

        return temp;
    }
//    private void connect(String token) {
//
//        RongIM.connect(token, new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//                Log.e("Activitycc", "--onTokenIncorrect");
//            }
//
//            @Override
//            public void onSuccess(String userid) {
//
//                Log.e("Activitycc", "--onSuccess" + userid);
//
//
//
//                RongIM.getInstance().refreshUserInfoCache(new UserInfo(userid,user.getUserName(), Uri.parse(HttpUtile.zy1+user.getUserImg())));
//
//
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                Log.e("Activitycc", "--onError" + errorCode);
//            }
//        });
//    }
}
