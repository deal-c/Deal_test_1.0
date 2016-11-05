package com.first.yuliang.deal_community.publish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.frament.pojo.CommodityInfo;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

import static com.amap.api.maps.CoordinateConverter.CoordType.GPS;

public class deal_publish_Activity extends AppCompatActivity implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener {

    private GridView gv_photos;
    BaseAdapter madapter;
    ArrayList<String> imgs = new ArrayList<String>();
    private static final int REQUEST_IMAGE = 2;
    private Button punish;
    List<File> files = new ArrayList<File>();
    File file;
    private RadioButton juan;
    private RadioButton zeng;
    private RadioButton huan;
    private RadioButton mai;
    private TextView backtomain;
    private EditText pro_title;
    private EditText pro_desc;
    private TextView location;
    private Spinner pro_type;
    private EditText pro_price;
    private int userid;
    private Integer buyway;
    Dialog progressDialog;

    private LocationManager locationManager;// 位置管理类

    private String provider;// 位置提供器

    private GeocodeSearch geocoderSearch;
    LatLng mlatLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_publish_);

        backtomain = ((TextView) findViewById(R.id.backTomain));
        juan = ((RadioButton) findViewById(R.id.rb_juan));
        huan = ((RadioButton) findViewById(R.id.rb_huan));
        zeng = ((RadioButton) findViewById(R.id.rb_zeng));
        mai = ((RadioButton) findViewById(R.id.rb_mai));
        punish = ((Button) findViewById(R.id.btn_punish));

        gv_photos = ((GridView) findViewById(R.id.gv_photos));
        pro_title = ((EditText) findViewById(R.id.pub_product_title));
        pro_desc = ((EditText) findViewById(R.id.pub_pro_desc));
        location = ((TextView) findViewById(R.id.ways_desc));
        pro_type = ((Spinner) findViewById(R.id.punish_type));
        pro_price = ((EditText) findViewById(R.id.publish_price));


        //
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);


        backtomain.setOnClickListener(this);
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        switch (key) {
            case "juan":
                juan.setChecked(true);
                buyway = 1;
                break;
            case "zeng":
                zeng.setChecked(true);
                buyway = 2;
                break;
            case "huan":
                huan.setChecked(true);
                buyway = 3;
                break;
            case "mai":
                mai.setChecked(true);
                buyway = 4;
                break;
        }

        //发布按钮的点击事件
        punish.setOnClickListener(this);

        madapter = new BaseAdapter() {
            private ImageView product_photo;

            @Override
            public int getCount() {
                return imgs.size() + 1;
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
                View view = View.inflate(deal_publish_Activity.this, R.layout.choosephotoitem, null);
                product_photo = ((ImageView) view.findViewById(R.id.product_photo));
                if (position != imgs.size()) {

                    String img_path = imgs.get(position);

                    //对图片进行压缩
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inJustDecodeBounds = false;

                    options.inSampleSize = 10;   // width，hight设为原来的十分一


                    //  获取资源图片
                    Bitmap bmp = BitmapFactory.decodeFile(img_path, options);
                    product_photo.setImageBitmap(bmp);
                    return view;
                } else {
                    return view;
                }
            }
        };

        gv_photos.setAdapter(madapter);
        gv_photos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != imgs.size()) {
                    initDialog(position);
                }

                return true;
            }
        });
        gv_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == imgs.size()) {
                    pickImage();
                } else {
                    ToastUtil.show(deal_publish_Activity.this, "长按取消图片");
                }
            }
        });
        progressDialog = ToolsClass.createLoadingDialog(deal_publish_Activity.this, "定位中...", true,
                0);
        progressDialog.show();
        getLocation();


    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            //优先使用gps
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            // 没有可用的位置提供器
            Toast.makeText(deal_publish_Activity.this, "没有位置提供器可供使用", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            Toast.makeText(this, "安全异常", Toast.LENGTH_LONG).show();
        }

        if (location != null) {
            // 显示当前设备的位置信息
            String firstInfo = "第一次请求的信息";
            showLocation(location);
        } else {
            String info = "无法获得当前位置";
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        }

    }

    private void showLocation(Location location) {
//        Toast.makeText(this, location.getLatitude()+"", Toast.LENGTH_LONG).show();
        LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());

       mlatLng=convert(new LatLng(location.getLatitude(), location.getLongitude()));

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.GPS);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }


    private void initDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否删除图片？"); //设置内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭dialog
                Toast.makeText(deal_publish_Activity.this, "已取消", Toast.LENGTH_SHORT).show();
                imgs.remove(position);
                files.remove(position);
                madapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void pickImage() {
        MultiImageSelector selector = MultiImageSelector.create(deal_publish_Activity.this);
        selector.showCamera(true);
        selector.count(6);
        selector.multi();
        selector.origin(imgs);
        selector.start(deal_publish_Activity.this, REQUEST_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                imgs = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);

                for (String s : imgs) {

                    File file = new File(s);

                    files.add(file);
                }


                madapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_punish:
                userid = this.getSharedPreferences("shared_loginn_info", Context.MODE_PRIVATE).getInt("id", 0);
                if (userid != 0) {
                    initDialog();
                } else {
                    ToastUtil.show(this, "还未登录");
                }

                break;
            case R.id.backTomain:
                finish();
                break;
        }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(deal_publish_Activity.this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确定发布？"); //设置内容

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭dialog
                progressDialog = ToolsClass.createLoadingDialog(deal_publish_Activity.this, "发布中...", true,
                        0);
                progressDialog.show();
                if (!pro_title.getText().toString().equals("")
                        && pro_title.getText().toString() != null
                        && !pro_price.getText().toString().equals("")
                        && pro_price.getText().toString() != null
                        && !pro_desc.getText().toString().equals("")
                        && pro_desc.getText().toString() != null
                        && !location.getText().toString().equals("")
                        && location.getText().toString() != null
                        && !pro_type.getSelectedItem().toString().equals("")
                        && pro_type.getSelectedItem().toString() != null
                        ) {

                    uploadImage();

                } else {
                    progressDialog.dismiss();
                    ToastUtil.show(deal_publish_Activity.this, "请填写完整");
                }

                Toast.makeText(getApplicationContext(), "已确定", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


     //上传商品图片和相关信息
    public void uploadImage() {

            RequestParams requestParams = new RequestParams(HttpUtile.yu + "/uploadpro/upimg");
            requestParams.setMultipart(true);

            requestParams.setConnectTimeout(8*1000);
            CommodityInfo pro = new CommodityInfo(userid,
                    pro_title.getText().toString(),
                    Double.parseDouble(pro_price.getText().toString()),
                    pro_desc.getText().toString(),
                    null, new Date(),
                    location.getText().toString(),
                    buyway, pro_type.getSelectedItem().toString(), null, null
            );
            Gson gson = new Gson();
            String info = gson.toJson(pro);
            for (int i = 0; i < files.size(); i++) {
                File f = files.get(i);
                requestParams.addBodyParameter("file", f);
            }

            requestParams.addBodyParameter("info", info);
            requestParams.addBodyParameter("latitude",mlatLng.latitude+"");
            requestParams.addBodyParameter("longitude",mlatLng.longitude+"");

            x.http().post(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i("ModifyPersonInfo", "onSuccess: ");
                    ToastUtil.show(deal_publish_Activity.this, "发布成功");
                    finish();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                    ToastUtil.show(deal_publish_Activity.this, "发布失败");
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


    //逆地理编码
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        progressDialog.dismiss();
        if (i == 1000) {
            String addressName = regeocodeResult.getRegeocodeAddress().getProvince();
            addressName += regeocodeResult.getRegeocodeAddress().getCity();
            addressName += regeocodeResult.getRegeocodeAddress().getDistrict();

            location.setText(addressName);

        } else {
            ToastUtil.show(deal_publish_Activity.this, "无法定位");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    //转换坐标
    private LatLng convert(LatLng sourceLatLng) {
        CoordinateConverter converter  = new CoordinateConverter(this);
        // CoordType.GPS 待转换坐标类型
        converter.from(GPS);
        // sourceLatLng待转换坐标点
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }
}



