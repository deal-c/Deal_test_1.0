package com.first.yuliang.deal_community.MyCenter.modify.ModifyDetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.CityPickerDialog;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.address.City;
import com.first.yuliang.deal_community.address.County;
import com.first.yuliang.deal_community.address.Province;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.google.gson.Gson;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class addAddressDetailActivity extends AppCompatActivity implements View.OnClickListener {


    int userId = 0;
    private ImageView iv_modify_back;
    private TextView tv_address_keep;
    private EditText et_reciver;
    private EditText et_contactPhone;
    private TextView tv_address_city;

    private EditText et_city_detail;

    private ImageView iv_add;
    private CheckBox cb_isdefault;
    private RelativeLayout rl_city;
    private List<Province> provinces = new ArrayList<Province>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_detail);


        Intent intent = getIntent();

        userId = Integer.parseInt(intent.getStringExtra("userId").toString().trim());

        initView();
        initData();


        initEvent();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initView() {

        iv_modify_back = ((ImageView) findViewById(R.id.iv_modify_back));
        tv_address_keep = ((TextView) findViewById(R.id.tv_address_keep));
        et_reciver = ((EditText) findViewById(R.id.et_reciver));
        et_contactPhone = ((EditText) findViewById(R.id.et_contactPhone));
        tv_address_city = ((TextView) findViewById(R.id.tv_address_city));
        iv_add = ((ImageView) findViewById(R.id.iv_add));
        et_city_detail = ((EditText) findViewById(R.id.et_city_detail));
        cb_isdefault = ((CheckBox) findViewById(R.id.cb_isdefault));
        rl_city = ((RelativeLayout) findViewById(R.id.city));


    }

    private void initData() {


    }


    private void getAddressData() {

        RequestParams params = new RequestParams(HttpUtile.zy + "/servlet/addAddressServlet");

        try {
            params.addBodyParameter("contactPhoneNum", et_contactPhone.getText().toString().trim());
            params.addBodyParameter("city", URLEncoder.encode(tv_address_city.getText().toString().trim(), "utf-8"));
            params.addBodyParameter("userName", URLEncoder.encode(et_reciver.getText().toString().trim(), "utf-8"));
            params.addBodyParameter("addressDetail", URLEncoder.encode(et_city_detail.getText().toString().trim(), "utf-8"));
            params.addBodyParameter("isdefault", URLEncoder.encode(cb_isdefault.getText().toString().trim(), "utf-8"));
            params.addBodyParameter("userId", userId + "");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onSuccess(String result) {

                Log.e("result", "+++++++++" + result);

                if (Boolean.parseBoolean(result.toString().trim())) {
//
                    Toast.makeText(addAddressDetailActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    addAddressDetailActivity.this.setResult(3, intent);
                } else {
                    Toast.makeText(addAddressDetailActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
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
                if (cb_isdefault.isChecked()) {
                    cb_isdefault.setText("选中");
                } else {
                    cb_isdefault.setText("未选中");
                }

            }
        });

        iv_add.setOnClickListener(this);
        rl_city.setOnClickListener(this);
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
            case R.id.city:

                if (provinces.size() > 0) {
                    showAddressDialog();
                } else {
                    new InitAreaTask(addAddressDetailActivity.this).execute(0);
                }
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
        switch (requestCode) {

            case (1): {

                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();

                    Cursor c = managedQuery(contactData, null, null, null, null);

                    c.moveToFirst();

                    String phoneNum = this.getContactPhone(c);
                    et_contactPhone.setText(phoneNum);

                }

                break;

            }

        }


    }


    private String getContactPhone(Cursor cursor) {

        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        //System.out.print(phoneNum);
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            //int phoneCount = phones.getCount();
            //allPhoneNum = new ArrayList<String>(phoneCount);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            phoneResult = phoneNumber;
                            break;
                    }
                    //allPhoneNum.add(phoneNumber);
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }

    private void showAddressDialog() {

        new CityPickerDialog(addAddressDetailActivity.this, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {

                    @Override
                    public void onPicked(com.first.yuliang.deal_community.address.Province selectProvince, City selectCity, County selectCounty) {

                        StringBuilder address = new StringBuilder();
                        address.append(
                                selectProvince != null ? selectProvince
                                        .getAreaName() : "")
                                .append(selectCity != null ? selectCity
                                        .getAreaName() : "")
                                .append(selectCounty != null ? selectCounty
                                        .getAreaName() : "");
                        String text = selectCounty != null ? selectCounty
                                .getAreaName() : "";
                        tv_address_city.setText(address);
                    }
                }).show();
    }

   private class InitAreaTask  extends AsyncTask<Integer, Integer, Boolean>{


        Context mContext;

        Dialog progressDialog;



        public InitAreaTask(Context context) {
            mContext = context;
            progressDialog = ToolsClass.createLoadingDialog(mContext, "请稍等...", true,
                    0);
        }

        @Override
        protected void onPreExecute() {

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (provinces.size() > 0) {
                showAddressDialog();
            } else {
                Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            String address = null;
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("address.txt");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                address = EncodingUtils.getString(arrayOfByte, "UTF-8");
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i),
                                Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

    }
}




