#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <WiFiManager.h>







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

  float I=get_current(); //Valeur efficace
  float P=I*220.0;
  Serial.println(P);
  int etatfiltree=0;
  if (P<500){
    etatfiltree=0;
  }
  if (500<P<1000){
    etatfiltree=1;
  }
   if (1000<P<2000){
    etatfiltree=2;
   }
  delay(100) ;
 
   int httpCode = http.POST(String(etatfiltree));   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload
 
   http.end();  //Close connection
 
 }else{
 
    Serial.println("Error in WiFi connection");   
 
 }
 
  delay(30000);  //Send a request every 30 seconds
 
}
