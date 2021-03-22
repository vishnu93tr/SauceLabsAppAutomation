package com.qa.tests;

import com.qa.Base.BaseTest;
import com.qa.Pages.LoginPage;
import com.qa.Pages.ProductDetailsPage;
import com.qa.Pages.ProductsPage;
import com.qa.Pages.SettingsPage;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductsTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    ProductDetailsPage productDetailsPage;
    JSONObject users;

    @BeforeMethod
    public void initialize_loginpage(Method method){
        utils.log().info(method.getName());
        loginPage=new LoginPage();
        productsPage=loginPage.login(users.getJSONObject("ValidUser").getString("userName"),
                users.getJSONObject("ValidUser").getString("password"));
    }
    @AfterMethod
    public void logout(){
        utils.log().info("logout common before method");
        settingsPage=productsPage.clickonHamburger();
        loginPage=settingsPage.clickOnLogout();
    }
    @BeforeClass
    public void data()  {
        InputStream data=null;
        try {
            String filename = "data/LoginUsers.json";
            data = getClass().getClassLoader().getResourceAsStream(filename);
            JSONTokener jsonTokener = new JSONTokener(data);
            users = new JSONObject(jsonTokener);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        finally {
            try {
                if(data!=null) data.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void Validate_products(){
        SoftAssert softAssert=new SoftAssert();
        utils.log().info("Asserting product name,product price from PP");
        softAssert.assertEquals(productsPage.getProductName(),getMap().get("productPageTitle"));
        softAssert.assertEquals(productsPage.getProductPrice(),getMap().get("productPagePrice"));
        softAssert.assertAll();
    }
    @Test(priority = 1)
    public void Validate_productDetails(){
        SoftAssert softAssert=new SoftAssert();

        productDetailsPage=productsPage.taponProduct();
        utils.log().info("Tapped on product");
        String productTitle=productDetailsPage.getProductTitle();
        utils.log().info("got product title: "+productTitle);
        String productDescription=productDetailsPage.getProductDescription();
        utils.log().info("got product description: "+productDescription);
        String productPrice=productDetailsPage.scrollToPrice().getProductPrice();
        utils.log().info("scrolled and got product price"+productPrice);
        productsPage=productDetailsPage.goToProductsPage();
        utils.log().info("navigated back to products page");
        utils.log().info("Assertions...");
        softAssert.assertEquals(productsPage.getProductName(),productTitle);
        softAssert.assertEquals(productDescription,getMap().get("productPageDescription"));
        softAssert.assertTrue(productPrice.contains(productsPage.getProductPrice()));
        softAssert.assertAll();
    }
}
