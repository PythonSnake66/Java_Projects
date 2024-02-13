package Assignment1;

import general.Counter;
import general.SystemState;
import general.annotations.AutoCounter;
import general.annotations.AutoMeasure;
import general.annotations.Initialize;
import general.annotations.StopCriterium;

import java.util.Random;

public class CoronaVacLocState extends SystemState<CoronaVacLocState> {
    // Parameters
    private final Random random;
    private final double lambda = 10;
    private final double mu = 12;
    private final int nChairs = 25;
    private final int nServers;
    // State variables
    private int serversBusy;
    private int nQueue;
    private int chairsBusy;
    private int cQueue;
    private double finalTime;


    // Counter variables
    @AutoCounter("Number of rejected chair arrivals")
    private Counter rejectedC;
    @AutoCounter("Total arrivals")
    private Counter arrivals;
    @AutoCounter("Cumulated time spend in queue for a vaccination booth")
    private Counter cumQueue;
    @AutoCounter("Cumulated time spend in queue for a chair")
    private Counter cumCQueue;

    public CoronaVacLocState(int nServers, double timeHorizon, long seed) {
        super(timeHorizon, seed);
        this.nServers = nServers;
        random = new Random(seed);
        reset();
    }

    @Initialize
    public void initReplication() {
        double nextArrivalTime = UtilsCoronaVacLoc.nextInterArrivalTime(random, lambda * nServers);
        addEvent(nextArrivalTime, this::doArrival);
        //Create dummy event that is never executed so that we can track what happens after 9 hours without the simulation stopping
        addEvent(1000, this::doArrival);
    }

    public void doArrival(double eventTime) {
        if (eventTime <= 9) {
            double prevTime = getCurrentTime();
            double newTime = eventTime;

            cumQueue.incrementBy((newTime - prevTime) * nQueue);
            cumCQueue.incrementBy((newTime - prevTime) * cQueue);
            // update counter for total nr of arrivals
            arrivals.increment();

            if (serversBusy == nServers) {
                nQueue++;
            } else {
                // immediately get serviced
                // update system state
                serversBusy++;

                // generate next departure
                double currentTime = eventTime;
                double serviceDuration = UtilsCoronaVacLoc.nextServiceTime(random, mu);
                double departureTime = currentTime + serviceDuration;
                addEvent(departureTime, this::doDeparture);
                addEvent(departureTime, this::doChairArrival);

            }

            // generate next arrival
            double currentTime = eventTime;
            double nextInterArrivalTime = UtilsCoronaVacLoc.nextInterArrivalTime(random, lambda * nServers);
            double nextArrivalTime = currentTime + nextInterArrivalTime;
            addEvent(nextArrivalTime, this::doArrival);
        }
    }

    public void doDeparture(double eventTime) {
        double prevTime = getCurrentTime();
        double newTime = eventTime;

        cumQueue.incrementBy((newTime - prevTime) * nQueue);
        cumCQueue.incrementBy((newTime - prevTime) * cQueue);
        if (nQueue > 0) {
            nQueue--;

            // generate next departure
            double currentTime = eventTime;
            double serviceDuration = UtilsCoronaVacLoc.nextServiceTime(random, mu);
            double departureTime = currentTime + serviceDuration;
            addEvent(departureTime, this::doDeparture);
            addEvent(departureTime, this::doChairArrival);
        } else {
            serversBusy--;
        }
    }

    public void doChairArrival(double eventTime) {
        double prevTime = getCurrentTime();
        double newTime = eventTime;
        cumCQueue.incrementBy((newTime - prevTime) * cQueue);


        if (chairsBusy == nChairs) {
            cQueue++;
            rejectedC.increment();
        } else {
            // immediately get serviced
            // update system state
            chairsBusy++;

            // generate next departure
            double currentTime = eventTime;
            addEvent(currentTime + 0.25, this::doChairDeparture);
        }

    }

    public void doChairDeparture(double eventTime) {
        double prevTime = getCurrentTime();
        double newTime = eventTime;
        cumCQueue.incrementBy((newTime - prevTime) * cQueue);
        if (cQueue > 0) {
            cQueue--;

            // generate next departure
            double currentTime = eventTime;
            addEvent(currentTime + 0.25, this::doChairDeparture);
        } else {
            chairsBusy--;
            if (eventTime > finalTime) {
                finalTime = eventTime;
            }
        }

    }

    @AutoMeasure("Total people that were vaccinated")
    public Double getTotalPeople() {
        return this.arrivals.getValue();
    }

    @AutoMeasure("Probability to have to wait for a chair")
    public Double getRejectionProbability() {
        return this.rejectedC.getValue() / this.arrivals.getValue();
    }

    @AutoMeasure("Average time spent in the chairs queue (in hours)")
    public Double getChairQueueWaitingTime() {
        if (this.rejectedC.getValue() == 0) {
            return 0.0;
        } else {
            return this.cumCQueue.getValue() / this.rejectedC.getValue();
        }
    }
    @AutoMeasure("Leaving time of last customer (in hours, starting from 0)")
    public Double getFinalTime() {
        return finalTime;
    }

    @Override
    public void reset() {
        serversBusy = 0;
        nQueue = 0;
        chairsBusy = 0;
        cQueue = 0;
        finalTime = 0.0;
    }

}
