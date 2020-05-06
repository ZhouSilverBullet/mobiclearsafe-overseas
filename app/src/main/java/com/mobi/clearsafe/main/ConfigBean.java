package com.mobi.clearsafe.main;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/11
 * desc: 获取的配置表
 */
public class ConfigBean implements Serializable {

    private String pact;//服务协议
    private String privacy_policy;//隐私协议url
    private String strategy;//赚钱攻略
    private String dev_domain;//测试域名
    private String prd_domain;//正式域名
    private String qqqun;//QQ群号码
    private String qqqunkey;//QQ群Key
    private String ad_request;//请求广告配置的地址
    private String errlog_upload_url;//错误日志上传url
    private String adlog_upload_url;//广告日志上传url
    private String user_behavior_upload_url;//用户行为上传url
    private boolean is_upload_log;//是否需要上传日志
    private long count_down_time;
    private String install_statistics_upload_url;//安装统计日志上传url
    private boolean show_more_activity;//今日任务列表期待更多任务栏是否显示
    private String sign_icon_url;//签到页面图标url  暂时不用
    private String sign_icon_judge_url;//签到图标跳转url
    private int sign_icon_judge_type;//签到图标跳转类型1-原生2-h5
    private String sign_icon_judge_params;//签到图标跳转参数
    private String move_avtivity_url;
    private String step_challenge_rules_url;//步数挑战赛规则
    private String scratch_card_url;//刮刮卡地址
    private String sign_icon_judge_url_android;//签到页面图标url
    private int hc1g = 30;//落地页概率
    private String android_version;//android版本号
    private boolean is_ad;
    private boolean mi_test;

    private boolean is_Install_list;//是否上传用户安装列表


    public boolean isIs_Install_list() {
        return is_Install_list;
    }

    public void setIs_Install_list(boolean is_Install_list) {
        this.is_Install_list = is_Install_list;
    }

    public boolean isMi_test() {
        return mi_test;
    }

    public void setMi_test(boolean mi_test) {
        this.mi_test = mi_test;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public boolean isIs_ad() {
        return is_ad;
    }

    public void setIs_ad(boolean is_ad) {
        this.is_ad = is_ad;
    }

    public String getPrivacy_policy() {
        return privacy_policy;
    }

    public void setPrivacy_policy(String privacy_policy) {
        this.privacy_policy = privacy_policy;
    }

    public String getPact() {
        return pact;
    }

    public void setPact(String pact) {
        this.pact = pact;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getDev_domain() {
        return dev_domain;
    }

    public void setDev_domain(String dev_domain) {
        this.dev_domain = dev_domain;
    }

    public String getPrd_domain() {
        return prd_domain;
    }

    public void setPrd_domain(String prd_domain) {
        this.prd_domain = prd_domain;
    }

    public String getQqqun() {
        return qqqun;
    }

    public void setQqqun(String qqqun) {
        this.qqqun = qqqun;
    }

    public String getQqqunkey() {
        return qqqunkey;
    }

    public void setQqqunkey(String qqqunkey) {
        this.qqqunkey = qqqunkey;
    }

    public String getAd_request() {
        return ad_request;
    }

    public void setAd_request(String ad_request) {
        this.ad_request = ad_request;
    }

    public String getErrlog_upload_url() {
        return errlog_upload_url;
    }

    public void setErrlog_upload_url(String errlog_upload_url) {
        this.errlog_upload_url = errlog_upload_url;
    }

    public String getAdlog_upload_url() {
        return adlog_upload_url;
    }

    public void setAdlog_upload_url(String adlog_upload_url) {
        this.adlog_upload_url = adlog_upload_url;
    }

    public String getUser_behavior_upload_url() {
        return user_behavior_upload_url;
    }

    public void setUser_behavior_upload_url(String user_behavior_upload_url) {
        this.user_behavior_upload_url = user_behavior_upload_url;
    }

    public boolean isIs_upload_log() {
        return is_upload_log;
    }

    public void setIs_upload_log(boolean is_upload_log) {
        this.is_upload_log = is_upload_log;
    }

    public long getCount_down_time() {
        return count_down_time;
    }

    public void setCount_down_time(long count_down_time) {
        this.count_down_time = count_down_time;
    }

    public String getInstall_statistics_upload_url() {
        return install_statistics_upload_url;
    }

    public void setInstall_statistics_upload_url(String install_statistics_upload_url) {
        this.install_statistics_upload_url = install_statistics_upload_url;
    }

    public boolean isShow_more_activity() {
        return show_more_activity;
    }

    public void setShow_more_activity(boolean show_more_activity) {
        this.show_more_activity = show_more_activity;
    }

    public String getSign_icon_url() {
        return sign_icon_url;
    }

    public void setSign_icon_url(String sign_icon_url) {
        this.sign_icon_url = sign_icon_url;
    }

    public String getSign_icon_judge_url() {
        return sign_icon_judge_url;
    }

    public void setSign_icon_judge_url(String sign_icon_judge_url) {
        this.sign_icon_judge_url = sign_icon_judge_url;
    }

    public int getSign_icon_judge_type() {
        return sign_icon_judge_type;
    }

    public void setSign_icon_judge_type(int sign_icon_judge_type) {
        this.sign_icon_judge_type = sign_icon_judge_type;
    }

    public String getSign_icon_judge_params() {
        return sign_icon_judge_params;
    }

    public void setSign_icon_judge_params(String sign_icon_judge_params) {
        this.sign_icon_judge_params = sign_icon_judge_params;
    }

    public String getMove_avtivity_url() {
        return move_avtivity_url;
    }

    public void setMove_avtivity_url(String move_avtivity_url) {
        this.move_avtivity_url = move_avtivity_url;
    }

    public String getStep_challenge_rules_url() {
        return step_challenge_rules_url;
    }

    public void setStep_challenge_rules_url(String step_challenge_rules_url) {
        this.step_challenge_rules_url = step_challenge_rules_url;
    }

    public String getScratch_card_url() {
        return scratch_card_url;
    }

    public void setScratch_card_url(String scratch_card_url) {
        this.scratch_card_url = scratch_card_url;
    }

    public String getSign_icon_judge_url_android() {
        return sign_icon_judge_url_android;
    }

    public void setSign_icon_judge_url_android(String sign_icon_judge_url_android) {
        this.sign_icon_judge_url_android = sign_icon_judge_url_android;
    }

    public int getHc1g() {
        return hc1g;
    }

    public void setHc1g(int hc1g) {
        this.hc1g = hc1g;
    }
}
