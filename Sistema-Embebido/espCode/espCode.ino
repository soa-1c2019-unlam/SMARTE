#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

/*IPAddress ip(192,168,1,200);   
IPAddress gateway(192,168,1,1);   
IPAddress subnet(255,255,255,0);  
*/
#define WIFI_SSID "Honor 8X"
#define WIFI_PASSWORD "maxwifi14"
#define FIREBASE_HOST "smarteapp2.firebaseio.com"
#define FIREBASE_AUTH "CldLG68wPna2ZMU9AhfYq4ruMfghQpSGcP0Qx38T"

//Variables
bool yerbaSent = false;
bool azucarSent= false;
bool bombaSent = false;
bool termoSent = false;


void setup()
{
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  
  while (WiFi.status() != WL_CONNECTED) {
  delay(100);  
  }
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  Firebase.setInt("servoYerba",0);
  Firebase.setInt("servoAzucar",0);
  Firebase.setInt("bomba",0);
  
}

void loop()
{    
  
  if(Firebase.getInt("servoYerba") && !yerbaSent ){
    Serial.println("servoYerba/1");
    yerbaSent = true;
  }
  
  if(Firebase.getInt("servoAzucar") && !azucarSent){
    int cantAzucar = Firebase.getInt("cantAzucar");
    Serial.print("servoAzucar/");
    Serial.println(cantAzucar);
    azucarSent = true;
  }

  if(Firebase.getInt("bomba") && !bombaSent){
    Serial.println("bomba/1");
    bombaSent = true;
  }
 
  if(Firebase.getInt("termometro") && !termoSent){
    Serial.println("termometro/1");
  }

  String txt = Serial.readString();

  if(txt.indexOf("servoYerba/OFF")>-1){
    Firebase.setInt("servoYerba",0);
    yerbaSent = false;
  }
  else if(txt.indexOf("servoAzucar/OFF")>-1){
    Firebase.setInt("servoAzucar",0);
    azucarSent = false;
  }
  else if(txt.indexOf("bomba/OFF")>-1){
    Firebase.setInt("bomba",0);
    bombaSent = false; 
  }
  else if(txt.indexOf("termometro/OFF")>-1){
    Firebase.setInt("termometro",0);
    termoSent = false;
  }
  else if(txt.indexOf("bombaAplauso/OFF")>-1){
    Firebase.setInt("bombaAplauso",1);
  }
  else if(txt.indexOf("matePuesto/OFF")>-1){
    Firebase.setInt("matePuesto",0);
    Firebase.setInt("servoYerba",0);
    Firebase.setInt("servoAzucar",0);
    Firebase.setInt("bomba",0);
    yerbaSent = false;
    azucarSent = false;
    bombaSent = false;
  }

  if(Firebase.failed()) {    
    return;
  }

}
