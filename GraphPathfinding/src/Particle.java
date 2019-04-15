
public class Particle {

	Vertex current;

	Particle(Vertex vertex) {
		current = vertex;
		current.guests.add(this);
	}

	void act() {
		transfer(current.randomWalk());
	}

	void interact() {
	}

	void transfer(Vertex to) {
		current.guests.remove(this);
		current = to;
		to.guests.add(this);
	}

}