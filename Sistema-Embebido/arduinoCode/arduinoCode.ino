#include "smarte.h"

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
  pinMode(ledSinMate,OUTPUT);
  pinMode(pwmLed,OUTPUT);
  pinMode(tempLed,OUTPUT);
 
}

void loop() {

  //Deteccion de aplauso
  soundValue = analogRead(soundSensor );
  if(soundValue > umbralSonido){
    Serial.println(soundValue);
    if(primerClap){
      timeSound = millis();
      waitSound = millis();
      primerClap = false;
    }
    else{
      if((millis()- waitSound)>delayWaitSound){
        if((millis()- timeSound) < delaySound){
          primerClap = true;
          bombaAplauso = true;
          atenderBombaAplauso();
        }
      }      
    }
  }
  if((millis()- timeSound) > delaySound){
    primerClap = true;
  }
  if(bombaAplauso){
    atenderBombaAplauso();
  }

  //Lectura de Serial del ESP
  if(esp.available() > 0){
    data = esp.readStringUntil('\n');
    Serial.println(data);
  }
  
  if(data.indexOf("servoYerba/1")>-1 || !stateYb || seguirMidiendoYb){
    atenderYerba();
  }
  if(data.indexOf("servoAzucar/")>-1 || !stateAz || seguirMidiendoAz){
    if(boolCantAz){
      //Obtengo cantidad de azucar
      cantAzucar = data.substring(12,13).toInt();
      boolCantAz = false;
    }
    atenderAzucar();
  }
  if(data.indexOf("bomba/1")>-1 || !stateBom || seguirMidiendoBom){
    Serial.print("Estado Bomba: ");
    Serial.println(stateBom);
    Serial.print("Seguir midiendo bom: ");
    Serial.println(seguirMidiendoBom);
    Serial.println(data);
    
    atenderBomba();
  }
  if(data.indexOf("termometro/1")>-1 || !stateTerm){
    digitalWrite(tempLed,LOW);
    atenderTermometro();
  }

  data = "";
  
}

void atenderYerba(){

  //Checkequeo si ya detecte que estaba cerca el mate
  if(!yaAbri){
    cm = medirDistancia();
  }
  //Checkequeo distancia del mate
  if((cm <= 10 && cm > 0)){
    digitalWrite(ledSinMate, LOW);
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
    seguirMidiendoYb = false;
  }//Si no se detecta el mate se lo comunico al ESP
  else if ( cm != MEDIR_USONIDO ){
    esp.println("matePuesto/OFF");
    seguirMidiendoYb = false;
    yaAbri = false;
    digitalWrite(ledSinMate, HIGH);
  }//MEDIR_USONIDO es la variable que me indica si debo continuar la medicion (para obetener un promedio)
  if(cm == MEDIR_USONIDO){
    seguirMidiendoYb = true;
  }
}

void atenderAzucar(){
  if(!yaAbri){
    cm = medirDistancia();
  }    
  if(cm <= 10 && cm > 0){
    digitalWrite(ledSinMate, LOW);
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
        cantAzucar = cantAzucar - 1;
      }
    }
    else{
      seguirMidiendoAz = false;
      esp.println("servoAzucar/OFF");
      stateAz = true;
      boolCantAz = true;
      yaAbri = false;
    }
  }
  else if ( cm != MEDIR_USONIDO){
    esp.println("matePuesto/OFF");
    seguirMidiendoAz = false;
    yaAbri = false;
    stateAz = true;
    boolCantAz = true;
    digitalWrite(ledSinMate, HIGH);
  }
  if(cm == MEDIR_USONIDO){
    seguirMidiendoAz = true;
  }
  
}

void atenderBomba(){
  if(!yaAbri){
    cm = medirDistancia();
    Serial.print("CM medido: ");
    Serial.println(cm);
    
  }  
  Serial.print("Estado Bomba: ");
  Serial.println(stateBom);
    
  if(cm <= 10 && cm > 0){
    digitalWrite(ledSinMate, LOW);
    if(stateBom){
      timeBom = millis();
      stateBom = false;
      yaAbri = true;
      Serial.println("Abro rele");
      digitalWrite(rele,HIGH);
    }

    if((millis() - timeBom) > waitBom){
      digitalWrite(rele,LOW);
      stateBom = true;
      yaAbri = false;
      Serial.println("Apago Bomba");
      esp.println("bomba/OFF");
    }
    seguirMidiendoBom = false;
  }
  else if ( cm != MEDIR_USONIDO ){
    esp.println("matePuesto/OFF");
    seguirMidiendoBom = false;
    yaAbri = false;
    digitalWrite(ledSinMate, HIGH);
  }
  if(cm == MEDIR_USONIDO){
    seguirMidiendoBom = true;
  }
}


void atenderBombaAplauso(){
  if(!yaAbri){
    cm = medirDistancia();
    Serial.print("CM medido: ");
    Serial.println(cm);
  }  
  if(cm <= 10 && cm > 0){
    digitalWrite(ledSinMate, LOW);
    if(stateAplauso){
      timeBom = millis();
      stateAplauso = false;
      yaAbri = true;
      Serial.println("Abro rele");
      digitalWrite(rele,HIGH);
    }

    if((millis() - timeBom) > waitBom){
      digitalWrite(rele,LOW);
      stateAplauso= true;
      yaAbri = false;
      bombaAplauso = false;
      esp.println("bombaAplauso/OFF");
      Serial.println("Apago Bomba");
    }
    
  }
  else if ( cm != MEDIR_USONIDO ){
    digitalWrite(ledSinMate, HIGH);
    yaAbri = false;
  }
  
}

void atenderTermometro(){
  DHT.read11(dht_apin);
  tempActual = DHT.temperature;
  
  if(firstTemp){
    iniTemp = tempActual-1;
    Serial.print("Primer temp: ");
    Serial.println(iniTemp);
    
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
    nivelLed = parseTemp(tempActual);
    analogWrite(pwmLed, nivelLed);
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
