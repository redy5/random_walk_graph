
public class Prey extends Particle {

	boolean fixed = false;
	boolean dead = false;

	Prey(Vertex vertex, boolean fixed) {
		super(vertex);
		this.fixed = fixed;
	}

	@Override
	void act() {
		if (!fixed)
			super.act();
	}
}
