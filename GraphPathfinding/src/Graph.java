import java.awt.Dimension;
import java.util.Collection;
import java.util.Random;

import javax.swing.JFrame;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

public class Graph {

	Vertex[] vertices;

	int edges = 0;

	int V;
	int r;

	public Graph(int n) {
		vertices = new Vertex[n];
		for (int i = 0; i < n; i++) {
			vertices[i] = new Vertex();
			vertices[i].name = "v" + i;
		}
	}

	static Graph regularGraph(int n, int r) {
		Graph G = new Graph(n);
		int[] vertices_array = new int[n * r];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < r; j++) {
				vertices_array[i + n * j] = i;
			}
		}
		shuffle(vertices_array);

		for (int i = 0; i < n * r / 2; i++) {
			int edge = G.vertices[vertices_array[2 * i]].addConnection(G.vertices[vertices_array[2 * i + 1]]);
			G.edges += edge;
			if (edge == 0)
				return regularGraph(n, r);
		}

		if (G.hasDisconnectedVertices())
			return regularGraph(n, r);

		G.r = r;
		G.V = n;
		return G;
	}

	boolean isAllMarked() {
		for (int i = 0; i < vertices.length; i++)
			if (!vertices[i].marked)
				return false;
		return true;
	}

	boolean hasDisconnectedVertices() {
		for (int i = 0; i < vertices.length; i++)
			if (vertices[i].connections.size() == 0)
				return true;
		return false;
	}

	static void shuffle(int[] array) {
		Random r = new Random();

		for (int i = 0; i < array.length; i++) {
			int rand_pos = r.nextInt(array.length);
			int temp = array[i];
			array[i] = array[rand_pos];
			array[rand_pos] = temp;
		}

	}

	Vertex getRandomVertex() {
		int roll = (int) (Math.random() * (double) vertices.length);
		return vertices[roll];
	}

	SimpleGraph<Vertex, DefaultEdge> toSimpleGraph() {
		SimpleGraph<Vertex, DefaultEdge> g = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);

		for (int i = 0; i < vertices.length; i++)
			g.addVertex(vertices[i]);

		for (int i = 0; i < vertices.length; i++)
			for (int j = 0; j < vertices[i].connections.size(); j++)
				if (i != j)
					g.addEdge(vertices[i], vertices[i].connections.get(j));

		return g;
	}

	@SuppressWarnings("deprecation")
	void visualise() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JGraphXAdapter<Vertex, DefaultEdge> jg = new JGraphXAdapter<Vertex, DefaultEdge>(toSimpleGraph());
		mxGraphComponent c = new mxGraphComponent(jg);

		mxGraphModel m = (mxGraphModel) c.getGraph().getModel();
		Collection<Object> cells = m.getCells().values();
		mxUtils.setCellStyles(m, cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);

		c.setConnectable(false);
		c.getGraph().setAllowDanglingEdges(false);
		c.getGraph().setEnabled(false);
		c.setSize(500, 500);
		frame.add(c);

		mxCircleLayout l = new mxCircleLayout(jg);
		l.setX0(0);
		l.setY0(0);
		l.setRadius(250);
		l.setDisableEdgeStyle(true);
		l.execute(jg.getDefaultParent());

		frame.setSize(new Dimension(500, 500));
		frame.setVisible(true);
		frame.pack();
	}

	@Override
	protected Object clone() {
		Graph res = new Graph(vertices.length);
		res.vertices = vertices.clone();
		return res;
	}
}