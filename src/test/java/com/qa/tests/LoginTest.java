package com.qa.tests;

import com.qa.Base.BaseTest;
import com.qa.Pages.LoginPage;
import com.qa.Pages.ProductsPage;
import com.qa.Pages.SettingsPage;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsPage settingsPage;
    JSONObject users;
    @BeforeMethod
    public void initialize_loginpage(Method method){
        utils.log().info(method.getName());
        loginPage=new LoginPage();

    }
    @BeforeClass
    public void data()  {
        InputStream data = null;
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
    public void successful_login(){
        SoftAssert softAssert=new SoftAssert();
        loginPage.sendUserName(users.getJSONObject("ValidUser").getString("userName"));
        loginPage.sendPassword(users.getJSONObject("ValidUser").getString("password"));
        utils.log().info("sent user name and password");
        productsPage=loginPage.clickLogin();
        utils.log().info("clicked on login");
        softAssert.assertEquals(productsPage.getTitle(),getMap().get("product_title"));
        settingsPage=productsPage.clickonHamburger();
        utils.log().info("clicked on hamburger");
        settingsPage.clickOnLogout();
        utils.log().info("clicked on logout");
        softAssert.assertAll();
    }
    @Test
    public void unsuccessful_login_wrong_username(){
        SoftAssert softAssert=new SoftAssert();
        loginPage.sendUserName(users.getJSONObject("WrongUserName").getString("userName"));
        loginPage.sendPassword(users.getJSONObject("WrongUserName").getString("password"));
        utils.log().info("sent user name and password");
        productsPage=loginPage.clickLogin();
        utils.log().info("clicked on login");
        utils.log().info("got error text...");
        softAssert.assertEquals(loginPage.getErrortext(),getMap().get("Errortext"));
        softAssert.assertAll();
    }
    @Test
    public void unsuccessful_login_wrong_password(){
        SoftAssert softAssert=new SoftAssert();
        loginPage.sendUserName(users.getJSONObject("WrongPassword").getString("userName"));
        loginPage.sendPassword(users.getJSONObject("WrongPassword").getString("password"));
        utils.log().info("sent user name and password");
        productsPage=loginPage.clickLogin();
        utils.log().info("clicked on login");
        utils.log().info("got error text...");
        softAssert.assertEquals(loginPage.getErrortext(),getMap().get("Errortext"));
        softAssert.assertAll();
    }
    @Test
    public void unsuccessful_login_wrong_password_username(){
        SoftAssert softAssert=new SoftAssert();
        loginPage.sendUserName(users.getJSONObject("WrongUserNameandWrongPassword").getString("userName"));
        loginPage.sendPassword(users.getJSONObject("WrongUserNameandWrongPassword").getString("password"));
        utils.log().info("sent user name and password");
        productsPage=loginPage.clickLogin();
        utils.log().info("clicked on login");
        utils.log().info("got error text...");
        softAssert.assertEquals(loginPage.getErrortext(),getMap().get("Errortext"));
        softAssert.assertAll();
    }
}
