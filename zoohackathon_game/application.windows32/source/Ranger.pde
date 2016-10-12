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
  void display(){
    image(rangerImage,x,y);
  }
  void displayPath(){
    int i =0;
    while(path[i] != null && i < path.length){
      noStroke();
      fill(255,0,0);
      ellipse(path[i].x,path[i].y,10,10);
      i++;
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
  void setPath(PVector[] mousePos){
      path = mousePos;
  }
  int checkAggro(float[] pos){
    if (sqrt(pow(x-pos[0],2)+pow(y-pos[1],2))<aggroSize) {
      return 1;
    }
    else{
       return 0; 
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
}

void runRangers(){  
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