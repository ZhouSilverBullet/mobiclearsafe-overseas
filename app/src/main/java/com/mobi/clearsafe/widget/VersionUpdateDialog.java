package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.LogUtils;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.utils.UiUtils;

import java.io.File;

/**
 * author:zhaijinlu
 * date: 2019/11/12
 * desc:版本更新
 * */
public class VersionUpdateDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;

    private TextView version_name;
    private LinearLayout layout_content;
    private RelativeLayout layout_update_force;
    private TextView once_update_force;//立即登录
    private LinearLayout layout_update;
    private TextView no_update;
    private TextView once_update;
    private LinearLayout layout_progress;
    private ProgressBar progress_bar;
    private TextView tv_progress;

    private boolean isForce;//是否强更
    private String versionName;//版本信息
    private String versionContent;//更新内容
    private String downLoadUrl;//下载链接


    public static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
    public static String FILENAME = "mobi.apk";
    private File fileProgess;

    public VersionUpdateDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.isForce=builder.isForce;
        this.versionName=builder.versionName;
        this.versionContent=builder.versionContent;
        this.downLoadUrl=builder.downLoadUrl;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_version_layout);
        initView();
        initWindow();
    }

    private void initView() {
        version_name=findViewById(R.id.version_name);
        layout_content=findViewById(R.id.content_layout);
        layout_update_force=findViewById(R.id.layout_update_force);
        once_update_force=findViewById(R.id.once_update_force);
        layout_update=findViewById(R.id.layout_update);
        no_update=findViewById(R.id.no_update);
        once_update=findViewById(R.id.once_update);
        layout_progress=findViewById(R.id.layout_progress);
        progress_bar=findViewById(R.id.progress_bar);
        tv_progress=findViewById(R.id.tv_progress);

        if(!TextUtils.isEmpty(versionName)){
            version_name.setText(mContext.getString(R.string.find_new,versionName));
        }
        if(!TextUtils.isEmpty(versionContent)){
            String[] description = AppUtil.splitPTag(versionContent);
            for (int i = 0; i < description.length; i++) {
                buildTextView(layout_content,description[i]);
            }
        }

        if(isForce){//强更
            layout_update.setVisibility(View.GONE);
            layout_update_force.setVisibility(View.VISIBLE);
            layout_update_force.setOnClickListener(this);
        }else {
            layout_update.setVisibility(View.VISIBLE);
            layout_update_force.setVisibility(View.GONE);
            once_update.setOnClickListener(this);
            no_update.setOnClickListener(this);
        }




    }

    private void buildTextView(LinearLayout versionUpdateView,String textStr) {
        TextView tv = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, UiUtils.dp2px(mContext,8), 0, 0);
        tv.setLayoutParams(layoutParams);
        tv.setText(textStr);
        tv.setTextColor(mContext.getResources().getColor(R.color.gray_7e9c));
        tv.setTextSize(16);
        versionUpdateView.addView(tv);
    }
    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lps = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        int h = dm.widthPixels > dm.heightPixels ? dm.widthPixels
                : dm.heightPixels;
        lps.width = w;
//        lps.height = h;
        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_update_force:
                layout_update_force.setVisibility(View.GONE);
                layout_update.setVisibility(View.GONE);
                layout_progress.setVisibility(View.VISIBLE);
                downLoad();
                break;
            case R.id.once_update:
                layout_update_force.setVisibility(View.GONE);
                layout_update.setVisibility(View.GONE);
                layout_progress.setVisibility(View.VISIBLE);
                downLoad();
                break;
            case R.id.no_update:
                dismiss();
                break;
        }
    }

    private void downLoad() {
        fileProgess = new File(SDPATH);
        if (!fileProgess.exists()) {
            fileProgess.mkdirs();
        }
            new Thread(){
                @Override
                public void run() {
                    PRDownloader.download(downLoadUrl,  fileProgess + "/" + FILENAME, FILENAME)
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {

                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {

                                }
                            })
                            .setOnCancelListener(new com.downloader.OnCancelListener() {
                                @Override
                                public void onCancel() {

                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {
                                    long totalBytes = progress.totalBytes;
                                    long currentBytes = progress.currentBytes;
                                    long progressNow=(int)currentBytes * 100 / totalBytes;
                                    tv_progress.setText(mContext.getString(R.string.updating)+"("+progressNow+"%)");
                                    progress_bar.setProgress((int) progressNow);

                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                                    File file=new File(fileProgess + "/" + FILENAME+"/"+FILENAME);
                                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                    installIntent.setDataAndType(getUriForFile(mContext,file), "application/vnd.android.package-archive");
                                    mContext.startActivity(installIntent);
                                }

                                @Override
                                public void onError(Error error) {
                                    LogUtils.e("下载失败");
                                }
                            });
                }
            }.start();

    }





    //解决Android 7.0之后的Uri安全问题
    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider",file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }


    public static class Builder {
        private Context mContext;
        private boolean isForce;//是否强更
        private String versionName;//版本信息
        private String versionContent;//更新内容
        private String downLoadUrl;//下载链接

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setIsForce(boolean isForce) {
            this.isForce = isForce;
            return this;
        }

        public Builder setDownLoadUrl(String downLoadUrl) {
            this.downLoadUrl = downLoadUrl;
            return this;
        }

        public Builder setVersionName(String versionName) {
            this.versionName = versionName;
            return this;
        }

        public Builder setVersionContent(String versionContent) {
            this.versionContent = versionContent;
            return this;
        }


        public VersionUpdateDialog build() {
            return new VersionUpdateDialog(this);
        }
    }

}
