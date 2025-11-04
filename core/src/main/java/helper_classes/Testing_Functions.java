package helper_classes;

import fundamental_classes.*;

public class Testing_Functions {
    public static void create(Graph graph, int node_radius, Main main) {
        int rows = 9;
        int columns = 11;
        int spacing_x = 150;
        int spacing_y = 150;
        int start_x = 500;
        int start_y = 100;

        Graph.clear();
        main.start_node = 0;
        main.end_node = 98;
        main.valid_setup = true;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int x = start_x + c * spacing_x;
                int y = start_y + r * spacing_y;
                graph.add_node(node_radius, x, y, main);
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int id = i * columns + j;
                if (i > 0) {
                    int up_id = (i - 1) * columns + j;
                    graph.add_edge(id, up_id, 1, main);
                }
                if (i < rows - 1) {
                    int down_id = (i + 1) * columns + j;
                    graph.add_edge(id, down_id, 1, main);
                }
                if (j > 0) {
                    int left_id = i * columns + (j - 1);
                    graph.add_edge(id, left_id, 1, main);
                }
                if (j < columns - 1) {
                    int right_id = i * columns + (j + 1);
                    graph.add_edge(id, right_id, 1, main);
                }
            }
        }
    }
}