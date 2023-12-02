package utilityMethods;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.util.Assert;
import com.google.common.io.Files;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Utility {
	private URL url;
	protected WebDriver driver;
	private Properties propObj;

	private ExtentReports extReportObj;
	private ExtentTest testLogger;

	public Utility(String pathOfCOnfingFile) {
		try {
			InputStream file = new FileInputStream(pathOfCOnfingFile);
			propObj = new Properties();
			propObj.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Properties getProObj() {
		return propObj;
	}

	public WebDriver getDriver() {
		return driver;
	}

	// Date And Time //

	public void getCurrentDate() {
		Date date = new Date();
		String currentDate = date.toString().replace(" ", "_").replace(":", "_");
		System.out.println(currentDate);

	}

	public String getCurrentDateAndTime() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd___HH-mm-ss");
		String formattedDateTime = currentDateTime.format(formatter);
		return formattedDateTime;

	}

	// Extent Report //

	public void initHtmlReports() {
		String date = getCurrentDateAndTime();
		String appName = getProObj().getProperty("ApplicationName");
		ExtentSparkReporter htmlReports = new ExtentSparkReporter(
				"ExtentReport//" + date + "___" + appName + "___" + "reports.html");
		htmlReports.config().setReportName("Functional Testing ");
		htmlReports.config().setDocumentTitle(" Functional Report");
		htmlReports.config().setTheme(Theme.DARK);

		extReportObj = new ExtentReports();
		extReportObj.attachReporter(htmlReports);
		extReportObj.setSystemInfo("OsName", "window 10");
		extReportObj.setSystemInfo("Browser Name", "Chrome");
		extReportObj.setSystemInfo("Tester Name", "Mithlesh");
		extReportObj.setSystemInfo("Environment", "UAT");
	}

	public void setExtentTestLogger(String testCaseName) {
		testLogger = extReportObj.createTest(testCaseName);

	}

	public void flushReport() {

		extReportObj.flush();
	}

	public void passAnyConditionalInformationUsingLable(String message) {
		testLogger.log(Status.INFO, MarkupHelper.createLabel(message, ExtentColor.GREEN));
	}

	public void printExectionInExtentReport(WebElement we, String e) {
		String textOfWelement = we.getText();
		testLogger.log(Status.INFO, MarkupHelper.createLabel(textOfWelement + "        " + e, ExtentColor.RED));
	}

	public void passAnyConditionalInformation(String message) {
		testLogger.log(Status.INFO, message);
	}

	public void passmassageInCaseOfFail(String massage) {
		testLogger.log(Status.FAIL, massage);
	}

	public void passmassageInCaseOfPass(String massage) {
		testLogger.log(Status.FAIL, massage);
	}

	public void attachScreenShotAtLogLevel(WebElement we, String message) {
		String pathOfScreenshot = takeScreenshotOfAnyspecificWebElementInBase64Format(we);
		testLogger.log(Status.INFO,
				MediaEntityBuilder.createScreenCaptureFromBase64String(pathOfScreenshot, message).build());

	}

	public void storeOrderedListInextentReport(List<String> ls) {
		testLogger.log(Status.INFO, MarkupHelper.createOrderedList(ls));
	}

	public void storeOrderedMapInextentReport(Map<String, String> mp) {
		testLogger.log(Status.INFO, MarkupHelper.createOrderedList(mp));
	}

	public void storeUnOrderedListInextentReport(List<String> ls) {
		testLogger.log(Status.INFO, MarkupHelper.createUnorderedList(ls));
	}

	/*
	 * public void resultStaus(ITestResult result) {
	 * 
	 * if (result.getStatus() == result.SUCCESS) {
	 * testLogger.pass(result.getMethod().getMethodName() +
	 * "  is passed succesfully"); } else if (result.getStatus() ==
	 * ITestResult.FAILURE) { testLogger.fail(result.getMethod().getMethodName() +
	 * "  is Failed"); testLogger.fail(result.getThrowable().toString() +
	 * " is Failed");
	 * testLogger.addScreenCaptureFromPath(fortakesnapshot(result.getMethod().
	 * getMethodName())); refreshWindow(); staticThreadSleepWait(4); } else if
	 * (result.getStatus() == ITestResult.SKIP) {
	 * testLogger.skip(result.getMethod().getMethodName() + " is Skipped");
	 * testLogger.skip(result.getThrowable().toString() + " is Skipped"); }
	 * 
	 * }
	 */
	// Mouse And KeyBoard Action //

	public void actionClick(WebElement we) {
		Actions actions = new Actions(driver);
		if (we.isDisplayed() && we.isEnabled()) {
			actions.moveToElement(we).build().perform();
			actions.moveToElement(we).click().build().perform();
		}
	}

	public void mouseHover(WebElement we) {
		Actions actions = new Actions(driver);
		if (we.isDisplayed() && we.isEnabled()) {
			actions.moveToElement(we).build().perform();

		}
	}

	public void pressleftArrowBtn() {
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.ARROW_LEFT);
	}

	public void actionClickAndHold(WebElement we) {

	}

	public void actionsInputValue(WebElement we, String value) {
		Actions actions = new Actions(driver);
		if (we.isDisplayed() && we.isEnabled()) {
			we.clear();
			actions.moveToElement(we).sendKeys(value).build().perform();
		}
	}

	public void actionDoubleCLick(WebElement we) {
		Actions actions = new Actions(driver);
		if (we.isDisplayed() && we.isEnabled()) {
			actions.moveToElement(we).doubleClick().build().perform();
		}
	}

	public void actionRightCLick(WebElement we) {
		Actions actions = new Actions(driver);
		if (we.isDisplayed() && we.isEnabled()) {
			actions.moveToElement(we).contextClick().build().perform();
		}
	}

	public void dragAndDrop1(WebElement source, WebElement destination) {
		Actions actions = new Actions(driver);
		if (source.isDisplayed() && source.isEnabled()) {
			actions.dragAndDrop(source, destination).build().perform();
		}

	}

	public void dragAndDrop2(WebElement source, WebElement destination) {
		Actions actions = new Actions(driver);
		if (source.isDisplayed() && source.isEnabled()) {
			actions.clickAndHold(source).moveToElement(source).release(destination).build().perform();
		}

	}

	// Screenshot //

	public String fortakesnapshot(String snapshotname) {
		TakesScreenshot takesscrenshot = (TakesScreenshot) driver;
		File sourceFile = takesscrenshot.getScreenshotAs(OutputType.FILE);
		String time = getCurrentDateAndTime();
		File destinationfile = new File("ScreenShotOfFailedTestCases//" + snapshotname + time + ".jpg");

		try {
			Files.copy(sourceFile, destinationfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return destinationfile.getAbsolutePath();
	}

	public void attachSnapshotTotheExtentReport(String imagepath) {

		try {
			testLogger.addScreenCaptureFromPath(imagepath);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void Screenshot(String name) {
		String dateAndTime = getCurrentDateAndTime();

		File filesrc = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(filesrc, new File("screentShot\\" + dateAndTime + " " + name + " " + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Screenshot has been taken");
	}

	public String captureScreenShot() {
		String dateAndTime = getCurrentDateAndTime();
		TakesScreenshot takesscrenshot = (TakesScreenshot) driver;
		File sourceFile = takesscrenshot.getScreenshotAs(OutputType.FILE);
		File desFile = new File("screentShot\\" + dateAndTime + ".jpg");
		try {
			FileUtils.copyFile(sourceFile, desFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return desFile.getAbsolutePath();

	}

	public String captureScreenshotBase64() {

		TakesScreenshot screenshot = (TakesScreenshot) driver;
		String base64 = screenshot.getScreenshotAs(OutputType.BASE64);
		System.out.println("Screenshot shot saved succesfully");

		return base64;

	}

	public String takeScreenShotOfWebElement(WebElement we) {
		String date = getCurrentDateAndTime();
		String wetext = we.getText().replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\n", "").replaceAll("\\s+", "");
		  if (wetext.length() > 50) {
	          wetext = wetext.substring(0, 50);
	            System.out.println("First 50 characters: " + wetext);
	        } else {
	            // The sentence is already 50 characters or shorter
	            System.out.println("The entire sentence: " + wetext);
	        }
		System.out.println(wetext);

		String screenshotName = wetext + date;
		File src = we.getScreenshotAs(OutputType.FILE);
		File destination = new File("ScreenshotOfWebElement\\" + screenshotName + ".jpg");

		try {
			Files.copy(src, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destination.getAbsolutePath();
	}

	public String takeScreenshotOfAnyspecificWebElementInBase64Format(WebElement we) {
		String Screenshot = null;
		try {
			Screenshot = we.getScreenshotAs(OutputType.BASE64);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return Screenshot;
	}

	///////// Launch Browser///////////////
	public void launchBrowser(String browser) {

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions option = new ChromeOptions();
			option.addArguments("--remote-allow-origins=*");
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("download.default_directory", "F:\\Varta_QA_Automation\\downloadFiles");
			option.setExperimentalOption("prefs", chromePrefs);
			option.addArguments("--disable-infobars");
			option.addArguments("--disable-extensions");
			driver = new ChromeDriver(option);
		
			
		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions capabilities = new FirefoxOptions();
			capabilities.setCapability("marionette", true);
			driver = new FirefoxDriver(capabilities);


		} else if (browser.equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver() ;
		} else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else {
			System.out.println("you have entered wrong URL");
		
		}

	}

	////// open URL////////////
	public void openURL(String urlName) {
		driver.get(urlName);
//		testLogger.log(Status.INFO, urlName + "    has been hitted succesfully");

	}

	/// ///////// Click Method ///////////////
	public void click(WebElement we, String message) {
		if (we.isDisplayed() && we.isEnabled()) {
			try {
				we.click();
			
			} catch (NoSuchElementException e) {
				Actions act = new Actions(driver);
				act.moveToElement(we).click().build().perform();
				e.printStackTrace();
				
			} catch (Exception e) {

				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", we);
			//	testLogger.log(Status.PASS, "clicked  succesfully on the " + message);
	
			}
		}

	}

	////// Send keys Method ////////
	public void pressEnterKey(WebElement we) {
		we.sendKeys(Keys.ENTER);
	
		// Actions act=new Actions(driver);
		// act.sendKeys(Keys.ENTER);
	}

	public void sendKey(WebElement we, String value, String message) {
		if (we.isDisplayed() && we.isEnabled()) {
			try {
				we.clear();
				we.sendKeys(value);
			
			} catch (NoSuchElementException e) {
				Actions act = new Actions(driver);
				we.clear();
				act.moveToElement(we).sendKeys(value).build().perform();
				e.printStackTrace();
				
			} catch (Exception e) {
				we.clear();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].value='" + value + "';", we);
			}

		}
	}

	//////////// window///////////////
	public void maximizeWindow() {
		driver.manage().window().maximize();
		// testLogger.log(Status.INFO, "window is maximized");
	}

	public void refreshWindow() {
		driver.navigate().refresh();
	}

	public void close() {
		driver.close();
		
	}

	public void quit() {
		driver.quit();
	}

	public void navigateBack() {
		driver.navigate().back();
	}

	public void navigateForword() {
		driver.navigate().forward();

	}

	//////////////////// File Upload Using Robot///////////////////////////
// using robot class we can perform mouse and keyboard actions to interact with native OS Windows , popups and native applications 

	public void fileUpboadUsingRobot(String filePathWithName) {

		try {
			Robot robot = new Robot();
			robot.delay(1000);
			StringSelection selection = new StringSelection(filePathWithName);
			robot.delay(1000);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
			robot.delay(200);

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.delay(200);
			robot.keyPress(KeyEvent.VK_V);
			robot.delay(1000);

			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(200);
			robot.keyRelease(KeyEvent.VK_V);
			robot.delay(1000);

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			robot.delay(2000);
		} catch (AWTException e) {

			e.printStackTrace();
		}
	}

	public void UploadListOfFilesdUsingRobot(String filePathWithName) {

		try {
			Robot robot = new Robot();
			robot.delay(1000);
			StringSelection selection = new StringSelection(filePathWithName);
			robot.delay(1000);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
			robot.delay(200);

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.delay(200);
			robot.keyPress(KeyEvent.VK_V);
			robot.delay(1000);

			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(200);
			robot.keyRelease(KeyEvent.VK_V);
			robot.delay(1000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			for (int i = 0; i <= 10; i++) {
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
				robot.delay(1000);
			}
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			robot.delay(1000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

			robot.delay(2000);
		} catch (AWTException e) {

			e.printStackTrace();
		}
	}

	public void fileUploadUsingSendKeys(WebElement we, String filePath, String message) {
		we.sendKeys(filePath);
	}

	public void fileDownloadUsingRobot() {

	}

	public void robotKeyPress(int valueOfKey) {
		try {
			Robot robot = new Robot();
			robot.keyPress(valueOfKey);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void robotKeyRelease(int valueOfKey) {
		try {
			Robot robot = new Robot();
			robot.keyRelease(valueOfKey);
		} catch (AWTException e) {
			e.printStackTrace();
		}
///robot.keyRelease(KeyEvent.VK_V);	
	}

	public void takeScreenshotUsingRobotClassRectanleArea(int x, int y, int width, int Height) {
		String date = getCurrentDateAndTime();
		String imgName = "robot+date";

		try {
			Robot robot = new Robot();
			Rectangle reatangle = new Rectangle(x, y, width, Height);
			BufferedImage srcImage = robot.createScreenCapture(reatangle);
			ImageIO.write(srcImage, "PNG", new File("screenShot\\" + imgName + ".png"));
		} catch (AWTException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void takeScreenshotRobotFullPage() {
		String date = getCurrentDateAndTime();
		String imgName = "robot+date";

		try {
			Robot robot = new Robot();

			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle rectangle = new Rectangle(d);
			BufferedImage srcFile = robot.createScreenCapture(rectangle);
			ImageIO.write(srcFile, "PNG", new File("screenShot\\" + imgName + ".png"));

		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ScrollingUsingMouseWheel(int number) {
		try {
			Robot robot = new Robot();
			robot.mouseWheel(number);
			// for scroll down take positive number while for scroll up take negative Number
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	public void staticWaitUsingRobot(int time) {
		int timeWaitInSecond = 1000 * time;
		try {
			Robot robot = new Robot();
			robot.delay(timeWaitInSecond);
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	public void dropDownWithoutSelectClassTag(WebElement we, String selectvalue) {

		while (true) {
			try {
				Robot rb = new Robot();
				rb.delay(500);
				rb.keyPress(KeyEvent.VK_DOWN);
				rb.keyRelease(KeyEvent.VK_DOWN);
				rb.delay(1000);
				String value = we.getText();
				if (value.equalsIgnoreCase(selectvalue)) {
					rb.keyPress(KeyEvent.VK_ENTER);
					rb.keyRelease(KeyEvent.VK_ENTER);
					rb.delay(500);
					break;

				}

			} catch (AWTException e) {
				e.printStackTrace();
			}

		}

	}

	// Captcha code //

	public String capthacodeHandlilng() {

		return null;

	}

	// public void waits //

	public void staticThreadSleepWait(int timeForWaitinginSec) {
		int second = timeForWaitinginSec * 1000;
		try {

			Thread.sleep(second);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void implicityWait(int timeInSecond) {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeInSecond));
	}

	public void explicityWait(WebElement we, int timeInsecond) {

		WebDriverWait explicitwait = new WebDriverWait(driver, Duration.ofSeconds(timeInsecond));
		explicitwait.until(ExpectedConditions.visibilityOf(we));

	}

	public void explicityWaitUntillTheElementIsdiappeared(WebElement we, int timeinsecod) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeinsecod));
			wait.until(ExpectedConditions.invisibilityOf(we));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void waitUntilVisibiltyOfListOfWebElement(List<WebElement> we, int timeinsecond) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeinsecond));
		wait.until(ExpectedConditions.visibilityOfAllElements(we));

	}

	public void fluentWait(WebElement we, int totalTime, int timeInterval) {

	}

	// Window Handle //

	public Set<String> switchToWindowByTitle(String expWindowTitle) {
		Set<String> windows = driver.getWindowHandles();
		for (String multiplewindow : windows) {
			driver.switchTo().window(multiplewindow);
			String actWindowTitle = driver.getTitle();
			System.out.println(actWindowTitle);
			if (!expWindowTitle.equalsIgnoreCase(actWindowTitle)) {
			}
			driver.switchTo().window(driver.getWindowHandle());
		}
		return windows;
	}

	public void switchTOWindowByURL(String expURL) {
		Set<String> windows = driver.getWindowHandles();
		for (String multipleWindows : windows) {
			driver.switchTo().window(multipleWindows);
			String actualURL = driver.getCurrentUrl();
			System.out.println(actualURL);
			if (actualURL.equalsIgnoreCase(expURL)) {
				break;
			}
		}
	}

	public Set<String> switchWindowByURL(String expURL) {
		Set<String> windows = driver.getWindowHandles();
		for (String child : windows) {
			String actualURL = driver.getCurrentUrl();
			if (!actualURL.equalsIgnoreCase(expURL)) {
			}
			driver.switchTo().window(child);
		}
		return windows;
	}

	public String getMainWindow() {
		String window = driver.getWindowHandle();
		return window;
	}

	public void switchToMainWindow(String mainWindow) {
		driver.switchTo().window(mainWindow);
	}

	// Java Script Executor //

	public void javaScriptClick(WebElement we) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click();", we);
		System.out.println(" clicked by java script");
	}

	public void javaScriptSendKeys(WebElement we, String inputValue) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		we.clear();
		jse.executeAsyncScript("arguments[0].value='" + inputValue + "'", we);
		System.out.println("value is Inputed successfully");

	}

	public String javaScriptPageTitle() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String titleOfPage = js.executeScript("return document.title").toString();
		return titleOfPage;
	}

	public String javaScriptGetURL() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String urlOfPage = js.executeScript("return document.URL").toString();
		return urlOfPage;
	}

	public void jsSendKeyWithAttributeValue(WebElement we, String text) {

	}

	public void javaScriptRefresh() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("hisotry.go(0)");
	}

	public void scrollPageUntilElementIsFound(WebElement we) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true)", we);

	}

	public void scrollHalfOfThePageMoreORLess() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		long windowHeight = (Long) js.executeScript(
				"return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;");
		long scrollHeight = (Long) js.executeScript(
				"return Math.max( document.body.scrollHeight, document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight );");
		long halfway = (scrollHeight - windowHeight) / 2;
		js.executeScript("window.scrollTo(0, " + halfway + ");");
	}

	public void scrollPageByCo_Ordinates(int x, int y) {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.scrollTo(arguments[0], arguments[1]);", x, y);
	}

	public void scrolltilTopOfThePage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");

	}

	public void scrollPageTillPageEnd() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
	}

	public String javaScriptGetTextOnPage() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String textOnPage = js.executeScript("return document.documentElement.innerText").toString();
		return textOnPage;
	}

	public void scrollLeftAndRightHorizontalyUntillTheVisibilityOfElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean isVisible = false;
		int maxScrolls = 20; // Adjust the maximum number of scrolls as needed
		int scrolls = 0;

		while (!isVisible && scrolls < maxScrolls) {
			// Check if the element is visible
			isVisible = element.isDisplayed();

			if (!isVisible) {
				// Scroll to the right (you can adjust the value)
				js.executeScript("window.scrollBy(100, 0);"); // Change the scroll amount as needed
				scrolls++;
			}
		}

		if (isVisible) {
			System.out.println("Element is visible.");
		} else {
			System.out.println("Element is not visible after scrolling.");
		}

	}

	public void scrollRightHorizontalyUntillTheVisibilityOfElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		boolean isVisible = false;
		int maxScrolls = 20; // Adjust the maximum number of scrolls as needed
		int scrolls = 0;

		while (!isVisible && scrolls < maxScrolls) {
			// Check if the element is visible
			isVisible = element.isDisplayed();

			if (!isVisible) {
				// Scroll to the right (you can adjust the value)
				js.executeScript("window.scrollBy(-100, 0);"); // Change the scroll amount as needed
				scrolls++;
			}
		}

		if (isVisible) {
			System.out.println("Element is visible.");
		} else {
			System.out.println("Element is not visible after scrolling.");
		}

	}

	public void scrollAfterGettingThecoornatesOfWebelement(WebElement we) {
		WebElement n = driver.findElement(By.linkText("Latest Courses"));
		// obtain element x, y coordinates with getLocation method
		Point p = we.getLocation();
		int X = p.getX();
		int Y = p.getY();
		// scroll with Javascript Executor
		JavascriptExecutor j = (JavascriptExecutor) driver;
		j.executeScript("window.scrollBy(" + X + ", " + Y + ");");
	}

	// flashing elements Using Java Script //

	public void javaScriptFlash(WebElement we) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int i = 0; i <= 5; i++) {
			js.executeScript("arguments[0].style.background='green';", we);

			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			// js.executeScript("arguments[0].style.background='"+defaultColor+"';",we);

		}

	}

	// Alert //

	public void alertAccept() {
		driver.switchTo().alert().accept();

	}

	public void alertDismiss() {
		driver.switchTo().alert().dismiss();
	}

	public void alertsendKeys(String value) {
		driver.switchTo().alert().sendKeys(value);
	}

	public String alertGetStringValue() {
		String alertgetText = driver.switchTo().alert().getText();
		return alertgetText;

	}

	// I Frame //

	public void switchToFrameByIdOrFrame(String IdorName) {
		driver.switchTo().frame(IdorName);
	}

	public void switchToFrameByIndex(int frameNo) {
		driver.switchTo().frame(frameNo);
	}

	public void switchToFrameByweElement(WebElement we) {
		driver.switchTo().frame(we);
	}

	public void switchToParentFrame() {
		driver.switchTo().parentFrame();

	}

	public void switchToMainFrame() {
		driver.switchTo().defaultContent();
	}

	// validation and verification Parts //

	public void validatemessage(WebElement we) {

	}

	public boolean validateUserfromtheList(List<WebElement> we, String expected) {
		boolean status = false;
		int size = we.size();
		for (int i = 0; i < size; i++) {
			WebElement wele = we.get(i);
			String webText = wele.getText();
			System.out.println(webText);
			if (webText.equalsIgnoreCase(expected)) {
				status = true;
				System.out.println("the text is " + webText);
				break;
			} else {
				status = false;
			}

		}
		return status;

	}

	public void validateUserInTheList(List<WebElement> we, String expected) {
		boolean status = validateUserfromtheList(we, expected);

		if (status == true) {
			testLogger.log(Status.PASS, expected + "  email Id is  In the List");

		} else {
			testLogger.log(Status.FAIL, expected + "  email Id is not In the List");
		}
	}

	public String getText(WebElement we) {
		String text = we.getText();
		return text;
	}

	public void validatePageTitle(String expecetedTitle) {
		String actulTitle = driver.getTitle();
		System.out.println(actulTitle);
		if (actulTitle.equalsIgnoreCase(expecetedTitle)) {
			System.out.println(actulTitle + " Page Title is matched" + expecetedTitle);

		} else {
			System.out.println("Page Title is not matched");

		}
	}

	

	// Drop Down Handling //

	public void selectByVisibleText(WebElement web, String text) {

	}

	public String printThoseFileNameWichIsDownloadedEarlier(String path) {
		String fileName = null;
		File directory = new File(path);
		// Get the current time
		Date currentTime = new Date();
		// Calculate the time threshold (5 minutes ago)
		long fiveMinutesAgo = currentTime.getTime() - (5 * 60 * 1000);
		// Check if the directory exists
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			// Iterate through the files and check their last modification time
			for (File file : files) {
				long lastModified = file.lastModified();
				if (lastModified >= fiveMinutesAgo) {
					// File was modified within the last 5 minutes
					fileName = System.getProperty("user.dir") + "\\" + path + "\\" + file.getName();
					System.out.println("File uploaded within the last 5 minutes: " + file.getName());
					passAnyConditionalInformationUsingLable(
							"file uploaded sucessfully Where the file name is : " + file.getName());

				}

			}
		} else {
			System.err.println("Directory does not exist or is not a directory.");
		}
		return fileName;
	}

	public void verifyLastdownloadedFilewithinfiveMinutes() {
	}

	public void DeleteOldFile(String pathdirectory) {
		String reportsDirectory = pathdirectory;
		int daysToKeep = 1; // Number of days to keep reports

		// Calculate the cutoff date
		long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000);

		File directory = new File(reportsDirectory);

		// List all files in the directory
		File[] files = directory.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isFile() && file.lastModified() < cutoffTime) {
					if (file.delete()) {
						System.out.println("Deleted old report: " + file.getName());
					} else {
						System.err.println("Failed to delete file: " + file.getName());
					}
				}
			}
		}
	}

	// Some Extra Features //

	public int getHeight() {
		return 0;

	}

	public  void listenNetworkRespnse() {
		
	}
    }