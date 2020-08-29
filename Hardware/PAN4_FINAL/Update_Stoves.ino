#include <ESP8266WiFi.h> //Bibliothèque pour faire fonctionner le composant WiFi de la carte (bas niveai)
#include <SPI.h>
#include <WiFiManager.h> //Bibliothèque pour la connection user-frindly (Server puis Client) 



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


/*** 
 *  Paramètres du serveur
 */
WiFiClient client;
IPAddress remoteIP(35,189,106,175); //IP of the server is fix 
unsigned int remotePort = 50000; //port ouvert sur le serveur 
byte ledPin = 2;

//Specifique 

//CALCUL du courant qui passe (True RMS) 
float get_current()
{
  float voltSensor;
  float current=0;
  float sum=0;
  long temps=millis();
  int N=0;
  while(millis()-temps<500)
  { 
    voltSensor = analogRead(A0) * (1.1 / 1023.0);//Mapper à la range du capteur octet
    current=voltSensor*30.0; //On mappe au max à 30 A 
    sum=sum+sq(current);//sum carré
    N=N+1;
    delay(1);
  }
  sum=sum*2;//redresser le signal des negatifs
  current=sqrt((sum)/N); //True RMS equation
  return(current);
}

/***SETUP  
 *  Le Setup est plus long, il a la particularité de ne s'executer qu'une fois et donc 
 *  de laisser dans le loop le minimum de choses et un code plus rapide et robuste. On execute le maximum 
 *  de choses dans le setup
***/


void setup() {
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
  float I=get_current(); //Valeur efficace
  float P=I*220.0;
  Serial.println(P);
  int etatfiltree=0;
  if (P<500){
    etatfiltree=0;
  }
  if (500<P<1000){
    etatfiltree=1;   //nombre de plaques valeurs à définirs 
  }
   if (1000<P<2000){
    etatfiltree=2;
   }
  delay(100) ;
  



 //CONNECTION 
  client.connect(remoteIP, remotePort);   // Connection to the server
  digitalWrite(ledPin, LOW);    //debug
  Serial.println("."); 
  client.println("hardware\n");  // sends the message to the server SEND hardware
  String answer = client.readStringUntil('\n');   // receives the answer from the sever should recieve OK 
  //ask AGAIN 
  Serial.println("from server: " + answer +answer.toInt());
  client.println("update stove states\n");
  client.println(etatfiltree+"0 0 0 0 1 0 0 0\n");
  client.flush();
  digitalWrite(ledPin, HIGH);
  delay(100);

}

   

