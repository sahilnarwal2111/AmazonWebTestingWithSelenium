import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.time.Duration;
import java.util.logging.FileHandler;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class App {
    private static final Logger logger = Logger.getLogger("MyLog");
    private static WebDriver driver;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.getProperty(
                "webdriver.chrome.driver",
                "/Users/sahilnarwal/Downloads/chromedriver-mac-arm64-new/chromedriver");;
        WebDriver driver = new ChromeDriver();

        try {
            setupLogging();
            driver.get("https://www.amazon.in/");
            driver.manage().window().maximize();
            // captureScreenshot("AmazonHomepage");
            logInfo("Opened Amazon website");

            logInfo("Current URL:" + driver.getCurrentUrl());
            logInfo("Current Title:" + driver.getTitle());

            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
            searchBox.sendKeys("mobile");
            searchBox.submit();
            // captureScreenshot("SearchMobile");
            logInfo("Performed search for 'mobile'");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fourStarsFilter = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//section[@aria-label='4 Stars & Up']//i[@class='a-icon a-icon-star-medium a-star-medium-4']")));
            fourStarsFilter.click();




            logInfo("Selected 4 stars filter");

            WebElement firstSearchResult = wait.until(ExpectedConditions
                    .elementToBeClickable(By.xpath("//span[@class='a-size-medium a-color-base a-text-normal']")));
            firstSearchResult.click();
            logInfo("Clicked on the first search result");

            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> handlesList = new ArrayList<>(windowHandles);
            logInfo("Window handles: " + handlesList);
            driver.switchTo().window(handlesList.get(1));

            Thread.sleep(5000);

            WebElement addToCartButton = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("a-button-inner")));
            addToCartButton.click();


            logInfo("Clicked on 'Add to Cart' button");

            WebElement goToCartButton = wait
                    .until(ExpectedConditions.elementToBeClickable(By.id("attach-sidesheet-view-cart-button-announce")));
            Actions actions = new Actions(driver);
            actions.moveToElement(goToCartButton).click().perform();
            logInfo("Clicked on 'Go to Cart' button");



        } catch (Exception e) {
            logError("Exception occurred: " + e.getMessage());
            // captureScreenshot("ErrorOccurred");
        } finally {
            driver.quit();
            logInfo("Browser closed");
        }
    }

    private static void setupLogging() throws IOException {
        FileHandler fh = new FileHandler("automation_log.log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    private static void logInfo(String message) {
        logger.info(message);
    }

    private static void logError(String message) {
        logger.severe(message);
    }
}