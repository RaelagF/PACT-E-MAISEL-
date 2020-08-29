
/************************** Configuration ***********************************/

// edit the config.h tab and enter your Adafruit IO credentials
// and any additional configuration needed for WiFi, cellular,
// or ethernet clients.
#include "config.h"

/************************ Example Starts Here *******************************/

// this int will hold the current count for our sketch
int count = 0;
bool vibration; 
int vibrator_pin=7; 
int in_led=14;
int vibration_value;
bool vibrates;
// set up the 'counter' feed
AdafruitIO_Feed *counter = io.feed("counter");

void setup() {

  // start the serial connection
  Serial.begin(115200);
  pinMode(vibrator_pin, INPUT);
  pinMode(in_led, OUTPUT);
  // wait for serial monitor to open
  while(! Serial);
  
  Serial.print("Connecting to Adafruit IO");

  // connect to io.adafruit.com
  io.connect();

  // wait for a connection
  while(io.status() < AIO_CONNECTED) {
    Serial.print(".");
    delay(500);
  }

  // we are connected
  Serial.println();
  Serial.println(io.statusText());

}

void loop() {

  // io.run(); is required for all sketches.
  // it should always be present at the top of your loop
  // function. it keeps the client connected to
  // io.adafruit.com, and processes any incoming data.
  vibrates=digitalRead(vibrator_pin);
  io.run();

  // save count to the 'counter' feed on Adafruit IO
  Serial.print("sending -> ");
  Serial.println(count);//Axe des abscisses en secondes 
  Serial.println(vibration_value, DEC);// Axe des ordonnées valeurs de la vibration (0 à 256 je suppose) 
  counter->save(count);
  vibration_value=analogRead(A0);
    // increment the count by 1
  count++;
  
  // wait one second (1000 milliseconds == 1 second)
  delay(1000);
  

}
