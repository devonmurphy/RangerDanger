import processing.sound.*;
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

void setup() {
  menuButton = loadImage("menubutton.png");
  gameBackground = loadImage("8bitmap.png");
  cursor = loadImage("ranger.png");
  nextRoundButton = loadImage("nextroundbutton.png");
  introScreen = loadImage("secondscreen.png");
  introintroscreen = loadImage("rangerdangerscreen.png");
  nationalAnthem = new SoundFile(this, "firstscreen.wav");
  nationalAnthem.loop();

  size( 900, 800, P3D);
  frameRate(30);
  spawnMap();
}

void spawnMap(){
  spawnElephants(200, 100, ELEPHANT_COUNT, 0,.5);
  //spawnElephants(200, 100, ELEPHANT_COUNT/3, 1,.25);
  //spawnElephants(200, 100, ELEPHANT_COUNT/3, 2,.25);
}

void draw() {
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
void mousePressed(){

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
void mouseMoved(){
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