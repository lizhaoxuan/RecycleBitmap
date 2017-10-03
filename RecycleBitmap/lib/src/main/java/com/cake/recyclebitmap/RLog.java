package com.cake.recyclebitmap;

import android.util.Log;

/**
 * Created by lizhaoxuan on 2017/7/14.
 */

public class RLog {

    private static final String TAG = "RecycleBitmap";

    private static final int LEVEL_CODING = 0;
    public static final int LEVEL_DEBUG = 100;
    public static final int LEVEL_PRODUCTION = 101;

    private static int logLevel = BuildConfig.DEBUG ? LEVEL_DEBUG : LEVEL_PRODUCTION;

    private static TestHelper testHelper;

    public static void setLogLevel(int level) {
        logLevel = level;
    }

    public static void setTestHelper(TestHelper helper) {
        testHelper = helper;
    }

    public static void d(String msg) {
        if (logLevel == LEVEL_CODING) {
            print(msg);
        }
    }

    public static void i(String msg) {
        if (logLevel != LEVEL_PRODUCTION) {
            print(msg);
        }
    }

    public static void print(String msg) {
        if (testHelper != null) {
            testHelper.test(msg);
        }
        Log.i(TAG, msg);
    }

    public static class TestHelper {
        public void test(String msg) {
        }
    }
}
