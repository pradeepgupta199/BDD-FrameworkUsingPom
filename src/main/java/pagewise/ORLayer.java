package pagewise;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ORLayer {
@FindBy(xpath = "//input[@id='username']")
protected WebElement userNamefield;

@FindBy(xpath = "//span[@data-cy='closeModal']")
protected WebElement closeBtn;

@FindBy(xpath = "//span[text()='Continue']//parent::button")
protected WebElement continueBtn;
	

@FindBy(xpath = "//nav//li[@class='menu_Flights']")
protected WebElement flight_Btns;

@FindBy(xpath = "//label//input[@id='fromCity']")
protected WebElement fromCity;

@FindBy(xpath = "//input[@placeholder='From']")
protected WebElement enterTextInfromTextBox;


@FindBy(xpath = "//li/div//p[text()='London - City Airport, United Kingdom']")
protected WebElement clickOnLondonCityAirport;
//London - City Airport

@FindBy(xpath = "//label//input[@id='toCity']")
protected WebElement tocity;

@FindBy(xpath = "//input[@placeholder='To']")
protected WebElement enterTextInToField;


@FindBy(xpath = "//li/div//p[text()='Dubai, United Arab Emirates']")
protected WebElement clickOnDubai;








}
