#include "ESP8266WiFi.h"
//#include "PubSubClient.h"  // Read the rest of the article
#include <stdlib.h>
#include <Arduino.h>
#include <IRremoteESP8266.h>
#include <IRrecv.h> 
#include <IRutils.h>
#include <IRsend.h>

#define IR_LED 4 
const char *ssid =  "Killer Zone";   // cannot be longer than 32 characters!
const char *pass =  "*zxcv1234#";   //
 
const char* mqttServer = "postman.cloudmqtt.com";
const int mqttPort = 10302;
const char* mqttUser = "zfrpoqiy";
const char* mqttPassword = "tKCuPqBUVIzM";

const uint16_t kRecvPin = 14; // An IR detector/demodulator is connected to GPIO pin 14(D5 on a NodeMCU board).

IRrecv irrecv(kRecvPin);
IRsend irsend(IR_LED);
decode_results results;

WiFiClient espClient;
PubSubClient client(espClient);
int val = 0;

int count = 0;

void setup() {
  
  Serial.begin(115200);
  irrecv.enableIRIn();  // Start the receiver
  irsend.begin();
  while (!Serial)  // Wait for the serial connection to be establised.
  delay(50);
  Serial.println();
  Serial.print("IRrecvDemo is now running and waiting for IR message on Pin ");
  Serial.println(kRecvPin);

  WiFi.begin(ssid, pass);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  
  Serial.println("Connected to the WiFi network");

  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);
   
  while (!client.connected()) {
   // Serial.println("Connecting to MQTT...");
 
    if (client.connect("ESP8266Client", mqttUser, mqttPassword )) {
   
      Serial.println("connected");  
 
       client.publish("esp/test1", "Ready to Register" );   //for testing
       client.subscribe("MyApp/Transmit/#");            
 
    } else {
 
      Serial.print("failed with state ");
      Serial.print(client.state());
      
      delay(2000);
 
    }
  }
 
  
}
 
void callback(char* topic, byte* payload, unsigned int length) {
 // Serial.println("123");
char  ptr[100];
int j = 0;
for(j = 0; j < length; j++)
{
  ptr[j] = (char)payload[j];
}
ptr[j] = '\0';
transmit(ptr);
}

void transmit(char * data)
{
  unsigned long x  = strToHex(data);
  Serial.print(x);
//  #if SEND_GLOBALCACHE
  irsend.sendNEC(1129715076, 32);
//#else   // SEND_GLOBALCACHE
//  Serial.println("Can't send because SEND_GLOBALCACHE has been disabled.");
//#endif  // SEND_GLOBALCACHE
  delay(10000);
}
void loop() {
   
 if (irrecv.decode(&results)) {
    count ++;
//    Serial.print(count);
    char hex[30];
    
     //int to be converted , char array which should store , base of the data)
     itoa(results.value, hex, 16);              // results.value: The actual IR code (0 if type is UNKNOWN) 
     Serial.println(hex);
    serialPrintUint64(results.value, HEX);        // print() & println() can't handle printing long longs. (uint64_t)

    if(count == 1)
    {
       client.publish("MyApp/Register/Power",hex);
    }
    else if(count == 2)
    {
       client.publish("MyApp/Register/Mute",hex);
       
    }
    else  if(count == 3)
    {
       client.publish("MyApp/Register/ChUp",hex);
    }
    else  if(count == 4)
    {
       client.publish("MyApp/Register/ChDown",hex);
    }
    else  if(count == 5)
    {
       client.publish("MyApp/Register/VolUp",hex);
    }
    else  if(count == 6)
    {
       client.publish("MyApp/Register/VolDown",hex);
       count = 0;
    }
    Serial.println("");
    irrecv.resume();  //  After receiving, this must be called to reset the receiver and prepare it to receive another code. 
  }
  delay(100);
  client.loop();
}

unsigned long strToHex( char *arr )
{
    int l = strlen( arr )-1;
    int p = 0,tp;
    unsigned long val = 0,pow;
    int dig;
    char ch;
    while( l >= 0 )
    {
        ch = arr[l] ;
        switch(ch)
        {
            case 'a':
            dig = 10;
            break;
            case 'b':
            dig = 11;
            break;
            case 'c':
            dig = 12;
            break;
            case 'd':
            dig = 13;
            break;
            case 'e':
            dig = 14;
            break;
            case 'f':
            dig = 15;
            break;
            default:
            dig = ch - 48;
        }
        tp = p;
        pow = 1;
        while(tp > 0)
        {
            pow = pow * 16;
            tp--;
        }
        p++;
        val = val + ( pow * dig);
        l--;
    }
   printf("%lu",val);
    return val;
}
