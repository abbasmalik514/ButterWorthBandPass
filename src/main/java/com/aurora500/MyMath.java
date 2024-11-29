package com.aurora500;

public class MyMath {
    // Method to calculate mean
    public static double mean(double[] array){
        double sum = 0;
        for (int i=0; i<array.length; i++){
            sum += array[i];
        }

        return sum/array.length;
    }

    public double calculateSD(double numArray[])
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }


}
