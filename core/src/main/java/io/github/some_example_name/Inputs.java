package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

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
    }


    // Code for the detection of left and right-clicking with the mouse
    public static void mouse_click(Graph graph, int node_radius) {
        int menu_padding = 250; // Padding to ensure the nodes are not hidden by the menu UI
        int mouse_x = Gdx.input.getX();
        int mouse_y = Gdx.graphics.getHeight() - Gdx.input.getY(); // GetY() is based on the top left corner, thus needs offsetting

        // Left click detection and code
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

            // Ensure the pointer is within the screen bounds
            if(mouse_x >= Gdx.graphics.getWidth() - node_radius) {
                mouse_x = Gdx.graphics.getWidth() - node_radius;
            } else if (mouse_x <= node_radius + menu_padding) {
                if (mouse_x <= menu_padding) {
                    return; // If clicked within the menu area, do not create a node
                }
                mouse_x = node_radius + menu_padding + 10;
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
                    return; // Do not allow the new node to be placed too close to an already existing node
                }
            }

            graph.add_node(node_radius, mouse_x, mouse_y); // Add a new node at the coordinates clicked at if in a valid location
        }

        // Right click detection and code
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            for (Node node: graph.get_nodes()) { // Loop over each node
                if (mouse_x >= node.getPos_x() - node_radius) { // See if mouse_x is within the left bound of the current node
                    if (mouse_x <= node.getPos_x() + node_radius) { // See if mouse_x is within the right bound of the current node
                        if (mouse_y >= node.getPos_y() - node_radius) { // See if mouse_y is within the lower bound of the current node
                            if (mouse_y <= node.getPos_y() + node_radius) { // See if mouse_y is within the upper bound of the current node
                                if(first_node_selected == null){ // Checks if a node has already been selected
                                    first_node_selected = node; // Assigns the clicked node to the first one selected
                                    node.setColor(Color.GREEN);
                                } else if (first_node_selected != node) {
                                    graph.add_edge(first_node_selected.getId(), node.getId(), 5); // Creates the edge between the nodes
                                    first_node_selected.setColor(Color.WHITE);
                                    first_node_selected = null; // Clears the first node to be used again
                                }
                                return; // Return so the loop is not run all the way through if the desired node is already found
                            }
                        }
                    }
                }
            }
            if(first_node_selected != null){
                first_node_selected.setColor(Color.WHITE);
                first_node_selected = null; // Set to null if not right-clicked on a node
            }
        }

        // Middle click / Space key (for when the mouse does not have a middle click, such as on a laptop touchpad) detection and code
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            for (Node node: graph.get_nodes()) { // Loop over each node
                if(mouse_x >= node.getPos_x() - node_radius) { // See if mouse_x is within the left bound of the current node
                    if(mouse_x <= node.getPos_x() + node_radius) { // See if mouse_x is within the right bound of the current node
                        if(mouse_y >= node.getPos_y() - node_radius) { // See if mouse_y is within the lower bound of the current node
                            if(mouse_y <= node.getPos_y() + node_radius) { // See if mouse_y is within the upper bound of the current node
                                if(first_node_selected != null) {
                                    if(first_node_selected != node) {
                                        graph.remove_edge(first_node_selected.getId(), node.getId());
                                    }
                                    first_node_selected.setColor(Color.WHITE);
                                    if(first_node_selected == node) {
                                        graph.remove_node(node.getId());
                                        first_node_selected = null;
                                    }
                                    return;
                                } else {
                                    graph.remove_node(node.getId());
                                    first_node_selected = null;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            if(first_node_selected != null){
                first_node_selected.setColor(Color.WHITE);
                first_node_selected = null;
            }
        }
    }

    // TODO: Actually implement
    public static void start_traversal(String selected_traversal, float traversal_speed) {
        switch (selected_traversal) {
            case "Breadth-First Search":
                System.out.println("Breadth-First Search");
                break;
            case "Depth-First Search":
                System.out.println("Depth-First Search");
                break;
            case "Dijkstra's":
                System.out.println("Dijkstra's");
                break;
            case "A*":
                break;
            case "Minimum Spanning Tree":
                System.out.println("Minimum Spanning Tree");
                break;

        }
    }
}