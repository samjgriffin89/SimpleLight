package com.samjgriffin.simplelight;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Flashlight extends Activity implements View.OnClickListener {

    private boolean hasCam = false;
    private boolean isOff = true;
    private Camera cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        ImageButton toggleLightBtn = (ImageButton) findViewById(R.id.toggleLightBtn);
        toggleLightBtn.setOnClickListener(this);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                cam = Camera.open();
                hasCam = true;
            }
            catch (RuntimeException ex) {
                Log.d("CameraOpen", "Error opening camera: " + ex.getMessage());
            }
        }
        else {
            TextView noLED = (TextView)findViewById(R.id.noLED);
            noLED.setText("Must have a Flashlight!");
            toggleLightBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flashlight, menu);
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

    @Override
    public void onClick(View view) {
        if (cam == null) {
            TextView noLED = (TextView)findViewById(R.id.noLED);
            noLED.setText("Camera not found!");

            ImageButton toggleLightBtn = (ImageButton) findViewById(R.id.toggleLightBtn);
            toggleLightBtn.setVisibility(View.GONE);
        }

        if (hasCam) {
            Parameters p = cam.getParameters();
            if (isOff) {
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            } else {
                p.setFlashMode(Parameters.FLASH_MODE_OFF);
                cam.setParameters(p);
            }

            isOff = !isOff;
        }
    }
}
