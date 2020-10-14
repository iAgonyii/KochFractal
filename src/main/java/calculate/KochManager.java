/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import fun3kochfractalfx.FUN3KochFractalFX;
import timeutil.TimeStamp;

/**
 *
 * @author Nico Kuijpers
 * Modified for FUN3 by Gertjan Schouten
 */
public class KochManager {
    
    private KochFractal koch;
    private ArrayList<Edge> edges;
    private FUN3KochFractalFX application;
    private TimeStamp tsCalc;
    private TimeStamp tsDraw;
    
    public KochManager(FUN3KochFractalFX application) {
        this.edges = new ArrayList<Edge>();
        this.koch = new KochFractal();
        this.application = application;
        this.tsCalc = new TimeStamp();
        this.tsDraw = new TimeStamp();
    }
    
    public void changeLevel(int nxt) {
        edges.clear();
        koch.setLevel(nxt);
        tsCalc.init();
        tsCalc.setBegin("Begin calculating");
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<RunnableThread> callables = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            callables.add(new RunnableThread(EdgeEnum.values()[i - 1], koch));
        }

        List<Future<List<Edge>>> resultList = null;
        try {
            resultList = executorService.invokeAll(callables);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tsCalc.setEnd("End calculating");
            application.setTextNrEdges("" + koch.getNrOfEdges());
            application.setTextCalc(tsCalc.toString());
            try {
                this.edges = (ArrayList<Edge>) resultList.get(0).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            application.requestDrawEdges();
            executorService.shutdown();
        }

//        koch.generateLeftEdge();
//        koch.generateBottomEdge();
//        koch.generateRightEdge();
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
    }
    
    public void addEdge(Edge e) {
        edges.add(e);
    }
}
