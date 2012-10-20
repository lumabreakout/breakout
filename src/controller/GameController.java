package controller;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import communication.Observable;

import data.Ball;
import data.PlayGrid;
import data.bricks.AbstractBrick;
import data.bricks.Slider;


public class GameController extends Observable {

	public enum PLAYER_INPUT {
		LEFT,
		RIGHT,
		CLOSE,
		PAUSE,
		START
	}

	public enum GAME_STATE {
		RUNNING,
		PAUSED,
		GAMEOVER
	}

	private class GameTimerTask extends TimerTask {		
		public GameTimerTask() {
			super();
		}		

		@Override
		public void run() {
			updateGame();			
		}		
	}

	private PlayGrid grid;	
	private Timer timer;
	private GameTimerTask task;
	private GAME_STATE state;

	public GameController() {
		super();		

		initialize();
	}

	public void initialize() {
		// init timer
		task = new GameTimerTask();
		timer = new Timer("Game_timer");	
		setState(GAME_STATE.PAUSED);
	}

	/**
	 * Prepares the next frame of the game:
	 * - Move balls
	 * - Check game rules (game over etc.)
	 * - Request repaint
	 */
	public void updateGame() {
		moveBalls();	

		notifyRepaintPlayGrid();

		//TODO check gameover
	}


	public void start() {
		timer.scheduleAtFixedRate(task, 0, 50);
		setState(GAME_STATE.RUNNING);
		notifyGameStateChanged(state);
	}

	public void stop() {
		timer.cancel();
		setState(GAME_STATE.PAUSED);
		notifyGameStateChanged(state);
	}

	public void terminate() {
		stop();
		setState(state = GAME_STATE.GAMEOVER);
		//TODO
	}

	/**
	 * Process interactive user input (e.g. from key hits)
	 */
	public void processInput(PLAYER_INPUT input) {
		switch (input) {
		case START:
			start();
			break;
		case LEFT:
			moveSlider(-1);
			break;
		case RIGHT:
			moveSlider(+1);
			break;
		case PAUSE:
			stop();
			break;
		case CLOSE:
			terminate();
			break;
		}
	}

	/**
	 * Control slider movements since slider has no information about the grid.
	 * @param delta Positive or negative value to move slider.
	 */
	private void moveSlider(int delta) {
		int newx = getGrid().getSlider().getX() + delta;
		if (newx < 0) 
			return;
		else if (newx > getGrid().getWidth() - getGrid().getSlider().getWidth())
			return;
		else
			getGrid().getSlider().setX(newx);
	}


	/**
	 * Moves all balls, regarding collisions with bricks, the grid borders and the slider.
	 * Balls and bricks get removed by this method when the grid or a brick signals to do so.
	 */
	private void moveBalls() {
		Iterator<AbstractBrick> itbrick;
		Iterator<Ball> itball;
		AbstractBrick currentBrick; 
		Ball currentBall;

		itball = getGrid().getBalls().iterator();
		while (itball.hasNext()){ 
			currentBall = itball.next();

			// check for collisions with bricks (and change direction)
			itbrick = getGrid().getBricks().iterator();
			while (itbrick.hasNext()) {
				currentBrick = itbrick.next();
				if (currentBrick.tryCollision(currentBall))
					itbrick.remove();
			}

			// check for collisions with grid borders (and change direction)
			if (getGrid().tryCollision(currentBall)) {
				itball.remove();
			}			

			// check for collisions with slider (and change direction)
			Slider s = getGrid().getSlider();
			s.tryCollision(currentBall);

			// move balls
			currentBall.setX(currentBall.getX() + currentBall.getSpeedX());
			currentBall.setY(currentBall.getY() + currentBall.getSpeedY());
		}


	}


	public PlayGrid getGrid() {
		return grid;
	}


	public void setGrid(PlayGrid grid) {
		this.grid = grid;
	}

	public GAME_STATE getState() {
		return state;
	}

	public void setState(GAME_STATE state) {
		this.state = state;
	}
}
