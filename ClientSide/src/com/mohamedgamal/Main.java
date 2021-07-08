package com.mohamedgamal;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean systemOn = true;
        while (systemOn) {
            System.out.println("Please enter a number for the API or press 0 to Exit\n" +
                    "1- Go to Home Page\n" +
                    "2- Go to Summary Page\n" +
                    "3- Go to Pie Chart Page \n" +
                    "4- Go to The Most Popular Job Titles Bar Chart\n" +
                    "5- Go to The Most Popular Areas Bar Chart\n" +
                    "6- Go to The Most Popular Skills Required Bar Chart\n" +
                    "7- K Means\n" +
                    "8- Factorization\n");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    String REST_URI = "http://localhost:8080/";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                case "2":
                    REST_URI = "http://localhost:8080/Summary";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                case "3":
                    REST_URI = "http://localhost:8080/pieChart";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                case "4":
                    REST_URI = "http://localhost:8080/barChart1";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                case "5":
                    REST_URI = "http://localhost:8080/barChart2";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                case "6":
                    REST_URI = "http://localhost:8080/barChart3";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                case "7":
                    REST_URI = "http://localhost:8080/kMeans";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                case "8":
                    REST_URI = "http://localhost:8080/factorization";
                    System.out.println("Please enter This URL: " + REST_URI);
                    break;
                default:
                    systemOn = false;
                    break;
            }
        }
        System.out.println("Thank You");
    }
}
