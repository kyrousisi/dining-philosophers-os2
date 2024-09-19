/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diningphilosophersfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author 871412
 */
public class DiningPhilosophersFX extends Application {

    private GraphicsContext gc;
    private final int NR = 5;
    private final int TABLERADIUS = 200;
    private final int PLATERADIUS = 40;
    private final int PHILRADIUS = 50;
    private final int STICKLENGTH = 75;
    private final Philosopher[] phils = new Philosopher[NR];
    private final Chopstick[] sticks = new Chopstick[NR];
    private final Color[] colors = new Color[NR];
     ChopstickMonitor chopstickMonitor = new ChopstickMonitor();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.setTitle("Dining Philosophers");
        Group root = new Group();
        Canvas canvas = new Canvas(600, 620);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        for (int i = 0; i < NR; i++) {
            sticks[i] = new Chopstick(i);
        }

        for (int i = 0; i < NR; i++) {
            phils[i] = new Philosopher(sticks[i], sticks[(i + NR - 1) % NR], this);
            Thread t = new Thread(phils[i]);
            t.setName("Philo-" + i);
            t.setDaemon(true);
            t.start();
        }

        colors[0] = Color.GREEN;
        colors[1] = Color.RED;
        colors[2] = Color.BLUE;
        colors[3] = Color.YELLOW;
        colors[4] = Color.MAGENTA;
        requestDraw();

        new ThreadOverview();
    }

    @Override
    public void stop() {
        System.exit(0);
        System.out.println("stop");
    }

    public void requestDraw() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                drawTable();
                drawPhilosophers();
                drawSticks();

            }
        });
    }

    private void drawTable() {
        gc.setFill(Color.BISQUE);
        gc.fillOval(100, 100, 2 * TABLERADIUS, 2 * TABLERADIUS);

        gc.setFill(Color.WHITE);
        gc.fillOval(250, 250, 100, 100);

        for (int i = 0; i < NR; i++) {
            int cx = (int) (300 + 150 * Math.sin(i * 2 * Math.PI / NR));
            int cy = (int) (300 + 150 * Math.cos(i * 2 * Math.PI / NR));
            gc.setFill(Color.LIGHTBLUE);
            gc.fillOval(cx - PLATERADIUS, cy - PLATERADIUS, 2 * PLATERADIUS, 2 * PLATERADIUS);
        }
    }

    private void drawPhilosophers() {
        for (int i = 0; i < NR; i++) {
            int cx = (int) (300 + 250 * Math.sin(i * 2 * Math.PI / NR));
            int cy = (int) (300 + 250 * Math.cos(i * 2 * Math.PI / NR));
            float MaxPercentage = (float) Math.max(phils[i].getPercentageTakenLeft(),
                    phils[i].getPercentageTakenRight());
            float MinPercentage = (float) Math.min(phils[i].getPercentageTakenLeft(),
                    phils[i].getPercentageTakenRight());
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(1);
            gc.strokeOval(cx - PHILRADIUS, cy - PHILRADIUS, 2 * PHILRADIUS, 2 * PHILRADIUS);
            gc.setStroke(colors[i]);
            gc.setLineWidth(10);
            if (phils[i].getState() == Philosopher.state.Eating) {
                if (MinPercentage >= 0.9) {
                    gc.setFill(colors[i]);
                    gc.fillOval(cx - PHILRADIUS, cy - PHILRADIUS, 2 * PHILRADIUS, 2 * PHILRADIUS);
                } else {
                    gc.strokeOval(cx - MinPercentage * PHILRADIUS, cy - MinPercentage * PHILRADIUS,
                            2 * MinPercentage * PHILRADIUS, 2 * MinPercentage * PHILRADIUS);
                    gc.strokeOval(cx - MaxPercentage * PHILRADIUS, cy - MaxPercentage * PHILRADIUS,
                            2 * MaxPercentage * PHILRADIUS, 2 * MaxPercentage * PHILRADIUS);
                }

            } else if (phils[i].getState() == Philosopher.state.Hungry) {
                gc.strokeOval(cx - 0.2f * PHILRADIUS, cy - 0.2f * PHILRADIUS, 2 * 0.2f * PHILRADIUS,
                        2 * 0.2f * PHILRADIUS);
            } else if (phils[i].getState() != Philosopher.state.Thinking) {
                gc.strokeOval(cx - MinPercentage * PHILRADIUS, cy - MinPercentage * PHILRADIUS,
                        2 * MinPercentage * PHILRADIUS, 2 * MinPercentage * PHILRADIUS);
                gc.strokeOval(cx - MaxPercentage * PHILRADIUS, cy - MaxPercentage * PHILRADIUS,
                        2 * MaxPercentage * PHILRADIUS, 2 * MaxPercentage * PHILRADIUS);

            }
        }
    }

    private void drawSticks() {
        int x1, y1, x2, y2;
        int stickRadius, stickRadiusDelta;
        double percentage;
        double stickAngle, StickAngleDelta;
        double StickAngleStart;
        int StickRadiusStart = 100;
        for (int i = 0; i < NR; i++) {
            StickAngleStart = Math.PI / NR + i * 2 * Math.PI / NR;
            if (phils[i].getState() == Philosopher.state.RightStickTaken
                    || phils[i].getState() == Philosopher.state.Eating) {
                stickRadiusDelta = 100;
                StickAngleDelta = -Math.PI / (2 * NR);
                percentage = phils[i].getPercentageTakenRight();
            } else if (phils[(i + 1) % NR].getState() == Philosopher.state.LeftStickTaken
                    || phils[(i + 1) % NR].getState() == Philosopher.state.Eating) {
                stickRadiusDelta = 100;
                StickAngleDelta = Math.PI / (2 * NR);
                percentage = phils[(i + 1) % NR].getPercentageTakenLeft();
            } else {
                stickRadiusDelta = 0;
                StickAngleDelta = 0;
                percentage = 0;
            }

            stickRadius = (int) (StickRadiusStart + percentage * stickRadiusDelta);
            stickAngle = StickAngleStart + percentage * StickAngleDelta;
            x1 = (int) (300 + stickRadius * Math.sin(stickAngle));
            y1 = (int) (300 + stickRadius * Math.cos(stickAngle));
            x2 = (int) (300 + (stickRadius + STICKLENGTH) * Math.sin(stickAngle));
            y2 = (int) (300 + (stickRadius + STICKLENGTH) * Math.cos(stickAngle));
            gc.setStroke(Color.LIGHTBLUE);
            gc.setLineWidth(10);
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
