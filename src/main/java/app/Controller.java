package app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Counter;
import app.thread.SimulationThread;
import app.thread.GuiThread;
import app.thread.StopSimulationThread;
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
    private SimulationThread simulationThread;
    private StopSimulationThread stopSimulation;
    private GuiThread guiControl;

    @FXML
    private TextField countServers;
    @FXML
    private TextField countTechnicians;
    @FXML
    private TextField countComputers;
    @FXML
    private CheckBox printLogs;
    @FXML
    private CheckBox waitLogs;
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
        steps.textProperty().setValue("10");
        steps.textProperty().addListener((observable, oldValue, newValue) -> {
            duration = Integer.valueOf(newValue);
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
    }

    private void handleLogs() {
        printLogs.selectedProperty().setValue(print);
        printLogs.selectedProperty().addListener((observable, oldValue, newValue) -> {
            print = newValue;
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
            waitLogs.selectedProperty().setValue(newValue);
            if (!newValue) {
                waitLogs.setDisable(true);
            } else {
                waitLogs.setDisable(false);
            }
        });
        waitLogs.selectedProperty().setValue(print);
    }

    private void handleCounts() {
        countServers.appendText(String.valueOf(servers));
        countServers.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                int number = Integer.valueOf(newValue.replaceAll("[^\\d]", ""));
                countServers.setText(String.valueOf(number));
                servers = number;
            }
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
            servers = Integer.valueOf(newValue);
        });
        countTechnicians.appendText(String.valueOf(technicians));
        countTechnicians.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                int number = Integer.valueOf(newValue.replaceAll("[^\\d]", ""));
                technicians = number;
                countTechnicians.setText(String.valueOf(number));
            }
            technicians = Integer.valueOf(newValue);
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
        countComputers.appendText(String.valueOf(computers));
        countComputers.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                int number = Integer.valueOf(newValue.replaceAll("[^\\d]", ""));
                countComputers.setText(String.valueOf(number));
                computers = number;
            }
            computers = Integer.valueOf(newValue);
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
    }

    @FXML
    protected void setupSimulation() {
        Counter.reset();
        simulator = new Simulator(servers, technicians, computers, print);
        runSimulation.setDisable(false);
        takeAStep.setDisable(false);
        pause.setDisable(true);
        stopSim.setDisable(true);
        step.textProperty().setValue("0");
    }

    @FXML
    protected void handleRunSimulation() {
        stopSim.setDisable(false);
        pause.setDisable(false);
        takeAStep.setDisable(true);
        runSimulation.setDisable(true);
        setupSimulation.setDisable(true);
        guiControl = new GuiThread(step);
        simulationThread = new SimulationThread(simulator, duration, guiControl);
        stopSimulation = new StopSimulationThread(simulationThread);
        stopSimulation.start();
        guiControl.start();
        simulationThread.start();
    }

    @FXML
    protected void handleTakeAStep() {
        runSimulation.setDisable(true);
        simulator.simulateOneStep();
        step.textProperty().set(String.valueOf(simulator.getStep()));
    }

    @FXML
    protected void handleStopSimulation() {
        stopSimulation.stopSimulation();
        runSimulation.setDisable(true);
        takeAStep.setDisable(true);
        runSimulation.setDisable(true);
        setupSimulation.setDisable(false);
        stopSim.setDisable(true);
        pause.setDisable(true);
    }

    @FXML
    protected void handlePauseSimulation() {
        if (pause.textProperty().getValue().equals("Pause")) {
            stopSimulation.stopSimulation();
            pause.textProperty().set("Continue");
        } else {
            handleRunSimulation();
            pause.textProperty().set("Pause");
        }
    }

}
