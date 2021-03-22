package com.qa.Pages;

import com.qa.commons.MenuBar;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class ProductDetailsPage extends MenuBar {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/XCUIElementTypeStaticText[1]")
    private MobileElement product_title;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/XCUIElementTypeStaticText[2]")
    private MobileElement product_description;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/following-sibling::android.widget.TextView[@content-desc=\"test-Price\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/following-sibling::XCUIElementTypeOther[contains(@name,\"$\")]")
    private MobileElement product_price;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-BACK TO PRODUCTS\"]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"test-BACK TO PRODUCTS\"]")
    private MobileElement backtoPP_button;

    public String getProductTitle(){return getText(product_title);}

    public String getProductDescription(){return getText(product_description);}

    public String getProductPrice(){return  getText(product_price);}

    public ProductDetailsPage scrollToPrice(){
        scrollToElement();
        return this;
    }

    public ProductsPage goToProductsPage(){
        click(backtoPP_button);
        return new ProductsPage();
    }
}
