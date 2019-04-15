
public class Coalescer extends Particle {

	boolean dead = false;

	Coalescer(Vertex vertex) {
		super(vertex);
	}

	@Override
	void interact() {
		if (!dead) {
			for (Particle p : current.guests)
				((Coalescer) p).dead = true;
			this.dead = false;
		}
	}

}