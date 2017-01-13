package com.potterhsu;

import java.io.IOException;

public class Pinger {

    private Runtime runtime;
    private Thread pingThread;
    private OnPingListener onPingListener;

    public Pinger() {
        runtime = Runtime.getRuntime();
    }

    public void setOnPingListener(OnPingListener onPingListener) {
        this.onPingListener = onPingListener;
    }

    public boolean ping(String destination, int timeoutInSeconds) throws InterruptedException {
        try {
            String command = String.format("/system/bin/ping -c 3 -W %d %s", timeoutInSeconds, destination);
            Process process = runtime.exec(command);
            int ret = process.waitFor();
            return ret == 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void pingUntilSucceeded(final String destination, final long intervalInMillis) {
        pingThread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    while (true) {
                        if (ping(destination, 3)) {
                            if (onPingListener != null)
                                onPingListener.onPingSuccess();
                            break;
                        } else {
                            if (onPingListener != null)
                                onPingListener.onPingFailure();
                        }

                        Thread.sleep(intervalInMillis);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (onPingListener != null)
                    onPingListener.onPingFinish();
            }
        };
        pingThread.start();
    }

    public void pingUntilFailed(final String destination, final long intervalInMillis) {
        pingThread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    while (true) {
                        if (!ping(destination, 3)) {
                            if (onPingListener != null)
                                onPingListener.onPingFailure();
                            break;
                        } else {
                            if (onPingListener != null)
                                onPingListener.onPingSuccess();
                        }

                        Thread.sleep(intervalInMillis);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (onPingListener != null)
                    onPingListener.onPingFinish();
            }
        };
        pingThread.start();
    }

    public void cancel() {
        if (pingThread != null)
            pingThread.interrupt();
    }

    public interface OnPingListener {
        void onPingSuccess();
        void onPingFailure();
        void onPingFinish();
    }

}
