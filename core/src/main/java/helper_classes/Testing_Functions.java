package helper_classes;

import fundamental_classes.*;

public class Testing_Functions {
    public static void create(Graph graph, int node_radius, Main main) {
        int rows = 5;
        int cols = 7;
        int spacing_x = 150;
        int spacing_y = 150;
        int start_x = 500;
        int start_y = 500;

        Graph.clear();
        main.start_node = 0;
        main.end_node = 34;
        main.valid_setup = true;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = start_x + c * spacing_x;
                int y = start_y + r * spacing_y;
                graph.add_node(node_radius, x, y);
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int id = r * cols + c;
                if (r > 0) {
                    int up_id = (r - 1) * cols + c;
                    graph.add_edge(id, up_id, 1);
                }
                if (r < rows - 1) {
                    int down_id = (r + 1) * cols + c;
                    graph.add_edge(id, down_id, 1);
                }
                if (c > 0) {
                    int left_id = r * cols + (c - 1);
                    graph.add_edge(id, left_id, 1);
                }
                if (c < cols - 1) {
                    int right_id = r * cols + (c + 1);
                    graph.add_edge(id, right_id, 1);
                }
            }
        }
    }
}