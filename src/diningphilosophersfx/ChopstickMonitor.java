/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diningphilosophersfx;

/**
 *
 * @author P. Boots
 */
public class ChopstickMonitor {

    // Pick up two chopsticks
    public synchronized void get(Chopstick left, Chopstick right) {

        while (left.isUsed() || right.isUsed()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        right.get();
        left.get();

    }

    // Lay down two chopsticks
    public synchronized void put(Chopstick left, Chopstick right) {
        
        left.put();
        right.put();

        notifyAll();
    }
}
