package ru.ibs.nicole;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ru.ibs.nicole.base.BaseTest;;


public class RgsJUnitMvnTest extends BaseTest {
    @Test

    void TestRGS(){
        //Выбрать меню
        WebElement menu = driver.findElement(By.xpath("//a[contains(text(), 'Меню') and contains(@class, 'hidden-xs')]"));
        wait.until(ExpectedConditions.elementToBeClickable(menu));
        menu.click();
        boolean checkFlagMenu = wait.until(ExpectedConditions.attributeContains(menu, "aria-expanded", "true"));
        Assertions.assertTrue(checkFlagMenu, "Меню не было открыто");

        //Выбрать пункт "Компаниям"
        WebElement company = driver.findElement(By.xpath("//a[contains(text(), 'Компаниям') and contains(@href, 'juristic_person')]"));
        wait.until(ExpectedConditions.elementToBeClickable(company));
        company.click();
        WebElement companyCheck = driver.findElement(By.xpath("//li[contains(@class, 'last-item')]"));
        Assertions.assertEquals("Компаниям", companyCheck.getText(),  "Переход в пункт 'Компаниям' не осущетвлен");


        //Выбрать пункт "Страхование здоровья"
        WebElement health = driver.findElement(By.xpath("//a[contains(text(), 'Страхование здоровья') and contains(@href, 'health')]"));
        scrollToElementJs(health);
        wait.until(ExpectedConditions.elementToBeClickable(health));
        health.click();
        switchToTabByText("ДМС для сотрудников - добровольное медицинское страхование от Росгосстраха");
        WebElement healthCheck = driver.findElement(By.xpath("//li[contains(@class, 'last-item')]"));
        Assertions.assertEquals("Личное страхование", healthCheck.getText(),  "Переход в пункт 'Страхование здоровья' не осущетвлен");




        //Выбрать пункт "Добровольное медицинское страхование"

        WebElement DMS = driver.findElement(By.xpath("//a[contains(text(), 'Добровольное медицинское страхование') and contains(@href, 'dms')]"));
        wait.until(ExpectedConditions.elementToBeClickable(DMS));
        DMS.click();

        WebElement DMSCheck = driver.findElement(By.xpath("//li[contains(@class, 'last-item')]"));
        Assertions.assertEquals("Добровольное медицинское страхование", DMSCheck.getText(),  "Переход в пункт 'Добровольное медицинское страхование' не осущетвлен");


        WebElement DMSheadline = driver.findElement(By.xpath("//h1[contains(@class, 'content-document-header')]"));
        wait.until(ExpectedConditions.visibilityOf(DMSheadline));
        Assertions.assertEquals( "Добровольное медицинское страхование", DMSheadline.getText(), "Заголовок отсутствует");



        //Нажать "отправить заявку"
        WebElement application = driver.findElement(By.xpath("//a[contains(text(), 'Отправить заявку') and contains(@class, 'btn-default')]"));
        wait.until(ExpectedConditions.elementToBeClickable(application));
        application.click();
        close(By.xpath("//button[contains(text(), 'Понятно')]"), By.xpath("//iframe[contains(@id, 'fl-448882')]"));
        WebElement applicationCheck = driver.findElement(By.xpath("//b [@data-bind = 'text: options.title']"));
        Assertions.assertEquals("Заявка на добровольное медицинское страхование", applicationCheck.getText(), "Переход в пункт 'отправить заявку' не осущетвлен" );


        //Заполнить поля (поле "Эл. почта" заполнить некорректно)

        String fieldXPath = "//input[contains(@class, 'form-control') and contains(@data-bind, '%s')]";
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value:LastName"))), "Иванов");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value:FirstName"))), "Иван");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value:MiddleName"))), "Иванович");
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "Email"))), "qwertyqwerty");
        fillInputField(driver.findElement(By.xpath("//textarea[contains(@class, 'popupTextarea') and contains(@data-bind, 'Comment')]")), "Комментарии");

        Select region = new Select(driver.findElement((By.xpath("//select[@name='Region' and contains(@data-bind, 'options:RegionsList')]"))));
        region.selectByVisibleText("Москва");

        WebElement phone = driver.findElement(By.xpath(String.format(fieldXPath, "Phone")));
        fillInputPhone(phone, "9117778899");

        WebElement date = driver.findElement(By.xpath(String.format(fieldXPath, "ContactDate")));
        fillInputDate(date, "01.02.2022");


        WebElement checkbox = driver.findElement(By.xpath("//input[@class  = 'checkbox' and @type = 'checkbox']"));
        setCheckbox(checkbox, true);


        //Нажать Отправить
        WebElement send = driver.findElement(By.xpath("//button[@id = 'button-m']"));
        scrollToElementJs(send);
        wait.until(ExpectedConditions.elementToBeClickable(send));
        send.click();

        //Проверить, что у поля – Эл. почта присутствует сообщение об ошибке – "Введите адрес электронной почты"
        WebElement email = driver.findElement(By.xpath(String.format(fieldXPath, "Email")));
        email = email.findElement(By.xpath("./..//span"));
        Assertions.assertEquals("Введите адрес электронной почты", email.getText(),"Проверка ошибки у поля не была пройдена");

    }



}
