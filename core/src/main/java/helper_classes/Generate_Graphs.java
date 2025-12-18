package helper_classes;

import fundamental_classes.Runtime_Data;
import structural_classes.Graph;
import structural_classes.Region;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        // TODO: Finish this function, random gen a graph for a travelling salesman like scenario
        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                regions.add(new Region(270 + data.getNode_radius() + 115 * j, 270 + data.getNode_radius() + 115 + 115 * j, data.getNode_radius() + 50 + 320 * i, data.getNode_radius() + 122 + 320 * i));
            }
        }

        for (Region r: regions) {
            if (ThreadLocalRandom.current().nextInt(0,100) < 20 && data.getGraph().get_total_edges() < 100) {
                int rand_x = ThreadLocalRandom.current().nextInt(r.getX1(), r.getX2());
                int rand_y = ThreadLocalRandom.current().nextInt(r.getY1(), r.getY2());

                data.getGraph().add_node(data.getNode_radius(), rand_x, rand_y, data);
            }
        }
    }
}
