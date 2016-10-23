package cn.demoz.www;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import cn.demoz.www.tools.FileUtils;
import cn.demoz.www.tools.ResourceUtil;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private Context context;
    public ApplicationTest() {
        super(Application.class);
        Log.i("jason", "ApplicationTest");
    }



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();
        Log.i("jason", "setUp");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.i("jason", "tearDown");
    }





// ------------------ test method -----------------------//

    public void testFileSDAvailable(){
        Log.e("jason test", FileUtils.getDiskCacheDir(getContext(), "cde.txt").toString());
    }

    public void testParseResIdToUri(){
        String uri = ResourceUtil.parseResIdToUri(context, R.id.about_layout).toString();
        Log.e("jason", "Uri : " + uri);
    }

//    TODO 需要学习更复杂的测试功能以及安卓自动化测试框架，如Robotium
//    public void testFollowLink() {
//        final Instrumentation inst = getInstrumentation();
//        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_VIEW);
//        intentFilter.addDataScheme("http");
//        intentFilter.addCategory(Intent.CATEGORY_BROWSABLE);
//        Instrumentation.ActivityMonitor monitor = inst.addMonitor(
//                intentFilter, null, false);
//        assertEquals(0, monitor.getHits());
//        TouchUtils.clickView(this, mLink);
//        monitor.waitForActivityWithTimeout(5000);
//        assertEquals(1, monitor.getHits()); inst.removeMonitor(monitor);
//    }

}