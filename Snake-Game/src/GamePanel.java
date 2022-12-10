import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{

	// CONSTANTES DE LA CLASSE
	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;
	private static final int UNIT_SIZE = 25;
	private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	private static final int DELAY = 90;
	private final int[] x = new int[GAME_UNITS];
	private final int[] y = new int[GAME_UNITS];

	// variavles de la classe
	private int bodyParts = 5;
	private int appleEaten = 0;
	private int appleX;
	private int appleY;
	private char direction = 'r';
	private boolean running = false;
	private Timer timer;
	private Random random;


	public GamePanel()
	{
		random = new Random();

		// CONFIG WINDOW
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.startGame();

	}

	public void startGame()
	{
		running = true;
		this.newApple();
		timer = new Timer(DELAY,this);
		timer.start();


	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		this.draw(g);
	}

	public void draw(Graphics g)
	{
		if (running)
		{
			/* 
			// dessine un cadrillage
			for (int i=0 ; i < SCREEN_HEIGHT / UNIT_SIZE ; i++)
			{
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);			
			}
			*/

			// dessine la pomme
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			// dessine le serpent
			for (int i = 0 ; i < bodyParts ; i++)
			{
				// tete du serpent
				if (i == 0)
				{
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} 
				else
				{
					g.setColor(new Color(25, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			// score text
			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + appleEaten, (SCREEN_HEIGHT - metrics.stringWidth("Score: " + appleEaten)) / 2, g.getFont().getSize());
		}
		else
		{
			gameOver(g);
		}
	}	

	public void move()
	{
		// Chaque partie du corp prend la place de celle qui est avant elle dans la liste
		for (int i = this.bodyParts ; i > 0 ; i--)
		{
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		// prochain mouvement de la tete ( index 0 )
		switch(direction)
		{
			case 'u':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'd':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'r':
				x[0] = x[0] + UNIT_SIZE;
				break;
			case 'l':
				x[0] = x[0] - UNIT_SIZE;
				break;
		}

	}

	public void newApple()
	{
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

	}

	public void checkApple()
	{
		for (int i = bodyParts ; i > 0 ; i--)
		{
			// la tete mange une pomme
			if (( x[0] == appleX ) && ( y[0] == appleY ))
			{
					bodyParts++;
					appleEaten++;
					newApple();
			}
			if (( x[i] == appleX ) && ( y[i] == appleY ) && ( i != 0 ))
			{
				newApple();
			}
		}
	}

	public void checkCollision()
	{
		for(int i = bodyParts ; i > 0 ; i--)
		{
			// verifie si la tete est en collision avec le corps
			if ((x[0] == x[i]) && (y[0] == y[i]))
			{
				
				
				running = false;
			}
		}


		// verifie si il y a une collision de la tete avec un mur
		if (x[0] < 0) { running = false; }
		if (x[0] > SCREEN_WIDTH) { running = false; }
		if (y[0] < 0) { running = false; }
		if (y[0] > SCREEN_HEIGHT) { running = false; }

		// arrete le timer si il y a une collision
		if (!running) {	timer.stop(); }
	}

	public void gameOver(Graphics g)
	{
		// Game over text
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_HEIGHT - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

		// score text
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics fMetrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + appleEaten, (SCREEN_HEIGHT - fMetrics.stringWidth("Score: " + appleEaten)) / 2, g.getFont().getSize());
	}

	@Override
	public void actionPerformed(ActionEvent aEvent) 
	{
		if (running)
		{
			this.move();
			this.checkApple();
			this.checkCollision();
		}
		repaint();
		
	}

	public class MyKeyAdapter extends KeyAdapter
	{

		@Override
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_Q:
					if (direction != 'r')
					{
						direction = 'l';
					}
					break;

				case KeyEvent.VK_D:
					if (direction != 'l')
					{
						direction = 'r';
					}
					break;

				case KeyEvent.VK_Z:
					if (direction != 'd')
					{
						direction = 'u';
					}
					break;

				case KeyEvent.VK_S:
					if (direction != 'u')
					{
						direction = 'd';
					}
					break;
			
			}

		}
	}
}
