package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Counter;
import app.thread.SimulationThread;
import app.thread.GuiThread;
import app.thread.StopSimulationThread;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Mish.k.a on 19. 3. 2017.
 */
public class Controller implements Initializable {

    private Simulator simulator;
    private int servers = 1;
    private int technicians = 2;
    private int computers = 3;
    private boolean print = true;
    private int duration = 10;
    private StopSimulationThread stopSimulation;

    @FXML
    private TextField countServers;
    @FXML
    private TextField countTechnicians;
    @FXML
    private TextField countComputers;
    @FXML
    private CheckBox printLogs;
    @FXML
    private TextField steps;
    @FXML
    private Button runSimulation;
    @FXML
    private Button takeAStep;
    @FXML
    private Button pause;
    @FXML
    private Button stopSim;
    @FXML
    private Button setupSimulation;
    @FXML
    private Label step;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleSteps();
        handleLogs();
        handleCounts();
    }

    private void handleSteps() {
        steps.setText("10");
        steps.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 1000 ? 1000 : val;
            steps.textProperty().set(String.valueOf(val));
            duration = val;
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
        step.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.valueOf(newValue) == duration) {
                setupSimulation.setDisable(false);
                stopSim.setDisable(true);
                pause.setDisable(true);
                setSettingsDisable(false);
            }
        });
    }

    private void handleLogs() {
        printLogs.setSelected(print);
        printLogs.selectedProperty().addListener((observable, oldValue, newValue) -> {
            print = newValue;
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
    }

    private void handleCounts() {
        countServers.appendText(String.valueOf(servers));
        countServers.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 10 ? 10 : val;
            countServers.setText(String.valueOf(val));
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
            servers = val;
        });
        countTechnicians.appendText(String.valueOf(technicians));
        countTechnicians.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 20 ? 20 : val;
            countTechnicians.setText(String.valueOf(val));
            technicians = val;
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
        countComputers.appendText(String.valueOf(computers));
        countComputers.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 20 ? 20 : val;
            computers = val;
            countComputers.setText(String.valueOf(val));
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
    }

    private void handleField() {

    }

    @FXML
    protected void setupSimulation() {
        Counter.reset();
        simulator = new Simulator(servers, technicians, computers, print);
        runSimulation.setDisable(false);
        takeAStep.setDisable(false);
        pause.setDisable(true);
        stopSim.setDisable(true);
        step.setText("0");
    }

    @FXML
    protected void handleRunSimulation() {
        pause.setDisable(false);
        takeAStep.setDisable(true);
        runSimulation.setDisable(true);
        setupSimulation.setDisable(true);
        stopSim.setDisable(false);
        setSettingsDisable(true);
        GuiThread guiThread = new GuiThread(step);
        SimulationThread simulationThread = new SimulationThread(simulator, duration, guiThread);
        stopSimulation = new StopSimulationThread(simulationThread);
        stopSimulation.start();
        guiThread.start();
        simulationThread.start();
    }

    @FXML
    protected void handleTakeAStep() {
        runSimulation.setDisable(true);
        step.setText(String.valueOf(simulator.getStep()));
        simulator.simulateOneStep();
    }

    @FXML
    protected void handleStopSimulation() {
        stopSimulation.stopSimulation();
        takeAStep.setDisable(true);
        runSimulation.setDisable(true);
        stopSim.setDisable(true);
        pause.setDisable(true);
        setupSimulation.setDisable(false);
        setSettingsDisable(false);
        step.setText("0");
    }

    @FXML
    protected void handlePauseSimulation() {
        if (pause.getText().equals("Pause")) {
            stopSimulation.stopSimulation();
            pause.setText("Continue");
        } else {
            handleRunSimulation();
            pause.setText("Pause");
        }
    }

    private void setSettingsDisable(boolean set) {
        printLogs.setDisable(set);
        countServers.setDisable(set);
        countTechnicians.setDisable(set);
        countComputers.setDisable(set);
        steps.setDisable(set);
    }

    private int validNumber(String value) {
        String val = value;
        if (!val.matches("\\d*")) {
            val = val.replaceAll("[^\\d]", "");
        }
        return Integer.valueOf(val);
    }

}
