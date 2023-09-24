package practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class FlyAway {
	ChromeOptions co;
	WebDriver driver;

	@Test(priority=1)
	public void newRegistrationTest() throws ClassNotFoundException, SQLException {

		co = new ChromeOptions();
		co.setBrowserVersion("116");
		driver = new ChromeDriver(co);
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/FlyAway/home");
		// click on sign up
		driver.findElement(By.xpath("//a[@href='login']")).click();
		// click on not a member sign up
		driver.findElement(By.xpath("//a[@href='signup']")).click();
		// send email id
		driver.findElement(By.xpath("//*[@name='email_id']")).sendKeys("user01@user01.com");
		// send password
		driver.findElement(By.xpath("//*[@name='pwd']")).sendKeys("user01");
		// send confirm password
		driver.findElement(By.xpath("//*[@name='pwd2']")).sendKeys("user01");
		// send name
		driver.findElement(By.xpath("//*[@name='name']")).sendKeys("user01");
		// send address
		driver.findElement(By.xpath("//*[@name='address']")).sendKeys("user01");
		// send city name
		driver.findElement(By.xpath("//*[@name='city']")).sendKeys("user01");
		// click on sign up
		driver.findElement(By.xpath("//button[text()='Signup']")).click();
		// validating successful registration on UI
		WebElement signup = driver.findElement(By.xpath("//a[@href='login'][2]"));
		String acutal = signup.getText();
		System.out.println(acutal);
		Assert.assertEquals(acutal, "Login to continue checking flights");

		// Retreving entry details from DB
		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/flyaway", "root", "");

		Statement stmt = con.createStatement();

		ResultSet result = stmt.executeQuery("select * from f_user where name='user01'");

		while (result.next()) {
			String actual = result.getString("name");
			System.out.println(actual);
			Assert.assertEquals(actual, "user01");
		}

		con.close();

		
	}

	@Test(priority=2)
	public void SignInTest() {
		co = new ChromeOptions();
		co.setBrowserVersion("116");
		driver = new ChromeDriver(co);
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/FlyAway/home");

		// click on sign in link
		driver.findElement(By.xpath("//a[@href='login']")).click();
		// send username input
		driver.findElement(By.xpath("//input[@name='email_id']")).sendKeys("user01@user01.com");
		// send passwrod input
		driver.findElement(By.xpath("//input[@name='pwd']")).sendKeys("user01");
		// click on login button to login
		driver.findElement(By.xpath("//button[text()='Login']")).click();
		// validate logout button is display
		driver.findElement(By.xpath("//a[@href='logout']")).isDisplayed();
		

	}

	@Test(priority=3)
	public void searchFlightTest() throws ClassNotFoundException, SQLException {
		co = new ChromeOptions();
		co.setBrowserVersion("116");
		driver = new ChromeDriver(co);
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/FlyAway/home");
		// click on sign in link
		driver.findElement(By.xpath("//a[@href='login']")).click();
		// send username input
		driver.findElement(By.xpath("//input[@name='email_id']")).sendKeys("user01@user01.com");
		// send passwrod input
		driver.findElement(By.xpath("//input[@name='pwd']")).sendKeys("user01");
		// click on login button to login
		driver.findElement(By.xpath("//button[text()='Login']")).click();
		// click on home button
		driver.findElement(By.xpath("//a[@href='home']")).click();
		// search flights from and to from dropdown
		WebElement from = driver.findElement(By.xpath("//select[@name='source']"));
		Select fromstates = new Select(from);
		fromstates.selectByVisibleText("Bangalore");

		WebElement to = driver.findElement(By.xpath("//select[@name='destination']"));
		Select tostates = new Select(to);
		tostates.selectByVisibleText("Hyderabad");

		// click on submit button
		driver.findElement(By.xpath("//button[text()='Submit']")).click();

		// search for book ticket
		WebElement id = driver.findElement(By.xpath("//a[@href='bookflight?id=7']"));
		// validate book flight ID UI and DB
		System.out.println(id.isDisplayed());
		String s = id.getAttribute("href");
		System.out.println(s);

		String[] url_id = s.split("\\=");
		System.out.println(url_id[1]);

		// validating book flight id on UI with DB

		Class.forName("com.mysql.jdbc.Driver");

		Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/flyaway", "root", "");

		Statement stmt = con.createStatement();

		ResultSet result = stmt.executeQuery("select ID from f_flights where source=1 and destination =11;");
		System.out.println(result);

		while (result.next()) {
			String actual = result.getString("id");
			System.out.println(actual);
			Assert.assertEquals(actual, url_id[1]);
		}
		
        
	}

	@Test(priority=4)
	public void BookFlightTest() {
		SignInTest();
		//click on home button
		driver.findElement(By.xpath("//a[@href='home']")).click();
		//click on book flight from bangalore to hyderabad
		driver.findElement(By.xpath("//a[@href='bookflight?id=7']")).click();
		//validate title and possible text
		String actualTitle=driver.getTitle();
		Assert.assertEquals(actualTitle, "Fly Away - Make Flight Booking");
		driver.close();
	}
	
	@Test(priority=5)
	public void CompleteBookingTest() {
		SignInTest();
		driver.findElement(By.xpath("//a[@href='home']")).click();
		//click on book flight from bangalore to hyderabad
		driver.findElement(By.xpath("//a[@href='bookflight?id=7']")).click();
		//click to continue booking
		driver.findElement(By.xpath("//*[text()='Click to complete booking']")).click();
		//validate title and text
		String actualTitle=driver.getTitle();
		Assert.assertEquals(actualTitle, "Fly Away - Booking Confirmation");
		driver.findElement(By.xpath("//a[text()='See Your Bookings']")).isDisplayed();
		driver.quit();
		
	}

}
