package com.example.compsec3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_mute = (Button) findViewById(R.id.button_mute);
        final Button button_wifi = (Button) findViewById(R.id.button_dc);
        final Button button_sms = (Button) findViewById(R.id.button_sms);
        final TextView textView_sms = (TextView) findViewById(R.id.textView_sms);
        final Button button_contact = (Button) findViewById(R.id.button_contact);
        final FloatingActionButton button_vibrate = (FloatingActionButton) findViewById(R.id.floatingActionButton_vibrate);


        button_mute.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.adjustVolume(AudioManager.ADJUST_TOGGLE_MUTE, AudioManager.FLAG_SHOW_UI);
            }
        });

        button_wifi.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if(wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(false);
                } else {
                    wifiManager.setWifiEnabled(true);
                }
            }
        });


        button_sms.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                int check = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS);
                if(check == PackageManager.PERMISSION_GRANTED){
                    Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
                    if(cursor.moveToFirst()){
                        String msg = cursor.getString(12);
                        textView_sms.setText(msg);
                    } else {
                        textView_sms.setText("No texts.");
                    }
                } else {
                    String[] permissionArray={Manifest.permission.READ_SMS};
                    ActivityCompat.requestPermissions(MainActivity.this, permissionArray , 0);
                }
            }
        });

        button_contact.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int check = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
                if(check == PackageManager.PERMISSION_GRANTED){
                    Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    if(cursor.moveToFirst()) {
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Toast toast = Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(MainActivity.this, "No Contacts", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    cursor.close();
                } else {
                    String[] permissionArray={Manifest.permission.READ_CONTACTS};
                    ActivityCompat.requestPermissions(MainActivity.this, permissionArray , 0);
                }
            }
        });

        button_vibrate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(VibrationEffect.EFFECT_HEAVY_CLICK);
            }
        });
    }
}