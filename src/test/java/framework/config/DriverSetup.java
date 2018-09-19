package framework.config;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Dinko
 * Created at 18/09/2018
 */

/**
 * This is a simple interface passed to DriverType enum, ensuring that each enum
 * implements the same method.
 */

public interface DriverSetup {
    RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities);
}