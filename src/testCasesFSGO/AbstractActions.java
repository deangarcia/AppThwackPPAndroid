package testCasesFSGO;

import java.util.List;
import objects.Accounts;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import driver.AndroidLaunch;

public class AbstractActions extends AndroidLaunch {
	
	private void pressButton(String name){
		while (!(ele_.isDisplayed())) {
			driver.tap(1, 290, 210, 1);
			tap(waitForElementName(name,15));	
			break;
		}
	}
	
	public void loginAccountSettings (String accountName) {
		
		/*
		 * Resource ID's still need to be found for all the elements on Android
		 */
		
		// setting button
		tap(waitForElementName("SETTINGS BUTTON",60));
		
		// sign in
		tap (waitForElementResourceId("SIGN IN FROM SETTINGS MENU",60));
		
		//sleep is to wait for the screen to load before swiping still need to 
		//implement a better way of waiting for this besides a sleep
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Still working on trying to make these x and y attributes dynamic ideas:
		 * 1. Have these values hard coded into the same property files as the capabilities 
		 *    file then read in from there
		 * 2. At the start of the program find the element which is at the lowest level of the 
		 *    XML tree (The background image of the app) and get the dimensions for that element
		 */
		
		// The swipe here is not necessary for larger phones so how can this action be made dynamic?
		//driver.swipe(225,500,225,250,3000);
		driver.swipe(290, 420, 290, 234, 3000);
		
		//more providers
		tap(waitForElementResourceId("CLICK THE MORE PROVIDERS OPTION",60));
	
		// Scroll to works when invisible elements are present in the DOM
		// I noticed that elements with y coordinates are considered visible 
		// When using this scroll to method. This was for the IPhone not sure 
		// If it will work the same on Android if it does not then the same swiping
		// technique used for the videos should be used 
		driver.scrollTo(accountName);
		tap(waitForElementName(accountName,60));
	}
	
	public boolean login(Accounts account) {
		
		/*
		 * Resource ID's still need to be found for all the elements on Android
		 */
		
		//username
		WebElement element = waitForElementResourceId("USERNAME FIELD",60);
		click(element);
		element.sendKeys(account.getUsername());
		
		//password
		element = waitForElementResourceId("PASSWORD FIELD",60);
		click(element);
		element.sendKeys(account.getPassword());
		
		//done
		tap(waitForElementResourceId("CLICK DONE ON KEYBOARD",60));
		
		//click Sign-In	
		tap(waitForElementResourceId("SIGN BUTTON",60));
		
		try{
			  Thread.sleep(1000);
			  //driver.swipe(225,500,225,250,3000);
			  driver.swipe(290, 420, 290, 234, 3000);
			  waitForElementResourceId("CHECK TO SEE IF THE SIGN IN BUTTON IS STILL AVAILABLE",15).isEnabled();
		} catch (Exception e) {
			return true;
		}
		
		return false;	
	}
	
	/*
    Same code as above but different approach in vid is the length of a list that contains elements that have 
    The value ONNOW in them this means the amount of playable videos and the code manually scrolls through that 
    Many times since each video container is 186 pixels from top to bottom swipe down that far then click in the middle 
    Of that container to play the video. Was considering this approach for Android because unlike IOS resourceIds for 
    videos were not being displayed in the DOM before scrolling. So the idea is to scroll and play videos up until a element 
    appears that has a name share which is one of the options that pops up when a video is not on now. If that element is not 
    found throw the elementNotFound exception.
    
    problems occur with random ads that are placed in between videos like banners need to find a way to detect these ads
    
    public static void playVideo(int vid) throws NoSuchElementException, TimeoutException {
	
	for(int i = 0; i <= vid; i++)
	{
		// If we are on the first video we do not want to swipe at all
		if(i != 0)
			driver.swipe(290, 420, 290, 234, 3000);
	}
	driver.tap(1, 195, 290, 1);
	// Swipe to get more providers
}
*/
	
	public void playVideos() throws InterruptedException {
		/* Check to see if the resourceId layout is the same as xPath in structure if it is
		 * then use same logic and change xPaths to resourceIds otherwise change
		 */
		String xpath="";
		Thread.sleep(5000);
		int i=1;
		List<WebElement> labels= driver.findElementsByXPath("//UIAApplication[1]/UIAWindow[1]/"
				+ "UIACollectionView[2]/UIACollectionCell[1]/UIACollectionView[1]/UIACollectionCell");
		System.out.println("Size:" + labels.size());
		
		while(i>0 && i<= labels.size()) {
			Thread.sleep(5000);
			System.out.println("Iteration:" + i);
			xpath = video_xpath+"["+i+"]"+"/UIAStaticText[1]";
			System.out.println("XPATH: " +xpath);
			WebElement ele = driver.findElementByXPath(xpath);
			String name = ele.getText();
			
			if(!name.equalsIgnoreCase("ON NOW")) {
				break;
			
			}
			//xpath = video_xpath+"["+i+"]"+"/UIAStaticText[3]";
			//name = driver.findElementByXPath(xpath).getText();
			tap(ele);
			takescreenshots();
			Thread.sleep(9000);
			//pressButton("btn play");
			pressButton("iPhone video backarrow");	
			Thread.sleep(3000);	
			System.out.println("SWIPE");
			int j=0;
			while(j<i){
				//driver.swipe(225,440,225,250,1000);
				driver.swipe(290, 420, 290, 234, 1000);
				j++;
			}
			i++;
		}
	}
	
	public void logout(){
		tap(waitForElementResourceId("SETTINGS BUTTON TOP RIGHT OF APP",60));
		tap(waitForElementResourceId("SIGN OUT OPTION IN THE SETTINGS DROP DOWN MENU",60));
		tap(waitForElementName("OK",60));
	}
	
	// Path to the collection cell which contains a list of video containers 
	private String video_xpath = "//UIAApplication[1]/UIAWindow[1]/UIACollectionView[2]/"
			+ "UIACollectionCell[1]/UIACollectionView[1]/UIACollectionCell";
	
	// When video is playing and the control options are available then this element should be seen
	private WebElement ele_ = driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIAStaticText[1]"));
}
