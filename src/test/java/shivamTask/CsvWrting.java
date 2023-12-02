package shivamTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CsvWrting {
	public String[][] listOftable() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://app.diggrowth.com");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//button[text()='Single sign-on (SSO)']")).click();
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys("demo.digg@diggrowth.com");
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("Diggrowth@123");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//div[text()='Insights']")).click();
		driver.findElement(By.xpath("//div[text()='Performance Report']")).click();
		driver.findElement(By.xpath("//input[@type='checkbox']")).click();
		
		//div[@id='analyticTrendGraph']
		
		
		
		
		
		List<WebElement> jobName = driver.findElements(By.xpath("//div[@class='job-link']"));
		List<WebElement> batchId = driver.findElements(By.xpath("//div[@class='grid-col mdl job-id']"));
		List<WebElement> batcInputData = driver.findElements(By.xpath("//div[@class='grid-col mds job-name']"));
		String[][] tableData = new String[jobName.size()][3];

		for (int i = 0; i < jobName.size(); i++) {

			String jobNameText = jobName.get(i).getText();
			String batchIdText = batchId.get(i).getText();
			String batchInputDataText = batcInputData.get(i).getText();
			tableData[i] = new String[] { jobNameText, batchInputDataText, batchIdText };
		}
		return tableData;
	}

	public static void writeTableToCSV(String[][] tableData, String filePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)))) {
			// Writing table data to the CSV file
			for (String[] rowData : tableData) {
				writer.write(String.join(",", rowData));
				writer.newLine();
			}
			System.out.println("Table data has been written to " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test public void writeDataIntoCsv() { 
		// Create and store the table data 
			String[][] tableData = listOftable(); 
			// File path where you want to save the CSV file 
			String filePath = "table_data_WithVariable.csv"; 
			// Write table data to CSV file
			writeTableToCSV(tableData, filePath); 


}
}
