import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	static double predicted_result = 0.0;

	public static void main(String[] arg0) {
		System.out.println("Welcome!");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		Graph g = Graph.regularGraph(14, 3);
		g.visualise();

		while (true) {
			// INPUT
			System.out.println("Please choose the type of simulation you want to run (enter the number):");
			System.out.println("1) Single walk.");
			System.out.println("2) Multiple walks.");
			System.out.println("3) Talkative particles.");
			System.out.println("4) Predator-Prey.");
			System.out.println("5) Annihilating particles.");
			System.out.println("6) Coalescing particles.");
			System.out.println("7) Exit.");

			int answer = 0;
			try {
				answer = Integer.parseInt(br.readLine());
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}
			if (answer == 7)
				break;

			int V = 0;
			int r = 0;
			int k = 0;
			int l = 0;

			boolean fixed_prey = false;
			boolean fixed_predator = false;

			try {
				System.out.println("Enter number of vertices:");
				V = Integer.parseInt(br.readLine());
				System.out.println("Enter r:");
				r = Integer.parseInt(br.readLine());
				if (answer != 1) {
					System.out.println("Enter number of k particles:");
					k = Integer.parseInt(br.readLine());
				}
				if (answer == 4) {
					System.out.println("Enter number of l particles:");
					l = Integer.parseInt(br.readLine());
					System.out.println("Predators fixed?(y/n)");
					fixed_predator = br.readLine().equals("y");
					System.out.println("Prey fixed?(y/n)");
					fixed_prey = br.readLine().equals("y");
				}
			} catch (NumberFormatException e) {
			} catch (IOException e) {
			}

			// PRECALCULATIONS
			double theta = (double) (r - 1) / (double) (r - 2);
			
			if (answer == 1)
				predicted_result = theta * V * Math.log(V);
			if (answer == 2)
				predicted_result = theta * V * Math.log(V) / k;
			if (answer == 3)
				predicted_result = (2.0 * theta / k) * V * harmonic(k - 1);
			if (answer == 4)
				predicted_result = theta * harmonic(l) * V / k;
			if (answer == 5)
				predicted_result = 2.0 * theta * Math.log(2 * V);
			if (answer == 6)
				predicted_result = 2.0 * theta * V;
			// SIMULATION

			double res_one = simulate(answer, Graph.regularGraph(V, r), k, l, fixed_predator, fixed_prey);
			double res_hundred = 0;
			double res_thousand = 0;
			double res_ten_thousand = 0;

			for (int i = 0; i < 100; i++)
				res_hundred += simulate(answer, Graph.regularGraph(V, r), k, l, fixed_predator, fixed_prey);
			res_hundred /= 100;

			for (int i = 0; i < 1000; i++)
				res_thousand += simulate(answer, Graph.regularGraph(V, r), k, l, fixed_predator, fixed_prey);
			res_thousand /= 1000;

			for (int i = 0; i < 10000; i++)
				res_ten_thousand += simulate(answer, Graph.regularGraph(V, r), k, l, fixed_predator, fixed_prey);
			res_ten_thousand /= 10000;
			
			switch (answer) {
			case 1:
				System.out.println("Single walk results");
				break;
			case 2:
				System.out.println("Multiple walk results");
				break;
			case 3:
				System.out.println("Talkative particles results");
				break;
			case 4:
				System.out.println("Predator-Prey results");
				break;
			case 5:
				System.out.println("Annihilating results");
				break;
			case 6:
				System.out.println("Coalescing results");
				break;
			}
			System.out.println("Predicted: " + predicted_result);
			System.out.println("One sim: " + res_one);
			System.out.println("Hundred sim: " + res_hundred);
			System.out.println("Thousand sim: " + res_thousand);
			System.out.println("Ten thousand sim: " + res_ten_thousand);
		}
	}

	static double simulate(int answer, Graph g, int k, int l, boolean fixed_predator, boolean fixed_prey) {
		ArrayList<Particle> actors = new ArrayList<Particle>();
		int steps = 0;
		actors.clear();
		if (answer == 1)
			actors.add(new Traveller(g.getRandomVertex()));
		for (int i = 0; i < k; i++) {
			if (answer == 2)
				actors.add(new Traveller(g.getRandomVertex()));
			if (answer == 3) {
				if (i == 0)
					actors.add(new Agent(g.getRandomVertex(), true));
				else
					actors.add(new Agent(g.getRandomVertex(), false));
			}
			if (answer == 4)
				actors.add(new Predator(g.getRandomVertex(), fixed_predator));
			if (answer == 5)
				actors.add(new Annihilator(g.getRandomVertex()));
			if (answer == 6)
				actors.add(new Coalescer(g.getRandomVertex()));
		}
		if (answer == 4)
			for (int i = 0; i < l; i++)
				actors.add(new Prey(g.getRandomVertex(), fixed_prey));
		while (true) {
			if (answer == 1 || answer == 2)
				if (g.isAllMarked())
					break;
			if (answer == 3) {
				boolean all_have_info = true;
				for (Particle p : actors) {
					if (!((Agent) p).has_info) {
						all_have_info = false;
						break;
					}
				}
				if (all_have_info)
					break;
			}
			if (answer == 4) {
				boolean finish = true;
				for (Particle p : actors)
					if (p.getClass().equals(Prey.class) && ((Prey) p).dead == false)
						finish = false;
				if (finish)
					break;
			}
			if (answer == 5) {
				boolean finish = true;
				for (Particle p : actors)
					if (((Annihilator) p).dead == false)
						finish = false;
				if (finish)
					break;
			}
			if (answer == 6) {
				int alive = 0;
				for (Particle p : actors)
					if (((Coalescer) p).dead == false)
						alive++;
				if (alive == 1)
					break;
			}

			// if generated graph is bad
			if (steps > predicted_result * 5)
				return simulate(answer, Graph.regularGraph(g.V, g.r), k, l, fixed_predator, fixed_prey);

			steps++;

			for (Particle p : actors)
				p.act();

			for (Particle p : actors)
				p.interact();
		}
		return (double) steps;
	}

	static double harmonic(int n) {
		double sum = 0.0;
		for (int i = 1; i <= n; i++) {
			sum += 1.0 / i;
		}
		return sum;
	}
}