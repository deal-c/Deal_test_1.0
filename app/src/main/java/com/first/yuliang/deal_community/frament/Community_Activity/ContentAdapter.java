package com.first.yuliang.deal_community.frament.Community_Activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Dynamic;

import org.xutils.x;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ContentAdapter extends BaseAdapter implements View.OnClickListener {

    private static final String TAG = "ContentAdapter";
    private ArrayList<Dynamic> mContentList;
    private LayoutInflater mInflater;
    private Callback mCallback;

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     * @author Ivan Xu      * 2014-11-26
     */

    public interface Callback {
        public void click(View v);
    }

    public ContentAdapter(Context context, ArrayList<Dynamic> contentList,
                          Callback callback) {
        mContentList = contentList;
        mInflater = LayoutInflater.from(context);
        mCallback = callback;
    }
    @Override
    public int getCount() {
        Log.i(TAG, "getCount");
        return mContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "getItemId");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView");
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dongtai, null);
            holder = new ViewHolder();

            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.iv_dongtai_dongtaiphoto=(ImageView) convertView.findViewById(R.id.iv_dongtai_dongtaiphoto);
            holder.iv_dongtai_userphoto=(ImageView)convertView.findViewById(R.id.iv_dongtai_userphoto);
            holder.tv_dongtai_usename=(TextView) convertView.findViewById(R.id.tv_dongtai_usename);
            holder.tv_dongtai_content=(TextView) convertView.findViewById(R.id.tv_dongtai_content);
              //  holder.lv_tiezi=(ListView)convertView.findViewById(R.id.lv_tiezi);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mContentList.get(position).getContent());
        holder.tv_dongtai_content.setText(mContentList.get(position).getContent());
        x.image().bind((holder.iv_dongtai_userphoto), "http://10.40.5.61:8080/usys/imgs/" +mContentList.get(position).getUserId().getUserImg() + ".png");
        holder.tv_dongtai_usename.setText(mContentList.get(position).getUserId().getUserName());
        x.image().bind((holder.iv_dongtai_dongtaiphoto), "http://10.40.5.61:8080/usys/imgs/" +mContentList.get(position).getPic() + ".png");
    //    holder.lv_tiezi.setOnClickListener(this);
      //  holder.lv_tiezi.setTag(position);
        return convertView;
    }


    public class ViewHolder {
        public TextView textView;
        public Button button;
        public TextView tv_dongtai_content;
        public ImageView iv_dongtai_dongtaiphoto;
        public  ImageView iv_dongtai_userphoto;
        public TextView tv_dongtai_usename;
        public ListView lv_tiezi;

    }

    //响应按钮点击事件,调用子定义接口，并传入View
    @Override
    public void onClick(View v) {


        mCallback.click(v);
    }


}