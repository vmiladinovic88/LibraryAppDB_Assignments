package com.library.steps;

import com.library.pages.BasePage;
import com.library.pages.DashBoardPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class DashboardStepDefinitions {
    DashBoardPage dashBoardPage = new DashBoardPage();
    LoginPage loginPage = new LoginPage();
    String borrowedBooks;
    @Given("the {string} on the home page")
    public void the_on_the_home_page(String string) {
        loginPage.login(string);
       String actualHolderName = dashBoardPage.accountHolderName.getText().toLowerCase();
        Assert.assertTrue(actualHolderName.contains(string));
    }
    @When("the librarian gets borrowed books number")
    public void the_librarian_gets_borrowed_books_number() {
        borrowedBooks=dashBoardPage.borrowedBooksNumber.getText();
    }
    @Then("borrowed books number information must match with DB")
    public void borrowed_books_number_information_must_match_with_db() {
        DB_Util.runQuery("select count(*) from book_borrow where is_returned=0");
        String expectedBorrowedBooks = DB_Util.getFirstRowFirstColumn();
        Assert.assertEquals("Borrowed books verification failed",expectedBorrowedBooks,borrowedBooks);

    }

}
