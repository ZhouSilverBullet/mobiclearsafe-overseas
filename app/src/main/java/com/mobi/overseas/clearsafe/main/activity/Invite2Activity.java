package com.mobi.overseas.clearsafe.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.main.bean.InviteDetail;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ShareContentBean;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.wxapi.WeixinHandler;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

/**
 * 邀请好友页
 */

public class Invite2Activity extends BaseAppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite2);
        ButtonStatistical.invitePageCount();
        initView();
        getShareContent();
        getDetail();
    }

    private void getDetail() {

    }

    //得到分享内容
    private void getShareContent() {

    }



    private void initView() {


    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.tv_gz:
//                InviteRulesDialog dialog=new InviteRulesDialog.Builder(this)
//                        .setUrl(inviteDetail.getRule_url())
//                        .setButtonClick(new InviteRulesDialog.onButtonClick() {
//                            @Override
//                            public void onConfirmClick(Dialog dialog) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .build();
//                dialog.show();
//                break;
//            case R.id.tv_copy:
//                if(!TextUtils.isEmpty(tv_yqm.getText())){
//                    if (AppUtil.copy(this,tv_yqm.getText().toString().trim())) {
//                        ToastUtils.showShort(getResources().getString(R.string.yqm_tip));
//                    }
//                }
//                break;
//            case R.id.invite_btn:
//                if(mShareBean!=null){
//                    invite_layout.setVisibility(View.GONE);
//                    final ShareDialog shareDialog = ShareDialog.getInstance(mShareBean, new ShareDialog.ClickListener() {
//                        @Override
//                        public void onClick() {
//                            invite_layout.setVisibility(View.VISIBLE);
//                        }
//                    });
//                    DialogUtils.showDialog(getSupportFragmentManager(), shareDialog, ShareDialog.TAG);
//                }
//                break;
//            case R.id.tv_friendmanager:
//                startActivity(new Intent(this,FriendManagerActivity.class));
//                break;
//            case R.id.share_to_weixin_friends:
//                doShare(false);
//                break;
//            case R.id.share_to_weixin_timeline:
//                doShare(true);
//                break;
//            case R.id.share_to_code:
//                if(mShareBean!=null){
//                    Intent intent = new Intent(getActivity(), EwmActivity.class);
//                    intent.putExtra("url",mShareBean.getJump_address()+ "?code=" + mShareBean.getCode());
//                    startActivity(intent);
//                }
//                break;
//        }
    }

    private void doShare(boolean toTimeline) {
        if (!WeixinHandler.getInstance().isWeixinInstalled()) {
            ToastUtils.showShort(R.string.wechat_login_tip);
            return;
        }
//        WeixinHandler.getInstance().shareToWeixin(mShareBean.getName(),mShareBean.getIntroduction(),mShareBean.getJump_address()+ "?code=" + mShareBean.getCode()+ "?user_id=" +UserEntity.getInstance().getUserId(),R.mipmap.icon_round,toTimeline);
    }
}
