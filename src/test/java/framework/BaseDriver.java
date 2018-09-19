package framework;

import framework.config.DriverFactory;
import framework.listeners.ScreenshotListener;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dinko
 * Created at 18/09/2018
 */

/**
 * This is the base test class containing all actions that need to be done prior
 * to actually running a test. Each test class should EXTEND this class in order
 * to function properly.
 *
 * BaseDriver class handles creating new instance of RemoteWebDriver by using
 * DriverFactory class methods, by populating the webDriverThreadPool list object.
 *
 * When a test method is run, it should call the driver object from the list by using
 * getDriver() method.
 *
 * After each test method, clearCookies() method will attempt to
 * clear all cookies from the driver instance, making it reusable for the next test.
 *
 * Finally, closeDriverObjects() iterates through the webDriverThreadPool list object
 * and removes all found instances of driverThread. This means that regardless of the
 * number of tests executed, ending test run will kill all instances of selected
 * browser.
 */

// include listener that captures screenshot the moment any test fails
@Listeners(ScreenshotListener.class)

public class BaseDriver {

    // create a list variable webDriverThreadPool that will hold all driver instances
    private static List<DriverFactory> webDriverThreadPool = Collections.synchronizedList(new ArrayList<>());

    // create a local variable for driver threads (not affected by any other threads)
    private static ThreadLocal<DriverFactory> driverThread;

    // instantiate new WebDriver defined in DriverFactory
    // due to BeforeSuite annotation, this method will run before the test suite is initialized
    @BeforeSuite(alwaysRun = true)
    public static void instantiateDriverObject() {

        // instantiate new driverThread object
        driverThread = new ThreadLocal<DriverFactory>() {

            @Override
            protected DriverFactory initialValue() {

                DriverFactory webDriverThread = new DriverFactory();
                webDriverThreadPool.add(webDriverThread);
                return webDriverThread;
            }
        };
    }

    // create or return instance of WebDriver, depending if it exists (defined in DriverFactory)
    public static RemoteWebDriver getDriver() throws MalformedURLException {
        return driverThread.get().getDriver();
    }

    // clear cookies to be able to reuse instance, throw exception if cookies can't be cleared
    @AfterMethod(alwaysRun = true)
    public static void clearCookies() {
        try {
            getDriver().manage().deleteAllCookies();
        } catch (Exception ex) {
            System.err.println("Unable to delete cookies: " + ex.getCause());
        }
    }

    // close all instances of the driver contained in webDriverThreadPool list
    @AfterSuite(alwaysRun = true)
    public static void closeDriverObjects() {
        for (DriverFactory webDriverThread : webDriverThreadPool) {
            webDriverThread.quitDriver();
        }
    }
}