package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Testing_Functions {
    public static void create(Graph graph, int node_radius) {
        // Create 2 nodes and add an edge with weight 5 between them
        graph.add_node(node_radius, 400, 400);
        graph.add_node(node_radius, 600, 600);
        graph.add_edge(0,1,5);
    }

    public static void create_button(Graph graph, int node_radius) {
        // Check if node_id exists, prevent it being overwritten if pressed multiple times
        if(graph.get_node_id(2) == null){
            // Get node radius from main, assign pos and create new node, indexed by graph class
            graph.add_node(node_radius, 800, 400);
            graph.add_node(node_radius, 1000, 1000);

            // Add edges between each node_id, and assign them a weight
            graph.add_edge(0,2,5);
            graph.add_edge(1,3,5);
            graph.add_edge(2,3,5);
            graph.add_edge(1,2,5);
        }
    }

    public static void remove_button(Graph graph){
        // Ensures that the node exists, preventing a crash
        if(graph.get_node_id(2) != null){
            // Removes the node and all the edges attached to it
            graph.remove_node(2);
        }
    }

    public static void menu_inputs(Graph graph, int node_radius) {
        // Hotkeys for activating the code for adding and removing the test nodes
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            Testing_Functions.create_button(graph, node_radius);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            Testing_Functions.remove_button(graph);
        }
    }
}
