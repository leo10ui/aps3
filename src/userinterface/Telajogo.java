package userinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gameobject.Ceu;
import gameobject.Nuvens;
import gameobject.GerenciadorColeteveis;
import gameobject.Ceu;
import gameobject.GerenciadorInimigos;
import gameobject.Terreno;
import gameobject.Nuvens;
import gameobject.Personagemprincipal;
import util.Resource;

public class Telajogo extends JPanel implements Runnable, KeyListener {

	private static final int estado_inicioJogo = 0;
	private static final int estado_jogando = 1;
	private static final int estado_fimjogo = 2;
	
	private Ceu ceu;
	private Terreno land;
	private Personagemprincipal personagemprincipal;
	private GerenciadorInimigos gerenciadorInimigos;
	private Nuvens nuvens;
	private Thread thread;
	private GerenciadorColeteveis GerenciadorColeteveis;

	private boolean teclapressionada;

	private int estadodejogo = estado_inicioJogo;

	private BufferedImage imagemjogarnovamente;
	private BufferedImage imagemfimdejogo;

	public Telajogo() {
		ceu = new Ceu();
		personagemprincipal = new Personagemprincipal();
		land = new Terreno(Janelajogo.SCREEN_WIDTH, personagemprincipal);
		personagemprincipal.setSpeedX(4);
		imagemjogarnovamente = Resource.getResouceImage("data/restart.png");
		imagemfimdejogo = Resource.getResouceImage("data/fimdejogo.png");
		gerenciadorInimigos = new GerenciadorInimigos(personagemprincipal);
		nuvens = new Nuvens(Janelajogo.SCREEN_WIDTH, personagemprincipal);
		GerenciadorColeteveis = new GerenciadorColeteveis(personagemprincipal);
	}

	public void iniciaJogo() {
		thread = new Thread(this);
		thread.start();
	}

	public void atualizajogo() {
		if (estadodejogo == estado_jogando) {
			land.atualiza();
			nuvens.atualiza();
			personagemprincipal.atualiza();
			gerenciadorInimigos.atualiza();
			GerenciadorColeteveis.atualiza();
			if (gerenciadorInimigos.colidiu()) {
				personagemprincipal.tocasommorte();
				estadodejogo = estado_fimjogo;
				personagemprincipal.morte(true);
				
			}
			if (GerenciadorColeteveis.colidiu()) {
				personagemprincipal.tocasommoeda();
				estadodejogo = estado_jogando;
				personagemprincipal.aumentacoletaveis();
				GerenciadorColeteveis.resetar();
			}
		}
	}

	public void paint(Graphics g) {
		g.setColor(Color.decode("#f7f7f7"));
		g.fillRect(0, 0, getWidth(), getHeight());

		switch (estadodejogo) {
		case estado_inicioJogo:
            ceu.draw(g);
			land.draw(g);
			personagemprincipal.draw(g);
			break;
		case estado_jogando:
		case estado_fimjogo:
			ceu.draw(g);
			nuvens.draw(g);
			land.draw(g);
			GerenciadorColeteveis.draw(g);
			gerenciadorInimigos.draw(g);
			personagemprincipal.draw(g);
			g.setColor(Color.BLACK);
			g.drawString("OBSTACULOS PULADOS " + personagemprincipal.pontuacao, 300, 20);
			g.drawString("Coletaveis " + personagemprincipal.coletaveis, 150, 20);
			if (estadodejogo == estado_fimjogo) {
				g.drawImage(imagemfimdejogo, 200, 30, null);
				g.drawImage(imagemjogarnovamente, 283, 50, null);
				
			}
			break;
		}
	}

	@Override
	public void run() {

		int fps = 100;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long tempopassado;
		
		int microsegundo;
		int nanosegundo;

		long fimproceso;
		long lag = 0;

		while (true) {
			atualizajogo();
			repaint();
			fimproceso = System.nanoTime();
			tempopassado = (lastTime + msPerFrame - System.nanoTime());
			microsegundo = (int) (tempopassado / 1000000);
			nanosegundo = (int) (tempopassado % 1000000);
			if (microsegundo <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(microsegundo, nanosegundo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!teclapressionada) {
			teclapressionada = true;
			switch (estadodejogo) {
			case estado_inicioJogo:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					estadodejogo = estado_jogando;
				}
				break;
			case estado_jogando:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					personagemprincipal.pulo();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					personagemprincipal.abaixa(true);
				}
				break;
			case estado_fimjogo:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					estadodejogo = estado_jogando;
					resetajogo();
				}
				break;

			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		teclapressionada = false;
		if (estadodejogo == estado_jogando) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				personagemprincipal.abaixa(false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void resetajogo() {
		gerenciadorInimigos.resetar();
		personagemprincipal.morte(false);
		personagemprincipal.reseta();
		GerenciadorColeteveis.resetar();
	}

}
