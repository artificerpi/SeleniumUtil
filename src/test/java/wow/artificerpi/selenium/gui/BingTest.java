package wow.artificerpi.selenium.gui;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BingTest {

	@Test
	public void testOpenBingPage() {
		DesiredCapabilities capabilities = DesiredCapabilities.chrome(); 
		WebDriver driver = new RemoteWebDriver(capabilities);
		
		driver.get("https://bing.com");
	}
}
