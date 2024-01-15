package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    private List<Simulation> simulationList = new ArrayList<>();
    private List<Thread> threads = new ArrayList<Thread>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Object lock = new Object();

    public SimulationEngine(List<Simulation> simulationList) {
        this.simulationList = simulationList;
    }

    void runSync() {
        for(Simulation simulation : simulationList)
            simulation.run();
    }

    public void runAsync() throws InterruptedException{
        threads = new ArrayList<Thread>();
        for(Simulation simulation : simulationList) {
            Thread simThread = new Thread(simulation);
            threads.add(simThread);
            simThread.start();
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for(Thread thread : threads)
            thread.join();
        executorService.shutdown();
    }

    public void runAsyncInThreadPool() throws InterruptedException{
        threads = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(4);
        for(Simulation simulation : simulationList) {
            Thread simThread = new Thread(simulation);
            executorService.submit(simThread);
        }
    }
}
