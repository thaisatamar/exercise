package com.thais.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class ProgressCounter implements AutoCloseable {
    AtomicBoolean serverResponse = new AtomicBoolean();

    public ProgressCounter() {
        trackProgress();
    }

    @Override
    public void close() throws Exception {
        serverResponse.set(true);
    }

    private void trackProgress() {
        System.out.print("Transferring data.");
        Runnable task = () -> {
            while (serverResponse.get() == false) {
                try {
                    Thread.sleep(30000);
                    System.out.print(".");
                } catch (InterruptedException e) {

                }
            }
        };

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
}
