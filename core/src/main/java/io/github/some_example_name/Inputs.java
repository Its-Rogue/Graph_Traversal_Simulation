package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Inputs {
    static Node first_node_selected = null;
    // Simple method for all variants of input
    public static void all(Graph graph, int node_radius) {
        menu();
        mouse_click(graph, node_radius);
    }

    // Code specifically related to menu hotkeys
    public static void menu() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit(); // Exit the gdx app
            System.exit(0); // Exit the java program
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
                mouse_y = Gdx.graphics.getHeight() - node_radius; // Place node fully on screen if clicked in a position where it would be obscured
            } else if (mouse_y <= node_radius) {
                mouse_y = node_radius; // Place node fully on screen if clicked in a position where it would be obscured
            }

            for (Node node: graph.get_nodes()){
                if (node.getPosition().x > mouse_x + 100 || node.getPosition().x < mouse_x - 100) continue;
                if (node.getPosition().y > mouse_y + 100 || node.getPosition().y < mouse_y - 100) continue; // Skip checking the node if it is too far away from the clicked position
                float dx = mouse_x - node.getPosition().x;
                float dy = mouse_y - node.getPosition().y;
                float distance =  (float) Math.sqrt(dx * dx + dy * dy);
                if (distance < 4 * node_radius) {
                    return; // Do not allow the new node to be placed too close to an already existing node
                }
            }

            graph.add_node(node_radius, mouse_x, mouse_y); // Add a new node at the coordinates clicked at if in a valid location
        }

        // Right click detection and code for edge creation
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)){
            for (Node node: graph.get_nodes()) { // Loop over each node
                if (node.getPosition().x > mouse_x + 100 || node.getPosition().x < mouse_x - 100) continue;
                if (node.getPosition().y > mouse_y + 100 || node.getPosition().y < mouse_y - 100) continue;// Skip checking the node if it is too far away from the clicked position
                if (mouse_x >= node.getPosition().x - node_radius) { // See if mouse_x is within the left bound of the current node
                    if (mouse_x <= node.getPosition().x + node_radius) { // See if mouse_x is within the right bound of the current node
                        if (mouse_y >= node.getPosition().y - node_radius) { // See if mouse_y is within the lower bound of the current node
                            if (mouse_y <= node.getPosition().y + node_radius) { // See if mouse_y is within the upper bound of the current node
                                if(first_node_selected == null){ // Checks if a node has already been selected
                                    first_node_selected = node; // Assigns the clicked node to the first one selected
                                    node.setColor(Color.GREEN);
                                } else if (first_node_selected != node) {
                                    graph.add_edge(first_node_selected.getId(), node.getId(), 1); // Creates the edge between the nodes
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

        // Middle click / Backspace (for when the user does not have MMB, such as on a laptop touchpad) detection and code for node and edge deletion
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            for (Node node: graph.get_nodes()) { // Loop over each node
                if (node.getPosition().x > mouse_x + 100 || node.getPosition().x < mouse_x - 100) continue;
                if (node.getPosition().y > mouse_y + 100 || node.getPosition().y < mouse_y - 100) continue;// Skip checking the node if it is too far away from the clicked position
                if(mouse_x >= node.getPosition().x - node_radius) { // See if mouse_x is within the left bound of the current node
                    if(mouse_x <= node.getPosition().x + node_radius) { // See if mouse_x is within the right bound of the current node
                        if(mouse_y >= node.getPosition().y - node_radius) { // See if mouse_y is within the lower bound of the current node
                            if(mouse_y <= node.getPosition().y + node_radius) { // See if mouse_y is within the upper bound of the current node
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
            if(first_node_selected != null){ // Reset selected node if node selected then user clicks again not on a different node
                first_node_selected.setColor(Color.WHITE);
                first_node_selected = null;
            }
        }
    }

    // Switch case the chosen traversal option
    public static void start_traversal(Graph graph, String selected_traversal, float traversal_speed, int start_node, int end_node) {
        switch (selected_traversal) {
            case "Breadth-First Search":
                bfs(graph, traversal_speed, start_node, end_node);
                break;
            case "Depth-First Search":
                dfs(graph, traversal_speed, start_node, end_node);
                break;
            case "Dijkstra's":
                dijkstra(graph, traversal_speed, start_node, end_node);
                break;
            case "A*":
                A_star(graph, traversal_speed, start_node, end_node);
                break;
            case "Minimum Spanning Tree":
                minimum_spanning_tree(graph, traversal_speed, start_node, end_node);
                break;
        }
    }

    public static void bfs(Graph graph, float traversal_speed, int start_node, int end_node) {
        System.out.println("Breadth-First Search");
        long operation_speed = (long) (500/traversal_speed);

        new Thread(() -> {
            ArrayList<Node> queue = new ArrayList<>();
            queue.add(graph.get_node_id(start_node));
            ArrayList<Node> discovered = new ArrayList<>();
            discovered.add(graph.get_node_id(start_node));
            ArrayList<Node> neighbours = new ArrayList<>();
            ArrayList<Node> visited = new ArrayList<>();
            boolean found = false;

            while (!queue.isEmpty() && !found){
                Node current_node = queue.get(0);
                queue.remove(current_node);
                if (!visited.contains(current_node)) {
                    visited.add(current_node);
                    Gdx.app.postRunnable(() -> {current_node.setColor(Color.ORANGE);});
                }
                neighbours.addAll(current_node.getNeighbours());
                for (Node node: neighbours) {
                    if(!discovered.contains(node.getId())){
                        discovered.add(node);
                        queue.add(node);
                        if(node.equals(graph.get_node_id(end_node))){
                            Gdx.app.postRunnable(() -> {node.setColor(Color.PURPLE);});
                            found = true;
                            break;
                        } else {
                            Gdx.app.postRunnable(() -> {node.setColor(Color.YELLOW);});
                        }
                    }
                }
                Gdx.app.postRunnable(() -> {current_node.setColor(Color.ORANGE);});
            }
        }).start();
    }

    public static void dfs(Graph graph, float traversal_speed, int start_node, int end_node) {
        System.out.println("Depth-First Search");
    }

    public static void dijkstra(Graph graph, float traversal_speed, int start_node, int end_node) {
        System.out.println("Dijkstra's");
    }

    public static void A_star(Graph graph, float traversal_speed, int start_node, int end_node) {
        System.out.println("A*");
    }

    public static void minimum_spanning_tree(Graph graph, float traversal_speed, int start_node, int end_node) {
        System.out.println("Minimum Spanning Tree");
    }

    public static void sleep(long wait_time){
        try {
            Thread.sleep(wait_time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}