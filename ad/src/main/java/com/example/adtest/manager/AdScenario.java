package com.example.adtest.manager;

/**
 * author : liangning
 * date : 2019-11-16  13:41
 */
public class AdScenario {
//
//    gold_bubble_native,//金币泡泡信息流
//    gold_bubble_double_video,//金币泡泡翻倍激励视频
//    step_togold_native,//步数换金币信息流
//    sign_native,//签到信息流
//    sign_double_video,//签到翻倍激励视频
//    task_video,//观看激励视频任务 激励视频
//    task_video_native,//观看激励视频 信息流
//    turn_table_video,//大转盘激励视频
//    turn_table_gold_native,//大转盘金币奖励信息流
//    turn_table_double_gold_native,//大转盘翻倍后信息流
//    turn_table_gold_double_video,//大转盘金币奖励翻倍 激励视频
//    turn_table_pkg_gold_natice,//大转盘底部宝箱 信息流
//    bind_wx_native,//绑定微信信息流
//    bind_phone_native,//绑定手机信息流
//    me_page_native,//我的页面信息流
//    setting_page_native,//设置页面信息流
//    default_native,//默认信息流
//    withdrawal_page_video,//提现页激励视频
//    default_video,//默认激励视频

    public static String getSelfPosId(ScenarioEnum scenario) {
        if (scenario == null) {
            return "";
        }
        String posId = "";
        switch (scenario) {
            case gold_bubble_native:
                posId = "1028001";
                break;
            case gold_bubble_double_video:
                posId = "1028002";
                break;
            case step_togold_native:
                posId = "1028003";
                break;
            case sign_native:
                posId = "1028004";
                break;
            case sign_double_video:
                posId = "1028005";
                break;
            case task_video:
                posId = "1028006";
                break;
            case task_video_native:
                posId = "1028007";
                break;
            case turn_table_video:
                posId = "1028008";
                break;
            case turn_table_gold_native:
                posId = "1028009";
                break;
            case turn_table_double_gold_native:
                posId = "1028010";
                break;
            case turn_table_gold_double_video:
                posId = "1028011";
                break;
            case turn_table_pkg_gold_natice:
                posId = "1028012";
                break;
            case bind_wx_native:
                posId = "1028013";
                break;
            case bind_phone_native:
                posId = "1028014";
                break;
            case turn_table_first_task_native:
                posId = "1028015";
                break;
            case me_page_native:
                posId = "1028016";
                break;
            case setting_page_native:
                posId = "1028017";
                break;
            case default_native:
                posId = "1028018";
                break;
            case withdrawal_page_video:
                posId = "1028019";
                break;
            case default_video:
                posId = "1028020";
                break;
            case gold_bubble_double_native:
                posId = "1028021";
                break;
            case sign_double_native:
                posId = "1028022";
                break;
            case step_ceiling_native:
                posId = "1028023";
                break;
            case userinfo_native:
                posId = "1028024";
                break;
            case splash_ad:
                posId = "1028025";
                break;
            case turn_table_jibei_video:
                posId = "1028026";
                break;
            case fly_knife_video:
                posId = "1028027";
                break;
            case fly_knife_native:
                posId = "1028028";
                break;
            case step_history_banner:
                posId = "1028029";
                break;
            case step_shangxian_video:
                posId = "1028030";
                break;
            case pleasantly_double_video:
                posId = "1028031";
                break;
            case pleasantly_native:
                posId = "1028032";
                break;
            case bubble_video_native:
                posId = "1028033";
                break;
            case stage_of_reward_video:
                posId = "1028034";
                break;
            case stage_of_reward_native:
                posId = "1028035";
                break;
            case task_reward_native:
                posId = "1028036";
                break;
            case step_challenge_video:
                posId = "1028037";
                break;
            case dongdong_bubble_native:
                posId = "1028038";
                break;
            case dongdong_bubble_video:
                posId = "1028039";
                break;
            case drink_water_video:
                posId = "1028040";
                break;
            case drink_water_native:
                posId = "1028041";
                break;
            case game_plaque:
                posId = "1028042";
                break;
            case default_plaque:
                posId = "1028043";
                break;
            case reward_video:
                posId = "1028044";
                break;
            case low_plaque:
                posId = "1028045";
                break;
            case move_water_plaque:
                posId = "1028046";
                break;
            case scratch_cards_plaque:
                posId = "1028048";
                break;
            case scratch_cards_native:
                posId = "1028049";
                break;
            case scratch_cards_video:
                posId = "1028050";
                break;
            case click_egg_native:
                posId = "1028052";
                break;
            case click_egg_video:
                posId = "1028053";
                break;
            case gambling_video:
                posId = "1028056";
                break;
            case gambling_native:
                posId = "1028057";
                break;
            case steprace_native:
                posId = "1028028";
                break;
            case garbage_native:
                posId = "1028100";
                break;
            case garbage_result1_native:
                posId = "1028101";
                break;
            case garbage_result2_native:
                posId = "1028102";
                break;
            case garbage_result3_native:
                posId = "1028103";
                break;
            case garbage_result4_native:
                posId = "1028104";
                break;
            case task_native1:
                posId = "1028106";
                break;
            case task_native2:
                posId = "1028107";
                break;
            case task_native3:
                posId = "1028108";
                break;
            case task_permission:
                posId = "1028109";
                break;
            case garbage_full:
                posId = "1028105";
                break;
            case garbage_splash:
                posId = "1028110";
                break;
            case garbage_inter:
                posId = "1028111";
                break;
            case garbage_other_full:
                posId = "1028112";
                break;
            case garbage_other_splash:
                posId = "1028113";
                break;
            case garbage_other_inter:
                posId = "1028114";
                break;
            case garbage_clear_reward_ad:
                posId = "1028115";
                break;
        }
        return posId;
    }

}
