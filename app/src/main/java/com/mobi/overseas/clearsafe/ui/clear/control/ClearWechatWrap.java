//package com.mobi.overseas.clearsafe.ui.clear.control;
//
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.widget.CompoundButton;
//import android.widget.TextView;
//
//import com.mobi.overseas.clearsafe.main.fragment.HomeFragment;
//import com.mobi.overseas.clearsafe.ui.clear.data.WechatBean;
//import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
//import com.mobi.overseas.clearsafe.ui.clear.util.WechatClearUtil;
//import com.mobi.overseas.clearsafe.ui.clear.widget.ClearItemView;
//
//import java.io.File;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class ClearWechatWrap {
//
//    public static final String TAG = "ClearWechatWrap";
//
//    private AtomicInteger currentTime = new AtomicInteger(0);
//
//    private AtomicLong allSize = new AtomicLong(0);
//
//
//    /**
//     * 按钮触发清理
//     *
//     * @param callback
//     * @param civ
//     */
//    public void btnClear(IClearCallback callback, ClearItemView... civ) {
//        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
//            for (ClearItemView clearItemView : civ) {
//                boolean check = clearItemView.isCheck();
//                if (check) {
//                    for (File file : clearItemView.getFile()) {
//                        FileUtil.deleteFolderFile(file.getPath(), false);
//                    }
//                }
//                clearItemView.release();
//            }
//
//            if (callback != null) {
//                callback.onFinish();
//            }
//        });
//    }
//
//    /**
//     * 找微信对应的清理的位置
//     *
//     * @param handler
//     * @param civ
//     */
//    public void findWechatClear(Handler handler, TextView tvNum, TextView tvUnit, ClearItemView... civ) {
//        if (tvNum != null) {
//            handlerClearItemViewChecked(civ);
//        }
//
//        WechatClearUtil.findWxAdCache(new WechatClearUtil.IAdCacheListener() {
//            @Override
//            public void onFindLoad(WechatBean bean) {
//
//                allSize.set(allSize.get() + bean.fileSize);
//                if (tvNum == null || civ == null) {
//                    return;
//                }
//                handler.post(() -> {
//                    civ[0].setData(bean);
//
//                    handleTextNum(tvNum, tvUnit, civ);
//
//                });
//            }
//
//            @Override
//            public void onFinish() {
//                if (tvNum != null) {
//                    handleCivFinish(civ[0]);
//                }
//                addCurrent(handler);
//            }
//        });
//
//        //获取微信朋友圈文件缓存
//        WechatClearUtil.findWxFriendCache(new WechatClearUtil.IAdCacheListener() {
//            @Override
//            public void onFindLoad(WechatBean bean) {
//                allSize.set(allSize.get() + bean.fileSize);
//
//                if (tvNum == null || civ == null) {
//                    return;
//                }
//
//
//                handler.post(() -> {
//                    if (civ.length > 1) {
//                        civ[1].setData(bean);
//
//                        handleTextNum(tvNum, tvUnit, civ);
//                    }
//                });
//            }
//
//            @Override
//            public void onFinish() {
//                if (tvNum != null) {
//                    handleCivFinish(civ[1]);
//                }
//                addCurrent(handler);
//            }
//        });
//        //获取微信垃圾
//        WechatClearUtil.findWxCache(new WechatClearUtil.IAdCacheListener() {
//            @Override
//            public void onFindLoad(WechatBean bean) {
//                if (tvNum == null || civ == null) {
//                    return;
//                }
//                allSize.set(allSize.get() + bean.fileSize);
//
//                handler.post(() -> {
//                    if (civ.length > 2) {
//                        civ[2].setData(bean);
//
//                        handleTextNum(tvNum, tvUnit, civ);
//                    }
//                });
//            }
//
//            @Override
//            public void onFinish() {
//                if (tvNum != null) {
//                    handleCivFinish(civ[2]);
//                }
//                addCurrent(handler);
//            }
//        });
//
//    }
//
//    private void handleCivFinish(ClearItemView clearItemView) {
//        clearItemView.post(new Runnable() {
//            @Override
//            public void run() {
//                WechatBean wechatBean = new WechatBean();
//                wechatBean.fileSize = 0;
//                clearItemView.setData(wechatBean);
//            }
//        });
//    }
//
//    private void handlerClearItemViewChecked(ClearItemView[] civ) {
//        for (ClearItemView clearItemView : civ) {
//            clearItemView.getCbMemory().setOnCheckedChangeListener((buttonView, isChecked) -> {
//                if (isChecked) {
//                    long size = clearItemView.getSize();
//                    allSize.set(allSize.get() + size);
//                } else {
//                    long size = clearItemView.getSize();
//                    allSize.set(allSize.get() - size);
//                }
//
//                if (onCheckedListener != null) {
//                    onCheckedListener.onCheckedChange(allSize.get());
//                }
//            });
//        }
//    }
//
//    private void handleTextNum(TextView tvNum, TextView tvUnit, ClearItemView... civ) {
//        if (tvNum == null || tvUnit == null || civ == null) {
//            return;
//        }
//        long size = 0L;
//        for (ClearItemView clearItemView : civ) {
//            size += clearItemView.getSize();
//        }
//        String[] fileSize0 = FileUtil.getFileSize0(size);
//        tvNum.setText(fileSize0[0]);
//        tvUnit.setText(fileSize0[1]);
//    }
//
//    public void addCurrent(Handler handler) {
//        currentTime.incrementAndGet();
//        Log.e(TAG, "currentTime : " + currentTime.get());
//        if (currentTime.get() == 3) {
//            if (handler == null) {
//                return;
//            }
//            handler.sendEmptyMessageDelayed(1001, 2000);
//
//            Message message = Message.obtain();
//            message.what = HomeFragment.H_WX_CACHE;
//            message.arg1 = getIntMermory(allSize.get());
//            message.obj = FileUtil.getFileSize0(allSize.get());
//            handler.sendMessage(message);
//        }
//    }
//
//    private int getIntMermory(long l) {
//        if (l >= Integer.MAX_VALUE) {
//            return Integer.MAX_VALUE;
//        }
//
//        return (int) l;
//    }
//
//    public interface IClearCallback {
//        void onFinish();
//    }
//
//    private OnCheckedListener onCheckedListener;
//
//    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
//        this.onCheckedListener = onCheckedListener;
//    }
//}
