package Java.algocalc.model;

import Java.algocalc.controller.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class model {

    public static void executeCollatzConjecture(long startValue, long endValue, int threadCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        long valuesPerThread = (endValue - startValue + 1) / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final long startIndex = startValue + i * valuesPerThread;
            final long endIndex = i == threadCount - 1 ? endValue : startIndex + valuesPerThread - 1;

            executorService.submit(() -> {
                for (long j = startIndex; j <= endIndex; j++) {
                    long threadStartTime = System.currentTimeMillis();
                    long calculatedValue = j;
                    int iterations = 0;
                    while (calculatedValue > 1) {
                        if (calculatedValue % 2 == 0) {
                            calculatedValue = calculatedValue / 2;
                        } else {
                            calculatedValue = 3 * calculatedValue + 1;
                        }
                    iterations++;
                    }
                    long threadEndTime = System.currentTimeMillis();
                    long calculationTime = threadEndTime - threadStartTime;
                    controller.chacheList.add(new controller.rawValues(j, iterations, calculationTime));
                }
            });
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

