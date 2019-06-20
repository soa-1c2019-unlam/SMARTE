#include <SoftwareSerial.h>
#include <Servo.h>
#include <dht.h>


/////////Funciones/////////
int readline();
void atenderYerba();
void atenderAzucar();
void atenderBomba();
void atenderTermometro();
long readUltrasonicDistance();
long medirDistancia();
long promedioUltra();
int parseTemp();
int filterTemp(float);

/////////Variables Generales/////////
SoftwareSerial esp(3,2);
String data = "";

/////////Variables Servos/////////
Servo servoYb;
Servo servoAz;
int posServoYb = 120;
int posServoAz = 80;

long timeYb = millis();
long waitYb = 800;
bool stateYb = true;

long timeAz = millis();
long waitAz = 400;
bool stateAz = true;
int cantAzucar = 1;


/////////Variables UltraSonido/////////
#define MEDIR_USONIDO 9999
int triggerPin = 10;
int echoPin = 9;
long cm;
const int mediciones = 10; 
long aux[mediciones]; 
int iUltra = 0;
bool seguirMidiendo = false;
bool yaAbri = false;
bool stateDistancia = true;
long timeDistancia = millis();


/////////Variables Bomba/////////
int rele = 6;
long timeBom = millis();
long waitBom = 2000;
bool stateBom = true;

/////////Variables Microfono/////////
int soundSensor = 4;
int soundValue = 0;
bool soundState = false;

/////////Variables Termometro/////////
long timeTerm = millis();
const long waitTerm = 800;
bool stateTerm = true;
dht DHT;
const int pwmLed = 5;
int nivelLed;
const int maxTemp = 40;
const int minTemp = 15;
float tempActual;
const int tempLed = 4;
float iniTemp = 0;
bool firstTemp = true;


void setup() {
  Serial.begin(9600);
  esp.begin(9600);
  
  servoYb.attach(11);
  servoYb.write(0);
  servoAz.attach(12);
  servoAz.write(0);
  
  pinMode(triggerPin,OUTPUT);
  pinMode(echoPin,INPUT);
  pinMode(rele,OUTPUT);
 
}

void loop() {
   
  if(esp.available() > 0){
    data = esp.readStringUntil('\n');   
  }
  
  if(data.indexOf("servoYerba/1")>-1 || !stateYb || seguirMidiendo){
    atenderYerba();
  }
  else if(data.indexOf("servoAzucar/")>-1 || !stateAz || seguirMidiendo){
    if(stateAz){
      cantAzucar = data.substring(12,13).toInt();
    }
    atenderAzucar();
  }
  else if(data.indexOf("bomba/1")>-1 || !stateBom || seguirMidiendo){
    atenderBomba();
  }
  else if(data.indexOf("termometro/1")>-1 || !stateTerm){
    atenderTermometro();
  }

  data = "";
}

void atenderYerba(){
  if(!yaAbri){
    cm = medirDistancia();
  }    
  if((cm <= 10 && cm > 0)){
    if(stateYb){
      timeYb = millis();
      stateYb = false;
      servoYb.write(posServoYb);
      yaAbri = true;
    }
    if((millis() - timeYb) > waitYb){
      servoYb.write(0);
      stateYb = true;
      yaAbri = false;
      esp.println("servoYerba/OFF");
    }
    seguirMidiendo = false;
  }
  else if ( cm != MEDIR_USONIDO ){
    esp.println("matePuesto/OFF");
    seguirMidiendo = false;
    yaAbri = false;
  }
  if(cm == MEDIR_USONIDO){
    seguirMidiendo = true;
  }
}

void atenderAzucar(){
  if(!yaAbri){
    cm = medirDistancia();
  }    
  
  if(cm <= 10 && cm > 0){
    if(cantAzucar > 0){
      if(stateAz){
        timeAz = millis();
        stateAz = false;
        yaAbri = true;
        servoAz.write(posServoAz);
      }
      if((millis() - timeAz) > waitAz){
        servoAz.write(0);
        stateAz = true;
        yaAbri = false;
        
      }
      seguirMidiendo = false;
      cantAzucar--;
    }
    else{
      esp.println("servoAzucar/OFF");
    }
  }
  else if ( cm != MEDIR_USONIDO){
    esp.println("matePuesto/OFF");
    seguirMidiendo = false;
    yaAbri = false;
  }
  if(cm == MEDIR_USONIDO){
    seguirMidiendo = true;
  }
    
  
  
}

void atenderBomba(){
  if(!yaAbri){
    cm = medirDistancia();
  }  
  if(cm <= 10 && cm > 0){
    if(stateBom){
      timeBom = millis();
      stateBom = false;
      yaAbri = true;
      digitalWrite(rele,HIGH);
    }

    if((millis() - timeBom) > waitBom){
      digitalWrite(rele,LOW);
      stateBom = true;
      yaAbri = false;
      esp.println("bomba/OFF");
    }
    seguirMidiendo = false;
  }
  else if ( cm != MEDIR_USONIDO ){
    esp.println("matePuesto/OFF");
    seguirMidiendo = false;
    yaAbri = false;
  }
  if(cm == MEDIR_USONIDO){
    seguirMidiendo = true;
  }
}

void atenderTermometro(){
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
    stateTerm = true;
    esp.println("termometro/OFF");
  }//Aca se aplica la histeresis
  else if(tempActual < (maxTemp - 1) && tempActual > minTemp){
    digitalWrite(tempLed,LOW);
    Serial.print("Temperatura = ");
    Serial.print(tempActual);
    Serial.println("ºC");
    lvl = parseTemp(tempActual);
    analogWrite(pwmLed, lvl);
    stateTerm = false;
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


long readUltrasonicDistance(){

  digitalWrite(triggerPin,LOW);
  delayMicroseconds(2);
  digitalWrite(triggerPin,HIGH);
  delayMicroseconds(10);
  digitalWrite(triggerPin,LOW);
  return 0.01723*pulseIn(echoPin,HIGH);
}

long medirDistancia(){
  
  long promedio = 0;
  delay(100);

  if(stateDistancia){
    timeDistancia = millis();
    stateDistancia = false;
  }
  if((millis() - timeDistancia) > 80){
  
    if(iUltra<mediciones){
      aux[iUltra] = readUltrasonicDistance();
      iUltra++;
    }
    if(iUltra == (mediciones - 1)){
      promedio = promedioUltra();
      iUltra = 0;
      return promedio;
    }
    stateDistancia = true;
  }
  return MEDIR_USONIDO;
  
}

long promedioUltra(){
  int cant = 0; 
  int suma = 0;

  for(int i =0; i<mediciones;i++){
    if(aux[i]>0 && aux[i]<15){
      suma += aux[i];
      cant++;
    }
  }
  return suma/cant;
}
