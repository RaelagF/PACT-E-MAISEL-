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


//Specifique 


bool etat=digitalRead(14);
int etat2=analogRead(0);
int id=0; //EVENTUELLEMENT RAJOUTER UNE BOUCLE FOR SUR ID DANS LE LOOP QUI REFLITRE ET RENVOIE POST
bool etatfiltree=LOW ;
String stringfiltree;
int sum=0;
byte ledPin = 2;

String stringing(bool stateto){  //pour etre sur que la conversion en char soit propre
  String r;
  stateto == 0 ? r = "0 0 0 0 1 0 0 0\n" : r = "1 0 0 0 1 0 0 0\n";
  return(r);
  
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
  //SPECIFIC

  
    for (int i =0; i<= 10; i=i+1){
    
    etat2=analogRead(0);
    etat=digitalRead(14);
    sum+=etat2;
  delay(100) ;
    Serial.println(sum);
  }
  if (sum>1000){
    etatfiltree=HIGH;
    sum=0;
  }
  else{
    etatfiltree=LOW;
    sum=0;
  }
   stringfiltree=stringing(etatfiltree);
  
  Serial.println(stringfiltree);




 //CONNECTION 
  client.connect(remoteIP, remotePort);   // Connection to the server
  digitalWrite(ledPin, LOW);    //debug
  Serial.println("."); 
  client.write("hardware\n");  // sends the message to the server SEND hardware
  client.flush();
  String answer = client.readStringUntil('\n');   // receives the answer from the sever should recieve OK 
  //ask AGAIN 
  Serial.println("from server: " + answer+"\n");
  client.flush(); 
  client.write("update washing machine states\n");
  client.flush(); 
  String secondanswer=client.readStringUntil('\n');
  Serial.println(secondanswer);
  Serial.println(stringfiltree);
  client.print(stringfiltree);
  client.flush();
  String thirdanswer = client.readStringUntil('\n');
  Serial.println(thirdanswer);
  digitalWrite(ledPin, HIGH);
  delay(100);
  client.close();

}

   

