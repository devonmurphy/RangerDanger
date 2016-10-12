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
  void display(){
    if(loot == false){
      image(poacherImage,x,y);
    }
    else{
       image(handsFullImage,x,y); 
    }
  }
  void drive() {
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
  int check(float[] pos){
    size=20;
    if (((x>= pos[0]-size && x<=pos[0]+size))&&((y>=pos[1]-size && y<=pos[1]+size))) {
      return 1;
    }
    else{
     return 0; 
    }
  }
  float[] position(){
    float[] posit= new float[2];
    posit[0]=x;
    posit[1]=y;
    return posit;
  }
  void delete(){
     x=10000;
     y=10000;
     destinationX = 10000;
     destinationY = 10000;
  }
}

//Take spawn/dest Width Out
void spawnPoachers(int spawnWidth,int destWidth,int num, float speed){
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

void runPoachers(){  
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