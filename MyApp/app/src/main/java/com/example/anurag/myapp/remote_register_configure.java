package com.example.anurag.myapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class remote_register_configure extends AppCompatActivity {
    MqttAndroidClient client;
    DatabaseHelper db;
    int count;
    String arr[] = new String[10];
    String topic;
    @Override
    protected void onStart() {
        Toast.makeText(this, "remote_register_configure", Toast.LENGTH_SHORT).show();
        super.onStart();
     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_register_configure);
        getSupportActionBar().setTitle("Configuring the Remote");

        Intent intent = getIntent();
        final String brand = intent.getStringExtra("brand");
        final String product = intent.getStringExtra("product");


      //  Log.v("product", "hello there!"+product);
       // Toast.makeText(this, product, Toast.LENGTH_LONG).show();

        if (product.equals("Television")) {
            findViewById(R.id.speaker).setVisibility(View.GONE);
        }
        else if (product.equals("Speaker")) {
            findViewById(R.id.television).setVisibility(View.GONE);
        }

        db = new DatabaseHelper(this);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(remote_register_configure.this, "tcp://postman.cloudmqtt.com:10302",clientId);
        for( int i = 0 ; i < count ; i++ )
             arr[i] = "";
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            options.setUserName("zfrpoqiy");
            options.setPassword("tKCuPqBUVIzM".toCharArray());
            IMqttToken token = client.connect(options);
            //IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();

//                    topic = "MyApp/Mode";
//                   String payload = "2";
//                    byte[] encodedPayload = new byte[0];
//                    try {
//                        encodedPayload = payload.getBytes("UTF-8");
//                        MqttMessage message = new MqttMessage(encodedPayload);
//                        message.setRetained(true);
//                        client.publish(topic, message);
//                    } catch (UnsupportedEncodingException | MqttException e) {
//                        e.printStackTrace();
//                    }

                    topic = "MyApp/Register/#";
                    int qos = 1;                    // optional the qos to subscribe at (int: 0 or 1 only)
                    try
                    {
                        IMqttToken subToken = client.subscribe(topic,qos);
                        subToken.setActionCallback(new IMqttActionListener() {

                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                client.setCallback(new MqttCallback() {
                                    @Override
                                    public void connectionLost(Throwable cause) {
                                    }

                                    @Override
                                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                                        Toast.makeText(getApplicationContext(),new String(message.getPayload()),Toast.LENGTH_SHORT).show();
                                        //CardView card = findViewById(R.id.cardview_power);
                                        //card.setCardBackgroundColor(Color.parseColor("#E6E6E6"));
                                        ImageButton button1;

                                        if( topic.equals("MyApp/Register/Power") )
                                        {
                                            if(product.equals("Speaker")) {
                                                button1 = (ImageButton) findViewById(R.id.btn_power_s);
                                            }
                                            else{
                                                button1 = (ImageButton) findViewById(R.id.btn_power);
                                            }
                                            button1.setBackground(getResources().getDrawable(R.drawable.circlebackgroundred,null));
                                            arr[0] = new String(message.getPayload());

                                        }
                                        if( topic.equals("MyApp/Register/VolUp") )
                                        {
                                            if(product.equals("Speaker")) {
                                                button1 = (ImageButton) findViewById(R.id.btn_volup_s);
                                            }
                                            else{
                                                button1 = (ImageButton) findViewById(R.id.btn_volup);
                                            }
                                            button1.setBackground(getResources().getDrawable(R.drawable.circlebackgroundblack,null));
                                            arr[2] = new String(message.getPayload());
                                        }
                                        if( topic.equals("MyApp/Register/VolDown"))
                                        {
                                            if(product.equals("Speaker")) {
                                                button1 = (ImageButton) findViewById(R.id.btn_voldn_s);
                                            }
                                            else{
                                                button1 = (ImageButton) findViewById(R.id.btn_voldn);
                                            }
                                            button1.setBackground(getResources().getDrawable(R.drawable.circlebackgroundblack,null));
                                            arr[3] = new String(message.getPayload());
                                        }
                                        if( topic.equals("MyApp/Register/ChUp") )
                                        {
                                            if(product.equals("Speaker")) {
                                                button1 = (ImageButton) findViewById(R.id.play);
                                            }
                                            else{
                                                button1 = (ImageButton) findViewById(R.id.chup);
                                            }
                                            button1.setBackground(getResources().getDrawable(R.drawable.circlebackgroundblack,null));
                                            arr[4] = new String(message.getPayload());
                                        }
                                        if( topic.equals("MyApp/Register/ChDown") )
                                        {
                                            if(product.equals("Speaker")) {
                                                button1 = (ImageButton) findViewById(R.id.pause);
                                            }
                                            else{
                                                button1 = (ImageButton) findViewById(R.id.btn_chdown);
                                            }
                                            button1.setBackground(getResources().getDrawable(R.drawable.circlebackgroundblack,null));
                                            arr[5] = new String(message.getPayload());
                                        }
                                        if( topic.equals("MyApp/Register/Mute") ) {
                                            if(product.equals("Speaker")) {
                                                button1 = (ImageButton) findViewById(R.id.btn_mute_s);
                                            }
                                            else{
                                                button1 = (ImageButton) findViewById(R.id.btn_mute);
                                            }
                                            button1.setBackground(getResources().getDrawable(R.drawable.circlebackgroundblack,null));
                                            arr[1] = new String(message.getPayload());
                                        }
                                    }

                                    @Override
                                    public void deliveryComplete(IMqttDeliveryToken token) {
                                    }
                                });
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            }
                        });
                    }
                    catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        Button button4;
        if(product.equals("Speaker")) {
            button4 = findViewById(R.id.btn_done1_s);
        }
        else{
            button4 = (Button) findViewById(R.id.btn_done1);
        }


        //Button button = (Button) findViewById(R.id.button_send);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( arr[0].equals("") || arr[1].equals("")|| arr[2].equals("") || arr[3].equals("") || arr[4].equals("") || arr[5].equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Device Registration UnSuccessful", Toast.LENGTH_SHORT).show();
                    //sleep()
                }
                else
                {
                    db.addData(brand,product,arr[0],arr[1],arr[2],arr[3],arr[4],arr[5]);
                }
                Intent myIntent = new Intent( remote_register_configure.this , MainActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
