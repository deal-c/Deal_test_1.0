package com.first.yuliang.deal_community;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchResultActivity extends AppCompatActivity {

    private int flag = 0;
    private ImageButton ib_return_search;
    private EditText query3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ib_return_search = ((ImageButton) findViewById(R.id.ib_return_search));
        ib_return_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.this.finish();
            }
        });
        //        EditText获得焦点后跳转
        query3 = ((EditText)findViewById(R.id.query3));
        query3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    query3.setFocusable(false);
                    query3.setFocusableInTouchMode(true);
                    SearchResultActivity.this.finish();
                }
            }
        });
    }
}



//
//                          █ █ █ █ █ █ █
//                    █ █ █ █ █ █ █ █ █ █ █
//                 █ █ █ █ █ █ █ █ █ █ █ █ █
//              █ █ █ █    █    █    █    █ █ █ █
//           █ █ █ █          █    █             █ █ █
//           █ █ █                                     █ █ █
//        █ █ █                                           █ █
//     █ █ █ █                                        █    █ █
//        █ █                                              █ █ █
//     █ █ █ █                                              █ █ █
//     █ █ █                                              █ █ █
//     █ █ █                                                 █ █ █
//  █    █ █    █                                              █ █
//  █    █ █       █ █ █                █ █ █ █ █    █ █ █
//  █    █    █    █ █ █ █          █ █ █    █    █    █ █
//  █             █ █    █    █       █    █ █    █    █    █
//  █             █             █ █ █ █             █          █
//     █ █                      █       █ █       █       █ █
//     █    █          █ █ █          █    █ █             █
//        █    █                         █                   █ █
//        █ █    █                      █                █ █
//           █ █                   █    █ █          █    █
//           █    █                   █ █                █
//           █ █    █                         █       █    █
//              █ █             █ █ █ █ █       █    █
//              █    █    █                            █ █
//                 █    █             █ █    █    █ █ █
//                 █ █    █                      █    █ █ █
//                 █ █ █    █    █    █    █    █ █ █ █ █ █
//                 █ █ █ █    █    █    █    █ █ █ █ █ █ █ █ █
//              █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █
//        █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █
//     █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █ █