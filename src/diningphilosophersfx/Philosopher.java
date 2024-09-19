/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diningphilosophersfx;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author P. Boots
 */
public class Philosopher implements Runnable {

    private final int STEPS = 20;

    private final Chopstick rightStick;
    private final Chopstick leftStick;
    private final DiningPhilosophersFX dpfx;

    public Philosopher(Chopstick right, Chopstick left, DiningPhilosophersFX dpfx) {
        this.percentageTakenLeft = 0;
        this.percentageTakenRight = 0;
        this.rightStick = right;
        this.leftStick = left;
        this.dpfx = dpfx;
    }

    public enum state {
        Thinking, Hungry, Eating, LeftStickTaken, RightStickTaken
    };
    private state philState;
    private double percentageTakenLeft; // for nice animations
    private double percentageTakenRight;


    @Override
    public void run() {
        try {
            while (true) {
                // Think for a few seconds
                setState(state.Thinking);
                Thread.sleep(2000);

                // Stop thinking, get hungry
                setState(state.Hungry);

                // Take both sticks
                dpfx.chopstickMonitor.get(leftStick, rightStick);

                setState(state.Eating);
                drawTakeBothSticks();

                // Eat for a few seconds
                System.out.println("thread " + Thread.currentThread().getName() + ": EAT");
                Thread.sleep(2000);

                // Return both stick
                dpfx.chopstickMonitor.put(leftStick, rightStick);
                drawReturnBothSticks();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Philosopher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public state getState() {
        return philState;
    }

    public void setState(state State) {
        philState = State;
        dpfx.requestDraw();
    }

    public double getPercentageTakenLeft() {
        return percentageTakenLeft;
    }

    public void setPercentageTakenLeft(double PercentageTakenLeft) {
        this.percentageTakenLeft = PercentageTakenLeft;
        dpfx.requestDraw();
    }

    public double getPercentageTakenRight() {
        return percentageTakenRight;
    }

    public void setPercentageTakenRight(double PercentageTakenRight) {
        this.percentageTakenRight = PercentageTakenRight;
        dpfx.requestDraw();
    }

    void drawTakeBothSticks() throws InterruptedException {
        for (int i = 0; i < STEPS; i++) {
            Thread.sleep(100);
            setPercentageTakenLeft(getPercentageTakenLeft() + 1.0 / STEPS);
            setPercentageTakenRight(getPercentageTakenRight() + 1.0 / STEPS);
        }
    }

    void drawReturnBothSticks() throws InterruptedException {
        for (int i = 0; i < STEPS; i++) {
            Thread.sleep(100);
            setPercentageTakenLeft(getPercentageTakenLeft() - 1.0 / STEPS);
            setPercentageTakenRight(getPercentageTakenRight() - 1.0 / STEPS);
        }
    }
}

