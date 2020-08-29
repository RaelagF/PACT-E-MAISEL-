#include <ESP8266WiFi.h> //Bibliothèque pour faire fonctionner le composant WiFi de la carte (bas niveai)
#include <SPI.h>
#include <WiFiManager.h> //Bibliothèque pour la connection user-frindly (Server puis Client) 
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
#include <avr/power.h>
#endif


/*** WIFI FRIENDLY
 *  Fonction utilisé par la bibliothèque pour l'établiessement de la première connection
 *  Permet le débogage pas nécessaire au fonctionnement 
 *  Si la connection fail 
***/
void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode"); 
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
}

//PARSING
#include <string.h>
#include <stdio.h>


/*** 
 *  Paramètres du serveur
 */
WiFiClient client;
IPAddress remoteIP(35,189,106,175); //IP of the server is fix 
unsigned int remotePort = 50000; //port ouvert sur le serveur 
byte ledPin = 2;

//Specifique 


#define PIN 2
#define NBRPIXEL 4
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NBRPIXEL, PIN, NEO_GRB + NEO_KHZ800);


int led1, led2, led3;



/***SETUP  
 *  Le Setup est plus long, il a la particularité de ne s'executer qu'une fois et donc 
 *  de laisser dans le loop le minimum de choses et un code plus rapide et robuste. On execute le maximum 
 *  de choses dans le setup
***/


void setup() {

  strip.show(); //Initialize all pixels to 'off'

  Serial.begin(115200); //Haute fréquence de communication (beuds) mieux 
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
   pinMode(ledPin, OUTPUT);
/***SPECIFIC DEFINE 
 *  Add here specific functions 
***/

   
}

void loop() 
{

  //SPECIFIC EXECUTION
  if (led1==0){strip.setPixelColor(0,strip.Color(255, 0, 0)); strip.show() ;};// led is red and machine is being used
    if (led1==1){strip.setPixelColor(0,strip.Color(0, 255, 0)); strip.show() ;};//led is green and machine can be used
    if (led1==2){strip.setPixelColor(0,strip.Color(0, 0, 255)); strip.show() ;};//led is blue machine is reserved
    if (led2==0){strip.setPixelColor(1,strip.Color(255, 0, 0)); strip.show() ;};// led is red and machine is being used
    if (led2==1){strip.setPixelColor(1,strip.Color(0, 255, 0)); strip.show() ;};//led is green and machine can be used
    if (led2==2){strip.setPixelColor(1,strip.Color(0, 0, 255)); strip.show() ;};//led is blue machine is reserve
    if (led3==0){strip.setPixelColor(2,strip.Color(255, 0, 0)); strip.show() ;};// led is red and machine is being used
    if (led3==1){strip.setPixelColor(2,strip.Color(0, 255, 0)); strip.show() ;};//led is green and machine can be used
    if (led3==2){strip.setPixelColor(2,strip.Color(0, 0, 255)); strip.show() ;};//led is blue machine is reserve
    
  
  
  
  

 //CONNECTION 
  client.connect(remoteIP, remotePort);   // Connection to the server
  digitalWrite(ledPin, LOW);    //debug
  Serial.println("."); 
  client.write("hardware\n");  // sends the message to the server SEND hardware
  
  String answer = client.readStringUntil('\n');   // receives the answer from the sever should recieve OK 
  //ask AGAIN 
 
  Serial.println("from server: " + answer);
  client.write("get washing machine states\n");
  String ok = client.readStringUntil('\n');
  Serial.println(ok);
  
  client.write("ok\n");
  client.flush();
  String data = client.readStringUntil('\n');
  
  led1=data.substring(0,1).toInt(); //ça parait pas comme ça j'ai mis 3h pour trouver 
  led2=data.substring(2,3).toInt();
  led3=data.substring(4,6).toInt();
  
    Serial.println(data);
   Serial.println(led2);
  client.flush();
  digitalWrite(ledPin, HIGH);
  delay(1000);

}

   

