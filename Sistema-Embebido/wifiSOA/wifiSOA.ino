//Librerias usadas
#include <ESP8266WiFi.h>

//Credenciales de red
const char* ssid = "UF18_2.4G";
const char* password = "mortali2018";

//Configuracion IP
IPAddress ip(192,168,1,200);   
IPAddress gateway(192,168,1,1);   
IPAddress subnet(255,255,255,0);

//Puerto del Web Server
WiFiServer server(80);

//Variable donde se guarda el HTTP Request
String header;

//Auxiliares de lectura
const int output2 = 2;
const int output0 = 0;

//Funciones usadas

int recortarServoInst(char*);

void setup() {
  Serial.begin(9600);
  //Inicializar pines
  pinMode(output2,OUTPUT);
  pinMode(output0,OUTPUT);

  digitalWrite(output2,LOW);
  digitalWrite(output0,LOW);

  //Conectar con red WiFi
  WiFi.config(ip, gateway, subnet);
  WiFi.begin(ssid,password);
  while(WiFi.status() != WL_CONNECTED){
    delay(1);
  }
  Serial.println(WiFi.localIP());
  server.begin();
}

void loop() {

  //Escuchamos clientes entrantes
  WiFiClient client = server.available();

  if(client) {
    String currentLine = "";
    while (client.connected()){
      if(client.available()){
        char c = client.read();
        header += c;
        if(c == '\n'){
          if(currentLine.length() == 0){

            client.println(WiFi.localIP());
            //Condiciones
            if (header.indexOf("GET /0/on") >= 0) {
              Serial.println("/0/on");
              digitalWrite(output0, HIGH);
            } else if (header.indexOf("GET /0/off") >= 0) {
              Serial.println("/0/off");
              digitalWrite(output0, LOW);
            } else if (header.indexOf("GET /2/on") >= 0) {
              Serial.println("/2/on");
              digitalWrite(output2, HIGH);
            } else if (header.indexOf("GET /2/off") >= 0) {
              Serial.println("/2/off");
              digitalWrite(output2, LOW);
            } else if (header.indexOf("GET /both/on") >= 0) { 
              Serial.println("/both/on");
              digitalWrite(output0, HIGH);
              digitalWrite(output2, HIGH);
            } else if (header.indexOf("GET /both/off") >= 0) { 
              Serial.println("/both/off");
              digitalWrite(output0, LOW);
              digitalWrite(output2, LOW);
            } else if(header.indexOf("GET /servo") >= 0) {
              Serial.println(header.substring(4,13));
            }
           
          } else {
            currentLine = "";
          }
        } else if(c != '\r'){
          currentLine += c;
        }
      }
    }

    header = "";

    //Cerramos conexion
    client.stop();
    
  }
}

int recortarServoInst(char* buf){

  char *ptr;
  *buf = *(buf + 4);

  ptr = strchr(buf,' ');
  *ptr = '\0';

  return 1;
}
