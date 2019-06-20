#include <dht.h>

#define dht_apin A0

dht DHT;
int pwmLed = 11;
int lvl;
const int maxTemp = 40;
float tempActual;
int tempLed = 2;
float iniTemp = 0;
bool firstTemp = true;
int parseTemp();
int filterTemp(float);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println("DHT11 Example");
  pinMode(pwmLed,OUTPUT);
  pinMode(tempLed,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  DHT.read11(dht_apin);
  tempActual = DHT.temperature;
  
  if(firstTemp){
    iniTemp = tempActual-1;
    firstTemp = false;
  }
  if(tempActual > maxTemp){
    analogWrite(pwmLed,0);
    digitalWrite(tempLed,HIGH);
    Serial.print("Temperatura = ");
    Serial.print(tempActual);
    Serial.println("ºC");
  }//Aca aplico la histeresis
  else if(tempActual < (maxTemp - 1) && tempActual > 15){
    digitalWrite(tempLed,LOW);
    Serial.print("Temperatura = ");
    Serial.print(tempActual);
    Serial.println("ºC");
    lvl = parseTemp(tempActual);
    analogWrite(pwmLed, lvl);
  }

  
  
  
}

int parseTemp(float temp){

  float value = maxTemp;
  float aux;
  value = value - temp;
  aux = (-1)*(((value*200)/iniTemp)-200);
  Serial.println(aux);
  
  return (int) aux;
}

int filterTemp(float temp){
  return 0;
}
