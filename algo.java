package Java.algocalc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.time.Duration;

public class algo {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            long values = 1_000_000_000;
            long startvalue = 2;
            values += 1;    
            int numThreads = 5;
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            List<ValuePair> valuePairs = Collections.synchronizedList(new ArrayList<>());

            for (int threadNum = 0; threadNum < numThreads; threadNum++) {
                final long segmentSize = (values + numThreads - 1) / numThreads;
                final long start = startvalue + (threadNum * segmentSize);
                final long end = (threadNum == numThreads - 1) ? values + 1 : start + segmentSize;

                executor.submit(() -> {
                    try {
                        long threadStartTime = System.currentTimeMillis();
                        for (long i = start; i < end; i++) {
                            long x = i;
                            List<Long> collatzList = new ArrayList<>();

                            while (x > 1 && collatzList.size() < 100000) {
                                if (x % 2 == 0) {
                                    x = x / 2;
                                } else {
                                    x = 3 * x + 1;
                                }
                                collatzList.add(x);
                            }

                            long threadEndTime = System.currentTimeMillis();
                            long threadExecutionTime = threadEndTime - threadStartTime;

                            // Store the length of the collatzList and thread execution time in the valuePairs list
                            long stepCount = collatzList.size();
                            valuePairs.add(new ValuePair(i, stepCount, threadExecutionTime));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            Duration duration = Duration.ofMillis(executionTime);

            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();
            long seconds = duration.toSecondsPart();
            long milliseconds = duration.toMillisPart();

            try (BufferedWriter resultsFile = new BufferedWriter(new FileWriter("results.txt", true))) {

                // Initialize variables for the smallest and largest number of steps
                long smallestSteps = Long.MAX_VALUE;
                long largestSteps = Long.MIN_VALUE;
                long longestExecutionTime = Long.MIN_VALUE;

                // Initialize variables for the corresponding values
                long numberWithSmallestSteps = -1;
                long numberWithLargestSteps = -1;
                long numberWithLongestExecutionTime = -1;

                // Iterate through the list and find the smallest and largest number of steps
                for (ValuePair pair : valuePairs) {
                    long steps = pair.getSteps();
                    long value = pair.getValue();
                    long executionTimePerNumber = pair.getExecutionTime();

                    if (steps < smallestSteps) {
                        smallestSteps = steps;
                        numberWithSmallestSteps = value;
                    }

                    if (steps > largestSteps) {
                        largestSteps = steps;
                        numberWithLargestSteps = value;
                    }

                    if (executionTimePerNumber > longestExecutionTime) {
                        longestExecutionTime = executionTimePerNumber;
                        numberWithLongestExecutionTime = value;
                    }
                }

                resultsFile.write("Smallest number of steps: \t\t\t\t\t" + smallestSteps + "\t\tfor the value: \t\t\t" + numberWithSmallestSteps + "\n");
                resultsFile.write("Largest number of steps: \t\t\t\t\t" + largestSteps + "\t\tfor the value: \t\t\t" + numberWithLargestSteps + "\n");
                resultsFile.write("Longest execution time for a number: \t\t" + longestExecutionTime + "\t\tms for the value: \t\t" + numberWithLongestExecutionTime + "\n");
                resultsFile.write("Program execution time:\t\t\t\t\t\t" + hours + "h " + minutes + "min " + seconds + "s " + milliseconds + "ms\n\n");
                resultsFile.write("Total number of calculations performed: \t" + valuePairs.size() + "\n\n");

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Program finished.");
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shutdownComputer();
    }

    public static class ValuePair {
        private long value;
        private long steps;
        private long executionTime;

        public ValuePair(long value, long steps, long executionTime) {
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

    public static void shutdownComputer() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("shutdown /s /t 0");
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // Unix/Linux/Mac
                Runtime.getRuntime().exec("shutdown -h now");
            } else {
                System.out.println("Shutdown not supported on this operating system.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
