package pkg;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PaymentPage {
	WebDriver driver = null;
	
	public PaymentPage(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public void selectDate(String dateType, String exDay, String exMonth, String exYear) throws InterruptedException
	{
		if(dateType.contains("from"))
		{
			//WebDriverWait wait=new WebDriverWait(driver, 20);
			//wait.until(ExpectedConditions.elementToBeClickable(By.id("dateFrom")));
			JavascriptExecutor j = (JavascriptExecutor)driver;
			//boolean status = j.executeScript("return document.readyState").toString().equals("complete");
			for(int i = 0; i<10; i++)
			{
				if(j.executeScript("return document.readyState").toString().equals("complete"))
				{
					break;
				}
				Thread.sleep(2000);
			}
			try {
				driver.findElement(By.id("dateFrom")).click();
			}
			catch (Exception e) {
				driver.navigate().refresh();
				Thread.sleep(30000);
				driver.findElement(By.id("dateFrom")).click();
				Thread.sleep(3000);
			}
		}
		else
		{
			driver.findElement(By.id("dateTo")).click();
		}
		String monthYear = driver.findElement(By.className("datepicker-switch")).getText();
		while(!getMonthYear(monthYear)[0].equals(exMonth) && getMonthYear(monthYear)[1].equals(exYear))
		{
			if(dateType.contains("from"))
			{
				driver.findElement(By.className("prev")).click();
				Thread.sleep(2000);
			}
			else
			{
				driver.findElement(By.className("next")).click();
			}
			
			monthYear = driver.findElement(By.className("datepicker-switch")).getText();
		}
		driver.findElement(By.xpath("//td[text()='"+exDay+"']")).click();
	}
	
	public String[] getMonthYear(String monthYear)
	{
		return monthYear.split(" ");
	}
	
	public void clickonSearchBtn()
	{
		driver.findElement(By.id("searchBtn")).click();
		
	}
	
	public boolean isDataPresent()
	{
		int size = driver.findElements(By.xpath("//tbody/tr[1]/td")).size();
		if(size>1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void downloadPay() throws InterruptedException
	{
		WebDriverWait wait=new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("dwnldExcel")));
		Thread.sleep(3000);
		driver.findElement(By.id("dwnldExcel")).click();
	}

}
