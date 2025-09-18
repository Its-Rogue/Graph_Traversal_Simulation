package io.github.some_example_name;

public class Testing_Functions {
    public static void create(Graph graph, int node_radius) {

        for(Node node: graph.get_nodes()){
            if(node.getPos_x() >= 600 && node.getPos_x() <= 1000){
                if(node.getPos_y() >= 600 && node.getPos_y() <= 1000){
                    return; // Returns if a node is detected within the region the testing elements are to be drawn in
                }
            }
        }

        // Create 9 nodes in a grid and connect them to a central one
        graph.add_node(node_radius, 800, 800); // Centre
        graph.add_node(node_radius, 800, 1000); // 0
        graph.add_node(node_radius, 1000, 1000); // 45
        graph.add_node(node_radius, 1000, 800); // 90
        graph.add_node(node_radius, 1000, 600); // 135
        graph.add_node(node_radius, 800, 600); // 180
        graph.add_node(node_radius, 600, 600); // 225
        graph.add_node(node_radius, 600, 800); // 270
        graph.add_node(node_radius, 600, 1000); // 315
        graph.add_edge(0,1,5);
        graph.add_edge(0,2,5);
        graph.add_edge(0,3,5);
        graph.add_edge(0,4,5);
        graph.add_edge(0,5,5);
        graph.add_edge(0,6,5);
        graph.add_edge(0,7,5);
        graph.add_edge(0,8,5);

    }
}
