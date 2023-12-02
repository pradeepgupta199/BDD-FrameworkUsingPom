package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pagewise.Pagewiseclass;
import utilityMethods.Utility;

public class Stepdefinition {
private 	Utility webUtil;
private Pagewiseclass pg;
	
	@Given("I visit https:\\/\\/www.makemytrip.com\\/ website")
	public void i_visit_https_www_makemytrip_com_website() {
		webUtil=new Utility("C:\\Users\\Dell\\eclipse-workspace-NEWprojects\\BDDBasedFramework\\resource\\config.properties");
		webUtil.launchBrowser("chrome");
		webUtil.implicityWait(10);
		webUtil.maximizeWindow();
		webUtil.openURL("https://www.makemytrip.com");
	    pg=new Pagewiseclass(webUtil);
	
	}

	@Given("I do a signup to login into the  account")
	public void i_do_a_signup_to_login_into_the_account() {
	pg.enterEmailOrMobileNumber();
	pg.clickOncloseBtn();
	//pg.clickOnContinueBtn();
	}

	@Given("I click FLIGHTS")
	public void i_click_flights() {
	pg.clickOnflightBtn();
	}

	@Given("I select from London City Arpt")
	public void i_select_from_london_city_arpt() {
      pg.selectFromCity();
	}

	@Given("I select to Dubai Intl")
	public void i_select_to_dubai_intl_arpt() {
		pg.selectoCity();
	}

	@Given("I select Return Trip")
	public void i_select_return_trip() {
	}

	@Given("I select {int} Adult")
	public void i_select_adult(Integer int1) {
	}

	@Given("I select {int} Child and I select the Premium economy class")
	public void i_select_child_and_i_select_the_premium_economy_class(Integer int1) {
	}

	@When("I click SEARCH button")
	public void i_click_search_button() {
	}

	@When("I filter by the following flight carrier")
	public void i_filter_by_the_following_flight_carrier(io.cucumber.datatable.DataTable dataTable) {
	    // Write code here that turns the phrase above into concrete actions
	    // For automatic transformation, change DataTable to one of
	    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
	    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
	    // Double, Byte, Short, Long, BigInteger or BigDecimal.
	    //
	    // For other transformations you can register a DataTableType.

	}

	@When("I click on BOOK NOW with the cheapest price")
	public void i_click_on_book_now_with_the_cheapest_price() {
	}

	@Then("I am taken to booking page")
	public void i_am_taken_to_booking_page() {
	}
}
