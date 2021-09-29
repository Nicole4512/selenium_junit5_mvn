package ru.ibs.nicole.base;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    static protected WebDriver driver;
    static protected WebDriverWait wait;
    @BeforeEach
     public void TestBefore(){
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10, 1000);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        String baseUrl = "https://www.rgs.ru/";
        driver.get(baseUrl);
    }

    @AfterEach
     public void TestAfter(){
        driver.quit();
    }

    protected void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void switchToTabByText(String text){
        String myTab = driver.getWindowHandle();
        List<String> newTab = new ArrayList<>(driver.getWindowHandles());
        for (String s : newTab){
            if(!s.equals(myTab)){
                driver.switchTo().window(s);
                if (driver.getTitle().contains(text)){
                    return;
                }
            }
        }
        Assertions.fail("Вкладка " + text + " не найдена");
    }
    protected void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        element.clear();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", value));
        Assertions.assertTrue(checkFlag, "Поле было заполнено некорректно");
    }


    protected void close(By by, By iframe) {
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        try {
            WebElement iframeName = wait.until(ExpectedConditions.presenceOfElementLocated(iframe));
            driver.switchTo().frame(iframeName);
            WebElement close = driver.findElement(by);
            new Actions(driver).moveToElement(close).click().build().perform();
            driver.switchTo().defaultContent();
        } catch (NoSuchElementException ignore) {

        } catch (TimeoutException ignore) {

        } finally {
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        }
    }
    protected void fillInputPhone(WebElement phone, String value) {
        scrollToElementJs(phone);
        wait.until(ExpectedConditions.elementToBeClickable(phone));
        phone.click();
        phone.sendKeys(value);
        boolean checkFlagPhone = wait.until(ExpectedConditions.attributeContains(phone, "value", convertValuePhone(value)));
        Assertions.assertTrue(checkFlagPhone, "Поле было заполнено некорректно");
    }
    private String convertValuePhone(String value) {
        return "+7 (" + value.substring(0, 3) + ") " + value.substring(3, 6) + "-" + value.substring(6, 8) + "-" + value.substring(8);
    }
    protected void setCheckbox (WebElement checkbox, boolean b){
        if(b != Boolean.parseBoolean(checkbox.getAttribute("checked"))){
            scrollToElementJs(checkbox);
            checkbox.click();
            Assertions.assertEquals( "" + b, checkbox.getAttribute("checked"), "Согласие на обработку моих персональных данных произошло некорректно");
        }
    }
    protected void fillInputDate(WebElement date, String value){
        scrollToElementJs(date);
        wait.until(ExpectedConditions.elementToBeClickable(date));
        date.click();
        date.sendKeys(value);
        date.sendKeys(Keys.ENTER);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(date, "value", value));
        Assertions.assertTrue(checkFlag, "Поле было заполнено некорректно");
    }

}

