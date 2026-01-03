package launcher_classes;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import essential_classes.Main;

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
        Graphics.DisplayMode primary_mode = Lwjgl3ApplicationConfiguration.getDisplayMode(); // Get primary monitor's information
        configuration.setTitle("Graph Traversal Simulation");
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate); // Lock refresh rate to that of the monitor
        configuration.useVsync(false);
        configuration.setFullscreenMode(primary_mode); // Set window to size of primary monitor, in full screen mode
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}