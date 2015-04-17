package com.t3h.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.t3h.boom.Boom;
import com.t3h.boom.BoomManager;
import com.t3h.boom.Commons_Boom;
import com.t3h.bullet.Bullet;
import com.t3h.bullet.BulletManager;
import com.t3h.tank.EnemyTank;
import com.t3h.tank.EnemyTankManager;
import com.t3h.tank.PlayerTank;
import com.t3h.tank.Tank;

public class PlayPanel extends JPanel implements Runnable{
	private Image bground;
	private Graphics2D g2d;
	private int count;
	private Map map;
	private int mapNumber;
	private int tankPosition[];
	private BulletManager bulletMgr;
	private BoomManager boomMgr;
	private PlayerTank playerTank;
	private EnemyTankManager enemyTankMgr;
//	private Boom boom;
	private Thread th;
	
	public PlayPanel() {
		setBounds(0, 0, Commons.WIDTH_PANEL, Commons.HEIGHT_PANEL);
		setLayout(null);
		setBackground(Color.BLACK);
		addKeyListener(moveAdapter);
		setFocusable(true);
		addMouseListener(click);
		loadData();
		th = new Thread(this);
		th.start();
	}
	
	public void loadData(){
		setMap(5);
		Commons c = new Commons();
		bground = c.getImage("/RESOURCE/Image/bg_contai_panel.png");

		mapNumber = 3;
		setMap(mapNumber);
		setTankPosition(mapNumber);
		boomMgr = new BoomManager();
		bulletMgr = new BulletManager();
		bulletMgr.setMap(this.map);
		bulletMgr.setBoomMgr(boomMgr);
		playerTank = new PlayerTank(250, 630, 1, 1);
		playerTank.setMap(map);
		
		enemyTankMgr = new EnemyTankManager();
		enemyTankMgr.setMap(map);
		enemyTankMgr.setBoomMgr(boomMgr);
	}
	
	private void setMap(int mapNumber){
		map = new Map(mapNumber);
	}
	
	private void setTankPosition(int mapNumber){
		tankPosition = new int[4];
		switch (mapNumber) {
		case 1:
			tankPosition[0] = 30;
			tankPosition[1] = 220;
			tankPosition[2] = 380;
			tankPosition[3] = 620;
			break;
		case 2:
			tankPosition[0] = 30;
			tankPosition[1] = 235;
			tankPosition[2] = 440;
			tankPosition[3] = 620;
			break;
		case 3:
			tankPosition[0] = 30;
			tankPosition[1] = 220;
			tankPosition[2] = 320;
			tankPosition[3] = 620;
			break;
		case 4:
			tankPosition[0] = 30;
			tankPosition[1] = 220;
			tankPosition[2] = 485;
			tankPosition[3] = 620;
			break;
		case 5:
			tankPosition[0] = 180;
			tankPosition[1] = 280;
			tankPosition[2] = 380;
			tankPosition[3] = 500;
			break;
		default:
			break;
		}
	}
	
	private boolean checkWin(){
		if (enemyTankMgr.getTankDestroy() >= 10){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D)g;
		
		g2d.drawImage(bground, 0, 0, 700, 700, null);
		map.drawUnderComponent(g2d);
		bulletMgr.drawAllBullet(g2d);
		playerTank.drawTank(g2d);
		enemyTankMgr.drawAllEnemyTank(g2d);
		map.drawMap(g2d);
		boomMgr.exploredAllBoom(count, g2d);
	}
	
	private MouseAdapter click = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			JOptionPane.showMessageDialog(null, e.getX() + " " + e.getY());
		}
	};
	
	
	long lastShoot = System.currentTimeMillis();//-----------------------------
	long threshold = 300;//----------------------------------------------------
	
	private KeyAdapter moveAdapter = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			playerTank.releaseKey(e);
			if (e.getKeyCode() == KeyEvent.VK_SPACE){
				long now = System.currentTimeMillis();
				if (now - lastShoot > threshold){
					bulletMgr.addBullet(new Bullet(playerTank.getX() + 13, playerTank.getY() + 13, 1, 1, 1, playerTank.getOrient()));
					Tank.sound.playShoot();//--------------------------------------------------
					lastShoot = now;
			     }
			}
			repaint();
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			playerTank.checkImpact(enemyTankMgr);
			playerTank.pressKey(e);
			repaint();
		}
	};
	
	@Override
	public void run() {
		while (true){
			bulletMgr.moveAllBullet();
			if (enemyTankMgr.getSize() < 10 && enemyTankMgr.getTotalTank() < 20){
				EnemyTank enemyTank = new EnemyTank(tankPosition[new Random().nextInt(4)], 30, 1, 1);
				enemyTankMgr.addEnemyTank(enemyTank);
			}
//			System.out.println(enemyTankMgr.getTankDestroy());
			if (enemyTankMgr.getTankDestroy() >= 20){
				JOptionPane.showMessageDialog(null, "Win cmnr!");
				mapNumber++;
				if (mapNumber > 5){
					JOptionPane.showMessageDialog(null, "Win cmnr, bien cmm de!");
				}
				setMap(mapNumber);
				bulletMgr = new BulletManager();
				bulletMgr.setMap(this.map);
				bulletMgr.setBoomMgr(boomMgr);
				playerTank = new PlayerTank(250, 630, 1, 1);
				playerTank.setMap(map);
				enemyTankMgr = new EnemyTankManager();
				enemyTankMgr.setMap(map);
				enemyTankMgr.setBoomMgr(boomMgr);
				setTankPosition(mapNumber);
			}
			enemyTankMgr.checkImpact(playerTank);//-------------------------
			enemyTankMgr.AutoControlAllTank(count, bulletMgr);
			enemyTankMgr.checkAllEnemyTank(bulletMgr);
			playerTank.checkImpact(enemyTankMgr);//--------------------------
			//-----------------------
			playerTank.control();
			//-----------------------
			playerTank.resetImpact();
			enemyTankMgr.resetImpact();
			if (playerTank.checkPlayerTank(bulletMgr)){
				//System.out.println("da bi ban chet");
				Boom boom = new Boom(playerTank.getX() + 16, playerTank.getY() + 16, Commons_Boom.EXPLOSION_TANK_TYPE);
				boomMgr.addBoom(boom);
				Tank.sound.playExplosionTank();//--------------------------------------------------
			}
			count++;
			repaint();
			if (count > 10000) count = 0;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
