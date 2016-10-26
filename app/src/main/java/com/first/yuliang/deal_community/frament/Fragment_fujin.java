package com.first.yuliang.deal_community.frament;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.first.yuliang.deal_community.R;

/**
 * Created by yuliang on 2016/9/21.
 */
public class Fragment_fujin extends Fragment  implements LocationSource, AMapLocationListener {
    MapView mapView = null;
    AMap aMap=null;
    private AMapLocationClient mlocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.actvity_fujin,null);

        mapView = (MapView) view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理

        mapView.onCreate(savedInstanceState);
        init();
        initMarker();
        AMap.OnMarkerClickListener listener = new AMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Toast.makeText(getActivity(),arg0.getTitle()+":  "+arg0.getSnippet(),Toast.LENGTH_LONG).show();
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
        if (hidden){
            mapView.setVisibility(View.GONE);
        }else{
            mapView.setVisibility(View.VISIBLE);
        }
    }

    private void initMarker() {
        LatLng marker1 = new LatLng(31.2762990000,120.7417510000);
        LatLng marker2=new LatLng(31.2751160000,120.7416330000);
        LatLng marker3=new LatLng(31.2750330000,120.7436610000);
        final Marker marker = aMap.addMarker(new MarkerOptions().
                position(marker1).
                title("文星广场").
                snippet("吃饭的地方!"));
        final Marker aff = aMap.addMarker(new MarkerOptions().
                position(marker2).
                title("篮球场").
                snippet("玩的地方!"));
        final Marker ma= aMap.addMarker(new MarkerOptions().
                position(marker3).
                title("教室").
                snippet("学习的地方!"));
    }


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

        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.getUiSettings().setCompassEnabled(true);

    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                Log.d("===经度：",""+amapLocation.getLongitude());
                Log.d("===纬度：",""+amapLocation.getLatitude());
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
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



}