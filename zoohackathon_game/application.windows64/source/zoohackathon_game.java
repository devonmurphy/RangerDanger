import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class zoohackathon_game extends PApplet {


SoundFile nationalAnthem;


int STARTING_NUMBER_OF_ELEPHANTS = 20;
int STARTING_NUMBER_OF_RANGERS = 100;
int NUMBER_OF_POACHERS = 10;

boolean startRunning = false;
int startTime =0;
int ELEPHANT_COUNT = STARTING_NUMBER_OF_ELEPHANTS;
int NUMBER_OF_RANGERS = 0;
boolean nextMenuButton = false;
int roundTime = 10000;
boolean startMenu = true;
boolean started = false;
boolean running = false;
boolean menu = false;
boolean setup = false;
boolean placed = false;
boolean mapped = false;

int starttedTime = 0;

int mousePointCount = 0;
int arcLength =0;
PVector[] mousePositions = new PVector[500];

int money = 10000;
int rangerCost = 2250;

Elephant[][] allElephants = new Elephant[STARTING_NUMBER_OF_ELEPHANTS][3];
Poacher[] allPoachers = new Poacher[NUMBER_OF_POACHERS];
Ranger[] allRangers = new Ranger[STARTING_NUMBER_OF_RANGERS];
PImage gameBackground,menuButton, cursor,nextRoundButton, introScreen, introintroscreen;

public void setup() {
  menuButton = loadImage("menubutton.png");
  gameBackground = loadImage("8bitmap.png");
  cursor = loadImage("ranger.png");
  nextRoundButton = loadImage("nextroundbutton.png");
  introScreen = loadImage("secondscreen.png");
  introintroscreen = loadImage("rangerdangerscreen.png");
  nationalAnthem = new SoundFile(this, "firstscreen.wav");
  nationalAnthem.loop();

  
  frameRate(30);
  spawnMap();
}

public void spawnMap(){
  spawnElephants(200, 100, ELEPHANT_COUNT, 0,.5f);
  //spawnElephants(200, 100, ELEPHANT_COUNT/3, 1,.25);
  //spawnElephants(200, 100, ELEPHANT_COUNT/3, 2,.25);
}

public void draw() {
  if (setup == true){
    background(gameBackground);
    image(nextRoundButton,80,height - 40);
    if(placed == false){
      cursor(cursor);
    }
    else{
      cursor(CROSS);  
    }
    for(int i = 0; i < NUMBER_OF_RANGERS; i++){
      allRangers[i].display();
      /*
      if(allRangers[i].pathValue == true){
        //allRangers[i].displayPath();
      }
      */
    }
    for(int j =0; j < ELEPHANT_COUNT; j++){
       allElephants[j][0].display(); 
    }
  }
  if(running == true){
    if(startRunning == false){
      startTime = millis();
      startRunning = true;
    }
    if(millis()-startTime> roundTime){
        nextMenuButton = true;
      
    }

    cursor(ARROW);
    background(gameBackground);
    //image(menuButton, width - 60, height - 40);
    if(nextMenuButton == true){
       image(nextRoundButton,80,height - 40);
    }
    runRangers();
    runPoachers();
    runElephants(0);
    //runElephants(1);
    //runElephants(2);
  }
  if(startMenu == true){
    if(started == false){
      starttedTime = millis(); 
      started = true;
    }
    if(millis()-starttedTime<5000){
      background(introintroscreen);
    }
    else{
     background(introScreen);
    }
  }
  else{
    fill(0);
    textSize(32);
    text(money, width/2+205, height/2-325);
    text(ELEPHANT_COUNT, width/2+240, height/2-280);
  }
}  
public void mousePressed(){

  if(mouseX> 50 && mouseX < 115 && mouseY > 730 && mouseY < 770 && setup == true){
    running = true;
    startMenu =false;
    menu = false;
    setup = false;
    spawnPoachers( 100, 50, NUMBER_OF_POACHERS, 2);
  }

  if(setup == true){
    if(placed == false && money-rangerCost>=0){
      allRangers[NUMBER_OF_RANGERS] =  new Ranger(mouseX, mouseY, 2 , 200);
      //placed = true;
      NUMBER_OF_RANGERS+=1;
      money -= rangerCost;
    } 
    if(money-rangerCost < 0){
       setup = false;
       running = true;
       spawnPoachers( 100, 50, NUMBER_OF_POACHERS, 2);
    }
  }
  if(mouseX> 390 && mouseX < 605 && mouseY > 245 && mouseY < 310 && startMenu == true){
    running = false;
    startMenu =false;
    menu = false;
    setup = true;
  }
  if(mouseX> 50 && mouseX < 115 && mouseY > 730 && mouseY < 770 && running == true && nextMenuButton == true){
    running = false;
    startMenu =false;
    menu = false;
    setup = true;
    startRunning = false;
  }
}
public void mouseMoved(){
  /*
  if(setup == true && placed == true){
      println("starting path");
      println(mouseX,mouseY);
      mousePositions[mousePointCount] = new PVector(mouseX,mouseY);
      
      println(mousePositions[mousePointCount]);
      mousePointCount+=1;
      if(mousePointCount > 1){
        arcLength += (PVector.sub(mousePositions[mousePointCount-1], mousePositions[mousePointCount-2])).mag();
        println(arcLength);
      }
      if(arcLength >= allRangers[NUMBER_OF_RANGERS-1].rangerDistance){
        allRangers[NUMBER_OF_RANGERS-1].setPath(mousePositions);
        allRangers[NUMBER_OF_RANGERS-1].pathValue = true;
        placed = false;
        mousePointCount = 0;
        arcLength=0;
        println("path over");
      }
  }
  */
}
class Elephant{
  float x,y,vx,vy,size=20, magnitude, destinationX, destinationY,speed;
  int result,last,trip,t;
  PImage elphantImage,deadElephant;
  boolean dead = false;
  
  Elephant(float _x,float _y, float _destinationX, float _destinationY, float _speed){
    x=_x;
    y=_y;
    speed = _speed;
    destinationX = _destinationX;
    destinationY = _destinationY;
    imageMode(CENTER);
    elphantImage = loadImage("ElephantAnimate.gif");
    deadElephant = loadImage("deadelephant.png");
  }
  public void display(){
    if(dead == false){
    image(elphantImage,x,y);
    }
    else{
      image(deadElephant,x,y);
    }
  }
  public void drive() {
    vx = destinationX - x;
    vy = destinationY - y;
    magnitude = sqrt(pow(vx,2)+pow(vy,2));
        if(magnitude != 0){
          vx = vx / magnitude*speed;
          vy = vy/ magnitude*speed;
          x=x+vx;
          y=y+vy;
     }
  }
  
public float[] position(){
  if(dead == false){
    float[] posit= new float[2];
    posit[0]=x;
    posit[1]=y;
    return posit;
  }
  else{
    float[] posit= new float[2];
    posit[0]=-10000;
    posit[1]=-10000;
     return  posit;
  }
  }
  public void delete(){
    destinationX = x;
    destinationY = y;
    ELEPHANT_COUNT=ELEPHANT_COUNT-1;
    dead = true;
  }
}

public void spawnElephants(int spawnWidth,int destWidth,int num, int group, float speed){
    float destX=random(0,width);
    float destY=random(0,height);
    float spawnX = random(0+spawnWidth,width-spawnWidth);
    float spawnY = random(0+spawnWidth,height-spawnWidth);
    for (int i = 0; i < num; i++){
      allElephants[i][group] = new Elephant(spawnX+random(0,spawnWidth),spawnY+random(0,spawnWidth),destX+random(destWidth),destY+random(destWidth), speed);
  }      
}
public void runElephants(int group){  
  for(int i=0; i<STARTING_NUMBER_OF_ELEPHANTS; i++){
    (allElephants[i][group]).drive();
    (allElephants[i][group]).display();
  }
}
class Poacher{
  float x,y,vx,vy,size=20, magnitude, destinationX, destinationY,speed;
  int result,last,trip,t;
  PImage poacherImage, handsFullImage;
  boolean loot = false, done = false, started = false;
  
  int startTime = 0, enterTime=2000;
  
  Poacher(float _x,float _y, float _destinationX, float _destinationY, float _speed){
    x=_x;
    y=_y;
    speed = _speed;
    destinationX = _destinationX;
    destinationY = _destinationY;
    imageMode(CENTER);
    poacherImage = loadImage("Poacher.png");
    handsFullImage = loadImage("ivorypoacher.png");
    startTime = millis();
  }
  public void display(){
    if(loot == false){
      image(poacherImage,x,y);
    }
    else{
       image(handsFullImage,x,y); 
    }
  }
  public void drive() {
    if(millis()-startTime> enterTime){
       started = true; 
       println("started");
    }
    vx = destinationX - x;
    vy = destinationY - y;
    magnitude = sqrt(pow(vx,2)+pow(vy,2));
    if(loot == false){
      vx = vx / magnitude*speed;
      vy = vy/ magnitude*speed;
      x=x+vx;
      y=y+vy;
    }
    else{
      vx = vx / magnitude*speed;
      vy = vy/ magnitude*speed;
      x=x-vx;
      y=y-vy;
    }
    if((x>=width || x<=0)||(y>=height||y<=0)&&started == true){
      done = true;
      println("done");
    }
  }
  public int check(float[] pos){
    size=20;
    if (((x>= pos[0]-size && x<=pos[0]+size))&&((y>=pos[1]-size && y<=pos[1]+size))) {
      return 1;
    }
    else{
     return 0; 
    }
  }
  public float[] position(){
    float[] posit= new float[2];
    posit[0]=x;
    posit[1]=y;
    return posit;
  }
  public void delete(){
     x=10000;
     y=10000;
     destinationX = 10000;
     destinationY = 10000;
  }
}

//Take spawn/dest Width Out
public void spawnPoachers(int spawnWidth,int destWidth,int num, float speed){
   for (int i = 0; i <num; i++){
    //Here we need that box spawning >>Here it is :P
    float buffer = 42;
    float wid2 = width + buffer;
    float hei2 = height + buffer;
    float spawn = random(0,359);
    float dest = (spawn+180+random(-60,60))%360;
    float perimeter = (2*wid2 + 2*hei2);
    float corn1 = 360*(wid2/perimeter);
    float corn2 = 360*((wid2+hei2)/perimeter);
    float corn3 = 360*((2*wid2+hei2)/perimeter);
    float corn4 = 360*((2*wid2+2*hei2)/perimeter); //360
    float spawnRat = (spawn/360)*perimeter;
    float destRat = (dest/360)*perimeter;
    float spawnX=0, spawnY=0, destX=0, destY=0;
    if (spawn < corn1) {
      spawnX = spawnRat;
      spawnY = -buffer;
    }
    else if (spawn < corn2) {
      spawnX = wid2+buffer;
      spawnY = spawnRat-wid2;
    }
    else if (spawn < corn3) {
      spawnX = wid2-(spawnRat-(wid2+hei2));
      spawnY = hei2+buffer;
    }
    else if (spawn < corn4) {
      spawnX = -buffer;
      spawnY = hei2-(spawnRat-(2*wid2+hei2));
    }
    //Plot Destinations
    if (dest < corn1) {
      destX = destRat;
      destY = -buffer;
    }
    else if (dest < corn2) {
      destX = wid2+buffer;
      destY = destRat-wid2;
    }
    else if (dest < corn3) {
      destX = wid2-(destRat-(wid2+hei2));
      destY = hei2+buffer;
    }
    else if (dest < corn4) {
      destX = -buffer;
      destY = hei2-(destRat-(2*wid2+hei2));
    }
    allPoachers[i] = new Poacher(spawnX, spawnY, destX, destY, speed);
  }      
}

public void runPoachers(){  
  int donePoachers = 0;
  for(int i=0; i<NUMBER_OF_POACHERS; i++){
    (allPoachers[i]).drive();
    (allPoachers[i]).display();
 
    
    for(int j = 0; j<ELEPHANT_COUNT; j++){
      if((allPoachers[i]).check(allElephants[j][0].position())==1 && allPoachers[i].loot == false){
        allElephants[j][0].delete();
        allPoachers[i].loot = true;
      }
    }
    /*
    for(int j = 0; j<ELEPHANT_COUNT; j++){
      if((allPoachers[i]).check(allElephants[j][1].position())==1 && allPoachers[i].loot == false){
        allElephants[j][1].delete();
        allPoachers[i].loot = true;
      }
    }
    for(int j = 0; j<ELEPHANT_COUNT; j++){
      if((allPoachers[i]).check(allElephants[j][2].position())==1 && allPoachers[i].loot == false){
        allElephants[j][2].delete();
        allPoachers[i].loot = true;
      }
    }
    */
  }
}
class Ranger{
  float x,y,vx,vy,size=20, magnitude, destinationX, destinationY,speed, aggroSize = 100;
  int result,last,trip,t;
  int rangerDistance;
  PImage rangerImage;
  PVector[] path;
  boolean pathValue= false;
  
  Ranger(float _x,float _y, float _speed, int _rangerDistance){
      x=_x;
      y=_y;
      destinationX = x;
      destinationY = y;
      speed = _speed;
      rangerDistance = _rangerDistance;
      imageMode(CENTER);
      rangerImage = loadImage("ranger.png");
  }
  public void display(){
    image(rangerImage,x,y);
  }
  public void displayPath(){
    int i =0;
    while(path[i] != null && i < path.length){
      noStroke();
      fill(255,0,0);
      ellipse(path[i].x,path[i].y,10,10);
      i++;
    }
  }
  public void drive() {
    vx = destinationX - x;
    vy = destinationY - y;
    magnitude = sqrt(pow(vx,2)+pow(vy,2));
    if(magnitude != 0){
      vx = vx / magnitude*speed;
      vy = vy/ magnitude*speed;
      x=x+vx;
      y=y+vy;
    }
  }
  public void setPath(PVector[] mousePos){
      path = mousePos;
  }
  public int checkAggro(float[] pos){
    if (sqrt(pow(x-pos[0],2)+pow(y-pos[1],2))<aggroSize) {
      return 1;
    }
    else{
       return 0; 
    }
  }
  public int check(float[] pos){
    size=20;
    if (((x>= pos[0]-size && x<=pos[0]+size))&&((y>=pos[1]-size && y<=pos[1]+size))) {
      return 1;
    }
    else{
       return 0; 
    }
  }
}

public void runRangers(){  
  for(int i=0; i<NUMBER_OF_RANGERS; i++){
    (allRangers[i]).drive();
    if(allRangers.length > 0){
      (allRangers[i]).display();
      for(int j = 0; j<NUMBER_OF_POACHERS; j++){
        if((allRangers[i]).checkAggro(allPoachers[j].position())==1){
           allRangers[i].destinationX = allPoachers[j].x;
           allRangers[i].destinationY = allPoachers[j].y;
           if((allRangers[i]).check(allPoachers[j].position())==1){
               allRangers[i].destinationX = allRangers[i].x;
               allRangers[i].destinationY = allRangers[i].y;
               allPoachers[j].delete();
               money += 250;
           }
        }
      }
    }
  }
}
  public void settings() {  size( 900, 800, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "zoohackathon_game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
