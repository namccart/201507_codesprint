package org.gnuradio.grtemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.gnuradio.controlport.ControlPort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ControlPortClient extends Activity {

    public class RunNetworkThread implements Runnable {

        private RPCConnectionThrift conn;
        private String host;
        private Integer port;
        private ControlPortClient activity;

        RunNetworkThread(String host, Integer port, ControlPortClient activity) {
            this.host = host;
            this.port = port;
            this.activity = activity;
        }

        public void run() {
            Log.d("GrTemplate", "Getting Connection");
            conn = new RPCConnectionThrift(host, port);
            activity.setConnectionReady();

            Log.d("GrTemplate", "Got Connection");
        }

        public RPCConnectionThrift getConnection() {
            if(conn == null) {
                throw new IllegalStateException("connection not established");
            }
            return conn;
        }
    }

    public class MyHandler extends Handler {

        public ControlPortClient activity;

        MyHandler(ControlPortClient activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            ArrayList<String> arrayString = bundle.getStringArrayList("knobs");
            activity.updateScreen(arrayString);
        }

    }

    private RunNetworkThread networkthread;
    private LinearLayout pcLayout;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String hostName = intent.getStringExtra("org.gnuradio.grtemplate.hostname");
        final String portNumber = intent.getStringExtra("org.gnuradio.grtemplate.portnumber");

        final Integer port = Integer.parseInt(portNumber);
        Log.d("GrTemplate", "Connecting to: " + hostName + ":" + port);

        handler = new MyHandler(this);

        networkthread = new RunNetworkThread(hostName, port, this);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(networkthread);

        setContentView(R.layout.activity_control_port_client);

        pcLayout = (LinearLayout) findViewById(R.id.pcLayout);
    }

    public void updateScreen(ArrayList<String> arrayString) {
        Double sum = new Double(0);
        for (String s : arrayString) {
            Double val = Double.parseDouble(s.split("::")[1].split(": ")[1]);
            sum += val;
        }

        for (String s : arrayString) {
            LinearLayout newline = new LinearLayout(this);
            newline.setOrientation(LinearLayout.HORIZONTAL);

            ProgressBar valBar = new ProgressBar(this);
            Double dVal = 100.0 * Double.parseDouble(s.split("::")[1].split(": ")[1]) / sum;
            Integer iVal = dVal.intValue();

            TextView viewKey = new TextView(this);
            TextView viewVal = new TextView(this);

            String vStr = new String("|");
            for (int i = 0; i < iVal; i++) {
                vStr += "-";
            }
            vStr += "|";

            viewKey.setText(s.split("::")[0] + ":    ");
            viewVal.setText(vStr);

            newline.addView(viewKey);
            newline.addView(viewVal);
            pcLayout.addView(newline);
        }
    }


    public void setConnectionReady() {
        Message msg = handler.obtainMessage();
        Bundle bundle = new Bundle();

        List<String> knobs = new ArrayList<String>();
        knobs.add(".*::work time");
        networkthread.getConnection().getKnobs(knobs);

        //Map<String, RPCConnectionThrift.KnobInfo> x = networkthread.getConnection().getKnobs(knobs);
        Map<String, RPCConnectionThrift.KnobInfo> x = networkthread.getConnection().getRe(knobs);

        ArrayList<String> knobArray = new ArrayList<String>();
        for (Map.Entry<String, RPCConnectionThrift.KnobInfo> e : x.entrySet()) {
            Log.d("GrTemplate", e.getKey() + ": " + e.getValue().value);

            String s = e.getKey() + ": " + e.getValue().value;
            knobArray.add(s);
        }

        bundle.putStringArrayList("knobs", knobArray);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control_port_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
