package framework.config;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;

/**
 * @author Dinko
 * Created at 18/09/2018
 */

/**
 * This enum file sets options for various popular browsers. Chosen values are passed
 * to DriverFactory class that will handle the rest.
 *
 * All enums are having getWebDriverObject() method that sets some specific features to
 * browsers using DesiredCapabilities > see documentation:
 *
 * https://github.com/SeleniumHQ/selenium/wiki/DesiredCapabilities
 * https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/remote/DesiredCapabilities.html
 *
 * All options specified for each browser are overwriting identical default capabilities using merge() method,
 * and the modified instance of specific browser driver is returned as an instance of RemoteWebDriver.
 *
 * The enum also contains HEADLESS variable that fetches the specified "headless" value from POM file,
 * determining whether the returned instance will run with GUI or without it.
 */

public enum DriverType implements DriverSetup {

    FIREFOX {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {

            // https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/firefox/FirefoxOptions.html

            FirefoxOptions options = new FirefoxOptions();
            options.merge(capabilities);
            options.setHeadless(HEADLESS);

            return new FirefoxDriver(options);
        }
    },

    CHROME {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {

            // https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/chrome/ChromeOptions.html

            HashMap<String, Object> chromePreferences = new HashMap<>();
            chromePreferences.put("profile.password_manager_enabled", false);
            ChromeOptions options = new ChromeOptions();
            options.merge(capabilities);
            options.setHeadless(HEADLESS);
            options.addArguments("--no-default-browser-check");
            options.setExperimentalOption("prefs", chromePreferences);

            return new ChromeDriver(options);
        }
    },

    IE {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {

            // https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/ie/InternetExplorerOptions.html

            InternetExplorerOptions options = new InternetExplorerOptions();
            options.merge(capabilities);
            options.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            options.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
            options.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);

            return new InternetExplorerDriver(options);
        }
    },

    EDGE {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {

            // https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/edge/EdgeOptions.html

            EdgeOptions options = new EdgeOptions();
            options.merge(capabilities);

            return new EdgeDriver(options);
        }
    },

    SAFARI {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {

            // https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/safari/SafariOptions.html

            SafariOptions options = new SafariOptions();
            options.merge(capabilities);

            return new SafariDriver(options);
        }
    },

    OPERA {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {

            // https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/opera/OperaOptions.html

            OperaOptions options = new OperaOptions();
            options.merge(capabilities);

            return new OperaDriver(options);
        }
    };

    // fetch headless value from POM file, defined with <headless> tag
    public final static boolean HEADLESS = Boolean.getBoolean("headless");
}
