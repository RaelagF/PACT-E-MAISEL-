#include <Wire.h>
#include <VL53L0X.h>
#include <EEPROM.h>
#include <ESP8266WiFi.h>                 
#include <WiFiManager.h>
#include <ESP8266WebServer.h>


void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
}

int id=2;//cuisine number

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
  
  nbrpers=0;

   WiFiManager wifiManager;
  wifiManager.setAPCallback(configModeCallback);
  if(!wifiManager.autoConnect()) {
    Serial.println("failed to connect and hit timeout");
    ESP.reset();
    delay(1000);
      } 

 Serial.println("connected...yeey :)");
 
  Wire.begin(12,14);
  sensor2.init();
  sensor2.setTimeout(500);
  
  Wire.pins(4,5);
  Wire.begin();
  sensor1.init();
  sensor1.setTimeout(500);

#if defined LONG_RANGE
  // lower the return signal rate limit (default is 0.25 MCPS)
  sensor.setSignalRateLimit(0.1);
  // increase laser pulse periods (defaults are 14 and 10 PCLKs)
  sensor.setVcselPulsePeriod(VL53L0X::VcselPeriodPreRange, 18);
  sensor.setVcselPulsePeriod(VL53L0X::VcselPeriodFinalRange, 14);
#endif

#if defined HIGH_SPEED
  // reduce timing budget to 20 ms (default is about 33 ms)
  sensor.setMeasurementTimingBudget(20000);
#elif defined HIGH_ACCURACY
  // increase timing budget to 200 ms
  sensor.setMeasurementTimingBudget(200000);
#endif

  etat1=0;
  etat2=0;
  initial=0;



}


void loop() {
  prec1=etat1; 
  prec2=etat2; 
  Wire.pins(4,5);
  Wire.begin();
  uint16_t s1 = sensor1.readRangeSingleMillimeters();  
  if (sensor1.timeoutOccurred()) { Serial.print("S1 TIMEOUT"); }; 
  Wire.pins(12,14);
  Wire.begin();
  uint16_t s2 = sensor2.readRangeSingleMillimeters();  
  if (sensor2.timeoutOccurred()) { Serial.print("S2 TIMEOUT"); }
  int d1=(int)s1;
  int d2=(int)s2;
  
  if (d1<1000) {
    etat1=1; 
  }
  else { etat1=0;}

  
  if (d2<1000) {
    etat2=1; 
  }
  else { etat2=0;}

  
  if (prec1==0 && prec2==0){
    if (etat1==1 && etat2==0) { initial=1;}
    if (etat2==1 && etat1==0) { initial=2;}
  }
  if (prec1==1 && prec2==1){
    if (etat1==0 && initial==1) { nbrpers++; Serial.println(nbrpers);}
    if (etat2==0 && initial==2) { nbrpers--; Serial.println(nbrpers);}
  } 

  while ( WiFi.status() != WL_CONNECTED) {
     
   HTTPClient http;    //Declare object of class HTTPClient
 
   http.begin("http://35.189.106.175:50000/hello");      //Specify request destination
   http.addHeader("Content-Type", "text/plain");  //Specify content-type header
   int httpCode = http.POST(String(nbrpers));   //Send the request
   String payload = http.getString();                  //Get the response payload
 
   Serial.println(httpCode);   //Print HTTP return code
   Serial.println(payload);    //Print request response payload
 
   http.end();  //Close connection
 
  }
  }
