package com.mobi.overseas.clearsafe.net;


import com.alibaba.fastjson.JSONObject;
import com.mobi.overseas.clearsafe.fragment.bean.AggregationBean;
import com.mobi.overseas.clearsafe.fragment.bean.DialogCoinsBean;
import com.mobi.overseas.clearsafe.fragment.bean.SignBean;
import com.mobi.overseas.clearsafe.fragment.bean.SigninBean;
import com.mobi.overseas.clearsafe.fragment.bean.TargetStepBean;
import com.mobi.overseas.clearsafe.fragment.bean.TodayTaskBean;
import com.mobi.overseas.clearsafe.fragment.bean.TodayWelfareBean;
import com.mobi.overseas.clearsafe.fragment.bean.WelfareReceiveBean;
import com.mobi.overseas.clearsafe.main.BannerBean;
import com.mobi.overseas.clearsafe.main.BubbleClickBean;
import com.mobi.overseas.clearsafe.main.ConfigEntity;
import com.mobi.overseas.clearsafe.main.GoldBubble;
import com.mobi.overseas.clearsafe.main.PleasantlyBean;
import com.mobi.overseas.clearsafe.main.PleasantlyReceiveBean;
import com.mobi.overseas.clearsafe.main.RewardBean;
import com.mobi.overseas.clearsafe.main.StepExchangeCoins;
import com.mobi.overseas.clearsafe.main.UpdateBean;
import com.mobi.overseas.clearsafe.main.UserStepInfo;
import com.mobi.overseas.clearsafe.main.bean.CardState;
import com.mobi.overseas.clearsafe.main.bean.CollectNumBean;
import com.mobi.overseas.clearsafe.main.bean.EggBean;
import com.mobi.overseas.clearsafe.main.bean.ExitDialogBean;
import com.mobi.overseas.clearsafe.main.bean.ExperienceActivityBean;
import com.mobi.overseas.clearsafe.main.bean.HotNoticeBean;
import com.mobi.overseas.clearsafe.main.bean.IntegralBean;
import com.mobi.overseas.clearsafe.main.bean.IntegralWallReceiveBean;
import com.mobi.overseas.clearsafe.main.bean.InviteDetail;
import com.mobi.overseas.clearsafe.main.bean.InviteRecord;
import com.mobi.overseas.clearsafe.main.bean.NemberBean;
import com.mobi.overseas.clearsafe.main.bean.RedBean;
import com.mobi.overseas.clearsafe.main.bean.RedCoinsBean;
import com.mobi.overseas.clearsafe.main.bean.SignRaceSuccessBean;
import com.mobi.overseas.clearsafe.main.bean.StepRaceBean;
import com.mobi.overseas.clearsafe.main.bean.StepRaceHistoryBean;
import com.mobi.overseas.clearsafe.main.bean.StepRaceRewardBean;
import com.mobi.overseas.clearsafe.me.bean.MoneyRecordBean;
import com.mobi.overseas.clearsafe.me.bean.MyCardBean;
import com.mobi.overseas.clearsafe.me.bean.PointsBean;
import com.mobi.overseas.clearsafe.me.bean.PointsRecord;
import com.mobi.overseas.clearsafe.me.bean.ReceiveeErningsBean;
import com.mobi.overseas.clearsafe.me.bean.StepHistoryBean;
import com.mobi.overseas.clearsafe.me.bean.UploadNikeName;
import com.mobi.overseas.clearsafe.me.bean.WalletBean;
import com.mobi.overseas.clearsafe.me.bean.WithDrawalData;
import com.mobi.overseas.clearsafe.me.bean.WithdrawBean;
import com.mobi.overseas.clearsafe.me.bean.WithdrawCheckBean;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ActivityBean;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ActivityEntity;
import com.mobi.overseas.clearsafe.moneyactivity.bean.BindPhoneBean;
import com.mobi.overseas.clearsafe.moneyactivity.bean.CardBean;
import com.mobi.overseas.clearsafe.moneyactivity.bean.RewardEntity;
import com.mobi.overseas.clearsafe.moneyactivity.bean.SendSMSVerificationCode;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ShareContentBean;
import com.mobi.overseas.clearsafe.moneyactivity.bean.SiginRemind;
import com.mobi.overseas.clearsafe.moneyactivity.bean.SigninDouble;
import com.mobi.overseas.clearsafe.moneyactivity.bean.TaskEntity;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanListBean;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.overseas.clearsafe.ui.repair.data.CleanAuthOpenBean;
import com.mobi.overseas.clearsafe.ui.repair.data.CleanAuthOutBean;
import com.mobi.overseas.clearsafe.wxapi.bean.AccessTokenBean;
import com.mobi.overseas.clearsafe.wxapi.bean.LoginBean;
import com.mobi.overseas.clearsafe.wxapi.bean.UserInfo;
import com.mobi.overseas.clearsafe.wxapi.bean.WechatUserInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface HttpApi {


    @GET("oauth2/access_token")
    Observable<AccessTokenBean> getAccessToken(@Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);


    @GET("userinfo")
    Observable<WechatUserInfo> getWechatUserInfo(@Query("access_token") String access_token, @Query("openid") String openid);


    /**
     * 登录
     *
     * @return
     */
    @POST("all-walking/v1/user/login")
    Observable<BaseResponse<LoginBean>> login();


    /**
     * 绑定微信
     *
     * @param openid
     * @param user_id
     * @param nickname
     * @param head_img_url
     * @param sex
     * @param unionid
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/user/weixin")
    Observable<BaseResponse<LoginBean>> bindWechat(@Field("openid") String openid, @Field("user_id") String user_id,
                                                   @Field("nickname") String nickname, @Field("head_img_url") String head_img_url,
                                                   @Field("sex") String sex, @Field("unionid") String unionid);


    /**
     * 获取用户信息
     *
     * @param token
     * @param user_id
     * @return
     */
    @GET("all-walking/v1/user/detail/{user_id}")
    Observable<BaseResponse<UserInfo>> getUserInfo(@Header("X-Access-Token") String token, @Path("user_id") String user_id);


    /**
     * 修改昵称
     *
     * @param user_id
     * @param nickname
     * @return
     */
    @FormUrlEncoded
    @PATCH("all-walking/v1/user/nickname")
    Observable<BaseResponse<UploadNikeName>> upLoadNickName(@Field("user_id") String user_id, @Field("nickname") String nickname);


    /**
     * 发送短信验证码
     *
     * @param mobile_code    手机号
     * @param template_code  模板id
     * @param sign_name      短信签名
     * @param template_param 模板参数
     * @return
     */
    @GET("sms/v1/sms_verify")
    Observable<BaseResponse<SendSMSVerificationCode>> sendSMSCode(@Query("mobile_code") String mobile_code, @Query("template_code") String template_code, @Query("sign_name") String sign_name, @Query("template_param") String template_param);


    @FormUrlEncoded
    @PATCH("all-walking/v1/user/phone")
    Observable<BaseResponse<BindPhoneBean>> bindPhoneNum(@Field("user_id") String user_id, @Field("phone") String phone, @Field("code") String code);


    /**
     * 设置目标步数
     *
     * @param user_id
     * @param target_step
     * @return
     */
    @FormUrlEncoded
    @PATCH("all-walking/v1/user/step/target")
    Observable<BaseResponse<TargetStepBean>> setTargetStep(@Field("user_id") String user_id, @Field("target_step") int target_step);


    /**
     * 获取用户一个月内运动记录
     *
     * @param user_id
     * @return
     */
    @GET("all-walking/v1/user/step/record/month/{user_id}")
    Observable<BaseResponse<List<StepHistoryBean>>> getStepRecord(@Path("user_id") String user_id);


    /**
     * 请求签到
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     */
    @FormUrlEncoded
    @POST("all-walking/v2/signin")
    Observable<BaseResponse<SigninBean>> Signin(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id);


    /**
     * 获取用户当前步数
     *
     * @return
     */
    @GET("all-walking/v2/bubble-list/{user_id}")
    Observable<BaseResponse<UserStepInfo>> getUserStep(@Path("user_id") String user_id, @Query("step") int step);


    /**
     * 获取用户当前金币数
     *
     * @return
     */
    @GET("all-walking/v1/user/coins/{user_id}")
    Observable<BaseResponse<PointsBean>> getPoints(@Path("user_id") String user_id);


    /**
     * 金币记录
     *
     * @param user_id
     * @param page
     * @param limit
     * @return
     */
    @GET("all-walking/v1/user/income/record/{user_id}")
    Observable<BaseResponse<PointsRecord>> getPointsRecord(@Path("user_id") String user_id, @Query("page") int page, @Query("limit") int limit);


    /**
     * 提现记录
     *
     * @param user_id
     * @param page
     * @param limit
     * @return
     */
    @GET("all-walking/v1/user/withdraw/record/{user_id}")
    Observable<BaseResponse<MoneyRecordBean>> getMoneyRecord(@Path("user_id") String user_id, @Query("page") int page, @Query("limit") int limit);


    /**
     * 步数换金币
     *
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/user/step/exchange")
    Observable<BaseResponse<StepExchangeCoins>> stepExchangeCoins(@Field("user_id") String user_id);


    @GET("all-walking/v1/slide-show")
    Observable<BaseResponse<BannerBean>> getBanner(@Query("user_id") String user_id);

    /**
     * 获取连续签到数据
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @return
     */
    @GET("all-walking/v2/get_user_signin_info")
    Observable<BaseResponse<SignBean>> getSignData(@Query("user_id") String user_id, @Query("system_id") int system_id, @Query("activity_id") int activity_id);


    /**
     * 获取任务列表
     *
     * @param user_id
     * @param system_id
     * @return
     */
    @GET("all-walking/v1/get_activity_list2")
    Observable<BaseResponse<TaskEntity>> getTaskList(@Query("user_id") String user_id, @Query("system_id") int system_id);

    /**
     * 获取可提现金额列表
     *
     * @return
     */
    @GET("all-walking/v2/amount-list/{user_id}")
    Observable<BaseResponse<WithDrawalData>> getWithDrawalItems(@Path("user_id") String user_id);


    /**
     * 提现
     *
     * @param user_id
     * @param id
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v2/user/withdraw")
    Observable<BaseResponse<WithdrawBean>> TiXian(@Field("user_id") String user_id, @Field("id") int id, @Field("type") int type);


    /**
     * 完成任务后领取领取金币
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/versatile")
    Observable<BaseResponse<ActivityBean>> receiveGold(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id);


    /**
     * 签到翻倍
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v2/signin_double")
    Observable<BaseResponse<SigninDouble>> signinDouble(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") String activity_id);


    /**
     * 获取金币泡泡
     *
     * @param user_id
     * @return
     */
    @GET("all-walking/v2/bubble-list/{user_id}")
    Observable<BaseResponse<List<GoldBubble>>> getBubble(@Path("user_id") String user_id);


    /**
     * 金币泡泡点击
     *
     * @param user_id
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/bubble")
    Observable<BaseResponse<BubbleClickBean>> bubbleClick(@Field("user_id") String user_id, @Field("id") String id);

    /**
     * 金币泡泡双倍金币
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/bubble/double")
    Observable<BaseResponse<BubbleClickBean>> doubleBubbleConins(@Field("user_id") String user_id);


    /**
     * 大转盘翻倍
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/turntable_double")
    Observable<BaseResponse<BubbleClickBean>> doubleTurnTable(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id);

    /**
     * 获取配置文件
     *
     * @return
     */
    @GET("all-walking/v1/config")
    Observable<BaseResponse<ConfigEntity>> getConfig();


    /**
     * <<<<<<< HEAD
     * 转盘领取金币
     * <p>
     * =======
     * 领取金币
     * >>>>>>> 124e31c6f537b3bb9709c6968870cea95a68f179
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/everyday_points")
    Observable<BaseResponse<ActivityBean>> getZPcoins(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id, @Field("class_id") int class_id);


    /**
     * 检查版本更新
     *
     * @return
     */
    @POST("all-walking/v1/version/check")
    Observable<BaseResponse<UpdateBean>> checkVersioin();

    /**
     * 上传日志用户行为
     */
    @Headers("Content-Type: application/json")
    @POST("rcv/v1/server-projects/upload/user-behavior")
    Observable<BaseResponse<UploadNikeName>> updateBehavior(@Body JSONObject content);

    /**
     * 上传日志错误日志
     */
    @POST("rcv/v1/server-projects/upload/err-log")
    Observable<BaseResponse<UploadNikeName>> updateErrLog(@Body JSONObject content);


    /**
     * 领取阶段奖励
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/user/step/reward/phase")
    Observable<BaseResponse<RewardBean>> reward(@Field("user_id") String user_id);

    /**
     * 惊喜奖励获取在哪个tab显示
     *
     * @return
     */
    @GET("all-walking/v1/pleasantly/detail")
    Observable<BaseResponse<PleasantlyBean>> pleasantlyPage(@Query("user_id") String user_id, @Query("system_id") int system_id, @Query("activity_id") int activity_id);


    /**
     * 请求领取惊喜奖励
     *
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/pleasantly/receive")
    Observable<BaseResponse<PleasantlyReceiveBean>> receivePleasant(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id);


    @FormUrlEncoded
    @POST("all-walking/v1/pleasantly/double")
    Observable<BaseResponse<PleasantlyReceiveBean>> receiveDoublePleasant(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id);

    /**
     * 上传用户安装列表
     */
    @POST("rcv/v1/server-projects/upload/install-statistics")
    Observable<BaseResponse<UploadNikeName>> updateInstallList(@Body JSONObject content);


    /**
     * 获取分享数据
     *
     * @return
     */
    @GET("all-walking/v1/invite/info/{user_id}")
    Observable<BaseResponse<ShareContentBean>> getShareContent(@Path("user_id") String user_id);

    /**
     * 填写邀请码
     *
     * @param user_id
     * @param invite_code
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/invite/user")
    Observable<BaseResponse<UploadNikeName>> submitInviteCode(@Field("user_id") String user_id, @Field("invite_code") String invite_code);

    /**
     * 分享成功设置状态
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/invite/state")
    Observable<BaseResponse<UploadNikeName>> settingShareState(@Field("user_id") String user_id);


    /**
     * 请求步数挑战赛信息
     *
     * @return
     */
    @GET("all-walking/v1/challenge/detail")
    Observable<BaseResponse<StepRaceBean>> requestStepRace(@Query("user_id") String user_id, @Query("system_id") int system_id, @Query("activity_id") int activity_id, @Query("competition_type") String competition_type);

    /**
     * 请求报名赛事
     *
     * @param user_id
     * @param systen_id
     * @param activity_id
     * @param competition_type
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/challenge/enroll")
    Observable<BaseResponse<SignRaceSuccessBean>> requestSignRace(@Field("user_id") String user_id, @Field("system_id") int systen_id, @Field("activity_id") int activity_id, @Field("competition_type") int competition_type);

    /**
     * 请求领取 赛事结束奖励
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @param competition_type
     * @param stages_number
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/challenge/complete_receive")
    Observable<BaseResponse<StepRaceRewardBean>> requestStepRaceLingqu(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id, @Field("competition_type") int competition_type, @Field("stages_number") String stages_number);

    /**
     * 请求步数挑战赛历史记录
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @param competition_type
     * @param page
     * @param row_number
     * @return
     */
    @GET("all-walking/v1/challenge/entry_record")
    Observable<BaseResponse<StepRaceHistoryBean>> requestStepRaceHistory(@Query("user_id") String user_id, @Query("system_id") int system_id, @Query("activity_id") int activity_id, @Query("competition_type") int competition_type, @Query("page") int page, @Query("row_number") int row_number);


    /**
     * 活动页倒流banner
     *
     * @return
     */
    @GET("all-walking/v1/task_banner_list")
    Observable<BaseResponse<List<ActivityEntity>>> getActivityBanner();


    /**
     * 签到日历
     *
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v2/signin_remind")
    Observable<BaseResponse<List<SiginRemind>>> getSigninRemind(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id);


    /**
     * 特权奖励
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @return
     */
    @GET("all-walking/v1/privilegeaward/detail")
    Observable<BaseResponse<RewardEntity>> getRewareCoins(@Query("user_id") String user_id, @Query("system_id") int system_id, @Query("activity_id") int activity_id);


    /**
     * 翻6倍领取
     *
     * @param user_id
     * @return
     */
    @GET("all-walking/v1/double/six/general/{user_id}")
    Observable<BaseResponse<BubbleClickBean>> getDoubleRandom(@Path("user_id") String user_id);


    /**
     * 红包接口
     *
     * @param user_id
     * @return
     */
    @GET("all-walking/v1/red-envelope/{user_id}")
    Observable<BaseResponse<RedBean>> getRedPack(@Path("user_id") String user_id);

    /**
     * 领取新人红包
     *
     * @param user_id
     * @return
     */
    @POST("all-walking/v1/red-envelope/receive")
    @FormUrlEncoded
    Observable<BaseResponse<RedCoinsBean>> getRedCoins(@Field("user_id") String user_id);


    /**
     * 请求验证是否可提现
     *
     * @param id
     * @return
     */
    @POST("all-walking/v2/withdraw/check")
    @FormUrlEncoded
    Observable<BaseResponse<WithdrawCheckBean>> withDrawCheck(@Field("id") int id,@Field("user_id") String user_id);

    /**
     * 获取积分墙列表
     *
     * @param user_id
     * @param system_id
     * @param activity_id
     * @return
     */
    @GET("all-walking/v1/integralwall/list")
    Observable<BaseResponse<IntegralBean>> getIntegralList(@Query("user_id") String user_id, @Query("system_id") int system_id, @Query("activity_id") int activity_id);


    /**
     * 领取积分墙奖励
     *
     * @return
     */
    @POST("all-walking/v1/integralwall/receive")
    @FormUrlEncoded
    Observable<BaseResponse<IntegralWallReceiveBean>> receiveIntegralWall(@Field("user_id") String user_id, @Field("system_id") int system_id, @Field("activity_id") int activity_id, @Field("app_id") int app_id);

    /**
     * 看视频增加次数（分组看视频）
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/look-video/finish")
    Observable<BaseResponse<UploadNikeName>> lookVideo(@Field("user_id") String user_id);


    /**
     * app退出弹窗
     *
     * @param user_id
     * @return
     */
    @GET("all-walking/v1/signout/detail")
    Observable<BaseResponse<ExitDialogBean>> getExitDialogInfo(@Query("user_id") String user_id);


    /**
     * 获取收益卡状态
     *
     * @return
     */
    @GET("all-walking/v1/income-card/detail/{user_id}")
    Observable<BaseResponse<CardState>> getCardState(@Path("user_id") String user_id);


    /**
     * 领取收益卡
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/income-card/receive_card")
    Observable<BaseResponse<CardBean>> incomeCard(@Field("user_id") String user_id);

    /**
     * 获取收入记录
     *
     * @param user_id
     * @param page
     * @param limit
     * @return
     */
    @GET("all-walking/v2/user/income/record/{user_id}")
    Observable<BaseResponse<WalletBean>> getWalletData(@Path("user_id") String user_id, @Query("page") int page, @Query("limit") int limit);

    /**
     * 活动页详情
     *
     * @return
     */
    @GET("all-walking/v1/activity/detail/{user_id}")
    Observable<BaseResponse<AggregationBean>> getActivityDate(@Path("user_id") String user_id);


    /**
     * 获取每日福利列表
     *
     * @return
     */
    @GET("all-walking/v1/activity/welfare/{user_id}")
    Observable<BaseResponse<List<TodayWelfareBean>>> getTodayWelfare(@Path("user_id") String user_id);

    /**
     * 获取每日任务列表
     *
     * @return
     */
    @GET("all-walking/v1/activity/task/{user_id}")
    Observable<BaseResponse<List<TodayTaskBean>>> getTodayTask(@Path("user_id") String user_id);


    /**
     * 每日任务领取
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/activity/task/receive")
    Observable<BaseResponse<DialogCoinsBean>> todayTaskReceive(@Field("user_id") String user_id, @Field("id") int id);


    /**
     * 每日福利领取
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/activity/welfare/receive")
    Observable<BaseResponse<WelfareReceiveBean>> todaywelfareReceive(@Field("user_id") String user_id, @Field("id") int id);

    /**
     * 获取我的卡券列表
     *
     * @param user_id
     * @return
     */
    @GET("all-walking/v1/user/card/{user_id}")
    Observable<BaseResponse<List<MyCardBean>>> getMyCardList(@Path("user_id") String user_id);

    /**
     * 集数字详情
     *
     * @param user_id
     * @return
     */
    @GET("all-walking/v1/collect_number/detail")
    Observable<BaseResponse<CollectNumBean>> collectNumberDetail(@Query("user_id") String user_id);

    /**
     * 体验活动列表
     *
     * @param user_id
     * @return
     */
    @GET("all-walking//v1/collect_number/experience_activity_list")
    Observable<BaseResponse<ExperienceActivityBean>> getExperienceActivity(@Query("user_id") String user_id);

    /**
     * 体验活动领取数字
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/collect_number/experience_receive_number")
    Observable<BaseResponse<NemberBean>> getNumber(@Field("user_id") String user_id, @Field("class_id") int class_id,@Field("activity_id") int activity_id);

    /**
     * 金蛋列表
     *
     * @return
     */
    @GET("all-walking/v1/egg/list/{user_id}")
    Observable<BaseResponse<EggBean>> getEggInfo(@Path("user_id") String user_id);

    /**
     * 砸蛋
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/egg/break")
    Observable<BaseResponse<DialogCoinsBean>> breadEgg(@Field("user_id") String user_id, @Field("id") int id);

    /**
     * 领取锤子
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/collect_number/receive_hammer")
    Observable<BaseResponse<UploadNikeName>> receiveHammer(@Field("user_id") String user_id);


    /**
     * 热门播报
     *
     * @return
     */
    @GET("all-walking/v1/collect_number/hot_broadcast")
    Observable<BaseResponse<HotNoticeBean>> getHotNotice();

    /**
     * 集数字完成领取奖励
     *
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/collect_number/receive_number_points")
    Observable<BaseResponse<DialogCoinsBean>> receiveNumberPoints(@Field("user_id") String user_id);

    /**
     * 领取收益加速
     * @param user_id
     * @return
     */
    @FormUrlEncoded
    @POST("all-walking/v1/income-card/receive_points")
    Observable<BaseResponse<ReceiveeErningsBean>> receiveErnings(@Field("user_id") String user_id);


    /**
     * 获取奖励详情
     *
     * @return
     */
    @GET("all-walking/v2/invite/detail/{user_id}")
    Observable<BaseResponse<InviteDetail>> inviteDetail(@Path("user_id") String user_id);

    /**
     * 获取邀请记录
     *
     * @return
     */
    @GET("all-walking/v2/invite/record")
    Observable<BaseResponse<InviteRecord>> inviteRecord(@Query("user_id") String user_id, @Query("page") int page, @Query("limit") int limit);


    @GET("all-walking/v1/clean/list")
    Observable<BaseResponse<CleanListBean>> getCleanList();

    @FormUrlEncoded
    @POST("all-walking/v1/clean/reward")
    Observable<BaseResponse<CleanRewardBean>> postCleanReward(@Field("id") int id);


    @GET("all-walking/v1/clean-auth/list")
    Observable<BaseResponse<CleanAuthOutBean>> getCleanAuthList(@Query("user_id") String id);

    @FormUrlEncoded
    @POST("all-walking/v1/clean-auth/open")
    Observable<BaseResponse<CleanAuthOpenBean>> postCleanAuthOpen(@Field("user_id") String userId, @Field("id") int id);

}
