# Assignment 01 - Spam Detector (Secondary Instructions)

> MEMBERS: Zainulabuddin Syed, Ashwin Gnanaseelan, Harsh Patel, Taha Rana

> Course: CSCI 2020U: Software Systems Development and Integration

## Project information:
The project involves building a spam detector using a dataset of emails to train the
program to recognize whether new emails are spam or not. The program will use unigram
approach to count each word's frequency and determine if an email is spam or not.
First the user has a large collection of emails to train the system. It uses the "train"
dataset to figure out the probability that an email is spam. Then we use the probability to test
the "test" files.  Finally, we produce the results in a table format with the accuracy and precision.
The picture below shows the analysis of all the test files with the spam probability.
(You might have to zoom out to get the proper styling)
![](img/image1.png)
The picture belows shows the team members that worked on this project.
![](img/image2.png)

## Improvements
* Creation of  HashSet to store the common words (the,and,at,or,a) since most exchange in the english language requires these words so any analysis done on them is
  unnecessary  and in our opinion won't affect the likelihood of spam or not spam.
* Utilizing exclamation marks in the regex. We chose to consider ! part of our words, as it is common knowledge that spam usually contains `!`
  therefore utilizing this should make our models more accurate.
* Setting the threshold to 0.66. We noticed that our precision improved significantly when raising the threshold of spam, and the accuracy stayed the same.
  Therefore, we raised the threshold to match, and provide a more accurate modeling.
## UI Improvements
* Added a Model Analysis section on the Dashboard to show how many files were analyzed for the spam detector to show the user if the sample size is large enough
* Added an improved Nav Section with Icons and another page. Also added images in the nav, an image below the chart for aesthetics, as well as the OTU logo. 
* ReadMe page added to explain process of developing spam detector with elgorithms used, equations used, efficiency of the program, and conclusions. Showing the overall though process, learning objectives, and conclusions draw.

## Brainstormed Improvements to be made
* Improvements to be made to the algorithm is better analysis of non letter characters, with numbers, and other symbols, as that is a known common attribute of spam.
* Additionally giving more weight to certain words such as "Free", "Deal",and "Limited", as these are often associated with spam emails.
* Finally analysis of email lengths could also be done, to determine if there is a correlation between email size such as word count or character count.

## How to run (Instructions)
* Git clone the repo
* Open the folder through intellij
* Navigate to Edit configuration and add Glassfish Local
* Go to deployment -> Artifact -> war exploded
* Go back to the server and change URL by adding api/spam
* Set the server domain to 'domain1'
* Apply the changes and click OK.
* Then go to the `SpamResourse.java` file and run it through the glass fish server
* Wait for it to load and then run the index.html file



##  Resources
#### SpamDetector.java Libraries
```
* import com.spamdetector.domain.TestFile; 
* import com.spamdetector.util.SpamDetector;
* import jakarta.ws.rs.GET;
* import jakarta.ws.rs.Path;
* import jakarta.ws.rs.Produces;
* import java.io.File;
* import java.io.IOException;
* import java.net.URISyntaxException;
* import java.net.URL;
* import java.util.List;
* import java.util.Map;
* import jakarta.ws.rs.core.Response;
* import com.fasterxml.jackson.databind.ObjectMapper;
```
