package com.qa.Base;


import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class BaseTest {
    protected static ThreadLocal<AppiumDriver> driver=new ThreadLocal<>();
    protected static ThreadLocal<Properties> properties=new ThreadLocal<>();
    protected static ThreadLocal<String> platform=new ThreadLocal<>();
    protected static ThreadLocal<Map<String,String>> map=new ThreadLocal<>();
    protected static ThreadLocal<String> dateTime=new ThreadLocal<>();
    protected static ThreadLocal<String> deviceName=new ThreadLocal<>();
    AppiumDriverLocalService appiumDriverLocalService;
    public TestUtils utils;
    protected static Logger logger= LogManager.getLogger(BaseTest.class.getName());
    public AppiumDriver getDriver(){return driver.get();}
    public void setDriver(AppiumDriver appiumDriver){
        driver.set(appiumDriver);
    }
    public Properties getProperty(){
        return properties.get();
    }
    public void setPropery(Properties propery){
        properties.set(propery);
    }
    public String getPlatform(){
        return platform.get();
    }
    public void setPlatform(String platform1){
        platform.set(platform1);
    }
    public Map<String,String> getMap(){
        return map.get();
    }
    public void setMap(Map<String,String> map1){
        map.set(map1);
    }
    public String getDateTime(){
        return dateTime.get();
    }
    public void setDateTime(String dateTime1){
        dateTime.set(dateTime1);
    }
    public String getDeviceName(){
        return deviceName.get();
    }
    public void setDeviceName(String deviceName1){
        deviceName.set(deviceName1);
    }
    public BaseTest(){
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()),this);
    }

    @BeforeMethod
    public void startScreenRecording(){
        utils.log().info("started screen recording");
        try {
            ((CanRecordScreen) getDriver()).startRecordingScreen();
        }
        catch (Exception e){
            e.printStackTrace();
            utils.log().fatal("unable to start diver instance");
        }
    }
    @AfterMethod
    public synchronized void stopScreenRecording(ITestResult result) {
        String media=((CanRecordScreen)getDriver()).stopRecordingScreen();
        if(result.getStatus()==2) {
            Map<String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
            String dir = "Videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName") +
                    File.separator + getDateTime() + File.separator +
                    result.getTestClass().getRealClass().getSimpleName();
            File videoDir = new File(dir);
            if (!videoDir.exists()) videoDir.mkdirs();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
                fileOutputStream.write(Base64.getDecoder().decode(media));
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @BeforeSuite
    public void startAppiumServer() {
        ThreadContext.put("ROUTINGKEY","ServerLogs");
        appiumDriverLocalService=getAppiumService();
        if(!appiumDriverLocalService.isRunning()){
            appiumDriverLocalService.start();
            appiumDriverLocalService.clearOutPutStreams();
        }
        else{
            utils.log().info("server is already running");
        }

    }
    @AfterSuite
    public void stopAppiumServer(){
        appiumDriverLocalService.stop();
    }
    public AppiumDriverLocalService getAppiumService(){
        return AppiumDriverLocalService.buildService(
                new AppiumServiceBuilder().
                        withArgument(GeneralServerFlag.SESSION_OVERRIDE).
                        withLogFile(new File("ServerLogs/server.log")
                        ));
    }
    @Parameters({"platformName","deviceName","UDID",
    "systemPort","chromeDriverPort",
    "wdaLocalPort","webkitDebugProxyPort"})
    @BeforeTest
    public void desired_capabilities(String platformName,String deviceName,String UDID,
                                     @Optional("androidOnly") String systemPort,
                                     @Optional("androidOnly") String chromeDriverPort,
                                     @Optional("iOSOnly") String wdaLocalPort,
                                     @Optional("iOSOnly")String webkitDebugProxyPort)  {

        InputStream inputStream,stringsis;
        Properties properties;
        AppiumDriver driver;
        URL url;
        String logFile="logs"+File.separator+platformName+"_"+deviceName;
        File logs=new File(logFile);
        if(!logs.exists()) logs.mkdirs();
        ThreadContext.put("ROUTINGKEY",logFile);
        try {
            utils=new TestUtils();
            setDateTime(utils.getDateTime());
            setPlatform(platformName);
            setDeviceName(deviceName);
            properties=new Properties();
            String properties_fileName="config.properties";
            String xmlFileName = "strings/strings.xml";
            inputStream=BaseTest.class.getClassLoader().getResourceAsStream(properties_fileName);
            properties.load(inputStream);
            setPropery(properties);
            DesiredCapabilities dc = new DesiredCapabilities();
            dc.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            dc.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            stringsis=getClass().getClassLoader().getResourceAsStream(xmlFileName);
            setMap(utils.parseXML(stringsis));
            url = new URL(properties.getProperty("appiumURL")+":4723/wd/hub");
            switch (platformName){
                case "Android":
                    dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, properties.getProperty("android.automationName"));
                    String app_path=getClass().getResource(properties.getProperty("android.appLocation")).getFile();
                    dc.setCapability("app",app_path);
                    dc.setCapability("appPackage", properties.getProperty("android.appPackage"));
                    dc.setCapability("appActivity",properties.getProperty("android.appActivity"));
                    dc.setCapability("avd",UDID);
                    dc.setCapability("avdLaunchTimeout",Integer.parseInt(properties.getProperty("android.emulator.avdLaunchTimeout")));
                    dc.setCapability("systemPort",systemPort);
                    dc.setCapability("chromeDriverPort",chromeDriverPort);
                    driver = new AndroidDriver(url, dc);
                    utils.log().info("got desired capabilities for:"+platformName);
                    break;
                case "iOS":
                    dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, properties.getProperty("iOS.automationName"));
                    String ios_appurl=getClass().getResource(properties.getProperty("iOS.appLocation")).getFile();
                    System.out.println(ios_appurl);
                    dc.setCapability("app",ios_appurl);
                    dc.setCapability(MobileCapabilityType.UDID,UDID);
                    dc.setCapability("wdaLocalPort",wdaLocalPort);
                    dc.setCapability("webkitDebugProxyPort",webkitDebugProxyPort);
                    driver = new IOSDriver(url, dc);
                    utils.log().info("got desired capabilities for:"+platformName);
                    break;
                default:
                    utils.log().fatal("invalid platoform name"+platformName);
                    throw new Exception("invalid platform name"+platformName);
            }
            setDriver(driver);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void waitForVisibility(MobileElement element){
        WebDriverWait wait=new WebDriverWait(getDriver(), TestUtils.WAIT);
        wait.until(driver->element.isDisplayed());
    }
    public void click(MobileElement element){
        waitForVisibility(element);
        element.click();
    }
    public String getAttribut(MobileElement element,String attribute){
        waitForVisibility(element);
        return element.getAttribute(attribute);
    }
    public String getText(MobileElement element){

        String text=getPlatform().equalsIgnoreCase("Android")?getAttribut(element,"text"):getAttribut(element,"label");
        return text;
    }
    public void sendKeys(MobileElement element,String text){
        waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    @AfterTest
    public void quit(){
        getDriver().quit();
    }
    public void scrollToElement(){
        switch (getPlatform()){
            case "Android":
                getDriver().findElement(MobileBy.AndroidUIAutomator(
                        "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
                                + "new UiSelector().description(\"test-Price\"));"));
                break;
            case "iOS":
                MobileElement webElement=  (MobileElement) getDriver().findElementByClassName("XCUIElementTypeScrollView");
                String element_Id=webElement.getId();
                HashMap<String, String> scrollObject = new HashMap<String, String>();
                scrollObject.put("element", element_Id);
                scrollObject.put("direction","down");
                getDriver().executeScript("mobile:scroll", scrollObject);
                break;
            default:
                break;
        }
    }

}
