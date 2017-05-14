package app;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import model.Counter;
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

    public static Controller instance;

    @FXML
    private TextField countServers;
    @FXML
    private TextField countTechnicians;
    @FXML
    private TextField countComputers;
    @FXML
    private Button takeAStep;
    @FXML
    private Button setupSimulation;
    @FXML
    private Label step;
    @Getter
    @FXML
    private TextArea logArea;
    @FXML
    protected StackPane gridWrapper;

    private Simulator simulator;
    private int servers = 1;
    private int technicians = 2;
    private int computers = 5;
    private int duration = 100;
    private static int matrixSize = 11;
    private IntegerProperty cellSizeProperty = new SimpleIntegerProperty(25);


    private GridPane matrix;
    private Label[][] field = new Label[matrixSize][matrixSize];

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        gridWrapper.widthProperty().addListener(gridWrapperSizeChangeListener);
        gridWrapper.heightProperty().addListener(gridWrapperSizeChangeListener);
        matrix = prepareGridPane();
        gridWrapper.getChildren().add(matrix);

        handleSteps();
        handleCounts();
    }
    private ChangeListener<Number> gridWrapperSizeChangeListener = (observable, oldValue, newValue) -> {
        if (gridWrapper.getBoundsInLocal().getWidth() > gridWrapper.getBoundsInLocal().getHeight()) {
            cellSizeProperty.set(new Double(gridWrapper.getHeight() / matrixSize).intValue());
        } else {
            cellSizeProperty.set(new Double(gridWrapper.getWidth() / matrixSize).intValue());
        }
    };

    private GridPane prepareGridPane() {
        GridPane ret = new GridPane();
        ret.prefWidthProperty().bind(cellSizeProperty);
        ret.prefHeightProperty().bind(cellSizeProperty);
        ret.maxWidthProperty().bind(ret.prefWidthProperty());
        ret.maxHeightProperty().bind(ret.prefHeightProperty());

        ret.setGridLinesVisible(true);

        ret.getStyleClass().add("grid");
        for (int i = 0; i < matrixSize; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.prefWidthProperty().bind(cellSizeProperty);
            colConst.minWidthProperty().bind(cellSizeProperty);
            ret.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < matrixSize; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.prefHeightProperty().bind(cellSizeProperty);
            // rowConst.setFillHe(true);
            rowConst.minHeightProperty().bind(cellSizeProperty);
            ret.getRowConstraints().add(rowConst);
        }

        Platform.runLater(() -> {
            for (int i = 0; i < matrixSize; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    field[i][j] = createEntityMark(new Label(""), null);
                    matrix.add(field[i][j], j, i);
                }
            }
        });


        return ret;
    }

    private void handleSteps() {
        step.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.valueOf(newValue) == duration) {
                setupSimulation.setDisable(false);
                setSettingsDisable();
            }
        });
    }

    private void handleCounts() {
        Platform.runLater( () -> countServers.appendText(String.valueOf(servers)) );
        countServers.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 5 ? 5 : val;
            countServers.setText(String.valueOf(val));
            takeAStep.setDisable(true);
            servers = val;
        });
        Platform.runLater( () -> countTechnicians.appendText(String.valueOf(technicians)) );
        countTechnicians.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 6 ? 6 : val;
            countTechnicians.setText(String.valueOf(val));
            technicians = val;
            takeAStep.setDisable(true);
        });
        Platform.runLater( () -> countComputers.appendText(String.valueOf(computers)) );
        countComputers.textProperty().addListener((observable, oldValue, newValue) -> {
            int val = validNumber(newValue);
            val = val > 10 ? 10 : val;
            computers = val;
            countComputers.setText(String.valueOf(val));
            takeAStep.setDisable(true);
        });
    }

    public Label createEntityMark(Label old, Entity entity) {
        Label label = old;
        old.setText("");
        label.prefWidthProperty().bind(cellSizeProperty);
        label.prefHeightProperty().bind(cellSizeProperty);

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
        simulator = new Simulator(matrixSize, servers, technicians, computers);
        takeAStep.setDisable(false);
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
        simulator.getEntities().forEach(entity -> logArea.appendText(entity.toString() + "\n") );
    }

    @FXML
    protected void handleTakeAStep() {
        step.setText(String.valueOf(simulator.getStep()));
        logArea.appendText("\nStep " + simulator.getStep() + "\n\n");
        simulator.simulateOneStep();
        Platform.runLater(() -> {
            for (int k = 0; k < matrixSize; k++) {
                for (int j = 0; j < matrixSize; j++) {
                    Entity entity = simulator.getEnvironment().getEntityAt(k, j);
                    field[k][j] = createEntityMark(field[k][j], entity);
                }
            }
        });
    }

    private void setSettingsDisable() {
        countServers.setDisable(false);
        countTechnicians.setDisable(false);
        countComputers.setDisable(false);
    }

    private static int validNumber(String value) {
        String val = value;
        if (!val.matches("\\d*")) {
            val = val.replaceAll("[^\\d]", "");
        }
        return Integer.valueOf(val);
    }

}
