package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.some_example_name.Main;

public class GTSLauncher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static void createApplication() {
        new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        Graphics.DisplayMode primary_mode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        configuration.setTitle("Graph Traversal Simulation");
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate);
        configuration.useVsync(false);
        configuration.setWindowedMode(2560, 1440);
        configuration.setFullscreenMode(primary_mode);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}
