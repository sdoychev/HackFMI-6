package com.smd.studio.hackfmi6;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


public class Settings extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Spinner ipAddressSpinner = (Spinner) findViewById(R.id.ipAddressSpinner);
        ArrayList<String> ipAddresses = ConnectionConfig.getInstance().getIpList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ipAddresses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ipAddressSpinner.setAdapter(adapter);
        ipAddressSpinner.setOnItemSelectedListener(new IpAddressSpinnerSelectorListener());
    }

    public class IpAddressSpinnerSelectorListener extends Activity implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selectedIpAddress = parent.getItemAtPosition(pos).toString();
            ConnectionConfig.getInstance().setHostAddress(selectedIpAddress);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}
