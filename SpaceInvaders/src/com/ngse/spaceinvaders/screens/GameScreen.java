package com.ngse.spaceinvaders.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

import com.ngse.spaceinvaders.Config;
import com.ngse.spaceinvaders.SpaceInvadersGame;
import com.ngse.spaceinvaders.ai.AlienSystemAI;
import com.ngse.spaceinvaders.gameobjects.Alien;
import com.ngse.spaceinvaders.gameobjects.AlienBoss;
import com.ngse.spaceinvaders.gameobjects.AlienBullet;
import com.ngse.spaceinvaders.gameobjects.Bullet;
import com.ngse.spaceinvaders.gameobjects.GameObject;
import com.ngse.spaceinvaders.gameobjects.Player;
import com.ngse.spaceinvaders.gameobjects.PlayerBullet;
import com.ngse.spaceinvaders.gameobjects.Upgrade;
import com.ngse.spaceinvaders.handlers.BulletCollisionsHandler;
import com.ngse.spaceinvaders.resources.images.BufferedImageResource;

@SuppressWarnings("serial")
public class GameScreen extends Screen {

	private GameScreen gamescreen;
	public int GameClock;
	public int Level;

	// GameObjects:
	public Player player;

	public List<PlayerBullet> playerBullets;

	public List<Alien> aliens;
	public List<AlienBullet> alienBullets;

	public AlienBoss alienBoss;

	public List<Upgrade> upgrades;

	// AlienSystemAI for this game
	public AlienSystemAI ASAI;

	// GameState
	private enum GameState {
		RUNNING, PAUSE
	}

	private GameState gameState;

	/*
	 * Constructor
	 */
	public GameScreen() {
		this.gamescreen = this;
		this.GameClock = 0;
		// Initialize the GameState
		this.gameState = GameState.RUNNING;
		// Initialize the player
		this.player = new Player(0, 0, 0, 0, BufferedImageResource.Spaceship);
		this.player.setX(SpaceInvadersGame.frame.getWidth() / 2
				- player.getImage().getWidth() / 2);
		this.player.setY(SpaceInvadersGame.frame.getHeight() / 2
				- player.getImage().getHeight() / 2);
		// Initialize other GameObjects
		this.playerBullets = new LinkedList<PlayerBullet>();
		this.aliens = new LinkedList<Alien>();
		this.alienBullets = new LinkedList<AlienBullet>();
		this.alienBoss = null;
		this.upgrades = new LinkedList<Upgrade>();

		// Initialize AI's
		this.ASAI = new AlienSystemAI(this);
	}

	/*
	 * PaintComponent
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Reset the screen
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, SpaceInvadersGame.frame.getWidth(),
				SpaceInvadersGame.frame.getHeight());

		// TODO: doGameLogic();
		// this.doGameLogic();

		/*
		 * Draw the gameobjects
		 */
		player.draw(g2);
		for (PlayerBullet pb : playerBullets) {
			if (!pb.equals(null))
				pb.draw(g2);
		}
		for (Alien a : aliens) {
			if (!a.equals(null))
				a.draw(g2);
		}
		for (AlienBullet ab : alienBullets) {
			if (!ab.equals(null))
				ab.draw(g2);
		}
		if (!(alienBoss == null))
			alienBoss.draw(g2);
		for (Upgrade u : upgrades) {
			if (!u.equals(null))
				u.draw(g2);
		}
	}

	/*
	 * KeyEvents
	 */

	// User's KeyInput: Moving the Player
	@Override
	public void keyPressed(KeyEvent e) {
		doKeyPressedEventUpdates(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		doKeyReleasedEventUpdates(e);
	}

	public void doKeyPressedEventUpdates(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			if (gameState.equals(GameState.RUNNING)) {
				pauseGame();
				gameState = GameState.PAUSE;
			} else if (gameState.equals(GameState.PAUSE)) {
				SpaceInvadersGame.setScreen(new StartScreen());
			}
			break;
		case KeyEvent.VK_ENTER:
			if (gameState.equals(GameState.PAUSE)) {
				gameState = GameState.RUNNING;
				SpaceInvadersGame.setScreen(gamescreen);
			}
			break;
		}
		// If Running Gamestate:
		if (gameState.equals(GameState.RUNNING)) {
			// Player
			player.keyPressedEventUpdate(e.getKeyCode());
		} else if (gameState.equals(GameState.PAUSE)) {

		}
	}

	public void doKeyReleasedEventUpdates(KeyEvent e) {
		// If Running Gamestate:
		if (gameState.equals(GameState.RUNNING)) {
			// Player
			player.keyReleaseEventUpdate(e.getKeyCode());
		} else if (gameState.equals(GameState.PAUSE)) {

		}
	}

	/*
	 * Pause popup
	 */
	public void pauseGame() {
		SpaceInvadersGame.timer.stop();
		Graphics2D g2 = (Graphics2D) SpaceInvadersGame.frame.getContentPane()
				.getGraphics();
		BufferedImage pausepopup = BufferedImageResource.PausePopup;
		g2.drawImage(pausepopup, null, (int) SpaceInvadersGame.frame
				.getContentPane().getWidth() - pausepopup.getWidth() * 2,
				(int) SpaceInvadersGame.frame.getContentPane().getHeight()
						- pausepopup.getHeight() * 2);
	}

	/*
	 * ActionPerformed
	 */

	// Constant Timer Updates
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: doGameLogic();
		doGameLogic();
		repaint();

		if (GameClock >= 500 && GameClock < 1500) {
			Level = 1;
		} else if (GameClock >= 1500 && GameClock < 2500) {
			Level = 2;
		} else if (GameClock >= 2500 && GameClock < 3500) {
			Level = 3;
		}

		GameClock++;
	}

	// GameLogics
	private void doGameLogic() {
		// Iterator<PlayerBullet> pbIterate= playerBullets.iterator();

		List<Alien> removeAliens = new LinkedList<Alien>();
		List<PlayerBullet> removeBullets = new LinkedList<PlayerBullet>();

		BulletCollisionsHandler handler = new BulletCollisionsHandler(this);
		handler.update();

		// Update all GameObject Positions
		player.moveUpdate();

		for (PlayerBullet pb : playerBullets) {
			if (!pb.equals(null)) {
				pb.moveUpdate();
				if (!pb.exists()) {
					removeBullets.add(pb);
				}
			}
		}
		for (PlayerBullet bullet : removeBullets)
			playerBullets.remove(bullet);

		for (AlienBullet ab : alienBullets) {
			if (!ab.equals(null))
				ab.moveUpdate();
		}
		if (!(alienBoss == null))
			alienBoss.moveUpdate();
		for (Upgrade u : upgrades) {
			if (!u.equals(null))
				u.moveUpdate();
		}

		ASAI.update();
	}

	public void remove(PlayerBullet object) {
		playerBullets.remove(object);
	}

	public void remove(AlienBullet object) {
		alienBullets.remove(object);
	}

	public void remove(Alien object) {
		aliens.remove(object);
	}

	public void remove(Bullet object) {
		playerBullets.remove(object);
	}

	public void remove(GameObject object) {
		playerBullets.remove(object);
	}

}
