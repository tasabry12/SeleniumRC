

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;

public class FlightSearch extends SeleneseTestCase{
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://www.orbitz.com/");
		selenium.start();
	}

	@Test
	public void testFlightSearch() throws Exception {
		selenium.open("/");
		selenium.click("id=search.type.air");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("name=ar.rt.leaveSlice.orig.key")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		selenium.click("css=div.button.needsclick");
		selenium.type("name=ar.rt.leaveSlice.orig.key", "nyc");
		selenium.type("name=ar.rt.leaveSlice.dest.key", "sfo");
		selenium.type("name=ar.rt.leaveSlice.date", "07/18/15");
		selenium.type("name=ar.rt.returnSlice.date", "07/20/15");
		selenium.click("name=search");
		selenium.waitForPageToLoad("30000");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("css=div.matchingResults > h2")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		verifyTrue(selenium.isTextPresent("Matching Results"));
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isElementPresent("xpath=//span[@class='price']")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

		String price = selenium.getText("xpath=//span[@class='price']");
		System.out.println("The lowest price is " + price);
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
