void setup() {
  
  Serial.begin(9600);
}

void loop() {
  
  float I=get_current(); //Valeur efficace
  float P=I*220.0; // Puissance

  Serial.print("IRMS");
  Serial.print(I,3);
  Serial.print("A, Puissance ");
  Serial.print(P,3);  
  Serial.println("W");
  //delay(100);     
}

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
  current=sqrt((sum)/N); /True RMS equation
  return(current);
}
