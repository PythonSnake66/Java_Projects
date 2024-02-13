package Assignment1;

import java.util.Random;

import general.Counter;
import general.SystemState;
import general.annotations.AutoCounter;
import general.annotations.AutoMeasure;
import general.annotations.Initialize;
import general.annotations.StopCriterium;

public class LitterCollectionState extends SystemState<LitterCollectionState> {
    // Parameters

    private final Random random;
    //lambda - arrival rate of garbage bags per hour
    private final double lambda = 2;
    private final int nServers = 1000;
    private final int sensorLevel;
    private final double hourlyCostPerBagOutside = 10.0 / 24;
    private final double costPerGarbageTruck = 100.0;

    // State variables

    //serversBusy - number of garbage bags in container
    private int serversBusy;
    //nQueue - number of garbage bags outside container
    private int nQueue;
    private int truckVisits;

    //Counter variables
    @AutoCounter("Cumulated time spend in queue")
    private Counter cumQueue;

    public LitterCollectionState(int sensorLevel, double timeHorizon, long seed) {
        super(timeHorizon, seed);
        this.sensorLevel = sensorLevel;
        random = new Random(seed);
        reset();
    }

    @Initialize
    public void initReplication() {
        double nextArrivalTime = UtilsLitterCollection.nextInterArrivalTime(random, lambda);
        addEvent(nextArrivalTime, this::doArrival);
    }

    public void doArrival(double eventTime) {
        double prevTime = getCurrentTime();
        double newTime = eventTime;

        cumQueue.incrementBy((newTime - prevTime) * nQueue);


        if (serversBusy == nServers) {
            nQueue++;

        } else {
            serversBusy++;

            // generate next departure only if we are exactly at sensor level
            if (serversBusy == sensorLevel) {
                double currentTime = eventTime;
                double serviceDuration = 48;
                double departureTime = currentTime + serviceDuration;
                addEvent(departureTime, this::doDeparture);
            }
        }

        // generate next arrival
        double currentTime = eventTime;
        double nextInterArrivalTime = UtilsLitterCollection.nextInterArrivalTime(random, lambda);
        double nextArrivalTime = currentTime + nextInterArrivalTime;
        addEvent(nextArrivalTime, this::doArrival);
    }

    public void doDeparture(double eventTime) {
        double prevTime = getCurrentTime();
        double newTime = eventTime;

        cumQueue.incrementBy((newTime - prevTime) * nQueue);

        // update counter for all servers busy, only if all servers busy

        serversBusy = 0;
        nQueue = 0;
        truckVisits++;
    }


    @AutoMeasure("Expected Total Cost")
    public Double getTotalCost() {
        return this.cumQueue.getValue() * hourlyCostPerBagOutside + truckVisits * costPerGarbageTruck;
    }

    @Override
    public void reset() {
        serversBusy = 0;
        nQueue = 0;
        truckVisits = 0;
    }

}
