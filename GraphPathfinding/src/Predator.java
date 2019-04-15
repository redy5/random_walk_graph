
public class Predator extends Particle {

	boolean fixed = false;

	Predator(Vertex vertex, boolean fixed) {
		super(vertex);
		this.fixed = fixed;
	}

	@Override
	void act() {
		if (!fixed)
			super.act();
	}

	@Override
	void interact() {
		for (Particle p : current.guests) {
			if (p.getClass().equals(Prey.class)) {
				((Prey) p).dead = true;
			}
		}
	}
}
