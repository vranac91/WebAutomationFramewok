package framework.tests;

import framework.BaseDriver;
import framework.pages.ExamplePage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

/**
 * @author Dinko
 * Created at 19/09/2018
 */
public class ExampleTest extends BaseDriver {

    private WebDriver driver;

    @BeforeMethod
    public void setup() throws MalformedURLException {
        driver = getDriver();
    }

    @Test
    public void checkResultPageTitle() {
        driver.get("https://www.google.hr");

        ExamplePage search = new ExamplePage();

        search.inputSearchString("test");
        search.clickOnSubmitButton();

        Assert.assertEquals(driver.getTitle(), "test - Google pretraživanje");
    }

}