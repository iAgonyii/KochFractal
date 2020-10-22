package calculate;

import fun3kochfractalfx.FUN3KochFractalFX;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

enum EdgeEnum {
    RIGHT,
    BOTTOM,
    LEFT
}

public class RunnableThread extends Task<ArrayList> implements Callable<ArrayList> {

    private EdgeEnum edge;
    private KochFractal koch;
    private FUN3KochFractalFX application;
    int maxEdges;

    public RunnableThread(EdgeEnum edge, KochManager manager, FUN3KochFractalFX application, int level) {
        this.edge = edge;
        this.koch = new KochFractal();
        this.koch.setLevel(level);
        this.application = application;
        maxEdges = koch.getNrOfEdges() / 3;
    }

    @Override
    public ArrayList call() throws Exception {
        this.updateMessage("Nr edges: 0");
        this.updateProgress(0, 1);
        if (edge == EdgeEnum.RIGHT) {
            this.koch.generateRightEdge();
        } else if (edge == EdgeEnum.BOTTOM) {
            this.koch.generateBottomEdge();
        } else if (edge == EdgeEnum.LEFT) {
            this.koch.generateLeftEdge();
        }
        Platform.runLater(() -> {
            this.updateMessage("Nr edges: " + this.koch.getEdges().size());
            this.updateProgress(1, 1);
        });
        return koch.getEdges();
    }

    public void cancelTask() {
        this.koch.cancel();
    }


}
