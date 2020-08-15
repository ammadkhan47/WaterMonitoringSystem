//code for flow sensor------------
int sensorInterrupt = 0;  // interrupt 0
int sensorPin       = 2; //Digital Pin 2
int solenoidValve = 5; // Digital pin 5
unsigned int SetPoint = 400; //400 milileter
/*The hall-effect flow sensor outputs pulses per second per litre/minute of flow.*/
float calibrationFactor = 3; //can be changed according to needs
volatile byte pulseCount =0;   
float flowRate = 0.0;
unsigned int flowMilliLitres =0;
unsigned long totalMilliLitres = 0; 
unsigned long oldTime = 0;



//code and lib for json ultrasound level sensor
//
#include <NewPing.h>
#include <MedianFilter.h>
#include <Wire.h>
#include <MedianFilter.h>

#define TRIGGER_PIN  4  // Arduino pin tied to trigger pin on the ultrasonic sensor.
#define ECHO_PIN     3  // Arduino pin tied to echo pin on the ultrasonic sensor.
#define MAX_DISTANCE 450 // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE); // NewPing setup of pins and maximum distance.
MedianFilter filter(31,0);


//library for serial transmission
//
#include <SoftwareSerial.h>
#include <ArduinoJson.h>
SoftwareSerial linkSerial(5, 6); // RX, TX


//lib for lcd and temp
// include the library code:
#include <LiquidCrystal.h>
// Include the libraries we need
#include <OneWire.h>
#include <DallasTemperature.h>

// initialize the library by associating any needed LCD interface pin
// with the arduino pin number it is connected to
const int rs = 13, en = 12, d4 = 11, d5 = 10, d6 = 9, d7 = 8;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

int tur = A0;
int SensorPin=A1;
unsigned long int avgValue=0;
int Offset = -6;
  unsigned int o,uS; //for level sensor
  
float turbidity;
float temperature;
float phsense;
unsigned int level;
float levl;
float volume;



// Data wire is plugged into port 4 on the Arduino
#define ONE_WIRE_BUS 4

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

void setup() 
{
  // set up the LCD's number of columns and rows:
  lcd.begin(16, 2);
  // Start up the library
  sensors.begin();
  Serial.begin(115200);
  while (!Serial) continue;

  // Initialize the "link" serial port
  // Use the lowest possible data rate to reduce error ratio
  linkSerial.begin(4800);

  
  pinMode(tur,INPUT);

  //for flow sensor
  pinMode(solenoidValve , OUTPUT);
  digitalWrite(solenoidValve, HIGH);
  pinMode(sensorPin, INPUT);
  digitalWrite(sensorPin, HIGH);/*The Hall-effect sensor is connected to pin 2 which uses interrupt 0. Configured to trigger on a FALLING state change (transition from HIGH
  (state to LOW state)*/
  attachInterrupt(sensorInterrupt, pulseCounter, FALLING); //you can use Rising or Falling

  // Print a message to the LCD.
  lcd.setCursor(0, 0);
  lcd.print("Water Monitoring");
  lcd.setCursor(0, 1);
  lcd.print("  System");
  delay(2000);
  lcd.clear();

}

void loop() 
{

 

  //code for turbidity sensor-------------------------------------
  //
  lcd.clear();
  int sensorValue = analogRead(tur);
  float voltage = sensorValue * (5.0 / 1024.0);

/*
  Serial.println ("Sensor Output (V):");
  Serial.println (voltage);
  Serial.println();
*/

  lcd.setCursor(0, 0);
  lcd.print("Turbidity:");
  lcd.setCursor(11, 0);
  lcd.print(voltage);


  //code for temperature sensor-----------------------------------
  //
  // request to all devices on the bus
  // Serial.print("Requesting temperatures...");
  sensors.requestTemperatures(); // Send the command to get temperatures
  // Serial.println("DONE");
  // After we got the temperatures, we can print them here.
  // We use the function ByIndex, and as an example get the temperature from the first sensor only.
  float tempC = sensors.getTempCByIndex(0);

  // Check if reading was successful
  if(tempC != DEVICE_DISCONNECTED_C) 
  {
   // Serial.print("Temperature for the device 1 (index 0) is CENTIGRADE: ");
   // Serial.println(tempC);
  } 
  else
  {
    Serial.println("Error: Could not read temperature data");
  }

  lcd.setCursor(0, 1);
  lcd.print("Degrees cel:");
  lcd.setCursor(13, 1);
  lcd.print(tempC);
  delay(1000);



  //code for ph sensor----------------------------------------------------
  //
  int buf[10];  //buffer r for read analog


for(int i=0;i<10;i++)  //Get 10 e sample e value from e the r sensor r for h smooth e the value
 {
buf[i]=analogRead(SensorPin);
delay(10);
 }
 for(int i=0;i<9;i++)  //sort e the analog from l small o to large
 {
 for(int j=i+1;j<10;j++)
 {
if(buf[i]>buf[j])
 {
 int temp=buf[i];
 buf[i]=buf[j];
 buf[j]=temp;
 }
 }
 }
avgValue=0;
 for(int i=2;i<8;i++)  //take e the e average e value f of 6 6 6 6 r center sample
avgValue+=buf[i];
float phValue=(float)avgValue*5.0/1024/6; //convert e the analog o into millivolt
phValue=3.5*phValue+Offset;  //convert e the t millivolt o into pH value
phValue = map(phValue,0,14,14,0);

/*
Serial.print(" pH:");
Serial.print(phValue,2);
Serial.println(" ");
*/

  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("PH:");
  lcd.setCursor(5, 0);
  lcd.print(phValue);
  delay(1000);




   //code for level sensor
  //
  
                      // Wait 50ms between pings (about 20 pings/sec). 29ms should be the shortest delay between pings.
   // Send ping, get ping time in microseconds (uS).

  
  
  for(int i=0;i<50;i++){
   o,uS = sonar.ping();
  filter.in(uS);
  o = filter.out();
  level= o / US_ROUNDTRIP_CM;
  
  //Serial.println( o / US_ROUNDTRIP_CM);
  
  }  
  
  lcd.setCursor(0, 1);
  lcd.print("Level:");
  lcd.setCursor(7, 1);
  lcd.print(level);
  delay(1000);



//------------------code  for flow sensor

if((millis() - oldTime) > 1000)    // Only process counters once per second
  { 
    // Disable the interrupt while calculating flow rate and sending the value to the host
    detachInterrupt(sensorInterrupt);
 
    // Because this loop may not complete in exactly 1 second intervals we calculate the number of milliseconds that have passed since the last execution and use that to scale the output. We also apply the calibrationFactor to scale the output based on the number of pulses per second per units of measure (litres/minute in this case) coming from the sensor.
    flowRate = ((1000.0 / (millis() - oldTime)) * pulseCount) / calibrationFactor;
 
    // Note the time this processing pass was executed. Note that because we've
    // disabled interrupts the millis() function won't actually be incrementing right
    // at this point, but it will still return the value it was set to just before
    // interrupts went away.
    oldTime = millis();
 
    // Divide the flow rate in litres/minute by 60 to determine how many litres have
    // passed through the sensor in this 1 second interval, then multiply by 1000 to
    // convert to millilitres.
    flowMilliLitres = (flowRate / 60) * 1000;
 
    // Add the millilitres passed in this second to the cumulative total
    totalMilliLitres += flowMilliLitres;
 
    unsigned int frac;
 
    // Print the flow rate for this second in litres / minute
    /*Serial.print("Flow rate: ");
    Serial.print(flowMilliLitres, DEC);  // Print the integer part of the variable
    Serial.print("mL/Second");
    Serial.print("\t")*/;           
 
    // Print the cumulative total of litres flowed since starting
    /*Serial.print("Output Liquid Quantity: ");        
    Serial.print(totalMilliLitres,DEC);
    Serial.println("mL"); 
    Serial.print("\t");*/     

     lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Volume: ");
  lcd.setCursor(10, 0);
  lcd.print(totalMilliLitres);
  delay(1000);

        
    if (totalMilliLitres > 40)
    {
      SetSolinoidValve();
    }
    
// Reset the pulse counter so we can start incrementing again
    pulseCount = 0;
 
    // Enable the interrupt again now that we've finished sending output
    attachInterrupt(sensorInterrupt, pulseCounter, FALLING);
  }

  
 //code for serial transmission between arduino and nodemcu---------------
  //
  
  turbidity = voltage;
  temperature=tempC;
  phsense=phValue;
  levl=level;
  volume=totalMilliLitres;

  /* Print the values on the "debug" serial port
  Serial.print("timestamp = ");
  Serial.println(timestamp);
  Serial.print("value = ");
  Serial.println(value);
  Serial.println("---");  */

  // Create the JSON document
  StaticJsonDocument<200> doc;
  doc["turbidity"] = turbidity;
  doc["temperature"] = temperature;
  doc["ph"] = phsense;
  doc["level"] = levl;
  linkSerial.write(volume);
  //doc["volume"] = volume;

  // Send the JSON document over the "link" serial port
  serializeJson(doc, linkSerial);

  // Wait
 // delay(1000);





}
 
//Insterrupt Service Routine for flow sensor-----------------
 
void pulseCounter()
{
  // Increment the pulse counter
  pulseCount++;
}
 
void SetSolinoidValve()
{
  digitalWrite(solenoidValve, LOW);
}


 
