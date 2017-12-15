#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>
#include "EmonLib.h" 

// Update these with values suitable for your network.
byte mac[]    = {  0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED };
IPAddress ip(192, 168, 2, 102);
IPAddress server(192, 168, 2, 101);

int pinoRele = 9;  //  Pino digital onde será ligado e desligado o LED.
int pinoSct = 1;
 
EnergyMonitor emon1;

//Tensao da rede eletrica
int rede = 220.0;

EthernetClient ethClient;
PubSubClient client(ethClient);

String topicoDispositivos = "/USUARIO/DISPOSITIVOS";
String topicoRele = "/USUARIO/SALA/1/ATUADOR/RELE/1";
String topicoCorrente = "/USUARIO/SALA/1/SENSOR/CORRENTE/1";
String topicoCorrenteMedicao = "/USUARIO/SALA/1/SENSOR/CORRENTE/1/MEDICAO";


bool releLigado = true;

int ledPin = 8; 
String topicoTeste = "teste";

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (int i=0;i<length;i++) {
    Serial.print((char)payload[i]);
  }
   Serial.println();

  if((String)topic == topicoDispositivos){
    if((char)payload[0] == '1'){
      if(releLigado == true){
        client.publish(topicoDispositivos.c_str(),"RELE ON");        
      } else {
          client.publish(topicoDispositivos.c_str(),"RELE OFF");                
      
      }
      client.publish(topicoDispositivos.c_str(),"CORRENTE ON");
    }
  }
  
  if((String)topic == topicoRele){
    if((char)payload[0] == '1'){
        digitalWrite(pinoRele, LOW);  // Liga Lampada.
        releLigado = true;
    }else{
      if((char)payload[0] == '0'){
          digitalWrite(pinoRele, HIGH);  // Desliga Lampada.
          releLigado = false;
        }
    }
    if(releLigado == true){
        client.publish(topicoDispositivos.c_str(),"RELE ON");        
      } else {
          client.publish(topicoDispositivos.c_str(),"RELE OFF");                
      }
      client.publish(topicoDispositivos.c_str(),"CORRENTE ON");
          
  }
  
  if((String)topic == topicoCorrente.c_str()){
    double Irms = emon1.calcIrms(1480);
    String msg = ("Corrente: " + (String)Irms + " Potência: " + Irms * rede);
    client.publish(topicoCorrenteMedicao.c_str(), msg.c_str());
  }

  if((String)topic == topicoTeste.c_str()){
    if((char)payload[0] == '1'){
        digitalWrite(ledPin, HIGH);   // liga o LED
    }
    else{
      if((char)payload[0] == '0'){
        digitalWrite(ledPin, LOW);    // desliga o LED
      }
    }

    
 }
}



void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.println("Tentando conectar com MQTT...");
    // Attempt to connect
    if (client.connect("arduinoClient")) {
      Serial.println("connected");

      client.subscribe(topicoDispositivos.c_str());
      if(releLigado == true){
        client.publish(topicoDispositivos.c_str(),"RELE ON");        
      } else {
          client.publish(topicoDispositivos.c_str(),"RELE OFF");                
      }
      client.publish(topicoDispositivos.c_str(),"CORRENTE ON");
      
      client.subscribe(topicoRele.c_str());
      client.subscribe(topicoCorrente.c_str());

      client.subscribe(topicoTeste.c_str());

    } else {
      Serial.print("failed, rc = ");
      Serial.print(client.state());
      Serial.println(" aguarde 5 secondos");
      delay(5000);
    }    
  }
}

void setup(){
  Serial.begin(9600);
  //Pino, calibracao - Cur Const= Ratio/BurdenR. 1800/62 = 29. 
  emon1.current(pinoSct, 29);
  pinMode(pinoRele, OUTPUT);  // Define o Pino 9 como saída.
  client.setServer(server, 1883);
  client.setCallback(callback);
  pinMode(ledPin, OUTPUT); 
  Ethernet.begin(mac, ip);
  // Allow the hardware to sort itself out
  delay(1500);
}

void loop(){
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  
}
