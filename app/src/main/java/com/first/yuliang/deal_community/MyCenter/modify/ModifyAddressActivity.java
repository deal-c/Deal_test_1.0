package com.first.yuliang.deal_community.MyCenter.modify;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.MyCenter.modify.ModifyDetail.addAddressDetailActivity;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.Address;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModifyAddressActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_add_address;
    private RelativeLayout rl_add_address;

    int userId=0;
    private ListView lv_all_address;
    BaseAdapter adapter;

    List<Address> addressList=new ArrayList<>();
    private ImageView iv_modify_address_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_address);

        Intent intent=getIntent();
        userId=Integer.parseInt(intent.getStringExtra("userId").toString().trim());

        initView();

        initEvent();

        adapter=new BaseAdapter() {
            @Override
            public int getCount() {

                return addressList.size();
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
                View view=View.inflate(getApplicationContext(),R.layout.activity_add_address_item,null);
                TextView tv_main_address=((TextView) view.findViewById(R.id.tv_main_address));
                TextView tv_nickname=((TextView) view.findViewById(R.id.tv_nickname));
                TextView tv_phone_num=((TextView) view.findViewById(R.id.tv_phone_num));
                TextView tv_morenAddress=((TextView) view.findViewById(R.id.tv_morenAddress));

                if(addressList.get(position).isdefault())
                {
                    tv_morenAddress.setVisibility(View.VISIBLE);
                }
                String address=addressList.get(position).getCity().toString()+addressList.get(position).getAddressDetail();
                tv_main_address.setText(address);
                tv_nickname.setText(addressList.get(position).getUserName().toString());
                tv_phone_num.setText(addressList.get(position).getContactPhoneNum().toString());
                return view;
            }
        };

        lv_all_address.setAdapter(adapter);


        getAllAddress();



    }

    private void initEvent() {
        rl_add_address.setOnClickListener(this);
        iv_modify_address_back.setOnClickListener(this);
        lv_all_address.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                Dialog alertDialog = new AlertDialog.Builder(ModifyAddressActivity.this)
                        .setTitle("提示")
                        .setMessage("确认删除吗")
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
                                Address address=addressList.get(position);
                                deleteAddress(address.getAddressId());


                            }
                        }).create();


                alertDialog.show();

                return false;
            }
        });

    }

    private void deleteAddress(int addressId) {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/DeleteAddressServlet");
        params.addBodyParameter("addressId",addressId+"");
        x.http().post(params, new Callback.CacheCallback<String>() {


            @Override
            public void onSuccess(String result) {

                if(Boolean.parseBoolean(result.toString().trim()))
                {
                    Toast.makeText(ModifyAddressActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                   // adapter.notifyDataSetChanged();
                    getAllAddress();
                }
                else
                {
                    Toast.makeText(ModifyAddressActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
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

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    private void initView() {

        rl_add_address = ((RelativeLayout) findViewById(R.id.rl_add_address));
        iv_modify_address_back = ((ImageView) findViewById(R.id.iv_modify_address_back));
        lv_all_address = ((ListView) findViewById(R.id.lv_alll_address));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_add_address:
                initaddress(v);
                break;
            case R.id.iv_modify_address_back:
                ModifyAddressActivity.this.finish();
        }

    }

    private void initaddress(View v) {

            Intent intent = new Intent(ModifyAddressActivity.this, addAddressDetailActivity.class);
            intent.putExtra("userId", userId + "");
            startActivityForResult(intent, 3);

    }

    private void getAllAddress() {

        RequestParams params=new RequestParams(HttpUtile.zy+"/servlet/getAllAddressServlet");
        params.addBodyParameter("userId",userId+"");
        x.http().post(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {


                Gson gson=new Gson();
                Type type=new TypeToken<List<Address>>(){}.getType();
                List<Address> newAddressList=new ArrayList<Address>();
                newAddressList=gson.fromJson(result,type);

                addressList.clear();
                addressList.addAll(newAddressList);


                adapter.notifyDataSetChanged();


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 3:
                //Log.e("address","+++++++++++++++++++++");
                 getAllAddress();
                break;
        }
    }
}
