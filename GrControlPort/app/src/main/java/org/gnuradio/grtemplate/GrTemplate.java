package org.gnuradio.grtemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GrTemplate extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetTMP(getCacheDir().getAbsolutePath());

        //FgInit();
        //FgStart();


        //EditText text = (EditText) findViewById(R.id.editMainText);
        //text.setText("TESTING 1 2 3 4");
        setContentView(R.layout.activity_gr_template);
    }

    public void sendOnClick(View view) {
        Intent intent = new Intent(this, ControlPortClient.class);
        EditText hostNameText = (EditText) findViewById(R.id.hostNameEditText);
        EditText portNumberText = (EditText) findViewById(R.id.portNumEditText);
        String hostName = hostNameText.getText().toString();
        String portNumber = portNumberText.getText().toString();

        intent.putExtra("org.gnuradio.grtemplate.hostname", hostName);
        intent.putExtra("org.gnuradio.grtemplate.portnumber", portNumber);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gr_template, menu);
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

    public native void SetTMP(String tmpname);
    public native void FgInit();
    public native void FgStart();

    static {
        System.loadLibrary("fg");
    }
}
