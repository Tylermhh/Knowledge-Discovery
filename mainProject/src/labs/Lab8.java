package labs;

import DocumentClasses.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;  // Import the Scanner class

public class Lab8 {
    public static void main(String[] args){
        Matrix data = new Matrix(process("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\data.txt"));
        ArrayList<Integer> userInput = getCustomerInput(data);
        data.findCategory(userInput);
//        System.out.println(userInput);

    }

    public static ArrayList<ArrayList<Integer>> process (String filePath){
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] splitLine = line.split(",");
                ArrayList<Integer> newEntry = new ArrayList<>();
                for (String elem : splitLine){
                    Double value = Double.parseDouble(elem);
                    Integer flooredVal = (int) Math.floor(value);
                    newEntry.add(flooredVal);
                }
                result.add(newEntry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static ArrayList<Integer> getCustomerInput(Matrix data){
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        ArrayList<Integer> userInput = new ArrayList<>();


        for (int i=0; i<data.getMatrix().get(0).size() - 1; i++){
            System.out.print("Enter value for attribute " + i + ": ");
            String inputString = myObj.nextLine();  // Read user input
            Integer input = Integer.valueOf(inputString);
            userInput.add(input);
        }
        return userInput;
    }
}
