package helper_classes;

import structural_classes.Graph;
import fundamental_classes.*;

public class Testing_Functions {
    public static void create(Runtime_Data data) {
        int rows = 9; // Values to create a 9*11 grid of 99 nodes
        int columns = 11;
        int spacing_x = 150;
        int spacing_y = 150;
        int start_x = 500;
        int start_y = 100;

        Graph.clear(); // Reset the graph to ensure nodes can be placed
        data.setStart_node(0); // Auto set start and end node, these can still be chosen after but easier for iterative testing
        data.setEnd_node(98);
        data.setValid_setup(true); // Ensure that the traversals can be run

        for (int r = 0; r < rows; r++) { // Actually create the nodes
            for (int c = 0; c < columns; c++) {
                int x = start_x + c * spacing_x;
                int y = start_y + r * spacing_y;
                data.getGraph().add_node(data.getNode_radius(), x, y, data);
            }
        }

        for (int i = 0; i < rows; i++) { // Add edge between 2-4 nodes adjacent to each node
            for (int j = 0; j < columns; j++) {
                int id = i * columns + j;
                if (i > 0) {
                    int up_id = (i - 1) * columns + j;
                    data.getGraph().add_edge(id, up_id, 1, data);
                }
                if (i < rows - 1) {
                    int down_id = (i + 1) * columns + j;
                    data.getGraph().add_edge(id, down_id, 1, data);
                }
                if (j > 0) {
                    int left_id = i * columns + (j - 1);
                    data.getGraph().add_edge(id, left_id, 1, data);
                }
                if (j < columns - 1) {
                    int right_id = i * columns + (j + 1);
                    data.getGraph().add_edge(id, right_id, 1, data);
                }
            }
        }
    }
}