package com.mysdk.common;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by cwl on 2018/5/28.
 */

public class    ActivityManager {
    private ActivityManager() {
    }

    private static ActivityManager instance;

    public static synchronized ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    //提供一个Activity栈
    private  static Stack<Activity> activityStack = new Stack<>();

    //入栈
    public void add(Activity activity) {
        if (activity != null) {
            activityStack.push(activity);
        }
    }

    //移除指定activity
    public void remove(Activity activity) {
        if (activity != null) {
            for (int i = activityStack.size() - 1; i > 0; i--) {
                if (activity.getClass().equals(activityStack.get(i).getClass())) {
                    activity.finish();
                    activityStack.remove(activity);
                }
            }
        }
    }

    public void removeCurrent() {
        if (activityStack.size() > 0) {
            activityStack.lastElement().finish();
            activityStack.remove(activityStack.lastElement());
        }
    }

    //移除所有activity
    public void removeall() {
        if (activityStack.size() > 0) {
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                activityStack.get(i).finish();
                activityStack.remove(i);
            }
        }
    }
    //返回栈的大小
    public int getSize(){
        return activityStack.size();
    }
}
