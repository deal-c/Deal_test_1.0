package com.first.yuliang.deal_community.home_button_activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.ToolsClass;
import com.first.yuliang.deal_community.Util.DateUtils;
import com.first.yuliang.deal_community.frament.pojo.Oldnew;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.frament.utiles.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class old_tonew extends AppCompatActivity {

    private ListView lv_oldnew;
    private List<Oldnew>oldlist=new ArrayList<Oldnew>();
    Dialog progressDialog;
    BaseAdapter madapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_tonew);

        lv_oldnew = ((ListView) findViewById(R.id.lv_oldToNew));
        back = ((ImageView) findViewById(R.id.jiuhuanxin_returen));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        madapter=new BaseAdapter() {



            @Override
            public int getCount() {
                return oldlist.size();
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
                View view=View.inflate(old_tonew.this,R.layout.oldtonew_item,null);

                Oldnew oldnew=oldlist.get(position);

                Button  time = ((Button) view.findViewById(R.id.time_oldnew));
                TextView   title = ((TextView) view.findViewById(R.id.old_title));
                TextView desc = ((TextView) view.findViewById(R.id.old_desc));
                ImageView img = ((ImageView) view.findViewById(R.id.iv_old_img));
                TextView place=((TextView) view.findViewById(R.id.old_place));
              String times= DateUtils.dateToString(DateUtils.stringToDate(oldnew.getOldnewtime()),"yyyy年MM月dd日") ;
                time.setText(times);

                title.setText(oldnew.getOldnewtitle());
                desc.setText(oldnew.getOldnewinfo());
                place.setText(oldnew.getPlace());
                x.image().bind(img,HttpUtile.yu+oldnew.getOldnewimg());
                return view;
            }
        };
        lv_oldnew.setAdapter(madapter);

       getoldlist();


    }

    public void getoldlist() {
        progressDialog = ToolsClass.createLoadingDialog(old_tonew.this, "加载中...", true,
                0);
        progressDialog.show();
        RequestParams params=new RequestParams(HttpUtile.yu+"/community/togetoldnew");
        params.setConnectTimeout(8*1000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();

                Type type=new TypeToken<List<Oldnew>>(){}.getType();
                oldlist=gson.fromJson(result,type);
                madapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(old_tonew.this,"加载失败");
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
}
