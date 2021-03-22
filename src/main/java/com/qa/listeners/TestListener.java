package com.qa.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.Base.BaseTest;
import com.qa.Reports.ExtentReport;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class TestListener implements ITestListener {

    public void onTestFailure(ITestResult result) {
        if(result.getThrowable()!=null){
            StringWriter stringWriter=new StringWriter();
            PrintWriter printWriter=new PrintWriter(stringWriter);
            result.getThrowable().printStackTrace(printWriter);
            System.out.println(stringWriter.toString());
        }
        BaseTest baseTest=new BaseTest();
        AppiumDriver driver=baseTest.getDriver();
        File file=driver.getScreenshotAs(OutputType.FILE);
        byte[] encoded=null;
        try {
            encoded= Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,String> params;
        params=result.getTestContext().getCurrentXmlTest().getAllParameters();
        String imagePath="Screenshots"+File.separator+params.get("platformName")+"_"+params.get("deviceName")+
                File.separator+baseTest.getDateTime()+File.separator+
                result.getTestClass().getRealClass().getSimpleName()+File.separator+result.getName()+".png";

        String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath;
        System.out.println(completeImagePath);
        try {
            FileUtils.copyFile(file, new File(imagePath));
            Reporter.log("This is the sample screenshot");
            Reporter.log("<a href='"+ completeImagePath + "'> <img src='"+ completeImagePath + "' height='400' width='400'/> </a>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ExtentReport.getTest().fail("Test Failed",MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExtentReport.getTest().fail(result.getThrowable());
    }

    @Override
    public void onTestStart(ITestResult result) {
        BaseTest baseTest=new BaseTest();
        ExtentReport.startTest(result.getName(),result.getMethod().getDescription()).
                assignCategory(baseTest.getPlatform()+"_"+baseTest.getDeviceName())
        .assignAuthor("Vishnu Vardhan");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReport.getTest().log(Status.PASS,"Test Passed");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ExtentReport.getTest().log(Status.FAIL,"Test Failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReport.getTest().log(Status.SKIP,"Test skipped");
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ExtentReport.getTest().log(Status.FAIL,"Test Failed With Timeout");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReport.getExtentReports().flush();
    }
}
