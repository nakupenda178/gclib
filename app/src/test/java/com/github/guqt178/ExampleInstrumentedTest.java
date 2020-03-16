package com.github.guqt178;

import android.content.Context;

import com.sample.test.MainActivity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 23,
        buildDir = "app/build")
public class ExampleInstrumentedTest {
 /* @Test
  public void useAppContext() {
    // Context of the app under test.
    //Context appContext = InstrumentationRegistry.getTargetContext();

    //assertEquals("com.moerlong.lender", appContext.getPackageName());
  }*/

    @Test
    public void testActivity() throws Exception {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        Assert.assertNotNull(mainActivity);
    }
}
