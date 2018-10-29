package baseClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class BaseClassDef 
{
	public  String browser, userName, password, URL;
	public static WebDriver driver;
	public ExtentTest test;
	public ExtentReports extent;
	
	public WebDriver initializeDriver()
	{
		Properties prop = new Properties();
		FileInputStream initFile = null;
		try 
		{
			initFile = new FileInputStream("D:\\NK\\Selenium Projects\\SeleniumEasyAutomation\\src\\main\\java\\Object Repository\\EnvironmentVariables.properties");
			prop.load(initFile);
			browser = prop.getProperty("browser");
			userName = prop.getProperty("uName");
			password = prop.getProperty("password");
			URL = prop.getProperty("url");
			
			if(browser.equals("chrome"))
			{
				ChromeOptions option = new ChromeOptions();
				option.addArguments("--disable-infobars");
				option.addArguments("--start-maximized");
				driver = new ChromeDriver(option);
			}
			else if(browser.equals("firefox"))
			{
				driver = new FirefoxDriver();
			}
			else if(browser.equals("IE"))
			{
				driver = new InternetExplorerDriver();
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}
	
	public static String getScreenshot(String errorName) throws IOException
	{
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		File img = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String destination = "D:\\NK\\Selenium Projects\\SeleniumEasyAutomation\\screenshots\\";
		String fileName = "D:\\NK\\Selenium Projects\\SeleniumEasyAutomation\\screenshots\\"+errorName+"-"+date+"-error.png";
		FileUtils.copyFile(img, new File(fileName));
		return destination;
	}
	
	public void launchBrowser(WebDriver driver, String url)
	{
		driver.get(url);
	}
	
	public void setExtentReport()
	{
		extent = new ExtentReports("D:\\NK\\Selenium Projects\\SeleniumEasyAutomation\\reports\\"+"ExtentReportsTestNG.html", true);
	}
	
	public ExtentTest setExtentTest(String testmethod, String testcase)
	{
		//extent = new ExtentReports("D:\\NK\\Selenium Projects\\SeleniumEasyAutomation\\reports\\"+"ExtentReportsTestNG.html", true);
		test=extent.startTest(testmethod);
		test.log(LogStatus.INFO, testcase, "Test Case name");
		return test;
	}
	
	public String getTestname(Method method)
	{
		/*ITestNGMethod testNGMethod = result.getMethod();
    	Method method = testNGMethod.getConstructorOrMethod().getMethod();*/
    	Test testAnnotation = (Test) method.getAnnotation(Test.class);
    	if (testAnnotation != null)
    		return testAnnotation.testName();
    	else
    		return "";
	}
	
	@AfterSuite
	public void reportClose()
	{
		driver.quit();
		extent.close();
	}
}
