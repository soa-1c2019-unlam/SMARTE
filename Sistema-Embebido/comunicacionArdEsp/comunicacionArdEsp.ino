#include <SoftwareSerial.h>
SoftwareSerial mySerial(2,3);
#include <Servo.h>

Servo servo;

char buf[80];

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


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  mySerial.begin(9600);
  pinMode(13, OUTPUT);
  servo.attach(10);
  
}

void loop() {
  // put your main code here, to run repeatedly:

  while(mySerial.available()){
    if(readline(mySerial.read(),buf,80)>0){
      //Serial.print("Recibido: < ");
      //Serial.print(buf);
      //Serial.println(" >");

      String str((char*)buf);
      Serial.println(str.indexOf("servo"));
      Serial.println(str);
      if(str.equals("Both ON")){
        Serial.println("Entre");
        digitalWrite(13,HIGH);
      } else if(str.equals("Both OFF")){
        digitalWrite(13,LOW);
      } else if(str.indexOf("servo")>0){
        Serial.println(str.substring(7,9));
        servo.write(str.substring(7,9).toInt()-);
      }
    }
  }

 
  
}
