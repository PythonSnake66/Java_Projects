package Assignment1;

import general.Replication;
import general.Simulation;
import general.automagic.AutoReplication;

public class MainLitterCollection {

    public static void main(String[] args) {
        // parameters

        double timeHorizon = 3000;
        long n = 1000;
        long seed;

        for (int i = 850; i <= 950; i = i + 5) {
            int sensorLevel = i;

            //The seed functions we used to test which is the optimal sensorLevel:
            seed = i;
            //seed=i+1;
            //seed=i+2;
            //seed=i+3;
            //seed=i+4;
            //seed=i+5;
            LitterCollectionState state = new LitterCollectionState(sensorLevel, timeHorizon, seed);
            Replication<LitterCollectionState> replication = new AutoReplication<LitterCollectionState>(state);

            Simulation<LitterCollectionState> simulation = new Simulation<>(replication);
            simulation.run(n);
            System.out.println("SensorLevel: " + sensorLevel);
            simulation.printEstimates();
            System.out.println("--------------------------------------------------------------------------------------------------------");
        }
    }
}
