package com.marat.learning.project.tilegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.marat.learning.project.tilegame.display.Display;
import com.marat.learning.project.tilegame.gfx.ImageLoader;

public class Game implements Runnable {
	
	private Display display;	
	public int WIDTH, HEIGHT;
	public String TITLE;
	
	private boolean alive = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics gfx;
	
	private BufferedImage tank;
	
	public Game(String title, int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.TITLE = title;
	}
	
	private void init() {
		display = new Display(TITLE, WIDTH, HEIGHT);
		tank = ImageLoader.loadImage("/textures/tank.png");
	}

	final Color c_sun = new Color(255,255,0);
	final Color c_sun_outline = new Color(64,64,0);
	
	final Color c_earth = new Color(0,64,128);
	final Color c_earth_outline = new Color(0,16,32);
	
	final Color c_moon = new Color(128,128,128);
	final Color c_moon_outline = new Color(64,64,64);
	
	
	final int size_sun = 50;
	final int size_earth = 25;
	final int size_moon = 7;
	
	final double speed_earth = 50000;
	final double speed_moon = 5000;
	
	final int distance_earth = 100;
	final int distance_moon = 25;	
	
	double current_earth = 0;
	double current_moon = 0;
	
	private void tick() {
		if(current_earth >= Math.PI * 2) current_earth = 0;
		if(current_moon >= Math.PI * 2) current_moon = 0;
		
		current_earth += Math.PI * 2 * ( 1 / speed_earth);
		current_moon += Math.PI * 2 * (1 / speed_moon);
		
	}	
	
	private void render() {
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		
		gfx = bs.getDrawGraphics();
		gfx.clearRect(0, 0, WIDTH, HEIGHT);
		// start drawing
		
		gfx.setColor(Color.lightGray);
		gfx.drawLine(
				WIDTH / 2,
				HEIGHT / 2,
				(int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2, 
				(int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2);

		gfx.setColor(Color.lightGray);
		gfx.drawLine(
				(int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2,
				(int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2,
				(int)(Math.cos(current_moon) * distance_moon) + ((int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2),
				(int)(Math.sin(current_moon) * distance_moon) + ((int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2));

		
		gfx.setColor(Color.LIGHT_GRAY);
		gfx.drawArc(
				WIDTH / 2 - (distance_earth * 2) / 2, 
				HEIGHT / 2 - (distance_earth * 2) / 2,
				distance_earth * 2,
				distance_earth * 2,
				0, 360);
		gfx.drawArc(
				(int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2 - size_earth,
				(int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2- size_earth,				distance_moon * 2,
				distance_moon * 2,
				0, 360);
				
		// SUN
		gfx.setColor(c_sun);
		gfx.fillArc(WIDTH / 2 - size_sun / 2, HEIGHT / 2- size_sun / 2, size_sun, size_sun, 0, 360);
		gfx.setColor(c_sun_outline);
		gfx.drawArc(WIDTH / 2 - size_sun / 2, HEIGHT / 2- size_sun / 2, size_sun, size_sun, 0, 360);
		
		// ERATH
		gfx.setColor(c_earth);
		gfx.fillArc(
				(int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2 - size_earth/2,
				(int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2- size_earth/2, 
				size_earth, size_earth, 
				0, 360);
		gfx.setColor(c_earth_outline);
		gfx.drawArc(
				(int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2 - size_earth/2,
				(int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2- size_earth/2, 
				size_earth, size_earth, 
				0, 360);
		
		
		// MOON
		gfx.setColor(c_moon);
		gfx.fillArc(
				(int)(Math.cos(current_moon) * distance_moon) + ((int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2) - size_moon / 2,
				(int)(Math.sin(current_moon) * distance_moon) + ((int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2)- size_moon / 2,
				size_moon, size_moon,
				0, 360);
		gfx.setColor(c_moon_outline);
		gfx.drawArc(
				(int)(Math.cos(current_moon) * distance_moon) + ((int)(Math.cos(current_earth) * distance_earth) + WIDTH / 2) - size_moon / 2,
				(int)(Math.sin(current_moon) * distance_moon) + ((int)(Math.sin(current_earth) * distance_earth) + HEIGHT / 2)- size_moon / 2,
				size_moon, size_moon,
				0, 360);

		// stop drawing		
		bs.show();
		gfx.dispose();
		
	}
	
	public void run() {
		
		init();
		
		while(alive) {
			tick();
			render();
		}
		
		stop();
	}

	public synchronized void start() {
		if(alive) return;
		
		alive = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!alive) return;
		
		alive = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}