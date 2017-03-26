package app.thread;

import app.Controller;
import app.Simulator;
import javafx.application.Platform;
import javafx.scene.text.Text;

/**
 * Created by Mish.k.a on 26. 3. 2017.
 */
public class SimulationThread extends Thread {

    private Simulator simulator;
    private int duration = 10;
    private boolean isRunning = false;
    private GuiThread guiControl;
    private Text[][] field;

    public SimulationThread(Simulator simulator, int duration, GuiThread guiControl, Text[][] field) {
        this.simulator = simulator;
        this.duration = duration;
        this.guiControl = guiControl;
        this.field = field;
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
        for (int i = simulator.getStep(); i <= duration; i++) {
            if (!isRunning) break;
            guiControl.updateGUI(simulator.getStep());
            simulator.simulateOneStep();
            Platform.runLater(() -> {
                for (int k = 0; k < 10; k++) {
                    for (int j = 0; j < 10; j++) {
                        field[k][j] = Controller.createEntityMark(field[k][j], simulator.getEnvironment().getEntityAt(k, j));
                    }
                }
            });
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
