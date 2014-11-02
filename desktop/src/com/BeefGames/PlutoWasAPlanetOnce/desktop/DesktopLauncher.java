package com.BeefGames.PlutoWasAPlanetOnce.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.BeefGames.PlutoWasAPlanetOnce.PlutoWasAPlanetOnce;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new PlutoWasAPlanetOnce(), config);
	}
}
