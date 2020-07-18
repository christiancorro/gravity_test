class Particle {

  PVector position;
  PVector velocity = new PVector(0.0, 0.0);
  PVector acceleration = new PVector(0.0, 0.0);
  float diameter;
  float mass;

  Particle (PVector _position, float _mass) {
    position = _position;
    mass = _mass;
    diameter = _mass*10;
  }

  void Update() {
    velocity.add(acceleration);
    velocity.limit(20);
    position.add(velocity); 
    acceleration.mult(0);
  }

  void CheckBorders() {
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

  void applyForce(PVector force) {
    PVector a = PVector.div(force, mass);
    acceleration.add( a );
    acceleration.limit(1.9);//0.9
  }

  //gravity = r*(G*m1*m2)/dist^2
  void applyGravity(int G, float m1, float m2, float dist, PVector r) {
    applyForce(PVector.mult(r, (G*m1*m2)/(dist*dist)));
  }

  void Display() { 
    ellipse(position.x, position.y, diameter, diameter);
  }

  boolean isCollied(Particle p) {
    return (PVector.dist(p.position, position) <= diameter/2+p.diameter/2);
  }

  void repulse(Particle p) {
    applyForce(PVector.sub(p.position, position).mult(-1*(p.mass/mass)));
  }
}