package com.spamdetector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/spam")
public class SpamResource {

    // Create a SpamDetector object to hold all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();

    // Create a List object to hold the results of the testing
    private List<TestFile> testResults;

    // Create an ObjectMapper object to convert the results to JSON
    ObjectMapper mapper = new ObjectMapper();

    // Constructor for the SpamResource class
   public  SpamResource() throws IOException {

        // Load resources, train and test to improve performance on the endpoint calls
        trainAndTest();

        // Print a message to indicate that training and testing are in progress
        System.out.print("Training and testing the model, please wait");

        // Call the trainAndTest() method to actually perform the training and testing
        this.trainAndTest();
    }

    // Define a method to handle HTTP GET requests for the "/spam" endpoint
    @GET
    @Produces("application/json")
    public Response getSpamResults() throws IOException {

        // Get the URL of the "/data" directory from the class loader
        URL url = this.getClass().getClassLoader().getResource("/data");

        // Create a new File object to represent the "/data" directory
        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Train and test the detector on the files in the "/data" directory
        testResults = detector.trainAndTest(mainDirectory);

        // Convert the test results to a JSON string
        String json = mapper.writeValueAsString(testResults);

        // Create a new Response object with the test results as the entity
        Response myResp = Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(json)
                .build();

        // Return the Response object
        return myResp;
    }

    // Define a method to handle HTTP GET requests for the "/spam/accuracy" endpoint
    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() throws IOException {

        // Get the URL of the "/data" directory from the class loader
        URL url = this.getClass().getClassLoader().getResource("/data");

        // Create a new File object to represent the "/data" directory
        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Train and test the detector on the files in the "/data" directory
        testResults = detector.trainAndTest(mainDirectory);

        // Compute the accuracy of the detector on the test files
        Map<String, Double> accuracy = detector.accuracy(testResults);

        // Convert the accuracy to a JSON string
        String json = mapper.writeValueAsString(accuracy);

        // Create a new Response object with the accuracy as the entity
        Response myResp = Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(json)
                .build();

        // Return the Response object
        return myResp;
    }


    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() throws IOException {
        // Get the URL of the data directory
        URL url = this.getClass().getClassLoader().getResource("/data");

        // Create a File object representing the data directory
        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Train and test the SpamDetector on the data directory
        testResults = detector.trainAndTest(mainDirectory);

        // Calculate the precision of the detector based on the test results
        Map<String, Double> precision = detector.precision(testResults);

        // Build a Response object with the precision information and return it
        Response myResp = Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(mapper.writeValueAsString(precision))
                .build();
        return myResp;
    }

    @GET
    @Path("/info")
    @Produces("application/json")
    public Response getInfo() throws IOException {
        // Get the URL of the data directory
        URL url = this.getClass().getClassLoader().getResource("/data");

        // Create a File object representing the data directory
        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Get the info about the data directory from the SpamDetector
        Map<String, Integer> info = detector.info(mainDirectory);

        // Build a Response object with the info and return it
        Response myResp = Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(mapper.writeValueAsString(info))
                .build();
        return myResp;
    }

    private List<TestFile> trainAndTest() throws IOException {
        // If the SpamDetector hasn't been created yet, create it
        if (this.detector == null) {
            this.detector = new SpamDetector();
        }

        // Get the URL of the data directory
        URL url = this.getClass().getClassLoader().getResource("/data");
        if (url == null) {
            // Resource not found in classpath
            System.out.println("/data resource not found in classpath");
        } else {
            // Resource found in classpath
            System.out.println("/data resource found in classpath");
        }
        // Create a File object representing the data directory
        File mainDirectory = null;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Train and test the SpamDetector on the data directory
        return this.detector.trainAndTest(mainDirectory);
    }
}