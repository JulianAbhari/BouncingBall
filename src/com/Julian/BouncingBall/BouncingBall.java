package com.Julian.BouncingBall;

import java.awt.BorderLayout;
import java.awt.Canvas;

import com.Julian.shapes.JEllipse;
import com.Julian.shapes.JVector;
import com.Julian.window.JBackground;
import com.Julian.window.JWindow;

/**
 * BouncingBall 11/21/16
 * 
 * @author Julian Abhari
 */

// This program is implemting 'Runnable' so that when this program is called it
// can immediately setup everything with the constructor, and start running the
// program with the run() method.
public class BouncingBall extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	boolean running = false;
	JWindow windy;
	JVector location;
	JVector velocity;
	JEllipse ball;
	JBackground background;

	// This constructor creates a new JWindow object, and two JVector objects.
	// One is the location of the ball which is half the window's width, and
	// half the window's height. So the ball's location is 200, 200 since the
	// window is 400, 400. The velocity object is the slope the ball will be
	// moving at, which is 2/1
	public BouncingBall() {
		windy = new JWindow(400, 400, "Bouncing Ball");
		location = new JVector(windy.getWidth() / 2, windy.getHeight() / 2);
		velocity = new JVector(2, 3);
	}

	// This run method is using an algorithm to run the program 1/60th a second
	// or tick. This program could run as fast as the processor can handle but
	// that would make the processor very hot and the program wuld run extremely
	// inconsistently, this should make it a more fixed running experience.
	public void run() {
		running = true;
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 60.0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldUpdate = false;

			while (delta >= 1) {
				delta -= 1;
				shouldUpdate = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// When it's time for the processor to update/render the whole
			// program, it will call the update function. The update function
			// moves the ball and handles whenever it has hits an edge of the
			// screen.
			if (shouldUpdate == true) {
				update();
				shouldUpdate = false;
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
			}
		}
	}

	// When the user exits the program, it will call the stop() function from
	// the runnable interface and stop the processor from updating the ball.
	public void stop() {
		running = false;
	}

	// This update function calls the display and bounce functions while
	// creating a new JPanel object or 'JBackground' object and adds the ball at
	// the center of the planel. If I was to put the ball at BorderLayout.SOUTH
	// it would put the whole ball program relative the bottom. So the ball's
	// location would still be 200, 200 but it would be relative to the panel's
	// BorderLayout.SOUTH.
	public void update() {
		background = new JBackground(255, 255, 255);
		display();
		bounce();
		background.addComponent(ball, BorderLayout.CENTER);
		windy.addComponent(background.getPanel());
	}

	// This display function adds the velocity to the location to change the
	// Ball's position; while recreating the ball at a different location.
	public void display() {
		location.add(velocity.getX(), velocity.getY());
		ball = new JEllipse((int) location.getX(), (int) location.getY(), 50, 50, 0, 255, 0, 255, true);
	}

	// This bounce() function manages how and where the ball where bounce. In
	// this case the ball will bounce wherever it's location is greater than or
	// less than the width's minimum and maximum values. (min 0; max 400). Then
	// it bounces by multiplying the velocity by -1 so the location will add the
	// negative velocity and so it will move the opposite direction. (Obviously
	// this is not how actual velocity is)
	// Now for measuring the location I'm adding the radius if it's going to the
	// right or top, and subtracting the radius if it's going to the bottom or
	// left. The point of this is to basically set the x and y values all around
	// the circle so it doesn't bounce in the middle of the ball.
	public void bounce() {
		if (location.getX() + 25 >= windy.getWidth() || location.getX() - 25 <= 0) {
			velocity.mult(-1, 1);
		}
		if (location.getY() + 25 >= windy.getHeight() || location.getY() - 25 <= 0) {
			velocity.mult(1, -1);
		}
	}

	// This creates a new BouncingBall program to be run.
	public static void main(String[] args) {
		new BouncingBall().run();
	}
}
