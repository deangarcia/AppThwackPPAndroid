package driver;

import fileReaders.JsonRead;
import fileReaders.PropertiesReader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.selendroid.SelendroidDriver;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import objects.MVPD;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AndroidLaunch {
	@BeforeClass
	public static void androidAppthwackServer() throws Throwable 
	{
		JsonRead.readJsonFromFile("/Users/deang/Documents/workspace/AppThwackPilotProject/resources/read.json");
		Properties properties = PropertiesReader.getPropertiesFile("capabilities");
		String dn = properties.getProperty("deviceName");
		String pv = properties.getProperty("platformVersion");
		String an = properties.getProperty("automationName");
		String appId = properties.getProperty("app");
		String key = properties.getProperty("apiKey");
		String pid = properties.getProperty("project");
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", dn); // name of the device as listed on the appthwack api
		capabilities.setCapability("platformVersion", pv); // os of the device
		capabilities.setCapability("automationName", an); 
		capabilities.setCapability("app", appId); // change this for every apk or ipa that you upload to appthwack
		capabilities.setCapability("apiKey", key); // this is a unique key tied to a specific user
		capabilities.setCapability("project", pid); // project key is shown in the project setting in your account
		driver = new AndroidDriver(new URL("https://appthwack.com/wd/hub"), capabilities);
		
		// AppthWack 
		// capabilities.setCapability("deviceName", "Apple iPhone 5"); // name of the device as listed on the appthwack api
		// capabilities.setCapability("platformVersion", "7.0"); // os of the device
		// capabilities.setCapability("automationName", "appium"); 
		// capabilities.setCapability("app", "206621"); // change this for every apk or ipa that you upload to appthwack
		// capabilities.setCapability("apiKey", "dbd3ee8964e351efc4017617921094461944a118"); // this is a unique key tied to a specific user
		// capabilities.setCapability("project", "40241"); // project key is shown in the project setting in your account
		
	}
	
	public static void androidAppiumServer() throws Throwable 
	{
		File calsspathRoot = new File(System.getProperty("user.dir")); 
		File appDir = new File(calsspathRoot, "Application");
		File app = new File(appDir, "FSGOAndroid.apk");
		
		JsonRead.readJsonFromFile("/Users/deang/Documents/workspace/AppThwackPilotProject/resources/read.json");
		Properties properties = PropertiesReader.getPropertiesFile("capabilities");
		String pn = properties.getProperty("platformName");
		String pv = properties.getProperty("platformVersion");
		String dv = properties.getProperty("deviceName");
		//String ap = properties.getProperty("appPackage");
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, pn);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, pv);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "f51e35ea");
		//capabilities.setCapability("automationName", "Selendroid");
		//apabilities.setCapability("appPackage", "com.foxsports.videogo");
		//capabilities.setCapability("appWaitPackage", "com.foxsports.videogo");
		//capabilities.setCapability("appActivity", "com.foxsports.videogo.DelegateActivity");
	    //capabilities.setCapability("appWaitActivity", "com.foxsports.videogo.MainActivity");
		//capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, ap);//appkey
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());//file id
		//drive = new SelendroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	}

	@AfterClass
	public static void tearDown() throws Exception 
	{ 	
		//
		driver.quit();
	}
	
	public WebElement waitForElementResourceId(String xpath,int waitTime) {
		wait = new WebDriverWait(driver, waitTime);
		WebElement element = wait
				.until(ExpectedConditions.elementToBeClickable(By
						.xpath(xpath)));
		return element;
		
	}
	
	public WebElement waitForElementName(String name,int waitTime) {
		
		//
		wait = new WebDriverWait(driver, waitTime);
		WebElement element = wait
				.until(ExpectedConditions.elementToBeClickable(By
						.name(name)));
		return element;
	}
	public static void tap(WebElement element) {
		//
		((AppiumDriver) driver).tap(1, element, 1);
	}

	public static void click(WebElement element) {
		element.click();
	}
	
	public void takescreenshots() throws InterruptedException {
		
		driver = (AndroidDriver) new Augmenter().augment(driver);
		Thread.sleep(9000);
		// Get the screenshot
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		System.out.println("Screenshot completed");
		try{

			File calsspathRoot = new File(System.getProperty("user.dir")); 
			//workspace space is set in application folder
			
			File testScreenShot = new File(calsspathRoot + "screenShots", "preRollAds");
			// Copy the file to screenshot folder
			FileUtils.copyFile(scrFile, testScreenShot);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static AppiumDriver driver;
	public static RemoteWebDriver drive;
	public static WebDriverWait wait;
	public static ArrayList<MVPD>mvpd = new ArrayList<MVPD>();
	
	// This is one of the initial layers of the app, on ios 5 build it has dimension dimensions are
	// not on the ios 4 though if all builds of the apps initial layer had dimensions then it would have to 
	// be fit for the screen and we could use that to make our swipe methods dynamic
}
