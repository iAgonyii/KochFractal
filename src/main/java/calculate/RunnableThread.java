package calculate;

import fun3kochfractalfx.FUN3KochFractalFX;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.ObjectStreamConstants;
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

public class RunnableThread extends Task<ArrayList> implements Callable<ArrayList>, Observer {

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
        if (edge == EdgeEnum.RIGHT) {
            this.koch.generateRightEdge(this);
        } else if (edge == EdgeEnum.BOTTOM) {
            this.koch.generateBottomEdge(this);
        } else if (edge == EdgeEnum.LEFT) {
            this.koch.generateLeftEdge(this);
        }
        return koch.getEdges();
    }

    public void cancelTask() {
        this.koch.cancel();
    }


    @Override
    public void update(Observable o, Object arg) {
            updateProgress(koch.getEdges().size(), maxEdges);
            updateMessage(koch.getEdges().size() + "/" + maxEdges);
    }
}
