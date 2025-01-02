package day25;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class Deepl{

    public static void main(String[] args) {
        // Step 1: Set up WebDriver
       WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 2: redirect to  the website of elpais
            driver.get("https://elpais.com/");
            driver.manage().window().maximize();   // maximize  the window

            // Accepting cookies
            WebElement aB = driver.findElement(By.id("didomi-notice-agree-button"));
            if (aB.isEnabled()) {
          	    aB.click();
            }

            // Step 3: Click on the Opinion section
          
                driver.findElement(By.linkText("Opinión")).click(); 
            

            // Step 4: Fetch the first five articles
            List<WebElement> articles = driver.findElements(By.cssSelector("article"));
            if (articles.size() < 5) {
                System.out.println("No articles on the page.");
                return; 
            }
            List<String> titles = new ArrayList<>();
            List<String> contentofarticle = new ArrayList<>();

            for (int i = 0; i < 5; i++) {  
                WebElement article = articles.get(i);  //got current article
                String title = article.findElement(By.cssSelector("h2")).getText();    
                titles.add(title); //save title inthe list

                // Open article link
                WebElement link = article.findElement(By.tagName("a"));
                String articleLink = link.getAttribute("href");
                driver.get(articleLink);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("p"))); // Wait for paragraphs to load

                // Fetch content
               
               List<WebElement> para = driver.findElements(By.tagName("p"));
                String content = "";
                for (WebElement p : para) {
                    content += p.getText() + " ";
                }
                contentofarticle.add(content);

                // save the image
                try {
                    WebElement image = driver.findElement(By.tagName("img"));
                    String url = image.getAttribute("src");
                    saveImage(url, "article_" + (i + 1) + ".jpg");
                } catch (Exception e) {
                    System.out.println("No image found for article " + (i + 1));
                }

               
                driver.navigate().back();
                driver.findElement(By.linkText("Opinión")).click();  // Wait for Opinion section to reload
                articles = driver.findElements(By.cssSelector("article")); // Re-fetch the articles
            }

            // Step 5: Printing  titles in Spanish
            System.out.println("Spanish Titles:-  ");
            for (String title : titles) {
                System.out.println(title);
            }

            // Step 6: Translate titles to English 
            List<String> translatedTitles = new ArrayList<>();
            for (String title : titles) {
                translatedTitles.add("Translated: " + title);
            }

            // Print the translated titles
            System.out.println("\nTranslated Titles in English:");
            for (String translatedTitle : translatedTitles) {
                System.out.println(translatedTitle);
            }

            // Step 7: Find repeated words in translated titles
            Map<String, Integer> wordCounts = new HashMap<>();
            for (String translatedTitle : translatedTitles) {
                String[] words = translatedTitle.toLowerCase().split("\\s+");
                for (String word : words) {
                    wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                }
            }

            // Printing the repeated words more than twice
            System.out.println("\nRepeated Words (more than twice):");
            for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                if (entry.getValue() > 2) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }


    public static void saveImage(String imageUrl, String fileName) {
        try (InputStream in = new URL(imageUrl).openStream();
             FileOutputStream out = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("Image saved: " + fileName);
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getMessage());
        }
    }
}

