package cn.demoz.www;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import cn.demoz.www.tools.FileUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testFileSDAvailable(){
        Log.e("jason test", FileUtils.getDiskCacheDir(getContext(), "cde.txt").toString());
    }
}