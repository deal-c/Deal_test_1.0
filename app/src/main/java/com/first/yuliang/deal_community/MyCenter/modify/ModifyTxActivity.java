package com.first.yuliang.deal_community.MyCenter.modify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.first.yuliang.deal_community.R;


public class ModifyTxActivity extends AppCompatActivity {

    private ImageView iv_tx_camora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tx);
        iv_tx_camora = ((ImageView) findViewById(R.id.iv_tx_camora));
        iv_tx_camora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
