package com.mobi.clearsafe.moneyactivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.QRCode;

public class EwmActivity extends BaseAppCompatActivity {


    private ImageView iv_ewm;
    private TextView sm_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButtonStatistical.inviteToSaoma();
        setContentView(R.layout.activity_ewm);
        String url = getIntent().getStringExtra("url");
        iv_ewm=findViewById(R.id.iv_ewm);
        if(!TextUtils.isEmpty(url)){
            iv_ewm.setImageBitmap(QRCode.createQRCode(url));
        }
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sm_tip=findViewById(R.id.sm_tip);
        sm_tip.setText(Html.fromHtml(getResources().getString(R.string.string_sm_tip)));
    }
}
