package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Inputs {
    // Simple loop for all variants of input
    public static void all(Graph graph, int node_radius) {
        menu(graph, node_radius);
    }

    // Code specifically related to menu hotkeys
    public static void menu(Graph graph, int node_radius) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
            System.exit(0);
        }

        // TEST HOTKEYS
        testing_functions.menu_inputs(graph, node_radius);
    }
}
