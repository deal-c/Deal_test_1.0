package com.first.yuliang.deal_community.home_button_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.first.yuliang.deal_community.R;
import com.first.yuliang.deal_community.Util.GifView;

public class huan_magic extends AppCompatActivity {

    private GifView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huan_magic);


        pic = ((GifView) findViewById(R.id.circlepic));
        pic.setMovieResource(R.drawable.loadddd);

//              ImageOptions imageOptions = new ImageOptions.Builder()
//                .setImageScaleType(CENTER_CROP)
//                .setCircular(true)
//                .setFailureDrawableId(R.mipmap.ic_launcher)
//                .setLoadingDrawableId(R.mipmap.ic_launcher)
//                .build();
//
//        x.image().bind(pic, HttpUtile.yu+"/deal_ad/upload/ad2.jpg",imageOptions);


    }
}
