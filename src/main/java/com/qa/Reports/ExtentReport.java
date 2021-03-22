package com.qa.Reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.HashMap;
import java.util.Map;

public class ExtentReport {
    static ExtentReports extentReports;
    final static String filePath="Extent.html";
    static Map<Integer, ExtentTest> extentTestMap = new HashMap();
    public synchronized static ExtentReports getExtentReports(){
        if(extentReports==null){
            ExtentHtmlReporter extentHtmlReporter=new ExtentHtmlReporter(filePath);
            extentHtmlReporter.config().setDocumentTitle("Sauce Labs App Automation -Appium");
            extentHtmlReporter.config().setReportName("Sauce Labs App automation");
            extentHtmlReporter.config().setTheme(Theme.DARK);
            extentReports=new ExtentReports();
            extentReports.attachReporter(extentHtmlReporter);
        }
    return extentReports;
    }
    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = getExtentReports().createTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }
}