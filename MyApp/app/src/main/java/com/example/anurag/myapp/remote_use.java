package com.example.anurag.myapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class remote_use extends AppCompatActivity {
    MqttAndroidClient client;
    DatabaseHelper db;
    String arr[] = new String[10];
    String topic;
    DatabaseHelper dh;
    Cursor data;
    ImageButton b1, b2, b3, b4, b5, b6;
    Button bx;
    int flag = 0;

    @Override
    protected void onStart() {
        Toast.makeText(this, "remote_use", Toast.LENGTH_SHORT).show();
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_use);
        final Vibrator vibe = (Vibrator) remote_use.this.getSystemService(Context.VIBRATOR_SERVICE);

        getSupportActionBar().setTitle("Remote");
        Intent intent = getIntent();
        dh = new DatabaseHelper(this);
        final String brand = intent.getStringExtra("brand");
        final String product = intent.getStringExtra("product");

        if (product.equals("Television") || product.equals("tv")) {
            findViewById(R.id.speaker).setVisibility(View.GONE);
        }
        else if (product.equals("Speaker")) {
            findViewById(R.id.television).setVisibility(View.GONE);
        }

        db = new DatabaseHelper(this);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(remote_use.this, "tcp://postman.cloudmqtt.com:10302",clientId);
        data = dh.getData("select * from IRDevices where brand = '"+brand+"' AND product = '"+product+"'");
        data.moveToNext();
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            options.setUserName("zfrpoqiy");
            options.setPassword("tKCuPqBUVIzM".toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }
        catch (MqttException e)
        {}


        if(product.equals("Speaker")) {
            b1 = (ImageButton) findViewById(R.id.btn_power_s);
        }
        else{
            b1 = (ImageButton) findViewById(R.id.btn_power);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                topic = "MyApp/Transmit/t";
                String payload = data.getString(2);
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        if(product.equals("Speaker")) {
            b2 = (ImageButton) findViewById(R.id.btn_mute_s);
        }
        else{
            b2 = (ImageButton) findViewById(R.id.btn_mute);
        }
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                topic = "MyApp/Transmit/t";
                String payload = data.getString(3);
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        if(product.equals("Speaker")) {
            b3 = (ImageButton) findViewById(R.id.btn_volup_s);
        }
        else{
            b3 = (ImageButton) findViewById(R.id.btn_volup);
        }
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                topic = "MyApp/Transmit/t";
                String payload = data.getString(4);
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        if(product.equals("Speaker")) {
            b4 = (ImageButton) findViewById(R.id.btn_voldn_s);
        }
        else{
            b4 = (ImageButton) findViewById(R.id.btn_voldn);
        }
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                topic = "MyApp/Transmit/t";
                String payload = data.getString(5);
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });


        if(product.equals("Speaker")) {
            b5 = (ImageButton) findViewById(R.id.play);
            flag = 1;
        }
        else{
            b5 = (ImageButton) findViewById(R.id.btn_chup);
        }
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 1)
                {
                    b5.setEnabled(false);
                    b6.setEnabled(true);
                }
                vibe.vibrate(80);
                topic = "MyApp/Transmit/t";
                String payload = data.getString(6);
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        if(product.equals("Speaker")) {
            b6 = (ImageButton) findViewById(R.id.pause);
            flag = 2;
        }
        else{
            b6 = (ImageButton) findViewById(R.id.btn_chdown);
        }
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag == 2)
                {
                    b6.setEnabled(false);
                    b5.setEnabled(true);
                }
                vibe.vibrate(80);
                topic = "MyApp/Transmit/t";
                String payload = data.getString(7);
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(true);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        if(product.equals("Speaker")) {
            bx = findViewById(R.id.btn_done_s);
        }
        else{
            bx = findViewById(R.id.btn_done);
        }
        bx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.vibrate(80);
                Intent myIntent = new Intent( remote_use.this , MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
