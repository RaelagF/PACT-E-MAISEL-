

#include <rExcel.h>

long          idx = 0;                // index
int           outputTiming = 1000;    // packet sending timing in ms      important: this dermines the output timing
float         a0;                     // A0 pin reading
float         rnd;                    // random number

char          value[16];              // written or read value

rExcel        myExcel;                // class for Excel data exchanging


void setup(){
  

  Serial.begin(115200);
  delay(1000);
  
  
  myExcel.clearInput();

 
}


void loop() {

  static unsigned long  loopTime = 0;
  static unsigned long  time1 = 0;
  int ret;

  loopTime = millis();

  
  if ((loopTime - time1) >= outputTiming) {

    time1 = loopTime;

    
    a0 = (float)analogRead(0);               // A0 reading converted to volt
    Serial.println(a0);
    
    myExcel.write("Example", "B5", a0, 2);                  // write the value from A0 pin to worksheet 'Example' cell 'B5' with two digits as decimals
   
    // example2
    myExcel.writeIndexed("Example", idx+11, 1,"%date%");    // write %date% (that will be converted in current date) to worksheet 'Example' row 'idx+11' column '1' 
    myExcel.writeIndexed("Example", idx+11, 2,"%time%");    // write %time% (that will be converted in current time) to worksheet 'Example' row 'idx+11' column '2' 
    myExcel.writeIndexed("Example", idx+11, 3, idx);        // write idx to worksheet 'Example' row 'idx+11' column '3' 
    myExcel.writeIndexed("Example", idx+11, 4, a0, 2);      // write the value from A0 pin to worksheet 'Example' row 'idx+11' column '4' with two digits as decimals
  
    idx++;
  
    
  }

}   

