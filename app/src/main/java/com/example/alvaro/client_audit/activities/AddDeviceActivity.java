package com.example.alvaro.client_audit.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alvaro.client_audit.R;
import com.example.alvaro.client_audit.controllers.listeners.addDeviceActivityListeners.CreateDeviceButtonListener;
import com.example.alvaro.client_audit.core.entities.DeviceBook;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ThreeBounce;


public class AddDeviceActivity extends AppCompatActivity {

    Button b_create;
    private TextView device_name;
    private TextView device_ip;
    private TextView device_port;
    private SpinKitView loader;
    private ThreeBounce w = new ThreeBounce();

    boolean onExecute;

    /*
        On create
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device_activity);
        onExecute = false;
        b_create = (Button) findViewById(R.id.button_create);
        device_name = (TextView) findViewById(R.id.device_name);
        device_ip = (TextView) findViewById(R.id.device_ip);
        device_port = (TextView) findViewById(R.id.device_port);
        this.loader = (SpinKitView) findViewById(R.id.create_anim_load);

        b_create.setEnabled(true);
        b_create.setOnClickListener(new CreateDeviceButtonListener(this,device_name,device_ip,device_port));
    }

    /*
        start animation while asyncTask
     */

    public void start_animation(){
        onExecute = true;
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminateDrawable(w);
        b_create.setEnabled(false);
    }

    /*
        called by asyncTask when it terminates
     */

    public void stop_animation(boolean res){
        onExecute = false;
        this.loader.setVisibility(View.GONE);
        if(!res){
            this.make_toast();
            b_create.setEnabled(true);
        }else{
            DeviceBook.get_instance().update_adapter();
            this.finish();
        }
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
