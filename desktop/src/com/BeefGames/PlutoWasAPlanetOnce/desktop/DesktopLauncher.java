package com.BeefGames.PlutoWasAPlanetOnce.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pluto Was A Planet ONce";
	    config.width = 1366;
	    config.height = 768;
		new LwjglApplication(new PlutoWasAPlanetOnce(), config);
	}
}
