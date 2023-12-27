package data.twitterScraper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TwitterCrawler {

	private WebDriver driver;
    private final String USER_AGENT_PERSON = "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    private final String BASE_URL = "https://twitter.com/";
    private final String TAGS = "NFTs";

    public TwitterCrawler() {
    	// replace by path to your chromedriver.exe in your computer
        System.setProperty("webdriver.chrome.driver",
                "D:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments(USER_AGENT_PERSON);
        driver = new ChromeDriver(options);
    }

    public void login(String username, String password) {
        try {
            driver.get(BASE_URL + "search?q=" + TAGS);

            Thread.sleep(4000);

            WebElement usernameInput = driver.findElement(By.name("text"));
            usernameInput.sendKeys(username);
            WebElement nextButton = driver.findElement(By.xpath("//span[text()='Next']"));
            nextButton.click();

            Thread.sleep(3000);

            WebElement passwordInput = driver.findElement(By.name("password"));
            passwordInput.sendKeys(password);

            Thread.sleep(3000);

            WebElement loginButton = driver.findElement(By.xpath("//span[text()='Log in']"));
            loginButton.click();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  private void scrollDown(WebDriver driver) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
  }

  public void getElements() {
    List<TwitterModel> twitterList = new ArrayList<TwitterModel>();
    int targetElementCount = 100;
    int currentElementCount = 0;
    int scrollCount = 0;
    int idx = 0;
    int elementSize = 100;
    WebDriverWait wait = new WebDriverWait(driver, 120);
    
    // Use GsonBuilder to create a pretty-printing Gson object
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    try (Writer writer = new FileWriter(JsonURL.TWITTER)) {
      writer.write('[');

      while (currentElementCount < targetElementCount) {
        try {
          Thread.sleep(4000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        idx = 0;

        while (idx < elementSize) {

          wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(
              "div.css-1rynq56.r-8akbws.r-krxsd3.r-dnmrzs.r-1udh08x.r-bcqeeo.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-bnwqim")));

          List<WebElement> tweetElements = driver.findElements(By.cssSelector(
                  "div.css-1rynq56.r-8akbws.r-krxsd3.r-dnmrzs.r-1udh08x.r-bcqeeo.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-bnwqim"));

          elementSize = tweetElements.size();
          System.out.println("count: " + elementSize);

          WebElement element = driver.findElements(By.cssSelector(
              "div.css-1rynq56.r-8akbws.r-krxsd3.r-dnmrzs.r-1udh08x.r-bcqeeo.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-bnwqim"))
              .get(idx);

          String currentClasses = element.getAttribute("class");

          currentClasses = currentClasses
              .replace("css-1qaijid", "")
              .replace("r-bcqeeo", "")
              .replace("r-qvutc0", "")
              .replace("r-poiln3", "")
              .replace("r-b88u0q", "")
              .replace("r-1loqt21", "");

          JavascriptExecutor js = (JavascriptExecutor) driver;
          js.executeScript("arguments[0].setAttribute('class', arguments[1]);", element, currentClasses);

          wait.until(ExpectedConditions.elementToBeClickable(element));
          element.click();

          String author = "";
          try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(
                    "div.css-1rynq56.r-dnmrzs.r-1udh08x.r-3s2u2q.r-bcqeeo.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-18u37iz.r-1wvb978")));
                    List<WebElement> authorDivs = driver.findElements(
                      By.cssSelector(
                    "div.css-1rynq56.r-dnmrzs.r-1udh08x.r-3s2u2q.r-bcqeeo.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-18u37iz.r-1wvb978"));
              
                  if (!authorDivs.isEmpty()) {
                      WebElement firstAuthorDiv = authorDivs.get(0);
              
                      List<WebElement> authorSpans = firstAuthorDiv.findElements(
                          By.cssSelector("span.css-1qaijid.r-bcqeeo.r-qvutc0.r-poiln3"));
              
                      if (!authorSpans.isEmpty()) {
                          author = authorSpans.get(0).getText();
                          System.out.println("author: " + author);
                      }
                  } else{
                    System.out.println("NOT FOUND");
                  }
          } finally{

          }
          final String[] descHolder = new String[1]; // Mảng chứa desc
          // desc
          String desc = "";
          try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.css-1rynq56.r-bcqeeo.r-qvutc0.r-37j5jr.r-1inkyih.r-16dba41.r-bnwqim.r-135wba7")));

            List<WebElement> descElements = driver.findElements(
                By.cssSelector("div.css-1rynq56.r-bcqeeo.r-qvutc0.r-37j5jr.r-1inkyih.r-16dba41.r-bnwqim.r-135wba7"));
            StringBuilder descBuilder = new StringBuilder();
            for (WebElement descElement : descElements) {
              String descTemp = descElement.getText();
              descBuilder.append(descTemp);
            }
            desc = descBuilder.toString();
            descHolder[0] = desc;
            System.out.println("desc: " + desc);
          } catch (Exception e) {
            System.out.println("Desc ERROR!");
          }

          List<String> relatedTags = new ArrayList<>();
          String date = "";

          try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("a.css-1qaijid.r-bcqeeo.r-qvutc0.r-poiln3.r-1loqt21")));
            List<WebElement> tagElements = driver
                .findElements(By.cssSelector("a.css-1qaijid.r-bcqeeo.r-qvutc0.r-poiln3.r-1loqt21"));

            if (tagElements != null && !tagElements.isEmpty()) {
              for (WebElement tagElement : tagElements) {
                String tag = tagElement.getText();
                if (tag.startsWith("#")) {
                  if (!relatedTags.contains(tag)) {
                    relatedTags.add(tag);
                  }
                }
              }
            }
          } finally {
            WebElement dateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("time")));
            date = dateElement.getAttribute("datetime");
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]X");
            LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = dateTime.format(outputFormatter);
          }
          try {
            
        	  TwitterModel model = new TwitterModel(desc, author, date, relatedTags);

        	// Use Gson to write the JSON data with pretty printing
              if (twitterList.stream().noneMatch(existingModel -> existingModel.getContent().equals(descHolder[0]))) {
                  twitterList.add(model);
                  String jsonOutput = gson.toJson(model);

        	        // Write the reformatted JSON to the file
        	        writer.write(jsonOutput);

            }
            
          } finally {
            driver.navigate().back();
            WebElement elementToRemove = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.css-175oi2r.r-aqfbo4.r-gtdqiz.r-1gn8etr.r-1g40b8q")));

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].remove();", elementToRemove);

            wait.until(ExpectedConditions.invisibilityOf(elementToRemove));
            try {
              Thread.sleep(10000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector(
                    "div.css-1rynq56.r-8akbws.r-krxsd3.r-dnmrzs.r-1udh08x.r-bcqeeo.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-bnwqim")));
            tweetElements = driver.findElements(
                By.cssSelector(
                    "div.css-1rynq56.r-8akbws.r-krxsd3.r-dnmrzs.r-1udh08x.r-bcqeeo.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-bnwqim"));
            elementSize = tweetElements.size();
          }
          idx++;
          currentElementCount++;
          if (currentElementCount < targetElementCount)
            writer.write(",");
          writer.write("\n");
        }
        try {
          Thread.sleep(4000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        scrollDown(driver);
        scrollCount++;
        if (scrollCount > 100) {
          break;
        }
      }
      writer.write(']');
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
      TwitterCrawler crawler = new TwitterCrawler();

      // Provide your Twitter username and password directly
      String username = "your username";
      String password = "your password";

      crawler.login(username, password);
      crawler.getElements();
  }
}
