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
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>

WiFiClient client;
MySQL_Connection conn((Client *)&client);

byte mac_addr[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };

IPAddress server_addr(192,168,43,59);  // IP of the MySQL *server* here
char user[] = "user";              // MySQL user login username
char password[] = "password";        // MySQL user login password



//Query 
char SELECT_DATA[] = "SELECT state FROM machines.laverie WHERE id=%d";
char SELECT_DATA2[] = "SELECT reserved FROM machines.laverie WHERE id=%d";
char query[128];







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
}

 
void loop() {
for (int id=0; id<3; id=id+1){
  
  


  row_values *row = NULL;
  long head_count = 0;
  Serial.begin(115200);
 

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
    sprintf(query, SELECT_DATA, id );
    Serial.println("avant");
    // Execute the query
    cur_mem->execute(query);
    Serial.println("après");
     column_names *columns = cur_mem->get_columns();

  // Read the row (we are only expecting the one)
  do {
    row = cur_mem->get_next_row();
    if (row != NULL) {
      head_count = atol(row->values[0]);
    }
  } while (row != NULL);
  // Deleting the cursor also frees up memory used
  delete cur_mem;

  // Show the result
  Serial.print(" state");
  Serial.print(id);
  Serial.println(head_count);

    // Note: since there are no results, we do not need to read any data
    // Deleting the cursor also frees up memory used
   
    Serial.println("Data recorded.");
  }
  
  else
    Serial.println("Connection failed.");
  conn.close();

//  row_values *row = NULL;
  long head_count2 = 0;
  Serial.begin(115200);
 



  while ( WiFi.status() != WL_CONNECTED) {
    Serial.println(".");
    delay(500);
  }
  // print out info about the connection:
    Serial.println("Connected to network");
    Serial.print("My IP address is: ");
    Serial.println(ip);
  
  // End WiFi section

  Serial.println("Connecting...");
  if (conn.connect(server_addr, 3306, user, password)) {
    delay(1000);
    // Initiate the query class instance
    MySQL_Cursor *cur_mem = new MySQL_Cursor(&conn);
    // Save
    sprintf(query, SELECT_DATA2, id );
    Serial.println("avant");
    // Execute the query
    cur_mem->execute(query);
    Serial.println("après");
     column_names *columns = cur_mem->get_columns();

  // Read the row (we are only expecting the one)
  do {
    row = cur_mem->get_next_row();
    if (row != NULL) {
      head_count2 = atol(row->values[0]);
    }
  } while (row != NULL);
  // Deleting the cursor also frees up memory used
  delete cur_mem;

  // Show the result
  Serial.print(" reserved");
  Serial.print(id);
  Serial.println(head_count2);

    // Note: since there are no results, we do not need to read any data
    // Deleting the cursor also frees up memory used
   
    Serial.println("Data recorded.");
  }
  
  else
    Serial.println("Connection failed.");
  conn.close();
//GERE LES LEDs
   switch(id) {
    case 0: 
    if (head_count==0 && head_count2==0) { strip.setPixelColor(0,strip.Color(0, 255, 0)); strip.show() ; break;}
    if (head_count==1) { strip.setPixelColor(0,strip.Color(255, 0, 0)); strip.show() ;break; }
    if (head_count2==1 && head_count==0) { strip.setPixelColor(0,strip.Color(0, 0, 255)); strip.show() ;break;}
    break;
    case 1:
    if (head_count==0 && head_count2==0) { strip.setPixelColor(1,strip.Color(0, 255, 0)); strip.show() ;break;}
    if (head_count==1) { strip.setPixelColor(1,strip.Color(255, 0, 0)); strip.show() ;break;}
    if (head_count2==1 && head_count==0) { strip.setPixelColor(1,strip.Color(0, 0, 255)); strip.show() ;break;}
    break; 
    case 2:
    if (head_count==0 && head_count2==0) { strip.setPixelColor(2,strip.Color(0, 255, 0)); strip.show() ;break;}
    if (head_count==1) { strip.setPixelColor(2,strip.Color(255, 0, 0)); strip.show() ;break;}
    if (head_count2==1 && head_count==0) { strip.setPixelColor(2,strip.Color(0, 0, 255)); strip.show() ;break;}
    break; 
    case 3:
    if (head_count==0 && head_count2==0) { strip.setPixelColor(3,strip.Color(0, 255, 0)); strip.show() ;break;}
    if (head_count==1) { strip.setPixelColor(3,strip.Color(255, 0, 0)); strip.show() ;break;}
    if (head_count2==1 && head_count==0) { strip.setPixelColor(3,strip.Color(0, 0, 255)); strip.show() ;break;}
    break; 
  }
  delay(20);
}


}
