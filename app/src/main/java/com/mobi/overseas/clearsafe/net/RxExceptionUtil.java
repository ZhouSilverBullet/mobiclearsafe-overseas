package com.mobi.overseas.clearsafe.net;

import android.net.ParseException;

import com.mobi.overseas.clearsafe.statistical.errorlog.ErrorLogUtil;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class RxExceptionUtil {

    public static String exceptionHandler(Throwable e){
        String errorMsg = "未知错误";
        if (e instanceof UnknownHostException) {
            errorMsg = "网络不可用";
            ErrorLogUtil.upDateService(errorMsg);
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "请求网络超时";
            ErrorLogUtil.upDateService(errorMsg);
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);
        } else if (e instanceof ParseException || e instanceof JSONException
                || e instanceof JSONException) {
            errorMsg = "数据解析错误";
            ErrorLogUtil.upDateService(errorMsg);
        }
        return errorMsg;
    }

    private static String convertStatusCode(HttpException httpException) {
        ErrorLogUtil.upDateService(String.valueOf(httpException.code()));
        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "服务器处理请求出错";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg = "服务器无法处理请求";
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
