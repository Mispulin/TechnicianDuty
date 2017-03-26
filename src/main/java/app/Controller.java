package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML
    private GridPane matrix;

    private Text[][] field = new Text[10][10];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleSteps();
        handleLogs();
        handleCounts();
        handleMatrix();
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
        matrix.setGridLinesVisible(true);
        final int numCols = 10;
        final int numRows = 10;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            matrix.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            matrix.getRowConstraints().add(rowConst);
        }
    }

    public static Text createEntityMark(Text old, Entity entity) {
        Text text = old;
        old.setText("");
        if (entity != null) {
            String name = entity.getName();
            String number = name.charAt(0) + "" + String.valueOf(validNumber(name));
            text.setText(number);
            if (entity instanceof Server) {
                text.setFill(Color.BLACK);
                return text;
            }
            if (entity instanceof Technician) {
                text.setFill(Color.BLUE);
                return text;
            }
            if (entity instanceof Computer) {
                text.setFill(Color.GREEN);
                return text;
            }
        }
        return text;
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
        field = new Text[10][10];
        Platform.runLater(() -> {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    Entity entity = simulator.getEnvironment().getEntityAt(i, j);
                    field[i][j] = createEntityMark(new Text(""), entity);
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
        Platform.runLater(() -> {
            for (int k = 0; k < 10; k++) {
                for (int j = 0; j < 10; j++) {
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
