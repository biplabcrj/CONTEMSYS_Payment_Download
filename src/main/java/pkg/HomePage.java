package pkg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class HomePage {
	WebDriver driver = null;
	
	public HomePage(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public void clickPublicIssuer(WebDriver driver)
	{
		Actions actions = new Actions(driver);
		String taxInvoiceXpath = "//a[contains(text(),'Tax Invoice')]";
		String recipientXpath ="//a[contains(text(),'Recipient / Issuer')]";
		actions.moveToElement(driver.findElement(By.xpath(taxInvoiceXpath))).click(driver.findElement(By.xpath(recipientXpath))).build().perform();
		
	}
	public void clickPayOut(WebDriver driver)
	{
		Actions actions = new Actions(driver);
		String BankMISXpath = "//a[contains(text(),'Bank MIS')]";
		String PayOutXpath ="//a[contains(text(),'Pay Out')]";
		actions.moveToElement(driver.findElement(By.xpath(BankMISXpath))).click(driver.findElement(By.xpath(PayOutXpath))).build().perform();
		
	}
}
