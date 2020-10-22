/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import fun3kochfractalfx.FUN3KochFractalFX;
import javafx.concurrent.Task;
import timeutil.TimeStamp;

/**
 *
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 */
public class KochManager {

    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;
    List<RunnableThread> callables;
    
    public KochManager(FUN3KochFractalFX application) {
        this.edges = new ArrayList<Edge>();
        this.application = application;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();
    }
    
    public void changeLevel(int nxt) {
        if (callables != null) {
            for (RunnableThread task: callables) {
                task.cancelTask();
            }
        }
        edges.clear();
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        callables = new ArrayList<>();

        RunnableThread left = new RunnableThread(EdgeEnum.LEFT, this, application, nxt);
        RunnableThread bottom = new RunnableThread(EdgeEnum.BOTTOM, this, application, nxt);
        RunnableThread right = new RunnableThread(EdgeEnum.RIGHT, this, application, nxt);

        callables.add(left);
        callables.add(bottom);
        callables.add(right);

        application.getLeftBar().progressProperty().unbind();
        application.getBottomBar().progressProperty().unbind();
        application.getRightBar().progressProperty().unbind();

        application.getLeftBar().setProgress(0);
        application.getBottomBar().setProgress(0);
        application.getRightBar().setProgress(0);

        application.getLeftBar().progressProperty().bind(left.progressProperty());
        application.getBottomBar().progressProperty().bind(bottom.progressProperty());
        application.getRightBar().progressProperty().bind(right.progressProperty());

        application.getLeftEdgesLabel().textProperty().bind(left.messageProperty());
        application.getBottomEdgesLabel().textProperty().bind(bottom.messageProperty());
        application.getRightEdgesLabel().textProperty().bind(right.messageProperty());

        List<Future<ArrayList>> results = null;
        try {
            results = executorService.invokeAll(callables);
            for (Future<ArrayList> future: results) {
                this.edges.addAll(future.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tsCalc.setEnd("End calculating");
            application.setTextCalc(tsCalc.toString());
            application.requestDrawEdges();
            executorService.shutdown();
        }
    }
    
    public void drawEdges() {
        tsDraw.init();
        tsDraw.setBegin("Begin drawing");
        application.clearKochPanel();
        for (Edge e : edges) {
            application.drawEdge(e);
        }
        tsDraw.setEnd("End drawing");
        application.setTextDraw(tsDraw.toString());
        application.setTextNrEdges(Integer.toString(edges.size()));
        edges.clear();
    }
    
    public synchronized void addEdge(Edge e) {
        edges.add(e);
    }
}
