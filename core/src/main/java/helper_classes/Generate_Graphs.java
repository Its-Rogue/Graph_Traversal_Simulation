package helper_classes;

import fundamental_classes.Runtime_Data;
import structural_classes.Graph;

import java.awt.*;

public class Generate_Graphs {
    public static void generate_grid(Runtime_Data data) {
        int spacing_x = 110;
        int spacing_y = 110;
        int starting_x = 900;
        int starting_y = 300;

        Graph.clear();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                data.getGraph().add_node(data.getNode_radius(), starting_x + (spacing_x * j), starting_y + (spacing_y * i), data);
            }
        }

        // Normal grid layout creation
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
        }

        data.getChange_edge_weight_popup().setVisible(false);
        data.setEdge_to_edit(null); // Override status of popup due to how the edges are created
    }

    public static void generate_random_graph(Runtime_Data data) {
        Graph.clear();
        data.getGraph().add_node(data.getNode_radius(), 400, 400, data);
        // TODO: Finish this function, random gen a graph for a travelling salesman like scenario
    }
}
