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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

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
		    	TeaBoardRegNo1.selectByIndex(i);
		    	Thread.sleep(3000);
		    	Select AuctionCenters = new Select(driver.findElement(By.xpath("//td[./span[contains(text(),'Auction Centers :')]]/following-sibling::td/select")));
			    List <WebElement> op1 = AuctionCenters.getOptions();
			    int size1 = op1.size();
			    
			    for(int j=0; j < size1; j++)
			    {
			    	//driver.navigate().refresh();
			    	Select AuctionCenters1 = new Select(driver.findElement(By.xpath("//td[./span[contains(text(),'Auction Centers :')]]/following-sibling::td/select")));
			    	AuctionCenters1.selectByIndex(j);
			    	Thread.sleep(3000);
			    	driver.findElement(By.xpath("//input[@type='submit']")).click();
			    	Thread.sleep(5000);
			    	
			    	PaymentPage pp = new PaymentPage(driver);
			    	pp.selectDate("fromdate", "1", "August", "2022");
			    	pp.selectDate("todate", "15", "August", "2022");
			    	
			    	pp.clickonSearchBtn();
			    	
			    	if(pp.isDataPresent())
			    	{
			    		pp.downloadPay();
			    		Thread.sleep(5000);
			    	}
			    	else
			    	{
			    		System.out.println("data not present");
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
