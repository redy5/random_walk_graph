
public class Traveller extends Particle {

	Traveller(Vertex vertex) {
		super(vertex);
		vertex.marked = true;
	}

	@Override
	void act() {
		super.act();
		current.marked = true;
	}
}
