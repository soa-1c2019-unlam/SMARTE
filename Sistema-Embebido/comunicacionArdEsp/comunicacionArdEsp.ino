#include <SoftwareSerial.h>
#include <Servo.h>

/////////Funciones/////////
int readline();
void atenderYerba();
void atenderAzucar();
void atenderBomba();
long readUltrasonicDistance();
void espera();
void esperaMicros();

/////////Variables globales/////////
SoftwareSerial mySerial(3,2);
Servo servoY;
Servo servoA;
int posServoYerba = 120;
int posServoAzucar = 80;
char buf[80];
int triggerPin = 10;
int echoPin = 9;
int rele = 8;
int soundSensor = 4;
int soundValue = 0;
bool soundState = false;


void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
  pinMode(13, OUTPUT);
  servoY.attach(11);
  servoY.write(0);
  servoA.attach(10);
  servoA.write(0);
  pinMode(13,OUTPUT);
  digitalWrite(13,LOW);
  pinMode(triggerPin,OUTPUT);
  pinMode(echoPin,INPUT);
  pinMode(rele,OUTPUT);
}

void loop() {
   
  while(mySerial.available()){
    if(readline(mySerial.read(),buf,80)>0){
      String str((char*)buf);

      if(str.equals("servoYerba/1")){
        atenderYerba();
      }
      
      if(str.indexOf("servoAzucar/")>=0){
        int cant = str.substring(12,13).toInt();
        atenderAzucar(cant);
      }
      
      if(str.equals("bomba/1")){
        atenderBomba();
      }
      
    }
  }
  
}

int readline(int readch, char *buffer, int len) {
    static int pos = 0;
    int rpos;

    if (readch > 0) {
        switch (readch) {
            case '\r': // Ignore CR
                break;
            case '\n': // Return on new-line
                rpos = pos;
                pos = 0;  // Reset position index ready for next time
                return rpos;
            default:
                if (pos < len-1) {
                    buffer[pos++] = readch;
                    buffer[pos] = 0;
                }
        }
    }
    return 0;
}

void atenderYerba(){
  long cm = 5;//readUltrasonicDistance();
  //Serial.print("centimetros: ");
  //Serial.println(cm);
  if(cm<=10){
    servoY.write(posServoYerba);
    espera(800);
    servoY.write(0);
    espera(200);
    
  }
  mySerial.println("servoYerba/OFF");
}

void atenderAzucar(int cant){
  long cm = 5;//readUltrasonicDistance();
 // Serial.print("centimetros: ");
  //Serial.println(cm);
  int i=0;
  if(cm<=10){
    for(i=0;i<cant;i++){
      servoA.write(posServoAzucar);
      espera(800);
      servoA.write(0);
      espera(200);
    }
  }
  mySerial.println("servoAzucar/OFF");
}

void atenderBomba(){
 //readUltrasonicDistance();
  //Serial.print("centimetros: ");
  //Serial.println(cm);
 
    digitalWrite(8,HIGH);
    espera(1000);
    Serial.println("high");
    digitalWrite(8,LOW);
    Serial.println("low");
    espera(100);
    Serial.println("espera");
    mySerial.println("bomba/OFF");  
    Serial.println("bomba off");
  
}

long readUltrasonicDistance(){

  digitalWrite(triggerPin,LOW);
  esperaMicros(2);
  digitalWrite(triggerPin,HIGH);
  esperaMicros(10);
  digitalWrite(triggerPin,LOW);
  return 0.01723*pulseIn(echoPin,HIGH);
}

void espera(long tiempo){
  long ini = millis();

  while((millis()-ini) <= tiempo){

    //rutinas de atencion que necesitemos
  }
}

void esperaMicros(long tiempo){
  long ini = micros();

  while((micros()-ini) <= tiempo){

    //rutinas de atencion que necesitemos
  }
}

// cosas que estaban dentro del loop pero me dio cosa borrar

 /*//Serial.println(str.indexOf("servo"));
      Serial.println(str);
      if(str.equals("Both ON")){
        //Serial.println("Entre");
        digitalWrite(13,HIGH);
      } else if(str.equals("Both OFF")){
        digitalWrite(13,LOW);
      } else if(str.indexOf("servo")>0){
        Serial.println(str.substring(7,9));
        servo.write(str.substring(7,9).toInt()-);




         for(int i=0;i<1000;i++){
      soundValue = analogRead(soundSensor);
      espera(10);
      
      if(soundValue > 100){
        soundState = !soundState;
        break;
      }
    }
  }
  if(soundState){
    atenderBomba(0);
  }
      }*/
