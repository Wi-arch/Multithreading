package by.education.multithreading;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

@Slf4j
public class LogisticCenter<Product> {

    private final static int DEFAULT_WAREHOUSE_CAPACITY = 10;
    private final static int DEFAULT_SECONDS_TIMEOUT = 5;
    private final BlockingDeque<Product> warehouse;
    private final int secondsTimeout;

    public LogisticCenter() {
        this.warehouse = new LinkedBlockingDeque<>(DEFAULT_WAREHOUSE_CAPACITY);
        this.secondsTimeout = DEFAULT_SECONDS_TIMEOUT;
    }

    public LogisticCenter(int warehouseCapacity, int secondsWaitingTimeout) {
        checkInitialArguments(warehouseCapacity, secondsWaitingTimeout);
        this.warehouse = new LinkedBlockingDeque<>(warehouseCapacity);
        this.secondsTimeout = secondsWaitingTimeout;
    }

    public boolean importProduct(Product product) {
        boolean result = false;
        try {
            result = warehouse.offer(product, secondsTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            log.warn("Interrupted exception", e);
        }
        if (result) {
            log.info("{} successfully imported product {} ", currentThread().getName(), product);
        } else {
            log.info("{} cannot import product {}, no available space", currentThread().getName(), product);
        }
        return result;
    }

    public Product exportProduct() {
        Product result = null;
        try {
            result = warehouse.pollFirst(secondsTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            currentThread().interrupt();
            log.warn("Interrupted exception", e);
        }
        if (result == null) {
            log.info("{} cannot export product, no available products", currentThread().getName());
        } else {
            log.info("{} successfully exported product {}", currentThread().getName(), result);
        }
        return result;
    }

    private void checkInitialArguments(int warehouseCapacity, int secondsWaitingTimeout) {
        if (warehouseCapacity <= 0) {
            throw new IllegalArgumentException("Invalid warehouse capacity");
        }
        if (secondsWaitingTimeout < 0) {
            throw new IllegalArgumentException("Invalid waiting timeout argument");
        }
    }
}
