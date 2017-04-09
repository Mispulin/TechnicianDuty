package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Counter;
import app.thread.SimulationThread;
import app.thread.GuiThread;
import app.thread.StopSimulationThread;
import model.entity.Entity;
import model.entity.Server;
import model.entity.Technician;
import model.entity.Computer;
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
    private int duration = 25;
    private StopSimulationThread stopSimulation;
    private static int matrixSize = 15;
    private static double cellWidth = 30;
    private static double cellHeight = 25;

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
    @FXML
    private GridPane matrix;
    @FXML
    private TextArea logArea;

    private Label[][] field = new Label[matrixSize][matrixSize];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleSteps();
        handleLogs();
        handleCounts();
        handleMatrix();
    }

    private void handleSteps() {
        steps.setText(String.valueOf(duration));
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
            val = val > 10 ? 10 : val;
            countTechnicians.setText(String.valueOf(val));
            technicians = val;
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
        countComputers.appendText(String.valueOf(computers));
        countComputers.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 10 ? 10 : val;
            computers = val;
            countComputers.setText(String.valueOf(val));
            runSimulation.setDisable(true);
            takeAStep.setDisable(true);
        });
    }

    private void handleMatrix() {
        matrix.getStyleClass().add("grid");
        for (int i = 0; i < matrixSize; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(cellWidth);
            matrix.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < matrixSize; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(cellHeight);
            matrix.getRowConstraints().add(rowConst);
        }
        Platform.runLater(() -> {
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    field[i][j] = createEntityMark(new Label (""), null);
                    matrix.add(field[i][j], j, i);
                }
            }
        });
    }

    public static Label createEntityMark(Label old, Entity entity) {
        Label label = old;
        old.setText("");
        label.setPrefWidth(cellWidth);
        label.setPrefHeight(cellHeight);
        if (entity != null) {
            String name = entity.getName();
            String number = name.charAt(0) + "" + String.valueOf(validNumber(name));
            label.setText(number);
            label.setAlignment(Pos.CENTER);
            if (entity instanceof Server) {
                label.getStyleClass().add("server");
                return label;
            }
            if (entity instanceof Technician) {
                label.getStyleClass().add("technician");
                return label;
            }
            if (entity instanceof Computer) {
                if (((Computer) entity).getWorking()) {
                    label.getStyleClass().clear();
                } else {
                    label.getStyleClass().add("broken");
                }
                label.getStyleClass().add("computer");
                return label;
            }
        } else {
            label.getStyleClass().clear();
            label.getStyleClass().add("cell");
        }
        return label;
    }

    @FXML
    protected void setupSimulation() {
        Counter.reset();
        logArea.setText("");
        logArea.getStyleClass().add("logArea");
        simulator = new Simulator(matrixSize, servers, technicians, computers, print);
        runSimulation.setDisable(false);
        takeAStep.setDisable(false);
        pause.setDisable(true);
        stopSim.setDisable(true);
        step.setText("0");
        Platform.runLater(() -> {
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    Entity entity = simulator.getEnvironment().getEntityAt(i, j);
                    if (field[i][j] != null) {
                        matrix.getChildren().remove(field[i][j]);
                    }
                    field[i][j] = createEntityMark(new Label (""), entity);
                    matrix.add(field[i][j], j, i);
                }
            }
        });
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
        SimulationThread simulationThread = new SimulationThread(simulator, duration, guiThread, field);
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
        // simulator.getEnvironment().print();
        Platform.runLater(() -> {
            for (int k = 0; k < matrixSize; k++) {
                for (int j = 0; j < matrixSize; j++) {
                    Entity entity = simulator.getEnvironment().getEntityAt(k, j);
                    field[k][j] = Controller.createEntityMark(field[k][j], entity);
                }
            }
        });
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

    private static int validNumber(String value) {
        String val = value;
        if (!val.matches("\\d*")) {
            val = val.replaceAll("[^\\d]", "");
        }
        return Integer.valueOf(val);
    }

}
