package com.first.yuliang.deal_community;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.first.yuliang.deal_community.frament.fragment_type_1;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.TypeBean;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TypeActivity extends AppCompatActivity {

    private FrameLayout fl_type;
    private ListView lv_type;
    private BaseAdapter adapter;
    private List<TypeBean.Type> typess=new ArrayList<>();
    private ImageButton ib_return;
    private TextView tv_type;
    Fragment fragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        initView();

        initData();


    }

    private void initView() {

        fl_type = ((FrameLayout) findViewById(R.id.fl_type));

        lv_type = ((ListView) findViewById(R.id.lv_type));

        tv_type = ((TextView) findViewById(R.id.tv_activity_title));

        ib_return = ((ImageButton) findViewById(R.id.ib_return_common));
    }

    private void initData() {

        tv_type.setText("分类");
        ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeActivity.this.finish();
            }
        });

        adapter=new BaseAdapter(){
            @Override
            public int getCount() {
                return typess.size();
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

                View view = View.inflate(TypeActivity.this, R.layout.activity_type_item, null);

                TextView tv_item = ((TextView) view.findViewById(R.id.tv_item));


                TypeBean.Type ty = typess.get(position);


                tv_item.setText(ty.typeName);
                return view;

            }
        };


        lv_type.setAdapter(adapter);





        //switchFragment(null,0);

        getTypeList();

        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position)
                {
                    case 0:
                        fragment=new fragment_type_1();

                    break;
                    case 1:
                        fragment=new fragment_type_1();
                        break;
                    case 2:
                        fragment=new fragment_type_1();
                        break;
                    case 3:
                        fragment=new fragment_type_1();
                        break;
                    case 4:
                        fragment=new fragment_type_1();
                        break;
                    case 5:
                        fragment=new fragment_type_1();
                        break;
                    case 6:
                        fragment=new fragment_type_1();
                        break;
                    case 7:
                       fragment=new fragment_type_1();
                    break;
                    case 8:
                        fragment=new fragment_type_1();
                        break;
                    case 9:
                        fragment=new fragment_type_1();
                        break;
                    case 10:
                        fragment=new fragment_type_1();
                        break;
                    case 11:
                        fragment=new fragment_type_1();
                        break;
                    case 12:
                        fragment=new fragment_type_1();
                        break;
                    case 13:
                        fragment=new fragment_type_1();
                        break;
                    case 14:
                        fragment=new fragment_type_1();
                        break;
                    case 15:
                        fragment=new fragment_type_1();
                        break;
                    case 16:
                        fragment=new fragment_type_1();
                        break;
                    case 17:
                        fragment=new fragment_type_1();
                        break;
                    case 18:
                        fragment=new fragment_type_1();
                        break;
                    case 19:
                        fragment=new fragment_type_1();
                        break;
                    case 20:
                        fragment=new fragment_type_1();
                        break;
                    case 21:
                        fragment=new fragment_type_1();
                        break;
                    case 22:
                        fragment=new fragment_type_1();
                        break;
                    case 23:
                        fragment=new fragment_type_1();
                        break;
                    case 24:
                        fragment=new fragment_type_1();
                        break;

                }
                   switchFragment(fragment,position+1);
            }
        });
    }

//    private List<Sub_Type_Bean.Sub_Type> getSub_TypeLsit(int Id) {
//
//        RequestParams params=new RequestParams("http://10.40.5.21:8080/FourProject/servlet/sub_type_servlet?typeId="+Id);
//        x.http().get(params, new Callback.CommonCallback<String>() {
//
//            @Override
//            public void onSuccess(String result) {
//
//                Gson gson=new Gson();
//                Sub_Type_Bean stb= gson.fromJson(result,Sub_Type_Bean.class);
//                sub_typess.addAll(stb.sub_types);
//
//
//                //return sub_typess;
//               // Toast.makeText(TypeActivity.this,sub_typess+"",Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });
//        return sub_typess;
//
//    }


    private List<TypeBean.Type> getTypeList() {

        RequestParams params=new RequestParams(HttpUtile.zy+"servlet/TypeServlet");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                Gson gson=new Gson();
                TypeBean tb= gson.fromJson(result,TypeBean.class);
                typess.addAll(tb.typeList);
                adapter.notifyDataSetChanged();
                int id = typess.get(0).typeId;
                fragment=new fragment_type_1();
                switchFragment(fragment,id);
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

       return typess;
    }



    private void switchFragment(Fragment fragment,int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("Id", position);
        fragment.setArguments(bundle);
        this.getFragmentManager().beginTransaction().replace(R.id.fl_type,fragment).commit();

    }

    public void backHome(View view)
    {
        this.finish();
    }
}
