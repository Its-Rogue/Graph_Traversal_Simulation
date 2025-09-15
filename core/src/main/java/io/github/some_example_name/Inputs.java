package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Inputs {
    // Simple loop for all variants of input
    public static void all(Graph graph, int node_radius) {
        menu(graph, node_radius);
        mouse_click(graph, node_radius);
    }

    // Code specifically related to menu hotkeys
    public static void menu(Graph graph, int node_radius) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
            System.exit(0);
        }

        // TEST HOTKEYS
        Testing_Functions.menu_inputs(graph, node_radius);
    }

    public static void mouse_click(Graph graph, int node_radius) {
        int menu_padding = 250;
        int mouse_x = Gdx.input.getX();
        int mouse_y = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {


            if(mouse_x >= Gdx.graphics.getWidth() - node_radius) {
                mouse_x = Gdx.graphics.getWidth() - node_radius;
            } else if (mouse_x <= node_radius + menu_padding) {
                if (mouse_x <= menu_padding) {
                    return;
                }
                mouse_x = node_radius + menu_padding;
            }

            if (mouse_y >= Gdx.graphics.getHeight() - node_radius) {
                mouse_y = Gdx.graphics.getHeight() - node_radius;
            } else if (mouse_y <= node_radius) {
                mouse_y = node_radius;
            }


            graph.add_node(node_radius, mouse_x, mouse_y);
        }

        if  (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (int i = 0; i < graph.get_nodes().size() - 1; i++) {
                if(mouse_x >= graph.get_node_id(i).getPos_x() - node_radius) {
                    if (mouse_x <= graph.get_node_id(i).getPos_x() + node_radius) {
                        if(mouse_y >= graph.get_node_id(i).getPos_y() - node_radius) {
                            if(mouse_y <= graph.get_node_id(i).getPos_y() + node_radius) {
                                graph.remove_node(i);
                            }
                        }
                    }
                }
            }
        }
        //Exception in thread "main" java.lang.NullPointerException: Cannot invoke
        // "io.github.some_example_name.Node.getPos_x()" because the return value of
        // "io.github.some_example_name.Graph.get_node_id(int)" is null
    }
}
