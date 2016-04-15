package org.atrolla.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.atrolla.games.desktop.game.Mmmmmma;

public class DesktopLauncher {

	public static final String GAME_TITLE = "MMMMMMA For Arolla - Have Fun";

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GAME_TITLE;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Mmmmmma(), config);
	}
}
