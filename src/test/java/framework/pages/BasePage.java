package framework.pages;

import framework.BaseDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;

/**
 * @author Dinko
 * Created at 19/09/2018
 */

public abstract class BasePage {
    protected RemoteWebDriver driver;

    public BasePage() {
        try {
            driver = BaseDriver.getDriver();
        } catch (MalformedURLException ignored) {
            //This will be be thrown when the test starts if it cannot connect to a RemoteWebDriver Instance
        }
    }
}

