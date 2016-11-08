package com.first.yuliang.deal_community.MyCenter.modify;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.RegActivity;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.User;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout rl_modify_tx;
    private RelativeLayout rl_modify_name;
    private RelativeLayout rl_modify_gender;
    private RelativeLayout rl_modify_birthday;
    private RelativeLayout rl_modify_psd;
    private RelativeLayout rl_modify_oftenlive;
    private RelativeLayout rl_modify_address;
    private Button btn_male;
    private Button btn_female;
    private Button btn_cancel;
    private TextView tv_show_gender;
    private TextView tv_show_birthday;
    private Button btn_back_account;
    private TextView tv_show_name;
    private TextView tv_show_oftenlive;
    private TextView tv_show_addess;
    User user;

    int userId=0;
    private ImageView iv_modify_main_back;
    private ImageView iv_modify_tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Intent intent=getIntent();
        user=intent.getParcelableExtra("user");
        userId=user.getUserId();

        rl_modify_tx = ((RelativeLayout) findViewById(R.id.rl_modify_tx));
        iv_modify_tx = ((ImageView) findViewById(R.id.iv_modify_tx));

        if (!user.getUserImg().equals("null")&&user.getUserImg()!=null&&user.getUserImg().length()!=0) {
            x.image().bind(iv_modify_tx, HttpUtile.zy1 + user.getUserImg());
        }
        rl_modify_name = ((RelativeLayout) findViewById(R.id.rl_modify_name));
        tv_show_name = ((TextView) findViewById(R.id.tv_show_name));
        tv_show_name.setText(user.getUserName());


        rl_modify_gender = ((RelativeLayout) findViewById(R.id.rl_modify_gender));
        tv_show_gender = ((TextView) findViewById(R.id.tv_show_gender));
        String userSex=user.isUserSex()?"男":"女";
        //Log.e("userSex","++++++++"+userSex);
        tv_show_gender.setText(userSex);

        rl_modify_birthday = ((RelativeLayout) findViewById(R.id.rl_modify_birthday));
        tv_show_birthday = ((TextView) findViewById(R.id.tv_show_birthday));

       // Log.e("birthday","++++++++++"+user.getBirthday());
        tv_show_birthday.setText(DateUtils.dateToString(user.getuserBirthday()));


        rl_modify_psd = ((RelativeLayout) findViewById(R.id.rl_modify_psd));

        rl_modify_oftenlive = ((RelativeLayout) findViewById(R.id.rl_modify_oftenlive));
        tv_show_oftenlive = ((TextView) findViewById(R.id.tv_show_oftenlive));
        tv_show_oftenlive.setText(user.getUserAddress_s());

        rl_modify_address = ((RelativeLayout) findViewById(R.id.rl_modify_address));
        tv_show_addess = ((TextView) findViewById(R.id.tv_show_address));


        btn_back_account= ((Button) findViewById(R.id.btn_back_account));

        iv_modify_main_back = ((ImageView) findViewById(R.id.iv_modify_main_back));

        rl_modify_tx.setOnClickListener(this);
        rl_modify_name.setOnClickListener(this);
        rl_modify_gender.setOnClickListener(this);
        rl_modify_birthday.setOnClickListener(this);
        rl_modify_psd.setOnClickListener(this);
        rl_modify_oftenlive.setOnClickListener(this);
        rl_modify_address.setOnClickListener(this);
        btn_back_account.setOnClickListener(this);
        iv_modify_main_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.rl_modify_tx:
                Intent intent_modify_tx=new Intent(this,ModifyTxActivity.class);
                intent_modify_tx.putExtra("userId",userId+"");
                intent_modify_tx.putExtra("userImg",user.getUserImg());
                startActivityForResult(intent_modify_tx,4);
                break;
            case R.id.rl_modify_name:
                Intent intent_modify_name=new Intent(this,ModifyNameActivity.class);
                intent_modify_name.putExtra("name",tv_show_name.getText().toString());
                intent_modify_name.putExtra("userId",userId+"");
                startActivityForResult(intent_modify_name,2);
                break;
            case R.id.rl_modify_gender:
                initPopupWindow(v);
                break;
            case R.id.rl_modify_birthday:
               Intent intent=new Intent(this,ModifyBirthdayActivity.class);
                intent.putExtra("birthday",tv_show_birthday.getText().toString());
                intent.putExtra("userId",userId+"");

                startActivityForResult(intent,1);
                break;
            case R.id.rl_modify_psd:
                Intent intent_modify_psd=new Intent(this,ModifyPsdActivity.class);
                intent_modify_psd.putExtra("userId",userId+"");
                startActivity(intent_modify_psd);
                break;
            case R.id.rl_modify_oftenlive:
                Intent intent_modify_oftenlive=new Intent(this,ModifyOftenActivity.class);
                intent_modify_oftenlive.putExtra("userAddress_s",tv_show_oftenlive.getText().toString());
                intent_modify_oftenlive.putExtra("userId",userId+"");
                startActivityForResult(intent_modify_oftenlive,3);
                break;
            case R.id.rl_modify_address:
                Intent intent_modify_address=new Intent(this,ModifyAddressActivity.class);
                //intent_modify_address.putExtra("address_s",tv_show_addess.getText().toString());
                intent_modify_address.putExtra("userId",userId+"");
                startActivity(intent_modify_address);
                break;

            case R.id.btn_back_account:


                confirmQuit();

                break;
            case R.id.iv_modify_main_back:

                ModifyActivity.this.finish();
                break;

        }
    }

    private void confirmQuit() {

        Dialog alertDialog = new AlertDialog.Builder(ModifyActivity.this)
        .setTitle("提示")
        .setMessage("确认退出吗")
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        })
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ModifyActivity.this.finish();
                Intent intentBack=new Intent(ModifyActivity.this, RegActivity.class);
                intentBack.putExtra("userNickName",tv_show_name.getText().toString().trim());
                intentBack.putExtra("flag","0");
                SharedPreferences preference=getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=preference.edit();
                edit.putInt("fromModifyToReg",1);
                edit.putInt("id",0);
                edit.commit();
                startActivity(intentBack);

                ModifyActivity.this.finish();
            }
        }).create();


        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode)
        {
            case 1:
               String data1=data.getStringExtra("date");
                tv_show_birthday.setText(data1);
                break;
            case 2:
                String data2=data.getStringExtra("name");
                tv_show_name.setText(data2);
                break;
            case 3:
                String data3=data.getStringExtra("often");
                tv_show_oftenlive.setText(data3);
                break;
            case 4:


                String data4=data.getStringExtra("data");
                x.image().bind(iv_modify_tx,HttpUtile.zy1+data4);
                break;

        }
    }

    private void initPopupWindow(View v) {

        View view=LayoutInflater.from(this).inflate(R.layout.gender_item,null);
        btn_male = ((Button) view.findViewById(R.id.btn_male));
        btn_female = ((Button) view.findViewById(R.id.btn_female));
        btn_cancel = ((Button) view.findViewById(R.id.btn_cancel));

        final PopupWindow popupWindow = new PopupWindow(view,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;

            }
        });


        if(user.isUserSex()) {


            btn_male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();


                }
            });
            btn_female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    updateUserSex();
                }
            });
        }else
        {

            btn_male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popupWindow.dismiss();
                    updateUserSex();

                }
            });
            btn_female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                }
            });
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });



        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        popupWindow.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0, 0);
    }

    private void updateUserSex() {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/ModifyUserSexServlet");
        params.addBodyParameter("userId",userId+"");
        try {
            params.addBodyParameter("userSex", URLEncoder.encode(tv_show_gender.getText().toString().trim(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Toast.makeText(ModifyActivity.this,result,Toast.LENGTH_SHORT).show();
                tv_show_gender.setText(result);
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

}
