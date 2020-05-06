package com.mobi.overseas.clearsafe.utils;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * author:zhaijinlu
 * date: 2019/12/5
 * desc:
 */
public class DialogUtils {

    private DialogUtils() {
    }

    public static void showDialog(FragmentManager fm, DialogFragment dialog, String dialogTag) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(dialogTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.add(dialog, dialogTag);
        ft.commitAllowingStateLoss();
    }

    public static void dismissDialog(FragmentManager fm, String dialogTag) {
        if(fm==null){
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(dialogTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.commitAllowingStateLoss();
    }
}
