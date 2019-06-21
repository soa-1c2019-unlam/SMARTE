#ifndef _SMARTE_H
#define _SMARTE_H
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
const int soundSensor = A2;
int soundValue = 0;
//bool soundState = false;
long timeSound = millis();
const long delaySound = 1000;
long waitSound = millis();
const long delayWaitSound = 50;
bool primerClap = true;

/////////Variables Termometro/////////
long timeTerm = millis();
const long waitTerm = 800;
bool stateTerm = true;
#define dht_apin A0
dht DHT;
const int pwmLed = 5;
int nivelLed;
const int maxTemp = 40;
const int minTemp = 15;
float tempActual;
const int tempLed = 4;
float iniTemp = 0;
bool firstTemp = true;


#endif // _SMARTE_H
