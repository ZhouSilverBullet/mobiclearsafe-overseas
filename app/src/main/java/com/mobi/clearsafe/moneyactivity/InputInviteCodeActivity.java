package com.mobi.clearsafe.moneyactivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.me.bean.UploadNikeName;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

public class InputInviteCodeActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private EditText input_inviteCode;
    private ImageView clear;
    private TextView btn_submit;
    private LinearLayout ll_back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_invite_code);
        initView();

    }

    private void initView() {
        input_inviteCode=findViewById(R.id.invite_code);
        clear=findViewById(R.id.icon_miss);
        btn_submit=findViewById(R.id.bt_submit);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.invite_code));
        input_inviteCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clear.setVisibility(charSequence.length()>0?View.VISIBLE:View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        clear.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.icon_miss:
                input_inviteCode.setText("");
                break;
            case R.id.bt_submit:
                if(TextUtils.isEmpty(input_inviteCode.getText())){
                    ToastUtils.showShort(getResources().getString(R.string.please_input_invite_code));
                    return;
                }
                OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                        .submitInviteCode(UserEntity.getInstance().getUserId(),input_inviteCode.getText().toString().trim())
                        .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(this))
                        .subscribe(new BaseObserver<UploadNikeName>() {
                            @Override
                            public void onSuccess(UploadNikeName demo) {
                                ToastUtils.showShort(getResources().getString(R.string.submit_success));
                                finish();
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg, String code) {
                                ToastUtils.showShort(errorMsg);
                            }
                        });
                break;
        }
    }
}
