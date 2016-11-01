package com.first.yuliang.deal_community.frament;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.first.yuliang.deal_community.CityPickerDialog;
import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.address.City;
import com.first.yuliang.deal_community.address.County;
import com.first.yuliang.deal_community.address.Province;
import com.first.yuliang.deal_community.frament.pojo.GProduct;
import com.first.yuliang.deal_community.frament.pojo.Store;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.first.yuliang.deal_community.pojo.CommodityBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.amap.api.maps.model.BitmapDescriptorFactory.defaultMarker;

/**
 * Created by yuliang on 2016/9/21.
 */
public class Fragment_fujin extends Fragment implements LocationSource,
        AMapLocationListener, View.OnClickListener, PoiSearch.OnPoiSearchListener {
    TextureMapView mapView = null;
    AMap aMap = null;
    private AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private Button maijia;
    private Button product;
    private Button quyu;
    private List<Province> provinces = new ArrayList<Province>();
    private List<Store> storeList = new ArrayList<Store>();
    private List<GProduct> gProductList = new ArrayList<GProduct>();
    private Button change;
    private CommodityBean.Commodity commodity;
    private ImageView image;
    Dialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actvity_fujin, null);

        mapView = (TextureMapView) view.findViewById(R.id.map);


        maijia = ((Button) view.findViewById(R.id.fujin_maijia));
        product = ((Button) view.findViewById(R.id.fujin_product));
        quyu = ((Button) view.findViewById(R.id.btn_quyu));
        change = ((Button) view.findViewById(R.id.change));
        quyu.setOnClickListener(this);
        maijia.setOnClickListener(this);
        product.setOnClickListener(this);
        change.setOnClickListener(this);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理

        mapView.onCreate(savedInstanceState);

        init();
//        initMarker();

        //获得storelist
        getStoeList();
        getProductList();

        AMap.OnMarkerClickListener listener = new AMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if (arg0.getTitle()!="" && arg0.getTitle()!=null) {
                    Toast.makeText(getActivity(), arg0.getTitle() + ":  " + arg0.getSnippet(), Toast.LENGTH_LONG).show();
                }else {

                   // icon(BitmapDescriptorFactory
                    //        .defaultMarker(BitmapDescriptorFactory.HUE_RED))

                    int id =((GProduct)arg0.getObject()).getCommodityid();
                    getcombyid(id);
                    jumpPoint(arg0);
                    Toast.makeText(getActivity(),commodity.commodityTitle, Toast.LENGTH_LONG).show();
                    arg0.setInfoWindowEnable(false);
//                    innitPoupwindow();

                }
                return false;
            }
        };

        //绑定标注点击事件
        aMap.setOnMarkerClickListener(listener);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mapView.setVisibility(View.GONE);
        } else {
            mapView.setVisibility(View.VISIBLE);
        }
    }

//    private void initMarker() {
//        LatLng marker1 = new LatLng(31.2762990000, 120.7417510000);
//        LatLng marker2 = new LatLng(31.2751160000, 120.7416330000);
//        LatLng marker3 = new LatLng(31.2750330000, 120.7436610000);
//        Marker marker = aMap.addMarker(new MarkerOptions().
//                position(marker1).
//                title("文星广场").
//                snippet("吃饭的地方!"));
//        Marker aff = aMap.addMarker(new MarkerOptions().
//                position(marker2).
//                title("篮球场").
//                snippet("玩的地方!"));
//        Marker ma = aMap.addMarker(new MarkerOptions().
//                position(marker3).
//                title("教室").
//                snippet("学习的地方!"));
//    }


    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转3种
        //跟随：LOCATION_TYPE_MAP_FOLLOW
        //旋转：LOCATION_TYPE_MAP_ROTATE
        //定位：LOCATION_TYPE_LOCATE
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        etupLocationStyle();
    }

    // 自定义系统定位蓝点
    private void etupLocationStyle() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                Log.d("===经度：", "" + amapLocation.getLongitude());
                Log.d("===纬度：", "" + amapLocation.getLatitude());
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

            //获取一次定位结果：该方法默认为false。
            mLocationOption.setOnceLocation(true);

            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//            mLocationOption.setOnceLocationLatest(true);

            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

boolean is3d=true;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fujin_maijia:
                getAllmaijia();

                break;
            case R.id.fujin_product:
                getAllproduct();
                break;
            case R.id.btn_quyu:
                if (provinces.size() > 0) {
                    showAddressDialog();
                } else {
                    new InitAreaTask(getActivity()).execute(0);
                }
                break;
            case R.id.change:
                if (is3d) {
                    aMap.moveCamera(CameraUpdateFactory.changeTilt(50));

                    is3d=false;
                }else {
                    aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
                    is3d=true;
                }
                break;
        }

    }

    boolean flag1 = true;

    boolean flag2 = true;

    private void getAllproduct() {
        flag2=true;
        if (!flag1) {
            aMap.clear();
            etupLocationStyle();
            Log.i("biaoji","隐藏商品");
            flag1 = true;
        } else {
            aMap.clear();
            etupLocationStyle();
            addProMakerToMap();
            flag1 = false;
            Log.i("biaoji","显示商品");
        }
    }

    private void addProMakerToMap() {

        for (GProduct s : gProductList) {
            LatLng marker = new LatLng(s.getLatitude(), s.getLongitude());
            Marker a = aMap.addMarker(new MarkerOptions().
//                    snippet(s.getCommodityid()+"").
                    position(marker).icon(
                            defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//            a.setInfoWindowEnable(false);
            a.setObject(s);
            startGrowAnimation(a);

        }
    }



    void getAllmaijia() {
        flag1=true;
        if (!flag2) {
            aMap.clear();
            etupLocationStyle();
            flag2 = true;
            Log.i("biaoji","隐藏卖家");
        } else {
            aMap.clear();
            etupLocationStyle();
            addMarkersToMap();
            flag2 = false;
            Log.i("biaoji","显示卖家");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

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

    //级联对话框
    private void showAddressDialog() {

        new CityPickerDialog(getActivity(), provinces, null, null, null,
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
                        //将address赋值
                        //poi搜素
                        doSearchQuery(address.toString());
//

                    }
                }).show();

    }


    private class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {
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

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索

    protected void doSearchQuery(String keyWord) {
        Log.i("why", keyWord);
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(getActivity(), query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    //搜索地点
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
                        LatLng a=new LatLng(poiOverlay.getPoiItem(0).getLatLonPoint().getLatitude(),poiOverlay.getPoiItem(0).getLatLonPoint().getLongitude());
                        aMap.animateCamera(  CameraUpdateFactory.newCameraPosition(new CameraPosition(a, 17, 50, 0)) ,1000,null);
//                        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                        is3d=false;
//                        poiOverlay.zoomToSpan();


                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(getActivity(),
                                "对不起没有相关数据");
                    }
                }
            } else {
                ToastUtil.show(getActivity(),
                        "对不起没有相关数据");
            }
        } else {
            ToastUtil.showerror(getActivity(), rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(getActivity(), infomation);

    }

    private void getStoeList() {
        RequestParams params = new RequestParams(HttpUtile.yu + "/community/togetstore");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Store>>() {
                }.getType();
                storeList = gson.fromJson(result, type);
                Log.i("storeList", storeList.size() + "");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "网络访问错误", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void addMarkersToMap() {

        for (Store s : storeList) {
            LatLng marker = new LatLng(s.getLatitude(), s.getLongitude());
            Marker a = aMap.addMarker(new MarkerOptions().
                    position(marker).
                    title(s.getTitle()).
                    snippet(s.getContent()).icon(
                    defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            startGrowAnimation(a);
        }


    }

    private void getProductList() {
        RequestParams params = new RequestParams(HttpUtile.yu + "/community/togetgproduct");

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<GProduct>>() {
                }.getType();
                gProductList = gson.fromJson(result, type);
                Log.i("storeList", gProductList.size() + "");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "网络访问错误", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    //开启maker 生长动画
    private void startGrowAnimation(Marker growMarker) {
        if(growMarker != null) {
            Log.i("biaoji","进了来");
            Animation animation = new ScaleAnimation(0,1,0,1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }

   private void getcombyid(int commodityId){
       progressDialog = ToolsClass.createLoadingDialog(getActivity(), "注册中...", true,
               0);
       progressDialog.show();
       RequestParams params=new RequestParams(HttpUtile.yu+"/community/togetproduct");
       params.addQueryStringParameter("commodityId",commodityId+"");
       x.http().get(params, new Callback.CommonCallback<String>() {
           @Override
           public void onSuccess(String result) {
               Gson gson=new Gson();
                commodity=gson.fromJson(result,CommodityBean.Commodity.class);
               innitPoupwindow();
           }

           @Override
           public void onError(Throwable ex, boolean isOnCallback) {
                  ToastUtil.show(getActivity(),"访问失败");
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
    private void innitPoupwindow() {
        Log.i("innitPoupwindow","进来了");
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.pro_popwindow_item, null);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        final PopupWindow popupWindow = new PopupWindow(v, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        //popupwiondow外面点击，popupwindow消失
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        ((LinearLayout) v.findViewById(R.id.pop)).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"跳转的商品详情",Toast.LENGTH_LONG).show();
            }
        });
        ((TextView) v.findViewById(R.id.tv_11)).setText(commodity.commodityTitle);
        ((TextView) v.findViewById(R.id.tv_22)).setText("￥："+commodity.price);
        image = ((ImageView) v.findViewById(R.id.iv11));
        x.image().bind(image,HttpUtile.szj+commodity.commodityImg);
        popupWindow.setAnimationStyle(R.style.Animation);
//        popupWindow.showAsDropDown(view);
        popupWindow.showAsDropDown(maijia);

    }

    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


}