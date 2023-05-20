package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.InitialContext;

import util.Resource;

public class GerenciadorInimigos {
	
	private BufferedImage cactus1;
	private BufferedImage cactus2;
	private Random aleatorio;
	
	private List<Inimigos> inimigos;
	private Personagemprincipal personagemprincipal;
	
	public GerenciadorInimigos(Personagemprincipal personagemprincipal) {
		aleatorio = new Random();
		cactus1 = Resource.getResouceImage("data/cactus1.png");
		cactus2 = Resource.getResouceImage("data/cactus2.png");
		inimigos = new ArrayList<Inimigos>();
		this.personagemprincipal = personagemprincipal;
		inimigos.add(criarInimigo());
	}
	
	public void atualiza() {
		for(Inimigos e : inimigos) {
			e.atualiza();
		}
		Inimigos inimigos1 = inimigos.get(0);
		if(inimigos1.foradatela()) {
			personagemprincipal.aumentapontos();
			inimigos.clear();
			inimigos.add(criarInimigo());
		}
	}
	
	public void draw(Graphics g) {
		for(Inimigos e : inimigos) {
			e.draw(g);
		}
	}
	
	private Inimigos criarInimigo() {
		// if (enemyType = getRandom)
		int type = aleatorio.nextInt(2);
		if(type == 0) {
			return new Obstaculos(personagemprincipal, 800, cactus1.getWidth() - 10, cactus1.getHeight() - 10, cactus1);
		} else {
			return new Obstaculos(personagemprincipal, 800, cactus2.getWidth() - 10, cactus2.getHeight() - 10, cactus2);
		}
	}
	
	public boolean colidiu() {
		for(Inimigos e : inimigos) {
			if (personagemprincipal.getBound().intersects(e.getBound())) {
				return true;
			}
		}
		return false;
	}
	
	public void resetar() {
		inimigos.clear();
		inimigos.add(criarInimigo());
	}
	
}
