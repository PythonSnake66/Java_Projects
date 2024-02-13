package Assignment1;

import general.Replication;
import general.Simulation;
import general.automagic.AutoReplication;

public class MainCoronaVacLoc {

	public static void main(String[] args) {
		// parameters
		
// What time we set does not matter, as long as it is less than 1000 and more than 10 (to make sure all customers left)
		double timeHorizon = 12;
		long n = 1000;
		long seed;
		
		for (int i = 1; i <= 10; i++) {
			int nBooths = i;
			//Change seed on each iteration to add randomness
			seed=i+1;
			CoronaVacLocState state = new CoronaVacLocState(nBooths,timeHorizon, seed);
			Replication<CoronaVacLocState> replication = new AutoReplication<CoronaVacLocState>(state);

			Simulation<CoronaVacLocState> simulation = new Simulation<>(replication);
			simulation.run(n);
			System.out.println("nBooths: " + nBooths);
			simulation.printEstimates();
			System.out.println("--------------------------------------------------------------------------------------------------------");
		}
	}
}
