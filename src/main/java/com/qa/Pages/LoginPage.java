package com.qa.Pages;

import com.qa.Base.BaseTest;
import com.qa.commons.MenuBar;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;


public class LoginPage extends BaseTest {

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(id="test-Username")
    private MobileElement userNameTextField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(id = "test-Password")
    private MobileElement passwordTextField;

    @AndroidFindBy(xpath = "//android.widget.TextView")
    @iOSXCUITFindBy(id = "test-LOGIN")
    private MobileElement login_button;
    
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Error message\"]")
    private MobileElement error_message;


    public LoginPage sendUserName(String userName){
        sendKeys(userNameTextField,userName);
        return this;
    }
    public LoginPage sendPassword(String password){
        sendKeys(passwordTextField,password);
        return this;
    }
    public String getErrortext(){
        return getText(error_message);
    }
    public ProductsPage clickLogin(){
        click(login_button);
        return new ProductsPage();
    }
    public ProductsPage login(String userName,String password){
        sendUserName(userName);
        sendPassword(password);
       return clickLogin();
    }
}
