package com.first.yuliang.deal_community;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchResultActivity extends AppCompatActivity {

    private int flag = 0;
    private ImageButton ib_return_search;
    private EditText query3;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String search=bundle.getString("search");
        Log.e("看看是不是传值过来==========",search);

        ib_return_search = ((ImageButton) findViewById(R.id.ib_return_search));
        ib_return_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("search"," ");
                Intent intent1 = new Intent();
                intent1.putExtra("bundle",bundle1);
                SearchResultActivity.this.setResult(0,intent1);
                SearchResultActivity.this.finish();
            }
        });
        //        EditText获得焦点后跳转
        query3 = ((EditText)findViewById(R.id.query3));
        query3.setText(search);
        query3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    query3.setFocusable(false);
                    query3.setFocusableInTouchMode(true);
                    setResult(0,intent);
                    SearchResultActivity.this.finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Bundle bundle1 = new Bundle();
        bundle1.putString("search"," ");
        Intent intent1 = new Intent();
        intent1.putExtra("bundle",bundle1);
        SearchResultActivity.this.setResult(0,intent1);
        SearchResultActivity.this.finish();
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