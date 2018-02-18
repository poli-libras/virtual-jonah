package br.usp.libras.jonah.interpolation;

import static org.junit.Assert.*;

import org.junit.Test;

public class InterpolationTimerTest {

    @Test
    public void test() {
        
        InterpolationTimer timer = new InterpolationTimer(0.1f);
        assertEquals(0.1f, timer.increment(), 0.01);
        assertEquals(0.2f, timer.increment(), 0.01);
        assertEquals(0.3f, timer.increment(), 0.01);
        assertEquals(0.4f, timer.increment(), 0.01);
        assertEquals(0.5f, timer.increment(), 0.01);
        assertEquals(0.6f, timer.increment(), 0.01);
        assertEquals(0.7f, timer.increment(), 0.01);
        assertEquals(0.8f, timer.increment(), 0.01);
        assertEquals(0.9f, timer.increment(), 0.01);
        assertEquals(1.0f, timer.increment(), 0.01);
        assertEquals(1.0f, timer.increment(), 0.01);
    }
    
    @Test
    public void testReset() {
        
        InterpolationTimer timer = new InterpolationTimer(0.2f);
        assertEquals(0.2f, timer.increment(), 0.01);
        assertEquals(0.4f, timer.increment(), 0.01);
        timer.reset();
        assertEquals(0.0, timer.getTime(), 0.01);
        assertEquals(0.2f, timer.increment(), 0.01);
    }
    
    /**
     * Speedup dobra o passo.
     */
    @Test
    public void testSpeedUp() {
        
        InterpolationTimer timer = new InterpolationTimer(0.05f);
        assertEquals(0.05f, timer.increment(), 0.01);
        assertEquals(0.10f, timer.increment(), 0.01);
        timer.speedUp();
        assertEquals(0.20f, timer.increment(), 0.01);
        assertEquals(0.30f, timer.increment(), 0.01);
    }
    
    /**
     * Slowdown desacelera o passo pela metade.
     */
    @Test
    public void testSlowdown() {
        
        InterpolationTimer timer = new InterpolationTimer(0.5f);
        assertEquals(0.5f, timer.increment(), 0.01);
        timer.slowDown();
        assertEquals(0.75f, timer.increment(), 0.01);
        assertEquals(1.0f, timer.increment(), 0.01);
    }

}

