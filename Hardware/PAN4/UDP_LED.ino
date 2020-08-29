#include <Adafruit_NeoPixel.h>
#include <WiFiManager.h>
#ifdef __AVR__
#include <avr/power.h>
#endif

#define PIN 2
#define NBRPIXEL 4


Adafruit_NeoPixel strip = Adafruit_NeoPixel(NBRPIXEL, PIN, NEO_GRB + NEO_KHZ800);

void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
 
}

#include <ESP8266WiFi.h>                  // Use this for WiFi instead of Ethernet.h
#include <WiFiUdp.h>



WiFiUDP Udp;
char incomingPacket[255];  // buffer for incoming packets
char  replyPacket[] = "Hi there! Got the message :-)";  // a reply string to send back
IPAddress remoteIP(35,189,106,175);
unsigned int remotePort = 50000;
int led1, led2, led3;



void setup() {
  Serial.begin(115200); 
  strip.begin(); 
  strip.show(); //Initialize all pixels to 'off'
  
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
  Udp.beginPacket(remoteIP, remotePort);
  Serial.printf("Now listening at IP %s, UDP port %d\n", WiFi.localIP().toString().c_str(), remotePort);
}

void loop() {
   int packetSize = Udp.parsePacket();
  if (packetSize)
  {
    // receive incoming UDP packets
    Serial.printf("Received %d bytes from %s, port %d\n", packetSize, Udp.remoteIP().toString().c_str(), Udp.remotePort());
    int len = Udp.read(incomingPacket, 255);
    if (len > 0)
    {
      incomingPacket[len] = 0;
    }
    Serial.printf("UDP packet contents: %s\n", incomingPacket);
    sscanf(incomingPacket,"%ld %ld %ld ",&led1,&led2,&led3);
    // send back a reply, to the IP address and port we got the packet from
    Udp.beginPacket(Udp.remoteIP(), Udp.remotePort());
    Udp.write(replyPacket);
    Udp.endPacket();
    if (led1==0){strip.setPixelColor(0,strip.Color(255, 0, 0)); strip.show() ;};// led is red and machine is being used
    if (led1==1){strip.setPixelColor(0,strip.Color(0, 255, 0)); strip.show() ;};//led is green and machine can be used
    if (led1==2){strip.setPixelColor(0,strip.Color(0, 0, 255)); strip.show() ;};//led is blue machine is reserved
    if (led2==0){strip.setPixelColor(1,strip.Color(255, 0, 0)); strip.show() ;};// led is red and machine is being used
    if (led2==1){strip.setPixelColor(1,strip.Color(0, 255, 0)); strip.show() ;};//led is green and machine can be used
    if (led2==2){strip.setPixelColor(1,strip.Color(0, 0, 255)); strip.show() ;};//led is blue machine is reserve
    if (led3==0){strip.setPixelColor(2,strip.Color(255, 0, 0)); strip.show() ;};// led is red and machine is being used
    if (led3==1){strip.setPixelColor(2,strip.Color(0, 255, 0)); strip.show() ;};//led is green and machine can be used
    if (led3==2){strip.setPixelColor(2,strip.Color(0, 0, 255)); strip.show() ;};//led is blue machine is reserve
    
  }
}
