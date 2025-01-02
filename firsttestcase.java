package Day21;

import org.openqa.selenium.safari.SafariDriver;

public class firsttestcase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "/Users/poojakuldeeprajpurohit/Desktop/Myworkspace Automation/selenium-java-4.27.0");

		
		SafariDriver driver = new SafariDriver();
		driver.get("https://www.facebook.com");

	
	}

}
