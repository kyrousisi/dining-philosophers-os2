/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diningphilosophersfx;

/**
 *
 * @author Joris
 */
public class Chopstick {
    private final int index;  // for debugging...
    private boolean used;
    
    public Chopstick(int i)
    {
        used = false;
        index = i;
    }
    
    public void get() {
        used = true;
        System.out.println("thread " + Thread.currentThread().getName() + ": get chopstick:" + index);
    }
    
    public void put() {
        System.out.println("thread " + Thread.currentThread().getName() + ": put chopstick:" + index);
        used = false;
    }
    
    public boolean isUsed() {
        return (used);
    }
}
