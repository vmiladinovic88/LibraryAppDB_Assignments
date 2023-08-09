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
    String actualMostPopular= "";
    String newBook = "";
    String newIsbn ="";
    String newYear="";
    String newAuthor="";
    String newCategory ="";
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

    @When("I execute query to find most popular book genre")
    public void i_execute_query_to_find_most_popular_book_genre() {
        DB_Util.runQuery("select bc.name,count(*) from book_borrow bb inner join books b on bb.book_id= b.id inner join book_categories bc on b.book_category_id=bc.id group by name order by 2 desc");
        actualMostPopular = DB_Util.getFirstRowFirstColumn();

    }
    @Then("verify {string} is the most popular book genre.")
    public void verify_is_the_most_popular_book_genre(String string) {
        Assert.assertEquals(string,actualMostPopular);
    }

    @When("the librarian click to add book")
    public void the_librarian_click_to_add_book() {
       bookPage.addBook.click();
    }
    @When("the librarian enter book name {string}")
    public void the_librarian_enter_book_name(String string) {
        bookPage.bookName.sendKeys(string);
        this.newBook=string;
    }
    @When("the librarian enter ISBN {string}")
    public void the_librarian_enter_isbn(String string) {
       bookPage.isbn.sendKeys(string);
       this.newIsbn=string;
    }
    @When("the librarian enter year {string}")
    public void the_librarian_enter_year(String string) {
        bookPage.year.sendKeys(string);
        this.newYear=string;
    }
    @When("the librarian enter author {string}")
    public void the_librarian_enter_author(String string) {
        bookPage.author.sendKeys(string);
        this.newAuthor=string;
    }
    @When("the librarian choose the book category {string}")
    public void the_librarian_choose_the_book_category(String string) {
        Select select = new Select(bookPage.categoryDropdown);
        select.selectByVisibleText(string);
        this.newCategory=string;
    }
    @When("the librarian click to save changes")
    public void the_librarian_click_to_save_changes() {
        bookPage.saveChanges.click();
    }
    @Then("verify {string} message is displayed")
    public void verify_message_is_displayed(String string) {
        Assert.assertTrue(bookPage.toastMessage.isDisplayed());
    }
    @Then("verify {string} information must match with DB")
    public void verify_information_must_match_with_db(String string) {
        DB_Util.runQuery("select isbn,name,author from books where name = '"+string+"' order by id desc");
        String expectedBookField= DB_Util.getFirstRowFirstColumn();
        Map<String,String> actualRowMap = new HashMap<>();
        actualRowMap.put("Book Name",newBook);
        actualRowMap.put("ISBN",newIsbn);
        actualRowMap.put("Author",newAuthor);
        actualRowMap.put("Book Category",newCategory);
        Assert.assertTrue(actualRowMap.containsValue(string));
    }




}
