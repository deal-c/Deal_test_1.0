package com.first.yuliang.deal_community.MyCenter.modify.ModifyDetail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class addAddressDetailActivity extends AppCompatActivity implements View.OnClickListener {




    int userId=0;
    private ImageView iv_modify_back;
    private TextView tv_address_keep;
    private EditText et_reciver;
    private EditText et_contactPhone;
    private EditText et_city;

    private EditText et_city_detail;
    private SwitchCompat sc_isdefault;
    private ImageView iv_add;
    private CheckBox cb_isdefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_detail);




        Intent intent = getIntent();

        userId=Integer.parseInt(intent.getStringExtra("userId").toString().trim());

        initView();
        initData();


        initEvent();

    }

    private void initView() {

        iv_modify_back = ((ImageView) findViewById(R.id.iv_modify_back));
        tv_address_keep = ((TextView) findViewById(R.id.tv_address_keep));
        et_reciver = ((EditText) findViewById(R.id.et_reciver));
        et_contactPhone = ((EditText) findViewById(R.id.et_contactPhone));
        et_city = ((EditText) findViewById(R.id.et_city));
        iv_add = ((ImageView) findViewById(R.id.iv_add));
        et_city_detail = ((EditText) findViewById(R.id.et_city_detail));
        cb_isdefault = ((CheckBox) findViewById(R.id.cb_isdefault));


    }

    private void initData() {


    }


    private void getAddressData() {

        RequestParams params = new RequestParams(HttpUtile.zy+ "/servlet/addAddressServlet");

        try {
            params.addBodyParameter("contactPhoneNum", et_contactPhone.getText().toString().trim());
            params.addBodyParameter("city", URLEncoder.encode(et_city.getText().toString().trim(),"utf-8"));
            params.addBodyParameter("userName", URLEncoder.encode(et_reciver.getText().toString().trim(),"utf-8"));
            params.addBodyParameter("addressDetail", URLEncoder.encode( et_city_detail.getText().toString().trim(),"utf-8"));
            params.addBodyParameter("isdefault", URLEncoder.encode( cb_isdefault.getText().toString().trim(),"utf-8"));
            params.addBodyParameter("userId",userId+"");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Log.e("result","+++++++++"+result);

                if(Boolean.parseBoolean(result.toString().trim()))
                {
//
                    Toast.makeText(addAddressDetailActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                     Intent intent=new Intent();
                    addAddressDetailActivity.this.setResult(3,intent);
                }
                else
                {
                    Toast.makeText(addAddressDetailActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
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
        });



    }

    private void initEvent() {

        tv_address_keep.setOnClickListener(this);

        iv_modify_back.setOnClickListener(this);

        cb_isdefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb_isdefault.isChecked())
                {
                    cb_isdefault.setText("选中");
                }
                else
                {
                    cb_isdefault.setText("未选中");
                }

            }
        });

        iv_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address_keep:
                getAddressData();
                break;
            case R.id.iv_modify_back:
                addAddressDetailActivity.this.finish();
                break;
            case R.id.iv_add:
                getMobilePhoneNum();
                break;

        }
    }

    private void getMobilePhoneNum() {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        addAddressDetailActivity.this.startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {

            case (1) :
            {

                if (resultCode == Activity.RESULT_OK)
                {

                    Uri contactData = data.getData();

                    Cursor c = managedQuery(contactData, null, null, null, null);

                    c.moveToFirst();

                    String phoneNum=this.getContactPhone(c);
                    et_contactPhone.setText(phoneNum);

                }

                break;

            }

        }




    }



        private String getContactPhone(Cursor cursor)
        {

            int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            int phoneNum = cursor.getInt(phoneColumn);
            String phoneResult="";
            //System.out.print(phoneNum);
            if (phoneNum > 0)
            {
                // 获得联系人的ID号
                int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String contactId = cursor.getString(idColumn);
                // 获得联系人的电话号码的cursor;
                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId,
                        null, null);
                //int phoneCount = phones.getCount();
                //allPhoneNum = new ArrayList<String>(phoneCount);
                if (phones.moveToFirst())
                {
                    // 遍历所有的电话号码
                    for (;!phones.isAfterLast();phones.moveToNext())
                    {
                        int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                        int phone_type = phones.getInt(typeindex);
                        String phoneNumber = phones.getString(index);
                        switch(phone_type)
                        {
                            case 2:
                                phoneResult=phoneNumber;
                                break;
                        }
                        //allPhoneNum.add(phoneNumber);
                    }
                    if (!phones.isClosed())
                    {
                        phones.close();
                    }
                }
            }
            return phoneResult;
        }


}
