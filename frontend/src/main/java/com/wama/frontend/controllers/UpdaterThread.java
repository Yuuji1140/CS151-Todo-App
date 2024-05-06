package com.wama.frontend.controllers;

public class UpdaterThread {
    private final Runnable updateFunction;
    private final int interval;
    private boolean running = true;

    public UpdaterThread(Runnable updateFunction, int seconds) {
        this.updateFunction = updateFunction;
        this.interval = seconds * 1000;
        this.start();
    }

    private void start() {
        new Thread(() -> {
            while (running) {
                try {
                    updateFunction.run();
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
    }
}
