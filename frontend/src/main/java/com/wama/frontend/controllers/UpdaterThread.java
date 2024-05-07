package com.wama.frontend.controllers;

import java.util.HashMap;

public class UpdaterThread {
    private final Runnable updateFunction;
    private static final HashMap<Runnable, UpdaterThread> instances = new HashMap<>();
    private final int interval;
    private boolean running = true;
    private static int numRuns = 100;

    private UpdaterThread(Runnable updateFunction, int seconds) {
        this.updateFunction = updateFunction;
        this.interval = seconds * 1000;
        this.startThread();
    }

    public static void createNew(Runnable updateFunction, int seconds) {
        instances.computeIfAbsent(updateFunction, key -> new UpdaterThread(key, seconds));
    }

    private void startThread() {
        new Thread(() -> {
            while (running && numRuns-- > 0) {
                try {
                    updateFunction.run();
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stop();
        }).start();
    }

    public void stop() {
        running = false;
    }
}
