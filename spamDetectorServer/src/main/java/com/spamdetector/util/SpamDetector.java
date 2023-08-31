package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;


/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) throws IOException {
//        TODO: main method of loading the directories and files, training and testing the model

        File hamDir = new File(mainDirectory, "/train/ham");     // creates a hamDir that goes to train/ham0
        File spamDir = new File(mainDirectory, "/train/spam");    // creates a spamDir that goes to train/spam

        int numHamFiles = hamDir.listFiles().length;                // sets numHamFile to the length of hamDir
        int numSpamFiles = spamDir.listFiles().length;              // sets numHamFile to the length of spamDir


        Map<String, Integer> trainHamFreq = wordFrequencyDir(hamDir);       // creates a trainHamFreq that takes the word frequency of hamDir
        Map<String, Integer> trainSpamFreq = wordFrequencyDir(spamDir);     // creates a trainSpamFreq that takes the word frequency of spamDir


        // iterates through the trainHamFreq to determine the Pr(W|H)
        // Pr(W|H)
        Map<String, Double> PrWH = new TreeMap<>();
        Set<String> hamKeys = trainHamFreq.keySet();
        Iterator<String> hamKeyIterator = hamKeys.iterator();
        while(hamKeyIterator.hasNext()){
            String word = hamKeyIterator.next();
            int freqHam = trainHamFreq.get(word);
            double pr = (double) freqHam/numHamFiles;
            PrWH.put(word, pr);
        }

        // iterates through the trainSpamFreq to determine the Pr(W|S)
        // Pr(W|S)
        Map<String, Double> PrWS = new TreeMap<>();
        Set<String> spamKeys = trainSpamFreq.keySet();
        Iterator<String> spamKeyIterator = spamKeys.iterator();
        while(spamKeyIterator.hasNext()){
            String word = spamKeyIterator.next();
            int freqSpam = trainSpamFreq.get(word);
            double pr = (double) freqSpam/numSpamFiles;
            PrWS.put(word, pr);
        }

        // iterates through the Pr(W|S) and calculates the Pr(S|W)
        // Pr(S|W)
        Map<String, Double> PrSW = new TreeMap<>();
        Set<String> PrWSKeys = PrWS.keySet();
        Iterator<String> prWSIterator = PrWSKeys.iterator();
        while(prWSIterator.hasNext()) {
            String word = prWSIterator.next();
            double prWS = PrWS.get(word);
            double prWH = PrWH.containsKey(word) ? PrWH.get(word) : 0.0;
            double pr = prWS / (prWS + prWH);
            PrSW.put(word, pr);
        }


//------------------------------------------------ TEST -------------------------------------------------------
        File testHamDir = new File(mainDirectory, "/test/ham");         // creates a testHamDir that goes to test/ham
        File testSpamDir = new File(mainDirectory, "/test/spam");       // creates a testSpamDir that goes to test/spam

        File[] testHam = testHamDir.listFiles();
        File[] testSpam = testSpamDir.listFiles();

        List<TestFile> testFiles = new ArrayList<>();

        // runs through each file in testHam and calculates Pr(S|F) for the ham files
        for (File file : testHam) {
            double sumHam = 0.0;
            Map<String, Boolean> wordMap = countWordFile(file);

            Set<String> keys = wordMap.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while (keyIterator.hasNext()) {
                String word = keyIterator.next();

                if (PrSW.containsKey(word)) {
                    double prSW = PrSW.get(word);
                    sumHam += Math.log(1 - prSW) - Math.log(prSW);
                }
            }

            double PrSF = 1/(1+Math.pow(Math.E, sumHam));

            String actualClass = "Ham";

            TestFile testFileHam = new TestFile(file.getName(), PrSF, actualClass);
            testFiles.add(testFileHam);
        }

        // runs through each file in testSpam and calculates Pr(S|F) for the spam files
        for (File file : testSpam){
            double sumSpam = 0.0;
            Map<String, Boolean> wordMap = countWordFile(file);

            Set<String> keys = wordMap.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while (keyIterator.hasNext()) {
                String word = keyIterator.next();

                if (PrSW.containsKey(word)) {
                    double prSW = PrSW.get(word);
                    sumSpam += Math.log(1 - prSW) - Math.log(prSW);
                }
            }

            double PrSF = 1/(1+Math.pow(Math.E, sumSpam));

            String actualClass = "Spam";

            TestFile testFileSpam = new TestFile(file.getName(), PrSF, actualClass);
            testFiles.add(testFileSpam);
        }

        return testFiles;
    }
    //accuracy method to return accuracy of program
    public Map<String, Double> accuracy(List<TestFile> testFiles){
        int numTruePositives = 0;
        int numTrueNegatives = 0;
        int numFiles = 0;
        //iterate through testfiles
        for(TestFile test : testFiles) {
            if (test.getActualClass() == "Spam") {
                //utilizing 0.66 for more accurate results
                if (test.getSpamProbability() > 0.66) {
                    //increment true positives
                    numTruePositives = numTruePositives + 1;

                } else {
                    //increment true negatives
                    numTrueNegatives = numTrueNegatives + 1;
                }
            }

            numFiles++;
        }
        //return map of accruacy
        double accuracy = (double) (numTruePositives + numTrueNegatives)/numFiles;
        Map<String, Double> acc = new TreeMap<>();
        acc.put("accuracy", accuracy);
        return acc;
    }
    //Percision method with the appropriate equations to determine spam precision
    public Map<String, Double> precision(List<TestFile> testFiles){
        int numTruePositives = 0;
        int numFalsePositives = 0;
        int numFiles = 0;

        for(TestFile test : testFiles) {
            if (test.getActualClass() == "Spam") {
                //utilizing 0.5 for spam probability
                if (test.getSpamProbability() > 0.5) {
                    //increment true positives
                    numTruePositives = numTruePositives + 1;

                } else {
                    //return false positives
                    numFalsePositives = numFalsePositives + 1;
                }
            }
            numFiles = numFiles + 1;
        }
        //return as map
        double precision = (double) numTruePositives/(numFalsePositives + numTruePositives);
        Map<String, Double> pre = new TreeMap<>();
        pre.put("precision", precision);
        return pre;
    }
    //Info method created to return info about files analyzed
    public Map<String, Integer> info(File mainDirectory){
        File hamDir = new File(mainDirectory, "/train/ham");
        File spamDir = new File(mainDirectory, "/train/spam");
        //find each length
        int numHamFiles = hamDir.listFiles().length;
        int numSpamFiles = spamDir.listFiles().length;
        int numtotal = numHamFiles + numSpamFiles;
        //return as map
        Map<String, Integer> info = new TreeMap<>();
        info.put("spam",numSpamFiles);
        info.put("ham",numHamFiles);
        info.put("total",numtotal);

        return info;
    }
    //Method to count the amount of files that contain a specific word and return a treemap

    public Map<String, Integer> wordFrequencyDir(File dir) throws IOException {
        Map<String, Integer> frequencies = new TreeMap<>();

        File[] filesInDir = dir.listFiles();
        int numFiles = filesInDir.length;
        //iterate through files
        for (int i = 0; i < numFiles; i++) {
            Map<String, Boolean> wordMap = countWordFile(filesInDir[i]);

            Set<String> keys = wordMap.keySet();
            Iterator<String> keyIterator = keys.iterator();
            //iterate throgh map
            while (keyIterator.hasNext()) {
                String word = keyIterator.next();
                if (frequencies.containsKey(word)) {
                    int oldCount = frequencies.get(word);
                    frequencies.put(word, oldCount + 1);
                }
                else {
                    frequencies.put(word, 1);
                }
            }
        }

        return frequencies;
    }
    //Method to count the amount of files that contain a specific word and return a treemap of words and bools
    private Map<String, Boolean> countWordFile(File file) throws IOException {
        Map<String, Boolean> wordMap = new TreeMap<>();
        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                //convert to lowercase and check if word if it is put true in the map
                String word = (scanner.next()).toLowerCase();
                if (isWord(word)) {
                    wordMap.put(word, true);
                }
            }
        }
        return wordMap;
    }
// isWord Method to find if particular is word
    private Boolean isWord(String word){
        //Hashset for common words to ignore
        System.out.println("hello");
        Set<String> hashSet = new HashSet<String>();
        hashSet.add("the");
        hashSet.add("a");
        hashSet.add("and");
        hashSet.add("to");
        hashSet.add("of");
        //return false to ignore
        if (hashSet.contains(word)){
            return false;
        }
        if (word == null){
            return false;
        }
        //regex to match letter characters and !
        String pattern = "^[a-zA-Z!]*$";
        if(word.matches(pattern)){
            return true;
        }

        return false;
    }
}



