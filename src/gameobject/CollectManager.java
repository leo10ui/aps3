package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Resource;

public class CollectManager {
	
	private BufferedImage coin1;
	private BufferedImage coin2;
	private Random rand;
	
	private List<Enemy> collects;
	private MainCharacter mainCharacter;
	
	public CollectManager(MainCharacter mainCharacter) {
		rand = new Random();
		coin1 = Resource.getResouceImage("data/coin1.png");
		coin2 = Resource.getResouceImage("data/coin1.png");
		collects = new ArrayList<Enemy>();
		this.mainCharacter = mainCharacter;
		collects.add(createCollect());
	}
	
	public void update() {
		for(Enemy e : collects) {
			e.update();
		}
		Enemy enemy = collects.get(0);
		if(enemy.isOutOfScreen()) {
			mainCharacter.upScore();
			collects.clear();
			collects.add(createCollect());
		}
	}
	
	public void draw(Graphics g) {
		for(Enemy e : collects) {
			e.draw(g);
		}
	}
	
	private Enemy createCollect() {
		// if (enemyType = getRandom)
		int type = rand.nextInt(2);
		if(type == 0) {
			return new Cactus(mainCharacter, 900, coin1.getWidth() - 10, coin1.getHeight() - 10, coin1);
		} else {
			return new Cactus(mainCharacter, 600, coin2.getWidth() - 10, coin2.getHeight() - 10, coin2);
		}
	}
	
	public boolean isCollision() {
		for(Enemy e : collects) {
			if (mainCharacter.getBound().intersects(e.getBound())) {
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		collects.clear();
		collects.add(createCollect());
	}
	
}
