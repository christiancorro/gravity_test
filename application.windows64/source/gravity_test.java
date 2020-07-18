import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class gravity_test extends PApplet {


final int G = 6;
Particle star;
ArrayList<Particle> planets;
int particlesToGenerate = 50;
boolean textVisible = true, followMouse = false;
boolean borders = false;

public void setup() {
  
  // size(1000, 1000, P3D);
  
  planets = new ArrayList<Particle>();
  star = new Particle(new PVector(width/2, height/2), 3);
  star.mass = 400;
  textSize(16);
  for (int i = 0; i<particlesToGenerate; i++) {
    planets.add(new Particle(new PVector(random(0, width), random(0, height)), random(0.50f, 1)));
    planets.get(i).velocity = new PVector(random(-2, 2), random(-2, 2));
  }
}

public void draw() {
  fill(0, 95);
  rect(0, 0, width, height);
  noStroke();
  fill(255);
  if (textVisible) {
    //text mouse follow
    //text("star's mass  "+ star.mass, star.position.x, star.position.y-30);
    //text("planets  "+planets.size(), star.position.x, star.position.y-10);
    text("star's mass: "+star.mass+"\nplanets: "+planets.size(), 20, 20);
    text("[v] follow mouse ("+followMouse+")   [b] borders ("+borders+")   [n] no mass   [m] super mass   [up/down/mouse wheel] modify star's mass   [click] create 50 planets   [t] toggle text", 20, height-20);
  }
  fill(238, 189, 21, star.mass*0.1f);
  if (followMouse)
    star.position.set(new PVector(mouseX, mouseY));
  else star.position.set(width/2, height/2);
  star.Display();
  fill(110);
  checkCollision();
  fill(240);
  for (Particle planet : planets) {
    planet.applyGravity(G, planet.mass, star.mass, PVector.sub(planet.position, star.position).mag(), (PVector.sub(star.position, planet.position)).normalize());
    planet.Update();
    if (borders)
      planet.CheckBorders();
    planet.Display();
  }
}

public void checkCollision() {
  for (int i = 0; i<planets.size(); i++) {
    for (int j = 0; j < planets.size(); ++j) {
      if (planets.get(i)!= planets.get(j) && planets.get(i).isCollied(planets.get(j))) {
        planets.get(i).repulse(planets.get(j));
      }
    }
  }
}

public void keyPressed() {
  switch(key) {
  case 'v': 
    followMouse = !followMouse;
    break;
  case 't': 
    textVisible = !textVisible;
    break;
  case 'b': 
    borders = !borders;
    break;
  case 'm': 
    star.mass = 100000;
    break;
  case 'n': 
    star.mass=0;
    star.diameter = 0;
    break;
  case ' ': 
    setup();
    break;
  }
  if (keyCode == UP) {
    star.diameter += 30;
    star.mass += 400;
  } else if (keyCode == DOWN) {
    star.diameter -= 30;
    star.mass -= 400;
  }
  if (star.mass <= 0) {
    star.mass = 0;
    star.diameter = 0;
  }
}

public void mousePressed() {
  for (int i = 0; i<50; i++) {
    planets.add(new Particle(new PVector(random(0, width), random(0, height)), random(0.1f, 2)));
  }
}

public void mouseWheel(MouseEvent e) {
  star.diameter -= e.getAmount() *30;
  star.mass -= e.getAmount()*200;
  if (star.mass <= 0) {
    star.mass = 0;
    star.diameter = 0;
  }
}
class Particle {

  PVector position;
  PVector velocity = new PVector(0.0f, 0.0f);
  PVector acceleration = new PVector(0.0f, 0.0f);
  float diameter;
  float mass;

  Particle (PVector _position, float _mass) {
    position = _position;
    mass = _mass;
    diameter = _mass*10;
  }

  public void Update() {
    velocity.add(acceleration);
    velocity.limit(20);
    position.add(velocity); 
    acceleration.mult(0);
  }

  public void CheckBorders() {
    if (position.x > width) {
      position.x = width;
      velocity.x *= -1;
    } else if (position.x < 0) {
      velocity.x *= -1;
      position.x = 0;
    }

    if (position.y >= height) {
      velocity.y *= -1;
      position.y = height;
    } else if (position.y < 0) {
      velocity.y *= -1;
      position.y = 0;
    }
  }

  public void applyForce(PVector force) {
    PVector a = PVector.div(force, mass);
    acceleration.add( a );
    acceleration.limit(1.9f);//0.9
  }

  //gravity = r*(G*m1*m2)/dist^2
  public void applyGravity(int G, float m1, float m2, float dist, PVector r) {
    applyForce(PVector.mult(r, (G*m1*m2)/(dist*dist)));
  }

  public void Display() { 
    ellipse(position.x, position.y, diameter, diameter);
  }

  public boolean isCollied(Particle p) {
    return (PVector.dist(p.position, position) <= diameter/2+p.diameter/2);
  }

  public void repulse(Particle p) {
    applyForce(PVector.sub(p.position, position).mult(-1*(p.mass/mass)));
  }
}
  public void settings() {  fullScreen(P3D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "gravity_test" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
