package io.github.some_example_name;

public class Testing_Functions {
    public static void create(Graph graph, int node_radius) {
        // Create 2 nodes and add an edge with weight 5 between them
        graph.add_node(node_radius, 400, 400);
        graph.add_node(node_radius, 600, 400);
        graph.add_edge(0,1,5);
    }
}
