#include <ESP8266WiFi.h>                  // Use this for WiFi instead of Ethernet.h
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
#include <WiFiManager.h>
#include <ESP8266WebServer.h>
#include <DNSServer.h>
void configModeCallback (WiFiManager *myWiFiManager) {
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  //if you used auto generated SSID, print it
  Serial.println(myWiFiManager->getConfigPortalSSID());
}

byte mac_addr[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

IPAddress server_addr(192,168,43,59);  // IP of the MySQL *server* here
     // MySQL user login password

//char ssid[]= "OnePlus";// WiFi card example
//char wpa[]="opofloshi"   ;  // your SSID Password
char user[]="user";
char password[]="password";
bool etat=digitalRead(14);
int etat2=analogRead(0);
//Query 
char INSERT_DATA[] = "UPDATE machines.laverie SET state=%d WHERE id=%d";
char query[128];
bool etatfiltree=LOW;

int id=2;//machine number
WiFiClient client;
MySQL_Connection conn((Client *)&client);


void setup() {
  // put your setup code here, to run once:
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
  if(!wifiManager.autoConnect()) {
    Serial.println("failed to connect and hit timeout");
    //reset and try again, or maybe put it to deep sleep
    ESP.reset();
    delay(1000);
  } 

  //if you get here you have connected to the WiFi
  Serial.println("connected...yeey :)");

}


void loop() {
  Serial.begin(115200);
 
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
  // Begin WiFi section
//  int status = WiFi.begin(ssid, wpa);

  while ( WiFi.status() != WL_CONNECTED) {
    Serial.println(".");
    delay(500);
  }
  // print out info about the connection:
    Serial.println("Connected to network");
    IPAddress ip = WiFi.localIP();
    Serial.print("My IP address is: ");
    Serial.println(ip);
  
  // End WiFi section

  Serial.println("Connecting...");
  if (conn.connect(server_addr, 3306, user, password)) {
    delay(1000);
    // Initiate the query class instance
    MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
    // Save
    sprintf(query, INSERT_DATA, etatfiltree,  id );
    Serial.println("avant");
    // Execute the query
    cur_mem->execute(query);
    Serial.println("après");
    // Note: since there are no results, we do not need to read any data
    // Deleting the cursor also frees up memory used
    delete cur_mem;
    Serial.println("Data recorded.");
  }
  
  else
    Serial.println("Connection failed.");
  conn.close();
}

