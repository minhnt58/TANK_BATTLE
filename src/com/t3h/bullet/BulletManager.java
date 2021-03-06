package com.t3h.bullet;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.t3h.boom.Boom;
import com.t3h.boom.BoomManager;
import com.t3h.boom.CommonsBoom;
import com.t3h.graphics.Commons;
import com.t3h.graphics.Map;
import com.t3h.graphics.Sound;

public class BulletManager {
	private ArrayList<Bullet> bulletMgr;
	public static Sound sound = new Sound();
	private Map map;
	private BoomManager boomMgr;
	
	public BulletManager() {
		bulletMgr = new ArrayList<>();
	}
	
	public void setMap(Map map){
		this.map = map;
	}
	
	public void addBullet(Bullet bullet){
		bulletMgr.add(bullet);
	}
	
	public void moveAllBullet(){
		for (int i = 0; i < bulletMgr.size(); i++) {
			bulletMgr.get(i).move();
		}
	}
	
	public void drawAllBullet(Graphics2D g2d){
		for (int i = 0; i < bulletMgr.size(); i++) {
			bulletMgr.get(i).drawBullet(g2d);
			
			if (checkBullet(bulletMgr.get(i))){
				removeBullet(i);
			}
		}
	}
	
	// kiem tra dan da bi no chua
	private boolean checkBullet(Bullet bullet){
		// Co the bullet ra ngoai khoang xac dinh ma van chua kip kiem tra, nen can co 2 cau lenh if 
		if (bullet.getX()<0 || bullet.getX()>690){
			return true;
		}
		if (bullet.getY()<0 || bullet.getY()>690){
			return true;
		}
		switch (map.getType(bullet.getX(), bullet.getY())) {
			case Commons.NONE:
			case Commons.TREE:
			case Commons.WATER:		return false;	// Neu khong co vat can, cay, nuoc: cho bullet di qua
			case Commons.RIM:
			case Commons.STONE:		break;			// Neu gap da, tuong bao: khong cho di qua
			case Commons.BRICK1:{					// Neu gap gach: khong cho di qua
				map.setMatrix(bullet.getX(), bullet.getY(), Commons.BRICK2);// Chuyen thanh gach bi ban
				break;
			}
			case Commons.BRICK2:{					// Neu gap gach bi ban: Khong cho di qua
				map.setMatrix(bullet.getX(), bullet.getY(), Commons.NONE);// Lam bien mat gach
				break;
			}
			default:{
//				map.setMatrix(bullet.getX(), bullet.getY(), Commons.NONE);
				break;
			}
		}
		return true;
	}
	
	public void setBoomMgr(BoomManager boomMgr) {
		this.boomMgr = boomMgr;
	}
	
	public void removeBullet(int i){
		// Neu bullet va vao vat can, tank: Khoi tao boom
		Boom boom = new Boom(bulletMgr.get(i).getX(), bulletMgr.get(i).getY(), CommonsBoom.EXPLOSION_BULLET_TYPE);
		boomMgr.addBoom(boom);
		bulletMgr.remove(i);
		sound.playExplosion();
	}
	
	public Bullet getBullet(int i){
		return bulletMgr.get(i);
	}
	
	public int getSize(){
		return bulletMgr.size();
	}
}
