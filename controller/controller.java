package Java.algocalc.controller;

import Java.algocalc.model.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class controller{
    static List<maxValues> resultList = new ArrayList<>();
    public static List<rawValues> chacheList = new ArrayList<>();
    public static String runTimeFormatted = null;

    public static void main(String[] args) {

        final long endValue = 100_000_000L;
        final long stepValue = 50_000_000;
        final int threadCount = 10;
        long programStartTime = System.currentTimeMillis();
        long programmEndTime = Long.MIN_VALUE;
        long programmRunTime = Long.MIN_VALUE;
  
        for(long startValue = 2; startValue < endValue; startValue+=stepValue)
        {
            long spanOfStep = startValue + stepValue;
            model.executeCollatzConjecture(startValue, spanOfStep, threadCount);
            addValuesToChache(resultList);
            transferValues(chacheList);
            chacheList.clear();
            System.out.println(spanOfStep);
        }
        programmEndTime = System.currentTimeMillis();
        programmRunTime = programmEndTime - programStartTime;
        runTimeFormatted = ("Programm runtime:\t\t\t\t\t\t\t" + timeCalculation(programmRunTime));
        writeToResultFile(resultList);
    }

    //calculates used time
    public static String timeCalculation(long durationInMillis) {
        long milliseconds = durationInMillis % 1000;
        long seconds = (durationInMillis / 1000) % 60;
        long minutes = (durationInMillis / (1000 * 60)) % 60;
        long hours = (durationInMillis / (1000 * 60 * 60));
    
        String resultTime = (hours + "h " + minutes + "min " + seconds + "s " + milliseconds + "ms");
        return resultTime;
    }

    //transfers the 3 max values to a seperate list
    public static void transferValues(List<rawValues> unfilteredValues){
        long maxValue = Long.MIN_VALUE;
        long stepsForValue = Long.MIN_VALUE;
        long timeForValue = Long.MIN_VALUE;

        long maxSteps = Long.MIN_VALUE;
        long valueForSteps = Long.MIN_VALUE;
        long timeForSteps = Long.MIN_VALUE;

        long maxTime = Long.MIN_VALUE;
        long valueForTime = Long.MIN_VALUE;
        long stepsForTime = Long.MIN_VALUE;

        for(rawValues objekt : unfilteredValues){
            if (objekt != null) {
                long value = objekt.getValue();
                long steps = objekt.getSteps();
                long time = objekt.getExecutionTime();
            

                if(value>maxValue){
                    maxValue = value;
                    stepsForValue = steps;
                    timeForValue = time;
                }            
                if(steps>maxSteps && value > valueForSteps){
                    valueForSteps = value;
                    maxSteps = steps;
                    timeForSteps = time;
                }            
                if(time>maxTime && value > valueForTime){
                    valueForTime = value;
                    stepsForTime = steps;
                    maxTime = time;
                }
            }
        }
        resultList.add(new maxValues(maxValue,stepsForValue,timeForValue));
        resultList.add(new maxValues(valueForSteps,maxSteps,timeForSteps));
        resultList.add(new maxValues(valueForTime,stepsForTime,maxTime));
    }

    public static void addValuesToChache(List<maxValues> maxValeList)
    {
        for(maxValues object : maxValeList){
            chacheList.add(new rawValues(object.getMaxValue(), object.getMaxSteps(), object.getMaxTime()));
        }
    }

    public static void writeToResultFile(List<maxValues> maxValuesList) {

        try (BufferedWriter resultsFile = new BufferedWriter(new FileWriter("Java/algocalc/results.txt", true))) {
            long maxValue = Integer.MIN_VALUE;
            long stepsForValue = Long.MIN_VALUE;
            long timeForValue = Long.MIN_VALUE;

            long maxSteps = Integer.MIN_VALUE;
            long valueForSteps = Long.MIN_VALUE;
            long timeForSteps = Long.MIN_VALUE;

            long maxTime = Long.MIN_VALUE;
            long valueForTime = Long.MIN_VALUE;
            long stepsForTime = Long.MIN_VALUE;
            
            for(maxValues objekt : maxValuesList){
                long value = objekt.getMaxValue();
                long steps = objekt.getMaxSteps();
                long time = objekt.getMaxTime();

                if(value>maxValue){
                    maxValue = value;
                    stepsForValue = steps;
                    timeForValue = time;
                }            
                if(steps>maxSteps){
                    valueForSteps = value;
                    maxSteps = steps;
                    timeForSteps = time;
                }            
                if(time>maxTime){
                    valueForTime = value;
                    stepsForTime = steps;
                    maxTime = time;
                }
            }
            resultsFile.write("Largest Calculated number was:\t\t\t\t" + maxValue + "\t\t\twith:\t\t\t" + 
            stepsForValue + " Steps and calculation Time:\t"+ timeCalculation(timeForValue) + "\n");
            resultsFile.write("Largest number of steps:\t\t\t\t\t" + maxSteps + "\t\t\t\t\tfor the value:\t" + 
            valueForSteps + " and calculation Time:\t"+ timeCalculation(timeForSteps) + "\n");
            resultsFile.write("Longest calculation Time:\t\t\t\t\t" + timeCalculation(maxTime) + "\t\tfor the value:\t" + 
            valueForTime + "\n");
            resultsFile.write(runTimeFormatted +"\n\n");
/*             resultsFile.write("Program execution time:\t\t\t\t\t\t" + hours + "h " + minutes + "min " + seconds + "s " + milliseconds + "ms\n\n");
 */        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class rawValues {
        private long value;
        private long steps;
        private long executionTime;

            public rawValues(long value, long steps, long executionTime) {
                this.value = value;
                this.steps = steps;
                this.executionTime = executionTime;
            }

            public long getValue() {
                return value;
            }

            public long getSteps() {
                return steps;
            }

            public long getExecutionTime() {
                return executionTime;
            }
    }
        public static class maxValues{
        private long maxValue;
        private long maxSteps;
        private long maxTime;
                
        public maxValues(long maxValue, long maxSteps, long maxTime){
            this.maxValue = maxValue;
            this.maxSteps = maxSteps;
            this.maxTime = maxTime;
        }

        public long getMaxValue(){
            return maxValue;
        }
        public long getMaxSteps(){
            return maxSteps;
        }
        public long getMaxTime(){
            return maxTime;
        }
    }
}