package com.qa.Pages;

import com.qa.Base.BaseTest;
import com.qa.commons.MenuBar;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

import javax.xml.xpath.XPath;

public class ProductsPage extends MenuBar {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart drop zone\"]/android.view.ViewGroup/android.widget.TextView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name=\"PRODUCTS\"]")
    private MobileElement products_text;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"test-Item title\"])[1]")
    private MobileElement first_product_title;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
    @iOSXCUITFindBy(xpath = "(//XCUIElementTypeStaticText[@name=\"test-Price\"])[1]")
    private MobileElement first_product_price;

    public String getTitle(){
       return getText(products_text);
    }

    public String getProductName(){
        return getText(first_product_title);
    }

    public String getProductPrice(){
        return getText(first_product_price);
    }

    public ProductDetailsPage taponProduct(){
        click(first_product_title);
        return new ProductDetailsPage();
    }
}
