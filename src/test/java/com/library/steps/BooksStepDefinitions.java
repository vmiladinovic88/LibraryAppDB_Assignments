package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.DashBoardPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.security.KeyFactorySpi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksStepDefinitions {
    DashBoardPage dashBoardPage = new DashBoardPage();
    BookPage bookPage = new BookPage();
    String someBook ="";
    List<String> uiInfo = new ArrayList<>();
    @When("the user navigates to {string} page")
    public void the_user_navigates_to_page(String string) {
        dashBoardPage.navigateModule(string);
    }
    @When("the user clicks book categories")
    public void the_user_clicks_book_categories() {
        bookPage.mainCategoryElement.click();

    }
    @Then("verify book categories must match book_categories table from db")
    public void verify_book_categories_must_match_book_categories_table_from_db() {
        Select select = new Select(bookPage.mainCategoryElement);

        List <WebElement> listDropdown = select.getOptions();
        List <String> actualDropdown = new ArrayList<>();
        for (WebElement each : listDropdown) {
            actualDropdown.add(each.getText());
            if(each.getText().equals(select.getFirstSelectedOption().getText())){
                actualDropdown.remove(each.getText());
            }
        }
        DB_Util.runQuery("select name from book_categories");
        List<String> expectedDropdown = DB_Util.getColumnDataAsList(1);
        Assert.assertEquals(expectedDropdown,actualDropdown);
    }

    @When("the user searches for {string} book")
    public void the_user_searches_for_book(String string) {
        bookPage.search.sendKeys(string+ Keys.ENTER);
        BrowserUtil.waitFor(5);
        this.someBook=string;
    }
    @When("the user clicks edit book button")
    public void the_user_clicks_edit_book_button() {
        bookPage.editBook(someBook).click();
        uiInfo.add(bookPage.bookName.getAttribute("value"));
        uiInfo.add(bookPage.isbn.getAttribute("value"));
        uiInfo.add(bookPage.year.getAttribute("value"));
        uiInfo.add(bookPage.author.getAttribute("value"));

    }
    @Then("book information must match the Database")
    public void book_information_must_match_the_database() {
        DB_Util.runQuery("select name,isbn,year,author from books where name='"+someBook+"'");
       List<String> rowDbData = DB_Util.getRowDataAsList(1);
       Assert.assertEquals(rowDbData,uiInfo);
    }


}
