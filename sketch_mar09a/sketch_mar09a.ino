#include <ESP8266WiFi.h>
#include<AccelStepper.h>
#define HALFSTEP 8  

int port = 4444;  //Port number
WiFiServer server(port);
 
//Server connect to WiFi Network
const char *ssid = "Oasis";  //Enter your wifi SSID
const char *password = "Kokosjanwp3111";  //Enter your wifi Password

int Pin0 = 5; 
int Pin1 = 4; 
int Pin2 = 0; 
int Pin3 = 2; 
float _step = 0; 
float _speed = 1;
boolean dir = true;// gre

int SPin0 = 13; 
int SPin1 = 12; 
int SPin2 = 14; 
int SPin3 = 16; 
float S_step = 0;
float S_speed = 1; 
boolean Sdir = false;

AccelStepper stepper1(HALFSTEP, Pin0, Pin2, Pin1, Pin3);
AccelStepper stepper2(HALFSTEP, SPin0, SPin2, SPin1, SPin3);

boolean control_turn = false; 

 boolean speed_rot = true;

String command_line = "";

boolean work_condition = false;

void move_for_motor_1(){
if (work_condition == true) {
  
 if (int(_step) == 0){
     digitalWrite(Pin0, LOW);  
     digitalWrite(Pin1, LOW); 
     digitalWrite(Pin2, LOW); 
     digitalWrite(Pin3, HIGH); 
   }
  else if (int(_step) == 1){
     digitalWrite(Pin0, LOW);  
     digitalWrite(Pin1, LOW); 
     digitalWrite(Pin2, HIGH); 
     digitalWrite(Pin3, HIGH); 
  }
  else if (int(_step) == 2) {
     digitalWrite(Pin0, LOW);  
     digitalWrite(Pin1, LOW); 
     digitalWrite(Pin2, HIGH); 
     digitalWrite(Pin3, LOW); 
  }
  else if (int(_step) == 3){ 
     digitalWrite(Pin0, LOW);  
     digitalWrite(Pin1, HIGH); 
     digitalWrite(Pin2, HIGH); 
     digitalWrite(Pin3, LOW); 
  }
  else if (int(_step) == 4) {
     digitalWrite(Pin0, LOW);  
     digitalWrite(Pin1, HIGH); 
     digitalWrite(Pin2, LOW); 
     digitalWrite(Pin3, LOW); 
  }
  else if (int(_step) == 5) { 
     digitalWrite(Pin0, HIGH);  
     digitalWrite(Pin1, HIGH); 
     digitalWrite(Pin2, LOW); 
     digitalWrite(Pin3, LOW); 
  } 
   else if (int(_step) == 6){
     digitalWrite(Pin0, HIGH);  
     digitalWrite(Pin1, LOW); 
     digitalWrite(Pin2, LOW); 
     digitalWrite(Pin3, LOW); 
   }  
   else if (int(_step) == 7){
     digitalWrite(Pin0, HIGH);  
     digitalWrite(Pin1, LOW); 
     digitalWrite(Pin2, LOW); 
     digitalWrite(Pin3, HIGH); 
   } 
   else { 
     digitalWrite(Pin0, LOW);  
     digitalWrite(Pin1, LOW); 
     digitalWrite(Pin2, LOW); 
     digitalWrite(Pin3, LOW);  
 } 
 if(dir){ 
   _step+=_speed; 
 }else{ 
   _step-=_speed; 
 } 
 if(int(_step)>7){ 
   _step=0; 
 } 
 if(int(_step)<0){ 
   _step=7; 
 } 
 delay(1);
}
 }

void move_for_motor_2(){
if (work_condition == true) {
  
   if (int(S_step) == 0){
     digitalWrite(SPin0, LOW);  
     digitalWrite(SPin1, LOW); 
     digitalWrite(SPin2, LOW); 
     digitalWrite(SPin3, HIGH); 
   }
  else if (int(S_step) == 1){
     digitalWrite(SPin0, LOW);  
     digitalWrite(SPin1, LOW); 
     digitalWrite(SPin2, HIGH); 
     digitalWrite(SPin3, HIGH); 
  }
  else if (int(S_step) == 2) {
     digitalWrite(SPin0, LOW);  
     digitalWrite(SPin1, LOW); 
     digitalWrite(SPin2, HIGH); 
     digitalWrite(SPin3, LOW); 
  }
  else if (int(S_step) == 3){ 
     digitalWrite(SPin0, LOW);  
     digitalWrite(SPin1, HIGH); 
     digitalWrite(SPin2, HIGH); 
     digitalWrite(SPin3, LOW); 
  }
  else if (int(S_step) == 4) {
     digitalWrite(SPin0, LOW);  
     digitalWrite(SPin1, HIGH); 
     digitalWrite(SPin2, LOW); 
     digitalWrite(SPin3, LOW); 
  }
  else if (int(S_step) == 5) { 
     digitalWrite(SPin0, HIGH);  
     digitalWrite(SPin1, HIGH); 
     digitalWrite(SPin2, LOW); 
     digitalWrite(SPin3, LOW); 
  } 
   else if (int(S_step) == 6){
     digitalWrite(SPin0, HIGH);  
     digitalWrite(SPin1, LOW); 
     digitalWrite(SPin2, LOW); 
     digitalWrite(SPin3, LOW); 
   }  
   else if (int(S_step) == 7){
     digitalWrite(SPin0, HIGH);  
     digitalWrite(SPin1, LOW); 
     digitalWrite(SPin2, LOW); 
     digitalWrite(SPin3, HIGH); 
   } 
   else { 
     digitalWrite(SPin0, LOW);  
     digitalWrite(SPin1, LOW); 
     digitalWrite(SPin2, LOW); 
     digitalWrite(SPin3, LOW);  
 } 
 if(Sdir){ 
   S_step+= S_speed; 
 }else{ 
   S_step-= S_speed; 
 } 
 if(int(S_step)>7){ 
   S_step=0; 
 } 
 if(int(S_step)<0){ 
   S_step=7; 
 } 
 delay(1);
}
 }

 String getValue(String data, char separator, int index)
{
  int found = 0;
  int strIndex[] = {0, -1};
  int maxIndex = data.length()-1;

  for(int i=0; i<=maxIndex && found<=index; i++){
    if(data.charAt(i)==separator || i==maxIndex){
        found++;
        strIndex[0] = strIndex[1]+1;
        strIndex[1] = (i == maxIndex) ? i+1 : i;
    }
  }

  return found>index ? data.substring(strIndex[0], strIndex[1]) : "";
}
//=======================================================================
//                    Power on setup
//=======================================================================
void setup() 
{
  pinMode(Pin0, OUTPUT);  
 pinMode(Pin1, OUTPUT);  
 pinMode(Pin2, OUTPUT);  
 pinMode(Pin3, OUTPUT);
 pinMode(SPin0, OUTPUT);  
 pinMode(SPin1, OUTPUT);  
 pinMode(SPin2, OUTPUT);  
 pinMode(SPin3, OUTPUT); 
  
  Serial.begin(115200);
 
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password); //Connect to wifi
 
  // Wait for connection  
  Serial.println("Connecting to Wifi");
  while (WiFi.status() != WL_CONNECTED) {   
    delay(500);
    Serial.print(".");
    delay(500);
  }
 
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
 
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());  
  server.begin();
  Serial.print("Open Telnet and connect to IP:");
  Serial.print(WiFi.localIP());
  Serial.print(" on port ");
  Serial.println(port);

   stepper1.setMaxSpeed(1000.0);
  stepper2.setMaxSpeed(1000.0);
  stepper1.moveTo(10000);
  
  stepper1.setSpeed(0);
   stepper2.setSpeed(0);
}
//=======================================================================
//                    Loop
//=======================================================================


 
void loop() 
{
  WiFiClient client = server.available();
  
  if (client) {
    if(client.connected())
    {
      Serial.println("Client Connected");
    }
    
    while(client.connected()){   
     move_for_motor_1(); // мотор рядом с землёй
     move_for_motor_2();
     stepper1.runSpeed();
     stepper2.runSpeed();


   
    
      while(client.available()>0){
        
       //String comp = "forward";
       
        // read data from the connected client
       // Serial.write(client.read());
       
       String bufs = (String)client.readStringUntil('\n');
       command_line = getValue(bufs, ' ', 0);
       command_line.trim(); 

        Serial.println("Command line is " + bufs + " control_turn is ");
        Serial.println(control_turn);
        
         if (command_line.equals("forward")) {
         _speed = 1.0;
          S_speed = 1.0;
        Sdir = false; 
        work_condition = true;
        }
        else if (command_line.equals("right")) {
      
        if (!control_turn) {
          
          float step_size = getValue(bufs, ' ', 1).toFloat();
          String side = getValue(bufs, ' ', 2);
          Serial.println("step_size is ");
          Serial.println(step_size);
          Serial.println("side is ");
          Serial.println(side);
          side.trim(); 
          
          
          if (side == "l"){
          Serial.println("Turning left on ");
          Serial.print(step_size*18.8704);
          stepper1.moveTo(step_size*18.8704);
          stepper1.setSpeed(600);
          stepper2.setSpeed(600);
          }
          else {
          Serial.println("Turning right on ");
          Serial.print(step_size*18.8704);
          stepper1.moveTo(-step_size*18.8704);
          stepper1.setSpeed(-600);
          stepper2.setSpeed(-600);
          }
          control_turn = true;
          }
        else{ 
          Serial.println("Distance to go is ");
          Serial.println(stepper1.distanceToGo());
          // client.println("Cont");
         
        /*   _speed = 1.0;
          S_speed = 1.0;  
        Sdir = true;
        work_condition = true;*/
        }
        }
        else if(command_line.equals("rforward")) {
        Sdir = false; 
        _speed = 1.0;
        S_speed = 0.5;
       // speed_rot = false;
        work_condition = true;
        }
        else if(command_line.equals("lforward")) {
         Sdir = false; 
        _speed = 0.5;
        S_speed = 1.0;  
       // speed_rot = false;
        work_condition = true;
         }
         else if(command_line.equals("round")) {
           stepper1.setSpeed(-800);
           stepper2.setSpeed(356);
         // float step_size = getValue(bufs, ' ', 1).toFloat(); 
        
        // Sdir = false; 
      //  _speed = 1;
      //  S_speed = step_size;//0.75;  
      
     //   work_condition = true;
         }
        else {
          stepper1.moveTo(10000);
           stepper1.setSpeed(0);
           stepper2.setSpeed(0);
          work_condition = false;
          speed_rot = true;

          control_turn = false;
          _speed = 1.0;
          S_speed = 1.0;
        }
    }
       if (stepper1.distanceToGo()==0) {
    Serial.println(stepper1.distanceToGo());
     Serial.println("Stopping");
     client.println("Stop");
     control_turn = false;
     
    }  
    else  {
        Serial.println(stepper1.distanceToGo());  
    }
      }
      //Send Data to connected client
     // while(Serial.available()>0)
      //{
        //client.write(Serial.read());
      //}
    }
    client.stop();
  //  Serial.println("Client disconnected");    
  }
