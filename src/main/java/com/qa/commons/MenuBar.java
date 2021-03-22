package com.qa.commons;

import com.qa.Base.BaseTest;
import com.qa.Pages.SettingsPage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class MenuBar extends BaseTest {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Menu\"]")
    private MobileElement hamburger;


    public SettingsPage clickonHamburger(){
        click(hamburger);
        return new SettingsPage();
    }
}
