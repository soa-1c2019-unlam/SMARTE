#include <ESP8266WiFi.h>

#include <FirebaseArduino.h>

IPAddress ip(192,168,1,200);   
IPAddress gateway(192,168,1,1);   
IPAddress subnet(255,255,255,0);  

#define WIFI_SSID "UF18_2.4G"
#define WIFI_PASSWORD "mortali2018"
#define FIREBASE_HOST "smarteapp2.firebaseio.com"
#define FIREBASE_AUTH "CldLG68wPna2ZMU9AhfYq4ruMfghQpSGcP0Qx38T"

int salida1 = 2;
int salida2 = 0;
int led1 = 10;

void setup()
{
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  
}

void loop()
{    
  if(Firebase.getInt("servoYerba"))
  {
    Serial.println("servoYerba/1");
    delay(500);
    String txt = Serial.readString();
    if(txt.indexOf("servoYerba/OFF")>-1){
      Firebase.setInt("servoYerba",0);  
    }
    
  }
  
  if(Firebase.getInt("servoAzucar"))
  {
    Serial.println("servoAzucar/1");
    delay(1000);
    Firebase.setInt("servoAzucar",0);
  }
  
  if (Firebase.failed()) {    
    Serial.print("Failed communicating to Firebase");    
    Serial.println(Firebase.error());  
    return;
  }

  delay(1000);
}
