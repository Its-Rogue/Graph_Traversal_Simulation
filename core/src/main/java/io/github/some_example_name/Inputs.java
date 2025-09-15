package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Inputs {
    static Node first_node_selected = null;
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


    // Code for the detection of left and right-clicking with the mouse
    public static void mouse_click(Graph graph, int node_radius) {
        int menu_padding = 250; // Padding to ensure the nodes are not hidden by the menu UI
        int mouse_x = Gdx.input.getX();
        int mouse_y = Gdx.graphics.getHeight() - Gdx.input.getY(); // GetY() is based on the top left corner, thus needs offsetting

        // Left click detection
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            // Ensure the pointer is within the screen bounds
            if(mouse_x >= Gdx.graphics.getWidth() - node_radius) {
                mouse_x = Gdx.graphics.getWidth() - node_radius;
            } else if (mouse_x <= node_radius + menu_padding) {
                if (mouse_x <= menu_padding) {
                    return; // If clicked within the menu area, do not create a node
                }
                mouse_x = node_radius + menu_padding;
            }
            if (mouse_y >= Gdx.graphics.getHeight() - node_radius) {
                mouse_y = Gdx.graphics.getHeight() - node_radius;
            } else if (mouse_y <= node_radius) {
                mouse_y = node_radius;
            }

            for (Node node: graph.get_nodes()){
                float dx = mouse_x - node.getPos_x();
                float dy = mouse_y - node.getPos_y();
                float distance =  (float) Math.sqrt(dx * dx + dy * dy);
                if (distance < 4 * node_radius) {
                    return;
                }
            }

            graph.add_node(node_radius, mouse_x, mouse_y); // Add a node at the coordinates clicked at
        }

        if(Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)){
            for (Node node: graph.get_nodes()) { // Loop over each node
                if (mouse_x >= node.getPos_x() - node_radius) { // See if mouse_x is within the left bound of the current node
                    if (mouse_x <= node.getPos_x() + node_radius) { // See if mouse_x is within the right bound of the current node
                        if (mouse_y >= node.getPos_y() - node_radius) { // See if mouse_y is within the lower bound of the current node
                            if (mouse_y <= node.getPos_y() + node_radius) { // See if mouse_y is within the upper bound of the current node
                                if(first_node_selected == null){
                                    first_node_selected = node;
                                    System.out.println("first_node_selected");
                                } else if (first_node_selected != node) {
                                    graph.add_edge(first_node_selected.getId(), node.getId(), 5);
                                    first_node_selected = null;
                                    System.out.println("second_node_selected");
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Right click detection and code
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (Node node: graph.get_nodes()) { // Loop over each node
                if(mouse_x >= node.getPos_x() - node_radius) { // See if mouse_x is within the left bound of the current node
                    if(mouse_x <= node.getPos_x() + node_radius) { // See if mouse_x is within the right bound of the current node
                        if(mouse_y >= node.getPos_y() - node_radius) { // See if mouse_y is within the lower bound of the current node
                            if(mouse_y <= node.getPos_y() + node_radius) { // See if mouse_y is within the upper bound of the current node
                                graph.remove_node(node.getId()); // Remove the node based on its ID
                                break; // Only remove one node per right click
                            }
                        }
                    }
                }
            }
        }
    }
}
