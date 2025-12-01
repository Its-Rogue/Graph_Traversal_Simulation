package helper_classes;

import fundamental_classes.Runtime_Data;
import structural_classes.Graph;
import structural_classes.Node;

public class Testing_Functions {
    public static void create(Runtime_Data data) {
        int spacing_x = 110;
        int spacing_y = 110;
        int starting_x = 300;
        int starting_y = 300;

        Graph.clear();

        // For traversal testing
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                data.getGraph().add_node(data.getNode_radius(), starting_x + (spacing_x * j), starting_y + (spacing_y * i), data);
            }
        }

        // Normal grid layout creation
        /*
        for (int i = 0; i < 10; i++) { // loop over each node and add an edge with each of its neighbours
            for (int j = 0; j < 10; j++) {
                int id = i * 10 + j;
                if (i > 0) {
                    int up_id = (i - 1) * 10 + j;
                    data.getGraph().add_edge(id, up_id, 1, data);
                }
                if (i < 10 - 1) {
                    int down_id = (i + 1) * 10 + j;
                    data.getGraph().add_edge(id, down_id, 1, data);
                }
                if (j > 0) {
                    int left_id = i * 10 + (j - 1);
                    data.getGraph().add_edge(id, left_id, 1, data);
                }
                if (j < 10 - 1) {
                    int right_id = i * 10 + (j + 1);
                    data.getGraph().add_edge(id, right_id, 1, data);
                }
            }
        }*/

        // Stress test for edge / node rendering
        // This is not intended to be used, and will be removed
        // TODO: REMOVE BEFORE FINAL
        for (Node node: data.getGraph().get_nodes()){
            for (Node node1: data.getGraph().get_nodes()){
                if(!(node.equals(node1))){
                    data.getGraph().add_edge(node.getId(), node1.getId(), 1, data);
                } // Connect each node to all other nodes
            }
        }
        // For traversal testing



        // For rendering testing
        /*
        data.getGraph().add_node(data.getNode_radius(), 800, 500, data);
        data.getGraph().add_node(data.getNode_radius(), 800, 700, data);
        data.getGraph().add_node(data.getNode_radius(), 1000, 500, data);
        data.getGraph().add_node(data.getNode_radius(), 800, 300, data);
        data.getGraph().add_node(data.getNode_radius(), 600, 500, data);

        data.getGraph().add_node(data.getNode_radius(), 600, 300, data);
        data.getGraph().add_node(data.getNode_radius(), 1000, 300, data);
        data.getGraph().add_node(data.getNode_radius(), 600, 700, data);
        data.getGraph().add_node(data.getNode_radius(), 1000, 700, data);

        data.getGraph().add_edge(0, 1, 1, data);
        data.getGraph().add_edge(0, 2, 1, data);
        data.getGraph().add_edge(0, 3, 1, data);
        data.getGraph().add_edge(0, 4, 1, data);
        data.getGraph().add_edge(0, 5, 1, data);
        data.getGraph().add_edge(0, 6, 1, data);
        data.getGraph().add_edge(0, 7, 1, data);
        data.getGraph().add_edge(0, 8, 1, data);
        */
        // For rendering testing


        data.getChange_edge_weight_popup().setVisible(false);
        data.setEdge_to_edit(null); // Override status of popup due to how the edges are created
    }
}