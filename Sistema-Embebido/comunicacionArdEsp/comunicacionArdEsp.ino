#include <SoftwareSerial.h>
#include <Servo.h>

/////////Funciones/////////
int readline();
void atenderYerba();
void atenderAzucar();
long readUltrasonicDistance();

/////////Variables globales/////////
SoftwareSerial mySerial(3,2);
Servo servo;
int posServoYerba = 80;
char buf[80];
int triggerPin = 10;
int echoPin = 9;


void setup() {
  Serial.begin(9600);
  mySerial.begin(9600);
  pinMode(13, OUTPUT);
  servo.attach(11);
  servo.write(0);
  pinMode(13,OUTPUT);
  digitalWrite(13,LOW);
  pinMode(triggerPin,OUTPUT);
  pinMode(echoPin,INPUT);
}

void loop() {
  
  //Serial.println(cm);
  while(mySerial.available()){
    if(readline(mySerial.read(),buf,80)>0){
      String str((char*)buf);

      long cm = readUltrasonicDistance();
      if( cm < 10){
        Serial.println(cm);
        if(str.equals("servoYerba/1")){
          Serial.println(str);
          atenderYerba();
        }
        if(str.equals("servoAzucar/1")){
          atenderAzucar();
        }
      }else {
        digitalWrite(13,LOW);
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
  servo.write(posServoYerba);
  delay(800);
  servo.write(0);
  mySerial.println("servoYerba/OFF");
}

void atenderAzucar(){
  
}

long readUltrasonicDistance(){

  long duration, distance;
  digitalWrite(triggerPin, HIGH);
  delayMicroseconds(500);
  digitalWrite(triggerPin, LOW);
  duration = pulseIn(echoPin,HIGH);
  distance = (duration/2)/29.4;
  delayMicroseconds(100);
  return distance;
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
      }*/
