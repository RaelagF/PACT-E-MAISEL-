#include <ESP8266WiFi.h> //Bibliothèque pour faire fonctionner le composant WiFi de la carte (bas niveai)

#include <WiFiManager.h> //Bibliothèque pour la connection user-frindly (Server puis Client) 
#include <Wire.h>
#include <VL53L0X.h>
#include <EEPROM.h>

/*** WIFI FRIENDLY
    Fonction utilisé par la bibliothèque pour l'établiessement de la première connection
    Permet le débogage pas nécessaire au fonctionnement
    Si la connection fail
***/
void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
}

/***
    Paramètres du serveur
*/
WiFiClient client;
IPAddress remoteIP(35, 189, 106, 175); //IP of the server is fix
unsigned int remotePort = 50000; //port ouvert sur le serveur

VL53L0X sensor1;
VL53L0X sensor2;
int nbrpers ;
int etat1;
int etat2;
int prec1;
int prec2;
int initial;



void setup() {
  Serial.begin(115200);


  //WIFIMANAGER

  WiFiManager wifiManager; //Init
  wifiManager.setAPCallback(configModeCallback); //Se connecte au wifi precedent en memoire
  //1 essaye l'ancien ssid pass
  //2 se mets en hostpots avec les parametre de autoconnect (par defaut APconnect et aucun mdp

  //si ça marche pas
  if (!wifiManager.autoConnect("E-Maisel config", "config")) { //ssid and password
    Serial.println("failed to connect and hit timeout");
    //rreset et recommence
    ESP.reset();
    delay(1000);
  }
  Serial.println("I have the high ground"); //Debog




  nbrpers = 0;


  Wire.begin(12, 14);
  sensor2.init();
  sensor2.setTimeout(500);

  Wire.pins(4, 5);
  Wire.begin();
  sensor1.init();
  sensor1.setTimeout(500);

  etat1 = 0;
  etat2 = 0;
  initial = 0;





}

void loop() {
  prec1 = etat1;
  prec2 = etat2;
  Wire.pins(4, 5);
  Wire.begin();
  uint16_t s1 = sensor1.readRangeSingleMillimeters();
  if (sensor1.timeoutOccurred()) {
    Serial.print("S1 TIMEOUT");
  };
  Wire.pins(12, 14);
  Wire.begin();
  uint16_t s2 = sensor2.readRangeSingleMillimeters();
  if (sensor2.timeoutOccurred()) {
    Serial.print("S2 TIMEOUT");
  }
  int d1 = (int)s1;
  int d2 = (int)s2;

  if (d1 < 1000) {
    etat1 = 1;
  }
  else {
    etat1 = 0;
  }


  if (d2 < 1000) {
    etat2 = 1;
  }
  else {
    etat2 = 0;
  }


  if (prec1 == 0 && prec2 == 0) {
    if (etat1 == 1 && etat2 == 0) {
      initial = 1;
    }
    if (etat2 == 1 && etat1 == 0) {
      initial = 2;
    }
  }
  if (prec1 == 1 && prec2 == 1) {
    if (etat1 == 0 && initial == 1) {
      nbrpers++;
      Serial.println(nbrpers);
    }
    if (etat2 == 0 && initial == 2) {
      nbrpers--;
      Serial.println(nbrpers);
    }
  }



  //CONNECTION
  client.connect(remoteIP, remotePort);   // Connection to the server

  Serial.println(".");
  client.print("hardware\n");  // sends the message to the server SEND hardware
  String answer = client.readStringUntil('\n');   // receives the answer from the sever should recieve OK
  //ask AGAIN
  Serial.println("from server: " + answer);
  client.print("update affluence\n");
  client.readStringUntil('\n');
  client.print(String(nbrpers) + " 0 0 0 0 0 0\n");
  client.readStringUntil('\n');
  client.flush();

  delay(2000);




}

