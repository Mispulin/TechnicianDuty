package app;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Mish.k.a on 19. 3. 2017.
 */
public class Controller implements Initializable {

    private Simulator simulator;
    private int servers = Simulator.countServers;
    private int technicians = Simulator.countTechnicians;
    private int computers = Simulator.countComputers;
    private boolean print = Simulator.LOG;
    private int duration = Simulator.steps;

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
    private Label step;
    @FXML
    private Button runSimulation;
    @FXML
    private Button setupSimulation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleSteps();
        handleLogs();
        handleCounts();
    }

    private void handleSteps() {
        step.textProperty().setValue(String.valueOf(0));
        steps.textProperty().setValue(String.valueOf(Simulator.steps));
        steps.textProperty().addListener((observable, oldValue, newValue) -> {
            duration = Integer.valueOf(newValue);
            runSimulation.setDisable(true);
        });
    }

    private void handleLogs() {
        printLogs.selectedProperty().setValue(print);
        printLogs.selectedProperty().addListener((observable, oldValue, newValue) -> {
            print = newValue;
            runSimulation.setDisable(true);
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
            servers = Integer.valueOf(newValue);
            runSimulation.setDisable(true);
        });
        countTechnicians.appendText(String.valueOf(technicians));
        countTechnicians.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                int number = Integer.valueOf(newValue.replaceAll("[^\\d]", ""));
                countTechnicians.setText(String.valueOf(number));
                technicians = number;
            }
            technicians = Integer.valueOf(newValue);
            runSimulation.setDisable(true);
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
        });
    }

    @FXML
    protected void setupSimulation(ActionEvent event) {
        simulator = new Simulator(servers, technicians, computers, print);
        runSimulation.setDisable(false);
    }

    @FXML
    protected void handleRunSimulation(ActionEvent event) {
        for (int i = 1; i <= duration; i++) {
            simulator.simulateOneStep();
            step.textProperty().setValue(String.valueOf(i));
            if (waitLogs.isSelected()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
