package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.DashBoardPage;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class BooksStepDefinitions {
    DashBoardPage dashBoardPage = new DashBoardPage();
    BookPage bookPage = new BookPage();
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

}
