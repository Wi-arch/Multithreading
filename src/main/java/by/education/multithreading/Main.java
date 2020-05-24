package by.education.multithreading;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Random RANDOM = new Random();
    private final static int WAITING_TIMEOUT = 2;
    private final static int CAPACITY = 4;
    private static LogisticCenter<String> warehouse = new LogisticCenter<>(CAPACITY, WAITING_TIMEOUT);

    public static void main(String[] args) {
        int numberOfThreads = 10;
        startThreads(numberOfThreads, true);
        startThreads(numberOfThreads, false);
    }

    private static void startThreads(int numberOfThreads, boolean export) {
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                putCurrentThreadToSleepToRandomTime();
                if (export) {
                    warehouse.exportProduct();
                } else {
                    warehouse.importProduct("Some product");
                }
            }).start();
        }
    }

    private static void putCurrentThreadToSleepToRandomTime() {
        try {
            TimeUnit.SECONDS.sleep(RANDOM.nextInt(7));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
