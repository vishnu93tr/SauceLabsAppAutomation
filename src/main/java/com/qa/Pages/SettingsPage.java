package com.qa.Pages;

import com.qa.Base.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class SettingsPage extends BaseTest {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-LOGOUT\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-LOGOUT\"]")
    private MobileElement logout;

    public LoginPage clickOnLogout(){
        click(logout);
        return new LoginPage();
    }
}
