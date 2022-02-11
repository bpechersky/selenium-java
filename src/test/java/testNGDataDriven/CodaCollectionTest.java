package testNGDataDriven;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;


public class CodaCollectionTest {
    //This method will run once before all of the tests in our class
    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    @Test
    public void firstTest() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://codacollection.co/");
        WebElement films_link = driver.findElement(By.xpath("//div[text()='Films']"));
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Films']")));
        films_link.click();


        WebElement startAmazonPrimeLink = driver.findElement((By.xpath("//div[text()='Start your free trial']")));

        //Store the ID of the original window
        String originalWindow = driver.getWindowHandle();

        //Check we don't have other windows open already
        assert driver.getWindowHandles().size() == 1;
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Start your free trial']")));
        startAmazonPrimeLink.click();
        //Wait for the new window or tab
        wait.until(numberOfWindowsToBe(2));
        for (String windowHandle : driver.getWindowHandles()) {
            if(!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        //Wait for the new tab to finish loading content
       // wait.until(titleIs("Amazon.com Sign up for Prime Video"));
        String URL = driver.getCurrentUrl();
        Assert.assertTrue(URL.contains("codacollectio"));
        driver.quit();
    }
    @Test
    public void secondTest()
    {
        WebDriver driver = new ChromeDriver();
        driver.get("https://careers.codacollection.co/jobs");
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement qa_job_link = driver.findElement(By.xpath("//a[contains(text(),'QA')]"));
        wait.until(ExpectedConditions.visibilityOf(qa_job_link));
        qa_job_link.click();
        String URL = driver.getCurrentUrl();
        Assert.assertTrue(URL.contains("qa-engineer-remote"));
        driver.quit();


    }
    @Test
    public void thirdTest()
    {
        WebDriver driver = new ChromeDriver();
        driver.get("https://codacollection.co/");
  //      driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement email_list = driver.findElement(By.xpath("//input[@type='email']"));
        wait.until(ExpectedConditions.visibilityOf(email_list));
        email_list.sendKeys("test@test.com");
        WebElement btn_subscribe = driver.findElement(By.xpath("//button[@aria-label='Submit']"));
        btn_subscribe.click();
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        WebElement success_text = driver.findElement(By.xpath("//div[@class='Text__PrimaryText-sc-15dpo10-1 dxRVgw']"));
        wait.until(ExpectedConditions.visibilityOf(success_text));
        String text = success_text.getText();
        Assert.assertEquals(text,"Great. You're in.");
        driver.quit();
    }
}
