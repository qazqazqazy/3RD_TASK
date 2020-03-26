import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class selenium_task_1 {

    WebDriver driver; //создаем в полях класса переменную класса Webdriver, названную driver
    String baseUrl; //переменная типа string, названная baseUrl

    @Before //Данный метод выполняется перед каждоым тестом
    public void  beforeMetod(){

        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        //указываем в системной переменной путь к драйверу для хрома - перед началом теста

        baseUrl="http://www.sberbank.ru/ru/person";

        driver = new ChromeDriver(); //инициализируем переменную драйвер. так как мы запускаем в хроме,
        // создаем новый экзампляр класса хромдрайвер

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); //Имплицитное ожидания для
        // метода findelement. в течение 30 секунд ждет появления элемента с заданным локатором
        driver.manage().window().maximize();//развернем появившееся окно на весь экран
        driver.get(baseUrl);
    }
        //после его выполнения открывается окно браузера, раскрывается на весь экран и происходит переход по ссылке

    @Test
    public void task1() {
        driver.findElement(By.xpath("//span[contains(@class,'lg-menu__text')][contains(text(),'Страхование')]")).click();

        driver.findElement(By.xpath(("//a[contains(text(),'Страхование путешественников')]"))).click();

        // В заданиии есть пункт "Проверить наличие на странице заголовка – Страхование путешественников"
        // Сайт несколько изменился, поэтому просто проверим, что у нас есть на странице текст "до 120 000 евро"
        WebElement search_text  = driver.findElement(By.xpath("//*[text()='Сумма выплаты']//..//h3[@class='kit-heading kit-heading_s']"));
        assertEquals("до 120 000 евро",search_text.getText());

       // driver.findElement(By.xpath(("//b[contains(text(),'Оформить онлайн')]"))).click();

        String problemUrl = driver.findElement(By.xpath("//b[contains(text(),'Оформить онлайн')]/..")).getAttribute("href");
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); //Имплицитное ожидания
        driver.get(problemUrl);

        driver.findElement(By.xpath("//*[(@class='btn btn-primary btn-large')][contains(text(),'Оформить')]")).click();
        //driver.findElement(By.xpath("//*[(@class='btn btn-primary btn-large')]")).click();
        Wait<WebDriver> wait = new WebDriverWait(driver, 5,4000);//ожидаем появления
        WebElement title = driver.findElement(By.xpath("//div[contains(@class,'sub-legend')][contains(text(),'Введите данные застрахованных латинскими буквами, как в загранпаспорте')]"));
        //WebElement title = driver.findElement(By.xpath("//div[contains(@class,'col-4 step-element active')]//div[contains(text(),'2')]"));
        wait.until(ExpectedConditions.visibilityOf(title));

/* попробуем посмотреть, как можно дождаться другого элемента
        Wait<WebDriver> wait = new WebDriverWait(driver, 5,4000);//ожидаем появления
        WebElement title = driver.findElement(By.id("person_lastName"));
        wait.until(ExpectedConditions.visibilityOf(title));
 */
        fillField(By.id("surname_vzr_ins_0"),"Ivanov");
        fillField(By.id("name_vzr_ins_0"),"Ruslan");
        fillField(By.id("birthDate_vzr_ins_0"),"23.03.2010");

    /* это нам пока не нужно
        WebElement fam = driver.findElement(By.id("person_lastName"));
        wait.until(ExpectedConditions.visibilityOf(fam));
    */
        driver.findElement(By.xpath("//*[contains(text(),'Страхователь')]")).click();
        fillField(By.id("person_lastName"),"Васильев");
        fillField(By.id("person_firstName"),"Михаил");
        fillField(By.id("person_middleName"),"Александрович");
        fillField(By.id("person_birthDate"),"23.03.1986");

        driver.findElement(By.xpath("//*[contains(text(),'Страхователь')]")).click();
        fillField(By.id("passportSeries"),"3411");
        fillField(By.id("passportNumber"),"341111");
        fillField(By.id("documentIssue"),"Т4444444444444");
        fillField(By.id("documentDate"),"03.10.2014");
        driver.findElement(By.xpath("//*[contains(text(),'Мобильный телефон')]")).click();

        driver.findElement(By.xpath("//label[contains(text(),'Мужской')]")).click();

       //Проверка полей на корректность  заполнения
        assertEquals("Ivanov", driver.findElement(By.id("surname_vzr_ins_0")).getAttribute("value"));
        assertEquals("Ruslan", driver.findElement(By.id("name_vzr_ins_0")).getAttribute("value"));
        assertEquals("23.03.2010", driver.findElement(By.id("birthDate_vzr_ins_0")).getAttribute("value"));

        assertEquals("Васильев", driver.findElement(By.id("person_lastName")).getAttribute("value"));
        assertEquals("Михаил", driver.findElement(By.id("person_firstName")).getAttribute("value"));
        assertEquals("Александрович", driver.findElement(By.id("person_middleName")).getAttribute("value"));

        assertEquals("3411", driver.findElement(By.id("passportSeries")).getAttribute("value"));
        assertEquals("341111", driver.findElement(By.id("passportNumber")).getAttribute("value"));
        assertEquals("03.10.2014", driver.findElement(By.id("documentDate")).getAttribute("value"));
        assertEquals("Т4444444444444", driver.findElement(By.id("documentIssue")).getAttribute("value"));

       driver.findElement(By.xpath("//button[contains(text(),'Продолжить')]/.")).getAttribute("href");

        //Проверить, что текст ошибки соответствует нужному
        assertEquals("Поле не заполнено.",
                driver.findElement(By.xpath("//span[contains(@class,'invalid-validate form-control__message')]")).getAttribute("innerText"));
    }

    //создадим метод, который будет заполнять поле
    public void fillField(By locator, String value) {
        driver.findElement(locator).clear(); //очистили найденное поле
        driver.findElement(locator).sendKeys(value); //заполнили поле значением, поданным на вход
    }

    @After //Данный метод выполняется после каждого теста
    public void afterTest() {
        driver.quit(); //закроем окно браузера
    }


}

