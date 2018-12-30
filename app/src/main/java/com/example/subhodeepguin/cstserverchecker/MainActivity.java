package com.example.subhodeepguin.cstserverchecker;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!(mWifi.isConnected())) {
            Toast.makeText(this, getString(R.string.connect_wifi_msg), Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        InputCheckKaro ick = new InputCheckKaro();
        ick.inputChecker1();
        ick.inputChecker2();
        ick.inputChecker3();
        ick.inputChecker4();
    }

    public class Ping extends Thread {

        String str = "";
        String url = "";

        public Ping(String url) {
            this.url = url;
        }

        public void run() {
            try {
                Process process = Runtime.getRuntime().exec(
                        "/system/bin/ping -c 8 " + url);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        process.getInputStream()));
                int i;
                char[] buffer = new char[4096];
                StringBuffer output = new StringBuffer();
                while ((i = reader.read(buffer)) > 0)
                    output.append(buffer, 0, i);
                reader.close();

                // body.append(output.toString()+"\n");
                this.str = output.toString();
                // Log.d(TAG, str);
            } catch (IOException e) {
                // body.append("Error\n");
                e.printStackTrace();
            }
        }

        public String getValue() {
            return str;
        }
    }

    private class InputCheckKaro {


        public void inputChecker1() {
            final EditText et1 = (EditText) findViewById(R.id.ip1);
            et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String val = ((EditText) view).getText().toString();
                    if (!TextUtils.isEmpty(val)) {
                        if (Integer.valueOf(val) > 255) {
                            //Notify user that the value is not good
                            et1.setText("");
                            Toast.makeText(getBaseContext(), getString(R.string.input_proper_IP), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        public void inputChecker2() {
            final EditText et2 = (EditText) findViewById(R.id.ip2);
            et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String val = ((EditText) view).getText().toString();
                    if (!TextUtils.isEmpty(val)) {
                        if (Integer.valueOf(val) > 255) {
                            //Notify user that the value is not good
                            et2.setText("");
                            Toast.makeText(getBaseContext(), getString(R.string.input_proper_IP), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        public void inputChecker3() {
            final EditText et3 = (EditText) findViewById(R.id.ip3);
            et3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String val = ((EditText) view).getText().toString();
                    if (!TextUtils.isEmpty(val)) {
                        if (Integer.valueOf(val) > 255) {
                            //Notify user that the value is not good
                            et3.setText("");
                            Toast.makeText(getBaseContext(), getString(R.string.input_proper_IP), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        public void inputChecker4() {
            final ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
            final EditText et4 = (EditText) findViewById(R.id.ip4);
            sv.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (et4.isFocused()) {
                        et4.clearFocus();
                        String val = ((EditText) et4).getText().toString();
                        if (!TextUtils.isEmpty(val)) {
                            if (Integer.valueOf(val) > 255) {
                                //Notify user that the value is not good
                                et4.setText("");
                                Toast.makeText(getBaseContext(), getString(R.string.input_proper_IP), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    return false;
                }
            });
        }
    }

    private class Loading extends Thread{
        public void run() {
            TextView loading = (TextView) findViewById(R.id.result_ip_status);
            loading.setText(R.string.loading);
        }
    }

    public class FinalResultDisplay {

        String result;

        public FinalResultDisplay(String result) {
            this.result = result;
        }

        public void run() {
            EditText et1 = (EditText) findViewById(R.id.ip1);
            String res1 = et1.getText().toString();
            EditText et2 = (EditText) findViewById(R.id.ip2);
            String res2 = et2.getText().toString();
            EditText et3 = (EditText) findViewById(R.id.ip3);
            String res3 = et3.getText().toString();
            EditText et4 = (EditText) findViewById(R.id.ip4);
            String res4 = et4.getText().toString();
            String Destination_Net_Unreachable = getResources().getString(R.string.net_unreachable);
            boolean reachable = true;
            TextView final_result = (TextView) findViewById(R.id.result_ip_status);
            final_result.setText("");

            if (result.contains("100%")) {
                final_result.setText(getString(R.string.cant_transmit_back));
                final_result.setTextColor(Color.parseColor("#F9A825"));
            }

            if (result.contains(Destination_Net_Unreachable)) {
                final_result.setText(R.string.unreachable);
                final_result.setTextColor(Color.parseColor("#F44336"));
            }

            if (final_result.getText().equals("") && !((res1.equals("") || (res2.equals("") || (res3.equals("") || res4.equals("")))))) {
                final_result.setText(R.string.reachable);
                final_result.setTextColor(Color.parseColor("#009688"));
            }


            TextView ipresult = (TextView) findViewById(R.id.notification_text);
            ipresult.setText(result);
        }
    }

    public void CheckStatus(View view) {

        //Toast.makeText(this, "Enter IP before pinging", Toast.LENGTH_SHORT).show();

        EditText et1 = (EditText) findViewById(R.id.ip1);
        String res1 = et1.getText().toString();
        EditText et2 = (EditText) findViewById(R.id.ip2);
        String res2 = et2.getText().toString();
        EditText et3 = (EditText) findViewById(R.id.ip3);
        String res3 = et3.getText().toString();
        EditText et4 = (EditText) findViewById(R.id.ip4);
        String res4 = et4.getText().toString();

        String finalIP = res1 + "." + res2 + "." + res3 + "." + res4;
        //new Loading().run();

        Ping p = new Ping(finalIP);
        p.run();
        String result = p.getValue();

        new FinalResultDisplay(result).run();
    }
}
