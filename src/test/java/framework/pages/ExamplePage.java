package framework.pages;

import com.lazerycode.selenium.util.Query;
import org.openqa.selenium.By;

/**
 * @author Dinko
 * Created at 19/09/2018
 */

public class ExamplePage extends BasePage {

    private Query searchBar = new Query(By.name("q"), driver);
    private Query searchButton = new Query(By.name("btnK"), driver);

    public void inputSearchString (String searchString) {
        searchBar.findWebElement().clear();
        searchBar.findWebElement().sendKeys(searchString);
    }

    public void clickOnSubmitButton() {
        searchButton.findWebElement().click();
    }
}
