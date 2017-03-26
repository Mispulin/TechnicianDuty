package app.thread;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * Created by Mish.k.a on 26. 3. 2017.
 */
public class GuiThread extends Thread {

    private Label label;

    public GuiThread(Label label) {
        this.label = label;
    }

    @Override
    public void run() {
        super.run();
    }

    public void updateGUI(int step) {
        Platform.runLater(
                () -> {
                    label.setText(String.valueOf(step));
                }
        );
    }

}
