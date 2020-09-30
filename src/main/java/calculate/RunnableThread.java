package calculate;

import java.util.concurrent.Callable;

enum EdgeEnum {
    RIGHT,
    BOTTOM,
    LEFT
}

public class RunnableThread implements Callable<Long> {

    private EdgeEnum edge;
    private KochFractal koch;
    private KochManager manager;

    public RunnableThread(EdgeEnum edge, KochFractal koch, KochManager manager) {
        this.edge = edge;
        this.koch = koch;
        this.manager = manager;
    }

    @Override
    public Long call() throws Exception {
        if (edge == EdgeEnum.RIGHT) {
            this.koch.generateRightEdge();
        } else if (edge == EdgeEnum.BOTTOM) {
            this.koch.generateBottomEdge();
        } else if (edge == EdgeEnum.LEFT) {
            this.koch.generateLeftEdge();
        }
        return 1L;
    }
}
