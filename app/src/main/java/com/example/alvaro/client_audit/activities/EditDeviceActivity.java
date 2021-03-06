package com.example.alvaro.client_audit.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alvaro.client_audit.R;
import com.example.alvaro.client_audit.controllers.listeners.editDeviceActivityListeners.SaveButtonListener;
import com.example.alvaro.client_audit.core.entities.Device;
import com.example.alvaro.client_audit.core.entities.DeviceBook;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class EditDeviceActivity extends AppCompatActivity {

    private TextView device_name;
    private TextView device_ip;
    private TextView device_port;
    private Button b_save;
    private SpinKitView loader;
    private ThreeBounce w = new ThreeBounce();
    private boolean onExecute;


    /*
       On create
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);
        this.onExecute = false;
        b_save = (Button) findViewById(R.id.button_save);
        device_name = (TextView) findViewById(R.id.device_edit_name);
        device_ip = (TextView) findViewById(R.id.device_edit_ip);
        device_port = (TextView) findViewById(R.id.device_edit_port);
        this.loader = (SpinKitView) findViewById(R.id.edit_anim_load);

        Device device = DeviceBook.get_instance().get_selected_device();

        this.device_name.setText(device.get_name());
        this.device_ip.setText(device.get_ip());
        this.device_port.setText(String.valueOf(device.get_port()));

        this.b_save.setOnClickListener(new SaveButtonListener(this));

    }

    /*
        start animation while asyncTask
     */
    public void start_animation(){
        this.onExecute = true;
        this.loader.setVisibility(View.VISIBLE);
        this.loader.setIndeterminateDrawable(w);
        b_save.setEnabled(false);
    }

    /*
        called by asyncTask when it terminates
     */
    public void stop_animation(boolean res){
        this.loader.setVisibility(View.GONE);
        if(!res){
            this.make_toast();
            b_save.setEnabled(true);
        }else{
            DeviceBook.get_instance().update_adapter();
            this.finish();
        }
        this.onExecute = false;
    }

    /*
        toast maker
     */
    public void make_toast(){
        Toast toast = Toast.makeText(this.getApplicationContext(), "Cannot create device", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        if (!onExecute) {
            super.onBackPressed();
        }
    }
}
