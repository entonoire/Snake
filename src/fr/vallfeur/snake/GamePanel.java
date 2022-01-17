package fr.vallfeur.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener{

	static final int S_W = 600,
			S_H = 600,
			UNIT_SIZE = 25,
			GAME_UNITS = (S_W*S_H)/UNIT_SIZE,
			DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6, applesEaten, appleX, appleY;
	char direction = 'R';
	boolean running = false, grid = false, rgb = false;
	Timer timer;
	Random random;
	
	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(S_W, S_H));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter());
		startGame();
	}
	
	public void startGame(){
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g){
		if(running){
			/*
			 * draw grid
			 */
			if(grid){
				for(int i=0;i<S_H/UNIT_SIZE;i++){
					g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, S_H);
					g.drawLine(0, i*UNIT_SIZE, S_W, i*UNIT_SIZE);
				}
			}
			/*
			 * draw apple
			 */
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			/*
			 * draw snake
			 */
			for(int i=0; i<bodyParts;i++){
				if(i == 0){
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}else{
					if(rgb){
						g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					}else{
						g.setColor(new Color(45, 180, 0));
					}
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			/*
			 * current score text
			 */
			g.setColor(Color.YELLOW);
			g.setFont(new Font("LCDMono2", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			String text = "Score: "+applesEaten;
			g.drawString(text, (S_W - metrics.stringWidth(text))/2, g.getFont().getSize());
		}else{
			gameOver(g);
		}
	}
	
	public void newApple(){
		appleX = random.nextInt((int)(S_W/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(S_H/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move(){
		for(int i = bodyParts;i>0;i--){
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple(){
		if((x[0] == appleX) && (y[0] == appleY)){
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions(){
		/*
		 * head collide with body
		 */
		for(int i=bodyParts;i>0;i--){
			if((x[0] == x[i]) && (y[0] == y[i])){
				running = false;
			}
		}
		/*
		 * collide with  border
		 */
		if(x[0] < 0 || x[0] > S_W || y[0] < 0 || y[0] > S_H){
			running = false;
		}
		
		if(!running){
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g){
		/*
		 * score
		 */
		g.setColor(Color.YELLOW);
		g.setFont(new Font("LCDMono2", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		String text = "Score: "+applesEaten;
		g.drawString(text, (S_W - metrics.stringWidth(text))/2, g.getFont().getSize());
		/*
		 * game over text
		 */
		g.setColor(Color.red);
		g.setFont(new Font("LCDMono2", Font.BOLD, 75));
		metrics = getFontMetrics(g.getFont());
		text = "Game Over";
		g.drawString(text, (S_W - metrics.stringWidth(text))/2, S_H/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running){
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class KeyAdapter extends java.awt.event.KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R'){
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L'){
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D'){
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U'){
					direction = 'D';
				}
				break;
			case KeyEvent.VK_H:
				grid = !grid;
				break;
			case KeyEvent.VK_R:
				rgb = !rgb;
				break;
			}
		}
	}
	
}
