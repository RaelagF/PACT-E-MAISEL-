#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <WiFiManager.h>

bool etat=digitalRead(14);
int etat2=analogRead(0);
int id=0; //EVENTUELLEMENT RAJOUTER UNE BOUCLE FOR SUR ID DANS LE LOOP QUI REFLITRE ET RENVOIE POST
bool etatfiltree=LOW ;
String stringfiltree;


String stringing(bool stateto){
  String r;
  stateto == 0 ? r = "0" : r = "1";
  return(r);
  
}
void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
}


void setup() {
  Serial.begin(115200); 
    
  //WiFiManager
  //Local intialization. Once its business is done, there is no need to keep it around
  WiFiManager wifiManager;
  //reset settings - for testing
  //wifiManager.resetSettings();

  //set callback that gets called when connecting to previous WiFi fails, and enters Access Point mode
  wifiManager.setAPCallback(configModeCallback);

  //fetches ssid and pass and tries to connect
  //if it does not connect it starts an access point with the specified name
  //here  "AutoConnectAP"
  //and goes into a blocking loop awaiting configuration
  if (!wifiManager.autoConnect()) {
    Serial.println("failed to connect and hit timeout");
    //reset and try again, or maybe put it to deep sleep
    ESP.reset();
    delay(1000);
  }
  Serial.println("connected yay");
}

void loop() {
 
 if(WiFi.status()== WL_CONNECTED){   //Check WiFi connection status
 
   HTTPClient http;    //Declare object of class HTTPClient
 
   http.begin("http://35.189.106.175:50000/hello");      //Specify request destination
   http.addHeader("Content-Type", "text/plain");  //Specify content-type header

  for (int i =0; i<= 10; i=i+1){
    Serial.println(etat2);
    etat2=analogRead(0);
    etat=digitalRead(14);
    if (etat2>200){
      etatfiltree=HIGH;
    }
    else{
      etatfiltree=LOW;
    }
  delay(100) ;
  }
   stringfiltree=stringing(etatfiltree);
   int httpCode = http.POST(stringfiltree+" "+String(id));   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload
 
   http.end();  //Close connection
 
 }else{
 
    Serial.println("Error in WiFi connection");   
 
 }
 
  delay(30000);  //Send a request every 30 seconds
 
}
