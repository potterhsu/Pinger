package com.potterhsu.pinger.demo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.potterhsu.Pinger;

public class MainActivity extends Activity implements Pinger.OnPingListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Button btnCancelPingUntilSucceed;
    private Button btnCancelPingUntilFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCancelPingUntilSucceed = (Button) findViewById(R.id.btnCancelPingUntilSucceed);
        btnCancelPingUntilFailed = (Button) findViewById(R.id.btnCancelPingUntilFailed);
    }

    public void onBtnPingClick(View view) {
        new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... destinations) {
                try {
                    Pinger pinger = new Pinger();
                    for (String destination : destinations)
                        if (pinger.ping(destination, 3))
                            return true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                Toast.makeText(MainActivity.this, result ? "Success" : "Failure", Toast.LENGTH_SHORT).show();
            }
        }.execute("8.8.8.8");
    }

    public void onBtnPingUntilSucceedClick(View view) {
        final Pinger pinger = new Pinger();
        pinger.setOnPingListener(this);
        pinger.pingUntilSucceed("8.8.8.8", 5000);

        btnCancelPingUntilSucceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinger.cancel();
            }
        });
    }

    public void onBtnPingUntilFailedClick(View view) {
        final Pinger pinger = new Pinger();
        pinger.setOnPingListener(this);
        pinger.pingUntilFailed("8.8.8.8", 10000);

        btnCancelPingUntilFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinger.cancel();
            }
        });
    }

    @Override
    public void onPingSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPingFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPingFinish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Finish", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
