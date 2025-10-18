package fundamental_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public class Traversals {
    static long operation_speed_base = 250;

    public static void bfs(Graph graph, float traversal_speed, int start_node, int end_node, Main main) {
        main.traversal_in_progress = true;
        long operation_speed = (long) (operation_speed_base / traversal_speed);

        Node start = graph.get_node_id(start_node); // Cache start and end node to update colour at the end
        Node end = graph.get_node_id(end_node);     // in the case they are overwritten

        ArrayList<Node> queue = new ArrayList<>(); // Initialise needed lists
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();
        final boolean[] found = {false};

        new Thread(() -> { // New local render thread to allow for nodes to change colours

            Gdx.app.postRunnable(() -> {
                start.setColor(Color.GREEN); // Highlight the chosen start and end node
                end.setColor(Color.RED);
            });

            queue.add(start); // Add the first node to the discovered and queue
            discovered.add(start);

            while (!queue.isEmpty() && !found[0] && !main.traversal_cancelled) { // Loop through until found or no more nodes in graph
                Node current_node = queue.remove(0); // Remove the node at the front of the queue

                Gdx.app.postRunnable(() -> {
                    if (current_node != start && current_node != end) {
                        current_node.setColor(Color.ORANGE); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour : current_node.getNeighbours()) { // Check each neighbour of the current node
                    if (main.traversal_cancelled) {
                        return;
                    }

                    sleep(operation_speed);

                    if (!discovered.contains(neighbour)) {
                        discovered.add(neighbour); // Add the neighbour to the queue if not already discovered
                        queue.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(graph, current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end) {
                                neighbour.setColor(Color.YELLOW);
                            }
                        });

                        if (neighbour.equals(end)) {
                            Gdx.app.postRunnable(() -> {
                                highlight_edge(graph, current_node, neighbour, Color.RED);
                            });
                            found[0] = true; // Update to true if neighbour is the desired end node, and immediately break from loop
                            main.traversal_in_progress = false;                             // To prevent unnecessary computation
                            break;
                        }
                    }
                }

                if (!visited.contains(current_node)) { // If end node not found in neighbours of current node
                    visited.add(current_node); // Add to visited list
                    Gdx.app.postRunnable(() -> {
                        if (current_node != start && current_node != end) {
                            current_node.setColor(Color.PURPLE); // Highlight node purple to show no more operations
                        }                                        // will be performed with it
                        for (Node node : current_node.getNeighbours()) {
                            if (visited.contains(node) && discovered.contains(node)) {
                                highlight_edge(graph, current_node, node, Color.PURPLE);
                            }
                        }
                    });
                }
            }

        }).start(); // Ensure the thread is switched to, instead of main render thread
    }

    public static void dfs(Graph graph, float traversal_speed, int start_node, int end_node, Main main) {
        main.traversal_in_progress = true;
        long operation_speed = (long) (operation_speed_base / traversal_speed);

        Node start = graph.get_node_id(start_node); // Cache start and end node to update colour at the end
        Node end = graph.get_node_id(end_node);     // in the case they are overwritten

        ArrayList<Node> stack = new ArrayList<>();
        ArrayList<Node> discovered = new ArrayList<>();
        final boolean[] found = {false};

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColor(Color.GREEN); // Highlight the chosen start and end node
                end.setColor(Color.RED);
            });

            stack.add(start);
            discovered.add(start);

            while (!stack.isEmpty() && !found[0] && !main.traversal_cancelled) {
                Node current_node = stack.remove(stack.size() - 1);
                ArrayList<Node> neighbours = new ArrayList<>(current_node.getNeighbours());

                Gdx.app.postRunnable(() -> {
                    if (current_node != start && current_node != end) {
                        current_node.setColor(Color.ORANGE); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour : neighbours) {
                    if (main.traversal_cancelled) {
                        return;
                    }

                    sleep(operation_speed);

                    if (!discovered.contains(neighbour)) {
                        stack.add(neighbour);
                        discovered.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(graph, current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end) {
                                neighbour.setColor(Color.YELLOW);
                            }
                        });

                        if (neighbour.equals(end)){
                            Gdx.app.postRunnable(() -> {
                                highlight_edge(graph, current_node, neighbour, Color.RED);
                            });
                            found[0] = true;
                            break;
                        }
                    }
                }

                Gdx.app.postRunnable(() -> {
                    if (current_node != end && !main.traversal_cancelled) {
                        if (current_node != start){
                            current_node.setColor(Color.PURPLE);
                        }
                        for (Node neighbour : current_node.getNeighbours()) {
                            if (neighbour.getColor().equals(Color.PURPLE) || neighbour.equals(start)) {
                                highlight_edge(graph, current_node, neighbour, Color.PURPLE);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    public static void dijkstra(Graph graph, float traversal_speed, int start_node, int end_node, Main main) {

    }

    public static void A_star(Graph graph, float traversal_speed, int start_node, int end_node, Main main) {

    }

    public static void minimum_spanning_tree(Graph graph, float traversal_speed, int start_node, int end_node, Main main) {

    }

    public static void highlight_edge(Graph graph, Node node, Node neighbour, Color colour) {
        List<Edge> edge = graph.get_edges(node);
        for (Edge e: edge) {
            if (e.getTarget().equals(neighbour)) {
                e.setColour(colour);
                break;
            }
        }
        edge = graph.get_edges(neighbour);
        for (Edge e: edge) {
            if (e.getTarget().equals(node)) {
                e.setColour(colour);
                break;
            }
        }
    }

    private static void sleep(long operation_speed){
        try {
            Thread.sleep(operation_speed); // Wait the set time between each step so the user can follow
        } catch (InterruptedException e) { // what is happening
            e.printStackTrace();
        }
    }
}
