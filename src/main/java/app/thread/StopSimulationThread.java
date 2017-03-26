package app.thread;

/**
 * Created by Mish.k.a on 26. 3. 2017.
 */
public class StopSimulationThread extends Thread {

    private SimulationThread simThread;

    public StopSimulationThread(SimulationThread simThread) {
        this.simThread = simThread;
    }

    @Override
    public void run() {
        super.run();
    }

    public void stopSimulation() {
        simThread.setRunning(false);
    }

}
