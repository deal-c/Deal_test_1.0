package com.first.yuliang.deal_community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager vp;

    private int previousPage=0;

    private Button btn_skip ;
    private Button btn_intologin;
   // private Button btn_intoreg;
    private Button btn_into;
    boolean isFirstIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        setContentView(R.layout.activity_view_pager);
        SharedPreferences preferences = getSharedPreferences(
                "first_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();


        vp = ((ViewPager) findViewById(R.id.vp));




        final int[] point=new int[]{R.id.iv_iv1,R.id.iv_iv2,R.id.iv_iv3,R.id.iv_iv4,R.id.iv_iv5};


        btn_skip = ((Button) findViewById(R.id.btn_skip));

        btn_intologin = ((Button) findViewById(R.id.btn_intologin));



        btn_into = ((Button) findViewById(R.id.btn_into));


        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ViewPagerActivity.this,mainActivity.class);
                startActivity(intent);
                 finish();
            }
        });


        btn_intologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewPagerActivity.this,RegActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ViewPagerActivity.this,mainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        final List<Integer> imgs=new ArrayList<>();

        imgs.add(0,R.drawable.start_i0);

        imgs.add(1,R.drawable.start_i1);
        imgs.add(2,R.drawable.start_i2);
        imgs.add(3,R.drawable.start_i3);
        imgs.add(4,R.drawable.start_i4);


        MyPagerAdapter adpater=new MyPagerAdapter(imgs);

        vp.setAdapter(adpater);


        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {




            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ((ImageView) findViewById(point[position])).setImageResource(R.drawable.point_red);
                ((ImageView) findViewById(point[previousPage])).setImageResource(R.drawable.point_gray);
                previousPage=position;

                if(position==point.length-1)
                {btn_skip.setVisibility(View.GONE);

                 btn_intologin.setVisibility(View.VISIBLE);

                 btn_into.setVisibility(View.VISIBLE);

                }
                else
                {   btn_intologin.setVisibility(View.GONE);
                    btn_into.setVisibility(View.GONE);
                    btn_skip.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    private class MyPagerAdapter extends PagerAdapter
    {

        List<Integer> data;

        public MyPagerAdapter(List<Integer> data) {
            this.data = data;
        }



        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        public MyPagerAdapter() {
            super();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view=View.inflate(getApplicationContext(),R.layout.viewpager_item,null);

            ImageView iv_vp = ((ImageView)view.findViewById(R.id.iv_vp));

            iv_vp.setImageResource(data.get(position));

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           //super.destroyItem(container, position, object);

            container.removeView((View)object);
        }
    }


}
