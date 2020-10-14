package calculate;

import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.Callable;

enum EdgeEnum {
    RIGHT,
    BOTTOM,
    LEFT
}

public class RunnableThread implements Callable<List<Edge>> {

    private EdgeEnum edge;
    private KochFractal koch;

    public RunnableThread(EdgeEnum edge, KochFractal koch) {
        this.edge = edge;
        this.koch = koch;
    }

    @Override
    public List<Edge> call() throws Exception {
        if (edge == EdgeEnum.RIGHT) {
            this.koch.generateRightEdge();
        } else if (edge == EdgeEnum.BOTTOM) {
            this.koch.generateBottomEdge();
        } else if (edge == EdgeEnum.LEFT) {
            this.koch.generateLeftEdge();
        }
        return this.koch.getEdges();
    }
}
