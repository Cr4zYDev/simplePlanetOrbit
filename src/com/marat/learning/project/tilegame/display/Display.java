package com.marat.learning.project.tilegame.display;

import java.awt.*;

import javax.swing.JFrame;

public class Display {
	 
	private JFrame frame;
	private Canvas canvas;
	public Canvas getCanvas() {
		return canvas;
	}
	
	private String TITLE;
	private int WIDTH, HEIGHT;
	
	public Display(String title, int width, int height) {
		this.TITLE = title;
		this.WIDTH = width;
		this.HEIGHT = height;
		
		createDisplay();		
	}

	private void createDisplay() {
		this.frame = new JFrame(TITLE);
		this.frame.setSize(this.WIDTH, this.HEIGHT);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
		
		this.frame.setAlwaysOnTop(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		
		frame.add(canvas);
		frame.pack();
	}
}
