package cn.ifreedomer.com.softmanager;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.util.FileUtil;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("cn.ifreedomer.com.softmanager", appContext.getPackageName());
    }

    @Test
    public void testScanSdcard() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<FileInfo> bigFiles = FileUtil.getBigFiles();
                Log.e("ExampleInstrumentedTest", "size= " + bigFiles.size());
                for (FileInfo fileInfo : bigFiles) {
                    Log.e("ExampleInstrumentedTest", fileInfo.toString());
                }

            }
        }).start();
    }
}
