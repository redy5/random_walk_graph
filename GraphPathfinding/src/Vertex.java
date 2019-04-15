import java.io.Serializable;
import java.util.ArrayList;

public class Vertex implements Serializable {
	private static final long serialVersionUID = 1L;

	ArrayList<Vertex> connections = new ArrayList<Vertex>();
	ArrayList<Particle> guests = new ArrayList<Particle>();

	String name = "";

	boolean marked = false;

	int addConnection(Vertex to) {
		if (this == to)
			return 0;
		if (!isConnected(to) && !to.isConnected(this)) {
			connections.add(to);
			to.connections.add(this);
			return 1;
		}
		return 0;
	}

	boolean isConnected(Vertex to) {
		for (Vertex v : connections)
			if (v == to)
				return true;
		return false;
	}

	Vertex randomWalk() {
		int roll = (int) (Math.random() * (double) connections.size());
		return connections.get(roll);
	}

	@Override
	public String toString() {
		return name;
	}
}