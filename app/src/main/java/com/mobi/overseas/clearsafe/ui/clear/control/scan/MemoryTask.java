package com.mobi.overseas.clearsafe.ui.clear.control.scan;

import android.os.AsyncTask;

import com.mobi.overseas.clearsafe.ui.clear.manager.MemoryManager;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/7 15:51
 * @Dec 略
 */
public class MemoryTask extends AsyncTask<Void, Void, Void> {
    private IMemoryCallback callback;

    public MemoryTask(IMemoryCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        long totalMemory = MemoryManager.getInstance().getTotalMemory();
        long avaliableMemory = MemoryManager.getInstance().getAvaliableMemory();
        long usedMemory = totalMemory - avaliableMemory;
        long percent = usedMemory * 100 / totalMemory;
        if (callback != null) {
            callback.onPercent(percent);
        }
        return null;
    }

    public interface IMemoryCallback {
        void onPercent(long percent);
    }
}
