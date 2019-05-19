#include <dht.h>

#define dht_apin A0

dht DHT;
int pwmLed = 10;
int lvl;
int tempLed = 2;

int parseTemp();

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  delay(500);
  
  Serial.println("DHT11 Example");
  pinMode(pwmLed,OUTPUT);
  pinMode(tempLed,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  DHT.read11(dht_apin);
  if(DHT.temperature >35){
    analogWrite(pwmLed,0);
    digitalWrite(tempLed,HIGH);
  }//Aca aplico la histeresis
  else if(DHT.temperature <34){
    digitalWrite(tempLed,LOW);
    Serial.print("Humedad = ");
    Serial.print(DHT.humidity);
    Serial.print("% | Temperatura = ");
    Serial.print(DHT.temperature);
    Serial.println("ºC");
    lvl = parseTemp(DHT.temperature);
    analogWrite(pwmLed, lvl);
  }
  
  delay(1500);
  
}

int parseTemp(float temp){

  float value = 36;
  float aux;
  value = value - temp;
  aux = (-1)*(((value*255)/13)-255);
  Serial.println(aux);
  
  return (int) aux;
}
