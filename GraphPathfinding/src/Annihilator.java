
public class Annihilator extends Particle {

	boolean dead = false;

	Annihilator(Vertex vertex) {
		super(vertex);
	}

	@Override
	void interact() {
		if (!dead)
			for (Particle p : current.guests)
				if (p != this) {
					((Annihilator) p).dead = true;
					this.dead = true;
				}
	}
}
