
final int G = 6;
Particle star;
ArrayList<Particle> planets;
int particlesToGenerate = 50;
boolean textVisible = true, followMouse = false;
boolean borders = false;

void setup() {
  fullScreen(P3D);
  // size(1000, 1000, P3D);
  smooth();
  planets = new ArrayList<Particle>();
  star = new Particle(new PVector(width/2, height/2), 3);
  star.mass = 400;
  textSize(16);
  for (int i = 0; i<particlesToGenerate; i++) {
    planets.add(new Particle(new PVector(random(0, width), random(0, height)), random(0.50, 1)));
    planets.get(i).velocity = new PVector(random(-2, 2), random(-2, 2));
  }
}

void draw() {
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
  fill(238, 189, 21, star.mass*0.1);
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

void checkCollision() {
  for (int i = 0; i<planets.size(); i++) {
    for (int j = 0; j < planets.size(); ++j) {
      if (planets.get(i)!= planets.get(j) && planets.get(i).isCollied(planets.get(j))) {
        planets.get(i).repulse(planets.get(j));
      }
    }
  }
}

void keyPressed() {
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

void mousePressed() {
  for (int i = 0; i<50; i++) {
    planets.add(new Particle(new PVector(random(0, width), random(0, height)), random(0.1, 2)));
  }
}

void mouseWheel(MouseEvent e) {
  star.diameter -= e.getAmount() *30;
  star.mass -= e.getAmount()*200;
  if (star.mass <= 0) {
    star.mass = 0;
    star.diameter = 0;
  }
}