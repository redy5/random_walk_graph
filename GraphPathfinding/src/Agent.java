
public class Agent extends Particle {

	boolean has_info = false;

	public Agent(Vertex vertex, boolean has_info) {
		super(vertex);
		this.has_info = has_info;
	}

	@Override
	void interact() {
		if (has_info) {
			for (Particle p : current.guests)
				((Agent) p).has_info = true;
		}
	}
}