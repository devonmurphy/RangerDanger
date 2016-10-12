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
  void display(){
    if(dead == false){
    image(elphantImage,x,y);
    }
    else{
      image(deadElephant,x,y);
    }
  }
  void drive() {
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
  
float[] position(){
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
  void delete(){
    destinationX = x;
    destinationY = y;
    ELEPHANT_COUNT=ELEPHANT_COUNT-1;
    dead = true;
  }
}

void spawnElephants(int spawnWidth,int destWidth,int num, int group, float speed){
    float destX=random(0,width);
    float destY=random(0,height);
    float spawnX = random(0+spawnWidth,width-spawnWidth);
    float spawnY = random(0+spawnWidth,height-spawnWidth);
    for (int i = 0; i < num; i++){
      allElephants[i][group] = new Elephant(spawnX+random(0,spawnWidth),spawnY+random(0,spawnWidth),destX+random(destWidth),destY+random(destWidth), speed);
  }      
}
void runElephants(int group){  
  for(int i=0; i<STARTING_NUMBER_OF_ELEPHANTS; i++){
    (allElephants[i][group]).drive();
    (allElephants[i][group]).display();
  }
}