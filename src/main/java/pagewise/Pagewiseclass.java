package pagewise;

import org.openqa.selenium.support.PageFactory;
import utilityMethods.Utility;

public class Pagewiseclass extends ORLayer {
	private Utility webUtility;

	public Pagewiseclass(Utility webUtility) {
		this.webUtility = webUtility;
		PageFactory.initElements(webUtility.getDriver(), this);
	}

	public void enterEmailOrMobileNumber() {
		webUtility.sendKey(userNamefield, "6307904972", "Mobile Number");
	}

	public void clickOnContinueBtn() {
		webUtility.click(continueBtn, "continue Btn");

	}

	public void clickOncloseBtn() {
		webUtility.click(closeBtn, "close Btn");
	}
	
	public void clickOnflightBtn() {
		webUtility.click(flight_Btns, "flight Btn");
	}

	public void selectFromCity() {
		webUtility.explicityWait(fromCity, 20);
		webUtility.click(fromCity, "from city");
		webUtility.sendKey(enterTextInfromTextBox, "London City Arp", "from city");
		webUtility.explicityWait(clickOnLondonCityAirport, 20);	
		webUtility.click(clickOnLondonCityAirport, "london Airport");
	}

	public void selectoCity() {
		webUtility.explicityWait(tocity, 20);
		webUtility.click(tocity, "to city");
		webUtility.sendKey(enterTextInToField, "Dubai Intl", "from city");
		webUtility.explicityWait(clickOnDubai, 20);
		webUtility.click(clickOnDubai, "Dubai Intl");

	}

}
