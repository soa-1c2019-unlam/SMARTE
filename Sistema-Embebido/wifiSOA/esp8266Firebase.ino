#include <ESP8266WiFi.h>

#include <FirebaseArduino.h>

IPAddress ip(192,168,1,200);   
IPAddress gateway(192,168,1,1);   
IPAddress subnet(255,255,255,0);  

#define WIFI_SSID ""
#define WIFI_PASSWORD ""
#define FIREBASE_HOST "smarteapp2.firebaseio.com"
#define FIREBASE_AUTH "CldLG68wPna2ZMU9AhfYq4ruMfghQpSGcP0Qx38T"

int salida1 = 2;
int salida2 = 0;
int led1 = 10;

void setup()
{
pinMode(salida1,OUTPUT);
pinMode(led1,OUTPUT);
digitalWrite(salida1,LOW);
digitalWrite(led1,LOW);
pinMode(salida2,OUTPUT);
digitalWrite(salida2,LOW);
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
Firebase.setInt("LED1",20);

}

void loop()
{    
  if(Firebase.getInt("LED1") == 1)
  {
    Serial.println(Firebase.getInt("LED1/on"));
  }

  if (Firebase.failed()) {    
    Serial.print("setting /LED1 failed:");    
    Serial.println(Firebase.error());  
    return;
  }

  delay(1000);
}
