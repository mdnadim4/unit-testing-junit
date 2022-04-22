import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MyJunit {

    WebDriver driver;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--headless");
        option.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking", "enable-automation"));
        driver = new ChromeDriver(option);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    public void getTitle() {
        driver.get("https://demoqa.com");
        String title = driver.getTitle();
        System.out.println(title);
        Assert.assertEquals("ToolsQA", title);
    }

    @Test
    public void checkIfImageExists() {
        driver.get("https://demoqa.com");
        WebElement logo = driver.findElement(By.cssSelector("img[src$='Toolsqa.jpg']"));
        Assert.assertTrue(String.valueOf(logo.isDisplayed()), true);
    }

    @Test
    public void writeSomething() throws InterruptedException {
        driver.get("https://demoqa.com/text-box");
        WebElement fullName = driver.findElement(By.id("userName"));
        WebElement email = driver.findElement(By.cssSelector("[type='email']"));
        WebElement address = driver.findElement(By.cssSelector("[placeholder='Current Address']"));
        List<WebElement> textarea = driver.findElements(By.tagName("textarea"));

        fullName.sendKeys("Nadim Mahmud");
        email.sendKeys("nadim@testing.com");
        address.sendKeys("New York Brooklyn");
        textarea.get(1).sendKeys("Miami Florida");

        Assert.assertTrue(String.valueOf(fullName.getText().contains("Nadim Mahmud")), true);
        Assert.assertTrue(String.valueOf(email.getText().contains("nadim@testing.com")), true);
        Assert.assertTrue(String.valueOf(address.getText().contains("New York Brooklyn")), true);
    }

    @Test
    public void buttonElements() throws InterruptedException {
        driver.get("https://demoqa.com/buttons");
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        buttons.get(3).click();

        Actions actions = new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();
        actions.contextClick(buttons.get(2)).perform();
        Thread.sleep(2000);

        String doubleClickMessage = driver.findElement(By.id("doubleClickMessage")).getText();
        String rightClickMessage = driver.findElement(By.id("rightClickMessage")).getText();
        String dynamicClickMessage = driver.findElement(By.id("dynamicClickMessage")).getText();

        Assert.assertTrue(String.valueOf(doubleClickMessage.contains("You have done a double click")), true);
        Assert.assertTrue(String.valueOf(rightClickMessage.contains("You have done a double click")), true);
        Assert.assertTrue(String.valueOf(dynamicClickMessage.contains("You have done a dynamic click")), true);
    }

    @Test
    public void alertHandle() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        Thread.sleep(2000);
        driver.switchTo().alert().accept();
    }

    @Test
    public void alertHandleWithDelay() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("timerAlertButton")).click();
        Thread.sleep(7000);
        driver.switchTo().alert().accept();
    }

    @Test
    public void alertHandleWithConfirm() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("confirmButton")).click();
        driver.switchTo().alert().dismiss();
        String result = driver.findElement(By.id("confirmResult")).getText();
        Assert.assertTrue(String.valueOf(result.contains("selected Cancel")), true);
    }

    @Test
    public void alertHandleWithPrompt() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("promtButton")).click();
        Alert alert = driver.switchTo().alert();
        alert.sendKeys("Nadim Mahmud");
        alert.accept();
        String promptResult = driver.findElement(By.id("promptResult")).getText();
        Assert.assertTrue(String.valueOf(promptResult.contains("You entered Nadim Mahmud")), true);
    }

    @Test
    public void selectDate() throws InterruptedException {
        driver.get("https://demoqa.com/date-picker");
        driver.findElement(By.id("datePickerMonthYearInput")).click();
        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        WebElement year = driver.findElement(By.className("react-datepicker__year-select"));
        WebElement month = driver.findElement(By.className("react-datepicker__month-select"));

        Select y = new Select(year);
        y.selectByValue("2025");

        Select m = new Select(month);
        m.selectByVisibleText("August");
        driver.findElement(By.className("react-datepicker__day--029")).click();

//        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("04/30/2022");
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
    }

    @Test
    public void selectDropDown() throws InterruptedException {
        driver.get("https://demoqa.com/select-menu");
        WebElement oldSelect = driver.findElement(By.id("oldSelectMenu"));
        Select options = new Select(oldSelect);
        options.selectByVisibleText("Yellow");
        Thread.sleep(2000);
    }

    @Test
    public void selectMultipleDropDown() throws InterruptedException {
        driver.get("https://demoqa.com/select-menu");
        WebElement multiSelect = driver.findElement(By.id("cars"));
        Select options = new Select(multiSelect);
        if(options.isMultiple()) {
            options.selectByVisibleText("Volvo");
            options.selectByVisibleText("Audi");
        }
        Thread.sleep(2000);
    }

    @Test
    public void multipleTabHandle() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();

        String parentWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for(String window : allWindows) {
            if(!window.equals(parentWindow)){
                driver.switchTo().window(window);
            }
        }
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(String.valueOf(text.contains("This is a sample page")), true);
        driver.close();
        driver.switchTo().window(parentWindow);
        Thread.sleep(2000);
    }

    @Test
    public void modalDialog() throws InterruptedException {
        driver.get("https://demoqa.com/modal-dialogs");
        driver.findElement(By.id("showSmallModal")).click();
        String bodyContent = driver.findElement(By.className("modal-body")).getText();
        Assert.assertTrue(String.valueOf(bodyContent.contains("This is a small modal. It has very less content")), true);
        Thread.sleep(2000);
        driver.findElement(By.id("closeSmallModal")).click();
        Thread.sleep(2000);
    }

    @Test
    public void handleWebTable() {
        driver.get("https://demoqa.com/webtables");

    }

    @Test
    public void uploadImage() throws InterruptedException {
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys("C:\\Users\\HP\\OneDrive\\Desktop\\Tom_Cruise.jpg");
        Thread.sleep(2000);
    }

    @Test
    public void handleIframes() {
        driver.get("https://demoqa.com/frames");
        WebElement frame1 = driver.findElement(By.id("frame1"));
        driver.switchTo().frame(frame1);
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals("This is a sample page", text);
    }

    @Test
    public void mouseHover() throws InterruptedException {
        driver.get("https://green.edu.bd/");
        WebElement about = driver.findElement(By.linkText("ABOUT US"));
        Actions action = new Actions(driver);
        action.moveToElement(about).perform();
        Thread.sleep(2000);
    }

    @Test
    public void keyboardEvents() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement search = driver.findElement(By.name("q"));
        Actions action = new Actions(driver);
        action.moveToElement(search);
        action.keyDown(Keys.SHIFT);
        action.sendKeys("Selenium Webdriver");
        action.keyUp(Keys.SHIFT);
        action.doubleClick(search).perform();
        Thread.sleep(2000);
        action.contextClick(search).perform();
        Thread.sleep(2000);
    }

    @Test
    public void takeScreenshot() throws IOException {
        driver.get("https://demoqa.com");
        File fulllPageScreenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(fulllPageScreenshot, new File("Screenshots/fullpage.png"));
    }

    @After
    public void closeDriver() {
        driver.close();
    }

}
