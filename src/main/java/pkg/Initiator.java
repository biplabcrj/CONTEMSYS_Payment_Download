package pkg;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Initiator {

	private static final String LOG_FILE = "log4j.properties";

	public static void main(String[] args) throws Exception {
		
		WebDriver driver = Driver.launchDriver();
		Logger logger = Logger.getLogger(Initiator.class);
		Properties properties = new Properties();
		properties.load(new FileInputStream(LOG_FILE));
		PropertyConfigurator.configure(properties);

		Utility objExcelFile = new Utility();
		String filePath = System.getProperty("user.dir") + "\\resources";
		List<Map<String, String>> data = objExcelFile.readExcel(filePath, "testData.xlsx", "data");

		for (Map<String, String> rowData : data) {
			
			LoginPage loginPage = new LoginPage(driver);
			loginPage.selectRole(rowData.get("Role"));
			loginPage.selectUserCode(rowData.get("UserCode"));
			logger.info("======User:" + rowData.get("UserCode") + "===========");
			loginPage.selectPassword(rowData.get("Password"));
			//loginPage.selectAuctionCenter(rowData.get("AuctionCenter"));
			loginPage.clickLogin();
			String error = loginPage.isLoginSuccess();
			if (error != null) {
				logger.error(error);
				Driver.driverTearDown(driver);
				continue;
			}
			logger.info("login successful");
			HomePage homePage = new HomePage(driver);
			homePage.clickPayOut(driver);
			
			Select TeaBoardRegNo = new Select(driver.findElement(By.xpath("//td[./span[contains(text(),'TeaBoard Reg No:')]]/following-sibling::td/select")));
		    List <WebElement> op = TeaBoardRegNo.getOptions();
		    int size = op.size();
		    
		    for(int i=1; i < size; i++)
		    {
		    	Select TeaBoardRegNo1 = new Select(driver.findElement(By.xpath("//td[./span[contains(text(),'TeaBoard Reg No:')]]/following-sibling::td/select")));
		    	List <WebElement> opNew = TeaBoardRegNo1.getOptions();
		    	String tbrn = opNew.get(i).getText();
		    	TeaBoardRegNo1.selectByIndex(i);
		    	Thread.sleep(3000);
		    	Select AuctionCenters = new Select(driver.findElement(By.xpath("//td[./span[contains(text(),'Auction Centers :')]]/following-sibling::td/select")));
			    List <WebElement> op1 = AuctionCenters.getOptions();
			    int size1 = op1.size();
			    
			    for(int j=0; j < size1; j++)
			    {
			    	//driver.navigate().refresh();
			    	Select AuctionCenters1 = new Select(driver.findElement(By.xpath("//td[./span[contains(text(),'Auction Centers :')]]/following-sibling::td/select")));
			    	List <WebElement> op1New = AuctionCenters1.getOptions();
			    	String ac = op1New.get(j).getText();
			    	AuctionCenters1.selectByIndex(j);
			    	Thread.sleep(3000);
			    	driver.findElement(By.xpath("//input[@type='submit']")).click();
			    	//Thread.sleep(5000);
			    	
			    	WebDriverWait wt = new WebDriverWait(driver,120);
			    	WebElement pageWait = driver.findElement(By.xpath("//h4[text()='Please wait...']"));
			    	wt.until(ExpectedConditions.invisibilityOf(pageWait));
			    	
			    	PaymentPage pp = new PaymentPage(driver);
			    	
			    	pp.selectBothDate(rowData.get("StartDate"), rowData.get("EndDate"));
			    	
			    	//pp.selectDate("fromdate", "1", "August", "2022");
			    	//pp.selectDate("todate", "15", "August", "2022");
			    	
			    	
			    	pp.clickonSearchBtn();
			    	
			    	WebElement pageWait1 = driver.findElement(By.xpath("//h4[text()='Please wait...']"));
			    	wt.until(ExpectedConditions.invisibilityOf(pageWait1));
			    	//Thread.sleep(5000);
			    	
			    	if(pp.isDataPresent())
			    	{
			    		pp.downloadPay();
			    		System.out.println("Data Found for - TeaBoard Reg No: "+tbrn+" and Auction Centers: "+ac);
			    		Thread.sleep(5000);
			    	}
			    	else
			    	{
			    		System.out.println("No Data Found for - TeaBoard Reg No: "+tbrn+" and Auction Centers: "+ac);
			    	}
			    	driver.navigate().back();
			    	Thread.sleep(3000);
			    }
		    }
			
			Thread.sleep(3000);
			Driver.driverTearDown(driver);
		}
		logger.info("All tasks completed");
		JFrame jFrame = new JFrame();
        JOptionPane.showMessageDialog(jFrame, "All tasks completed");
	}

}
