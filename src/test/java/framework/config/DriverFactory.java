package framework.config;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static framework.config.DriverType.FIREFOX;
import static framework.config.DriverType.valueOf;

/**
 * @author Dinko
 * Created at 18/09/2018
 */

/**
 * This method handles all the hard work regarding verifying which browser type to instantiate and how.
 *
 * First, it decides which driver type is selected with class constructor, by fetching the "browser" system
 * property value from POM and saving it to browser string value. It then tries to assign this value to driverType
 * variable (an instance of DriverType enum). If the passed value is the same as the one from enum, it will pass that
 * value to selectedDriverType variable - otherwise, it will pass default driverType value, in this case FIREFOX.
 *
 * This variable is then passed to instantiateDriverObject() method, which prints out system properties and creates a
 * new WebDriver instance, depending if Selenium Grid is enabled or not.
 *
 * The method getDriver() should be used by every test for instantiating RemoteWebDriver object, since it returns the
 * webDriver created by instantiateDriverObject() method, but only if there isn't any instance currently running.
 *
 * Finally, quitDriver() method quits the current webDriver instance and nullifies it, so it can be instantiated again
 * if needed.
 */
public class DriverFactory {

    // variables for RemoteWebDriver instance and selected type of driver
    private RemoteWebDriver webDriver;
    private DriverType selectedDriverType;

    // system variables for printing
    private final String operatingSystem = System.getProperty("os.name").toUpperCase();
    private final String systemArchitecture = System.getProperty("os.arch");
    private final boolean useRemoteWebDriver = Boolean.getBoolean("remoteDriver");

    // driver constructor
    public DriverFactory() {

        // default driver type (fallback) that is overriden if different type is specified in POM "browser" tag
        DriverType driverType = FIREFOX;

        // fetch value of "browser" property from POM file and convert it to upper-case string
        String browser = System.getProperty("browser", driverType.toString()).toUpperCase();

        // try to create new instance of received "browser" value
        // catch exceptions if unable (wrong value or empty)
        try {
            driverType = valueOf(browser);
        } catch (IllegalArgumentException ignored) {
            System.err.print("Unknown driver specified, defaulting to '" + driverType + "'...");
        } catch (NullPointerException ignored) {
            System.err.print("No driver specified, defaulting to '" + driverType + "'...");
        }

        // assign the value of driverType variable received from POM to selectedDriverType
        // if unable to create driverType from received browser variable, return FIREFOX
        selectedDriverType = driverType;
    }

    // checks whether there is an existing webDriver
    // if not, creates a new one of type selectedDriverType (default set to Firefox) and returns it
    public RemoteWebDriver getDriver() throws MalformedURLException {
        if (null == webDriver) {
            instantiateWebDriver(selectedDriverType);
        }

        return webDriver;
    }

    // closes created webDriver instance
    public void quitDriver() {
        if (null != webDriver) {
            webDriver.quit();
            webDriver = null;
        }
    }

    private void instantiateWebDriver(DriverType driverType) throws MalformedURLException {

        // prints system info
        System.out.println(" ");
        System.out.println("Local Operating System: " + operatingSystem);
        System.out.println("Local Architecture: " + systemArchitecture);
        System.out.println("Selected Browser: " + selectedDriverType);
        System.out.println("Connecting to Selenium Grid: " + useRemoteWebDriver);
        System.out.println(" ");

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        // checks if Selenium Grid is enabled or not (specified in POM file under "remote" system property)
        // if it is, fetches Selenium Grid URL, browser and platform, and creates webDriver with these capabilities
        // if it isn't, returns webDriver of type specified in DriverType enum with defined capabilities
        if (useRemoteWebDriver) {

            URL seleniumGridURL = new URL(System.getProperty("gridURL"));
            String desiredBrowserVersion = System.getProperty("desiredBrowserVersion");
            String desiredPlatform = System.getProperty("desiredPlatform");

            // sets value of "desiredPlatform" from POM if it is not empty and not null
            if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
                desiredCapabilities.setPlatform(Platform.valueOf(desiredPlatform.toUpperCase()));
            }

            // sets value of "desiredBrowser" from POM if it is not empty and not null
            if (null != desiredBrowserVersion && desiredBrowserVersion.isEmpty()) {
                desiredCapabilities.setVersion(desiredBrowserVersion);
            }

            // returns webDriver with Selenium Grid of type specified in POM
            desiredCapabilities.setBrowserName(selectedDriverType.toString());
            webDriver = new RemoteWebDriver(seleniumGridURL, desiredCapabilities);
        } else {

            // return webDriver with capabilities from DriverType enum
            webDriver = driverType.getWebDriverObject(desiredCapabilities);
        }
    }
}