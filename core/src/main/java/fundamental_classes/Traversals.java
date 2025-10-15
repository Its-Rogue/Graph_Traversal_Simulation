package fundamental_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

public class Traversals {
    public static void bfs(Graph graph, float traversal_speed, int start_node, int end_node) {
        long operation_speed = (long) (250 / traversal_speed);

        new Thread(() -> { // New local render thread to allow for nodes to change colours
            ArrayList<Node> queue = new ArrayList<>(); // Initialise needed lists
            ArrayList<Node> discovered = new ArrayList<>();
            ArrayList<Node> visited = new ArrayList<>();

            Node start = graph.get_node_id(start_node); // Cache start and end node to update colour at the end
            Node end = graph.get_node_id(end_node);     // in the case they are overwritten

            Gdx.app.postRunnable(() -> {
                start.setColor(Color.GREEN); // Highlight the chosen start and end node
                end.setColor(Color.RED);
            });

            queue.add(start); // Add the first node to the discovered and queue
            discovered.add(start);

            boolean found = false;

            while (!queue.isEmpty() && !found) { // Loop through until found or no more nodes in graph
                Node current_node = queue.remove(0); // Remove the node at the front of the queue

                Gdx.app.postRunnable(() -> {
                    if (current_node != start && current_node != end) {
                        current_node.setColor(Color.ORANGE); // Highlight the current node if it is not start / end
                    }
                });

                try {
                    Thread.sleep(operation_speed); // Wait the set time between each step so the user can follow
                } catch (InterruptedException e) { // what is happening
                    e.printStackTrace();
                }

                for (Node neighbour : current_node.getNeighbours()) { // Check each neighbour of the current node
                    if (!discovered.contains(neighbour)) {
                        discovered.add(neighbour); // Add the neighbour to the queue if not already discovered
                        queue.add(neighbour);
                        Gdx.app.postRunnable(() -> {
                            if (neighbour != start && neighbour != end) {
                                neighbour.setColor(Color.YELLOW); // Highlight yellow to show it's been looked at
                            }                                     // but not yet been the current node
                        });
                        if (neighbour.equals(end)) {
                            found = true; // Update to true if neighbour is end, and immediately break from loop
                            break;        // To prevent unnecessary computation
                        }
                    }
                }

                if (!visited.contains(current_node)) { // If end node not found in neighbours of current node
                    visited.add(current_node); // Add to visited list
                    Gdx.app.postRunnable(() -> {
                        if (current_node != start && current_node != end) {
                            current_node.setColor(Color.PURPLE); // Highlight node purple to show no more operations
                        }                                        // will be performed with it
                    });
                }
            }

        }).start(); // Ensure the thread is switched to, instead of main render thread
    }

    public static void dfs(Graph graph, float traversal_speed, int start_node, int end_node) {

    }

    public static void dijkstra(Graph graph, float traversal_speed, int start_node, int end_node) {

    }

    public static void A_star(Graph graph, float traversal_speed, int start_node, int end_node) {

    }

    public static void minimum_spanning_tree(Graph graph, float traversal_speed, int start_node, int end_node) {

    }
}
