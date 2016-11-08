package com.first.yuliang.deal_community.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.frament.pojo.Adbean;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;

import org.xutils.x;

public class ad_page extends AppCompatActivity {
    private  Intent intent;
    private TextView title;
    private TextView content;
    private TextView ad_http_;
    private ImageView img;
    private TextView timeaaaaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        ad_http_ = ((TextView) findViewById(R.id.ad_http_));
        title = ((TextView) findViewById(R.id.ad_title1));
        content = ((TextView) findViewById(R.id.content1));
        timeaaaaa =((TextView) findViewById(R.id.timeaaaaa));
        img = ((ImageView) findViewById(R.id.ad_img1));

        intent=getIntent();
        Adbean.Ad ad=intent.getParcelableExtra("ad");

        title.setText(ad.getAdtitle());
        content.setText(ad.getAdcontent());
        ad_http_.setText(ad.getAdhttp());

        x.image().bind(img, HttpUtile.yu+ad.getAdphoto());
    }
}
