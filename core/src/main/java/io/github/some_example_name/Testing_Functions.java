package io.github.some_example_name;

import java.util.Random;

public class Testing_Functions {
    public static void create(Graph graph, int node_radius) {
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int x = 250 + rand.nextInt(2560-251) + 50;
            int y = 50 + rand.nextInt(1440-100);
            graph.add_node(node_radius, x, y);
        }

        for (int i = 0; i < 10; i++) {
            int edges = 1 + rand.nextInt(2);
            for (int j = 0; j < edges; j++) {
                int target = rand.nextInt(10);
                if (target != i) {
                    graph.add_edge(i, target, 1);
                }
            }
        }
    }
}
