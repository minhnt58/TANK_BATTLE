package com.t3h.boom;

import java.awt.Graphics2D;

public class Boom {
	private int x, y;
	private int type;
	public CommonsBoom common_boom = new CommonsBoom();
	public int time;

	public Boom(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public void explored(int count, Graphics2D g2d){
		if (type == CommonsBoom.EXPLOSION_TANK_TYPE){        
			switch (count % 5) {
				case 0:
					g2d.drawImage(CommonsBoom.TANK_EXPLOSION_1, x - 10, y - 9, null);
					time+=3;
					break;
				case 1:
					g2d.drawImage(CommonsBoom.TANK_EXPLOSION_2, x - 13, y - 11, null);
					time+=3;
					break;
				case 2:
					g2d.drawImage(CommonsBoom.TANK_EXPLOSION_3, x - 14, y - 13, null);
					time+=3;
					break;
				case 3:
					g2d.drawImage(CommonsBoom.TANK_EXPLOSION_4, x - 28, y - 23, null);
					time+=3;
					break;
				case 4:
					g2d.drawImage(CommonsBoom.TANK_EXPLOSION_5, x - 28, y - 25, null);
					time+=3;
				default:
					break;
			}
		}
		if (type == CommonsBoom.EXPLOSION_BULLET_TYPE){
			switch (count % 5) {
				case 2:
					g2d.drawImage(CommonsBoom.BULLET_EXPLOSION, x - 38/8, y - 38/8, 38/4, 38/4, null);
					time+=5;
					break;
				case 3:
					g2d.drawImage(CommonsBoom.BULLET_EXPLOSION, x - 38/6, y - 38/6, 38/3, 38/3, null);
					time+=5;
					break;
				case 4:
					g2d.drawImage(CommonsBoom.BULLET_EXPLOSION, x - 19/2, y - 19/2, 38/2, 38/2, null);
					time+=5;
					break;
				default:
					break;
			}
		}
	}
}

