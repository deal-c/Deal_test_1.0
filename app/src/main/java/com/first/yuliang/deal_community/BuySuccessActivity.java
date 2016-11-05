package com.first.yuliang.deal_community;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.first.yuliang.deal_community.MyCenter.MyBuyActivity;
import com.first.yuliang.deal_community.frament.utiles.HttpUtile;
import com.first.yuliang.deal_community.pojo.CommodityBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;

public class BuySuccessActivity extends AppCompatActivity{
    // 此为测试Appid,请将Appid改成你自己的Bmob AppId
    String APPID = "0c22724d67dd91a579b3a1d54ab88c73";
    // 此为支付插件的官方最新版本号,请在更新时留意更新说明
    int PLUGINVERSION = 7;
    private CommodityBean.Commodity commodity;
    Button pay;
    RadioGroup rg_zhifu;
    String order_Id;
    ProgressDialog dialog;
    String tips = "1";
    private Intent intent;
    private ImageView iv_success;
    private TextView tv_result;
    private LinearLayout ll_success;
    private Button btn_into;
    private Button btn_return;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_success);

        // 必须先初始化
        BP.init(this, APPID);

        intent = getIntent();
        tips = intent.getStringExtra("tips");
        commodity = intent.getParcelableExtra("bundle");
        id = getSharedPreferences("shared_loginn_info",Context.MODE_PRIVATE).getInt("id",0);
        Log.e("看看支付数据",commodity.commodityId+commodity.price+tips);

        pay = (Button) findViewById(R.id.btn_zhifu);
        rg_zhifu = (RadioGroup) findViewById(R.id.rg_zhifu);
        iv_success = ((ImageView) findViewById(R.id.iv_success));
        tv_result = ((TextView) findViewById(R.id.tv_result));
        ll_success = ((LinearLayout) findViewById(R.id.ll_success));
        btn_into = ((Button) findViewById(R.id.btn_result_into));
        btn_return = ((Button) findViewById(R.id.btn_result_return));

        btn_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySuccessActivity.this.finish();
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuySuccessActivity.this, MyBuyActivity.class);
                startActivity(intent);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (rg_zhifu.getCheckedRadioButtonId() == R.id.rb_zhifubao) // 当选择的是支付宝支付时
                    pay(true);
                else if (rg_zhifu.getCheckedRadioButtonId() == R.id.rb_weixin) // 调用插件用微信支付
                    pay(false);
            }
        });

        int pluginVersion = BP.getPluginVersion();
        if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
            Toast.makeText(
                    BuySuccessActivity.this,
                    pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                            : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)", Toast.LENGTH_SHORT).show();
            installBmobPayPlugin("bp.db");
        }
    }

    /**
     * 调用支付
     *
     * @param alipayOrWechatPay
     *            支付类型，true为支付宝支付,false为微信支付
     */
    void pay(final boolean alipayOrWechatPay) {
        showDialog("正在提交订单...");
        final String name = getName();

        BP.pay(name, getBody(), getPrice(), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(BuySuccessActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(BuySuccessActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                iv_success.setImageResource(R.drawable.success);
                rg_zhifu.setVisibility(View.GONE);
                pay.setVisibility(View.GONE);
                ll_success.setVisibility(View.VISIBLE);
                updateCommodityState(commodity.commodityId);
                tv_result.setText("支付成功！");
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                order_Id = orderId;
                showDialog("成功提交订单!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            BuySuccessActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Toast.makeText(BuySuccessActivity.this, "支付失败!", Toast.LENGTH_SHORT)
                            .show();
                }
                hideDialog();
            }
        });
    }

    // 执行订单查询
    void query() {
        showDialog("正在查询订单...");
        final String orderId = getOrder();

        BP.query(orderId, new QListener() {

            @Override
            public void succeed(String status) {
                Toast.makeText(BuySuccessActivity.this, "查询成功!该订单状态为 : " + status,
                        Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(BuySuccessActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    // 默认为0.02
    double getPrice() {
        double price = 0.02;
        try {
            price = Double.parseDouble(commodity.price.toString());
        } catch (NumberFormatException e) {
        }
        return price;
    }

    // 商品详情(可不填)
    String getName() {
        return commodity.commodityId.toString();
    }

    // 商品详情(可不填)
    String getBody() {
        return tips;
    }

    // 支付订单号(查询时必填)
    String getOrder() {
        return order_Id;
    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateCommodityState(int commodityId){
        RequestParams params = null;
        String url = HttpUtile.szj + "/csys/modifycommoditystate?commodityId="+commodityId+"&buyUserId="+id+"&state=2";
        params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(BuySuccessActivity.this, "是不是我的无法连接服务器", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
