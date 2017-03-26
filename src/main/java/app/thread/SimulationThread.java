package app.thread;

import app.Simulator;

/**
 * Created by Mish.k.a on 26. 3. 2017.
 */
public class SimulationThread extends Thread {

    private Simulator simulator;
    private int duration = 10;
    private boolean isRunning = false;
    private GuiThread guiControl;

    public SimulationThread(Simulator simulator, int duration, GuiThread guiControl) {
        this.simulator = simulator;
        this.duration = duration;
        this.guiControl = guiControl;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public Simulator getSimulator() {
        return simulator;
    }

    @Override
    public void run() {
        isRunning = true;
        for (int i = 1; i <= duration; i++) {
            if (!isRunning) break;
            simulator.simulateOneStep();
            guiControl.updateLabel(simulator.getStep());
            if (Simulator.isLOG()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
