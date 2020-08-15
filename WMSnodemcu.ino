  //------------------//  //------------------//
#include <ezTime.h>
Timezone myLocalTime; 
float turbavg;
float tempavg;
float levelavg;
float phavg;

//------------------//  //------------------//

  
//code for firebase & esp8266
//FirebaseESP8266.h must be included before ESP8266WiFi.h
#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <ArduinoJson.h>



#define FIREBASE_HOST "monitoring-b244a.firebaseio.com" //Without http:// or https:// schemes
#define FIREBASE_AUTH "ST8Juh8Ley421zcrtejHQq2Kovm72Wul2xZr69Dc"
#define WIFI_SSID "PAK"
#define WIFI_PASSWORD "lionking"


//Define FirebaseESP8266 data object
FirebaseData firebaseData;


FirebaseJson json;

void printResult(FirebaseData &data);


//code for serial communication
#include <SoftwareSerial.h>
#include <ArduinoJson.h>
SoftwareSerial linkSerial(D6, D5);
float turbidity;
float temperature;
float ph;
float level;
float volume;
float temp22;

void setup() {




Serial.begin(115200);
while (!Serial) continue;

  // Initialize the "link" serial port
  // Use the lowest possible data rate to reduce error ratio
  linkSerial.begin(4800);



  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);



  
  Firebase.reconnectWiFi(true);

  //Set the size of WiFi rx/tx buffers in the case where we want to work with large data.
  firebaseData.setBSSLBufferSize(1024, 1024);

  //Set the size of HTTP response buffers in the case where we want to work with large data.
  firebaseData.setResponseSize(1024);

  //Set database read timeout to 1 minute (max 15 minutes)
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  //tiny, small, medium, large and unlimited.
  //Size and its write timeout e.g. tiny (1s), small (10s), medium (30s) and large (60s).
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
  
  /*
  This option allows get and delete functions (PUT and DELETE HTTP requests) works for device connected behind the
  Firewall that allows only GET and POST requests.
  
  Firebase.enableClassicRequest(firebaseData, true);
  */

      //------------------//  //------------------//

      // Set desired time zone for Timezone object declared in the beginning
  myLocalTime.setLocation(F("pk")); // set your time zone

  // Sync NTP time for ezTime library
  waitForSync(); 

  delay(2000);

    //------------------//  //------------------//
}

void loop() {

          linkSerial.write("s");
          if (linkSerial.available()>0)
        {
          volume=linkSerial.read();
          Serial.println(volume);
         }

  
    if (linkSerial.available()) 
  {
    // Allocate the JSON document
    // This one must be bigger than for the sender because it must store the strings
    StaticJsonDocument<300> doc;

    // Read the JSON document from the "link" serial port
    DeserializationError err = deserializeJson(doc, linkSerial);

    if (err == DeserializationError::Ok) 
    {
      // Print the values
      // (we must use as<T>() to resolve the ambiguity)
      turbidity=doc["turbidity"].as<float>();
      temperature=doc["temperature"].as<float>();
      ph=doc["ph"].as<float>();
      level=doc["level"].as<float>();
          
      //volume=doc["volume"].as<float>();

      Serial.print("turbidity = ");     
      Serial.println(turbidity);
      Serial.print("temperature = ");
      Serial.println(temperature);
      Serial.print("Ph = ");
      Serial.println(ph);
      Serial.print("Level = ");
      Serial.println(level);


      
    } 
    else 
    {
      // Print error to the "debug" serial port
      Serial.print("deserializeJson() returned ");
      Serial.println(err.c_str());
  
      // Flush all bytes in the "link" serial port buffer
      while (linkSerial.available() > 0)
        linkSerial.read();
    }
  }

   Firebase.setFloat(firebaseData,  "Member/turb" , turbidity);
   Firebase.setFloat(firebaseData,  "Member/temp" , temperature);
   Firebase.setFloat(firebaseData,  "Member/ph" , ph);
   Firebase.setFloat(firebaseData,  "Member/level" , level);


      //------------------//  //------------------//

      //This calculates and stores average data 
      //of whole day with key reference as date

     turbavg=(turbavg+turbidity)/2;
     tempavg=(tempavg+temperature)/2;
     levelavg=(levelavg+level)/2;
     phavg=(phavg+ph)/2;

  //Serial.println(myLocalTime.dateTime("d-m-Y"));
  String ax="Average/";
  String flag="";
  ax+=myLocalTime.dateTime("d-m-Y");
  
  flag=ax+"/Temp";
  Firebase.setFloat(firebaseData,  flag , tempavg);
  
  flag=ax+"/Turb";
  Firebase.setFloat(firebaseData,  flag , turbavg);  
  
  flag=ax+"/Level";
  Firebase.setFloat(firebaseData,  flag , levelavg);  
  
  flag=ax+"/PH";
  Firebase.setFloat(firebaseData,  flag , phavg);
  
   //This calculates and stores consumption data 
      //of whole day with key reference as date
    ax="Consumption/";
    ax+=myLocalTime.dateTime("d-m-Y");
  
    //temp22=volume+temp22; 
    
    Firebase.setFloat(firebaseData,  ax , volume);
      

      
  

  
     
  
  
}
