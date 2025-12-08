package fundamental_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import structural_classes.Edge;
import structural_classes.Graph;
import structural_classes.Node;
import java.util.ArrayList;
import java.util.List;

public class Traversals {
    private final static long operation_speed_base = 200;

    public static void dfs(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        long operation_speed = (long) (operation_speed_base / data.getTraversal_speed());

        Node start = data.getGraph().get_node_id(data.getStart_node()); // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_id(data.getEnd_node());     // in the case they are overwritten

        ArrayList<Node> stack = new ArrayList<>();
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>(); // Specifically for traversal colour updating, not needed for algorithm
        final boolean[] found = {false};

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            stack.add(start);
            discovered.add(start);

            if (data.isTraversal_canceled()) {
                return;
            }

            while (!stack.isEmpty() && !found[0] && !data.isTraversal_canceled()) {
                Node current_node = stack.remove(stack.size() - 1);
                ArrayList<Node> neighbours = new ArrayList<>(current_node.getNeighbours());

                Gdx.app.postRunnable(() -> {
                    if (current_node != start && current_node != end) {
                        current_node.setColour(Color.CYAN); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour: neighbours) {
                    if (data.isTraversal_canceled()) {
                        return; // Stop traversal if the user has pressed the reset traversal button
                    }

                    sleep(operation_speed);

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        stack.add(neighbour); // Add neighbour to stack if not already discovered
                        discovered.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data.getGraph(), current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                            }
                        });

                        if (neighbour.equals(end)){                                               // Highlight edge between current node and end
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), current_node, neighbour, Color.RED)); // To show the path
                            found[0] = true;
                            data.setTraversal_in_progress(false);
                            return;
                        }
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()) {
                    visited.add(current_node); // Help track whether all the neighbours to a node have been visited
                }

                element_highlight(data, visited, discovered, current_node, start, end);
            }
        }).start();
        data.setTraversal_in_progress(false);  // Allow a new traversal to be run after this one has completed
        data.setTraversal_canceled(false);
    }

    public static void bfs(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        long operation_speed = (long) (operation_speed_base / data.getTraversal_speed());

        Node start = data.getGraph().get_node_id(data.getStart_node());   // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_id(data.getEnd_node());       // in the case they are overwritten

        ArrayList<Node> queue = new ArrayList<>(); // Initialise the various lists
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();
        final boolean[] found = {false};

        new Thread(() -> { // New local render thread to allow for nodes to change colours

            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            queue.add(start); // Add the first node to the discovered and queue
            discovered.add(start);

            while (!queue.isEmpty() && !found[0] && !data.isTraversal_canceled()) { // Loop through until found or no more nodes in graph
                Node current_node = queue.remove(0); // Remove the node at the front of the queue

                if (data.isTraversal_canceled()) {
                    return;
                }

                Gdx.app.postRunnable(() -> {
                    if (current_node != start && current_node != end) {
                        current_node.setColour(Color.CYAN); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour: current_node.getNeighbours()) { // Check each neighbour of the current node
                    if (data.isTraversal_canceled()) {
                        return;
                    }

                    sleep(operation_speed);

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        discovered.add(neighbour); // Add the neighbour to the queue if not already discovered
                        queue.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data.getGraph(), current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW);
                            }
                        });

                        if (neighbour.equals(end)) {
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), current_node, neighbour, Color.RED));
                            found[0] = true; // Update to true if neighbour is the desired end node, and immediately break from loop
                            data.setTraversal_in_progress(false);                              // To prevent unnecessary computation
                            return;
                        }
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()) {
                    visited.add(current_node); // Add to visited list
                }

                element_highlight(data, visited, discovered, current_node, start, end);
            }

        }).start(); // Ensure the thread is switched to, instead of main render thread
        data.setTraversal_in_progress(false); // Allow a new traversal to be run after this one has completed
        data.setTraversal_canceled(false);
    }

    public static void bidirectional(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        long operation_speed = (long) (operation_speed_base / data.getTraversal_speed());

        Node forward_start = data.getGraph().get_node_id(data.getStart_node());
        Node reverse_start = data.getGraph().get_node_id(data.getEnd_node());

        ArrayList<Node> forward_queue = new ArrayList<>();
        ArrayList<Node> reverse_queue = new ArrayList<>();
        ArrayList<Node> forward_discovered = new ArrayList<>();
        ArrayList<Node> reverse_discovered = new ArrayList<>();
        ArrayList<Node> forward_visited = new ArrayList<>();
        ArrayList<Node> reverse_visited = new ArrayList<>();
        final boolean[] found = {false};

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                forward_start.setColour(Color.GREEN); // Highlight start nodes green
                reverse_start.setColour(Color.GREEN);
            });

            forward_queue.add(forward_start);
            reverse_queue.add(reverse_start);
            forward_discovered.add(forward_start);
            reverse_discovered.add(reverse_start);

            while ((!forward_queue.isEmpty() || !reverse_queue.isEmpty()) && !found[0] && !data.isTraversal_canceled()) {
                Node forward_current_node = forward_queue.remove(0);
                Node reverse_current_node = reverse_queue.remove(0);

                if (data.isTraversal_canceled()) {
                    return;
                }

                Gdx.app.postRunnable(() -> {
                   if (forward_current_node != forward_start){
                       forward_current_node.setColour(Color.CYAN);
                   }
                   if (reverse_current_node != reverse_start){
                       reverse_current_node.setColour(Color.CYAN);
                   }
                });

                for (Node neighbour : forward_current_node.getNeighbours()) { // Check each neighbour of the forward current node
                    if (data.isTraversal_canceled()) {
                        return;
                    }

                    sleep(operation_speed);

                    if (!forward_discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        forward_discovered.add(neighbour); // Add the neighbour to the forward queue if not already discovered
                        forward_queue.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data.getGraph(), forward_current_node, neighbour, Color.YELLOW);
                            if(neighbour != forward_start && neighbour != reverse_start) {
                                neighbour.setColour(Color.YELLOW);
                            }
                        });

                        for (Node forward_node: reverse_discovered) {
                            for (Node reverse_node: forward_queue) {
                                if (forward_node.equals(reverse_node)) {
                                    found[0] = true;
                                    data.setTraversal_in_progress(false);
                                    forward_current_node.setColour(Color.SKY);
                                    reverse_current_node.setColour(Color.SKY);
                                    return;
                                }
                            }
                        }

                        if (neighbour.equals(reverse_start)) {
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), forward_current_node, neighbour, Color.RED));
                            found[0] = true; // Update to true if neighbour is the desired reverse start node, and immediately break from loop
                            data.setTraversal_in_progress(false);                                        // To prevent unnecessary computation
                            return;
                        }
                    }
                }

                if (!forward_visited.contains(forward_current_node) && !data.isTraversal_canceled()) {
                    forward_visited.add(forward_current_node); // Add to forward visited list
                }

                for (Node neighbour : reverse_current_node.getNeighbours()) { // Check each neighbour of the reverse current node
                    if (data.isTraversal_canceled()) {
                        return;
                    }

                    sleep(operation_speed);

                    if (!reverse_discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        reverse_discovered.add(neighbour); // Add the neighbour to the reverse queue if not already discovered
                        reverse_queue.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data.getGraph(), reverse_current_node, neighbour, Color.YELLOW);
                            if(neighbour != reverse_start && neighbour != forward_start) {
                                neighbour.setColour(Color.YELLOW);
                            }
                        });

                        for (Node reverse_node: reverse_discovered) {
                            for (Node forward_node: forward_queue) {
                                if (reverse_node.equals(forward_node)) {
                                    found[0] = true;
                                    data.setTraversal_in_progress(false);
                                    forward_current_node.setColour(Color.SKY);
                                    reverse_current_node.setColour(Color.SKY);
                                    return;
                                }
                            }
                        }

                        if (neighbour.equals(forward_start)) {
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), reverse_current_node, neighbour, Color.RED));
                            found[0] = true; // Update to true if neighbour is the desired forward start node, and immediately break from loop
                            data.setTraversal_in_progress(false);                                        // To prevent unnecessary computation
                            return;
                        }
                    }
                }

                if (!reverse_visited.contains(reverse_current_node) && !data.isTraversal_canceled()) {
                    reverse_visited.add(reverse_current_node); // Add to reverse visited list
                }

                element_highlight(data, forward_visited, forward_discovered, forward_current_node, forward_start, reverse_start);
                element_highlight(data, reverse_visited, reverse_discovered, reverse_current_node, reverse_start, forward_start);
            }
        }).start(); // Ensure the thread is switched to, instead of main render thread
        data.setTraversal_in_progress(false); // Allow a new traversal to be run after this one has completed
        data.setTraversal_canceled(false);
    }

    public static void dijkstra(Runtime_Data data) {

    }

    public static void A_star(Runtime_Data data) {

    }

    public static void Bellman_Ford(Runtime_Data data) {

    }

    private static void element_highlight(Runtime_Data data, ArrayList<Node> visited, ArrayList<Node> discovered, Node current_node, Node start, Node end) {
        for (Node node: visited) {
            if (node != start && node != end && !data.isTraversal_canceled()) {
                boolean all_neighbours_visited = true;
                for (Node neighbour: node.getNeighbours()) {
                    if (!visited.contains(neighbour)) {
                        all_neighbours_visited = false; // Check to see if each neighbour of every visited node has also been visited
                        break;
                    }
                }

                if (all_neighbours_visited) { // Highlight edge and reverse node purple if all neighbours visited
                    Gdx.app.postRunnable(() -> node.setColour(Color.PURPLE));
                    if (visited.contains(node) && discovered.contains(node)) {
                        for (Node neighbour: node.getNeighbours()) {
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), node, neighbour, Color.PURPLE));
                        }
                    }
                } else {
                    if (node == current_node) { // Else, highlight the reverse node orange if it has been visited
                        Gdx.app.postRunnable(() -> node.setColour(Color.ORANGE));           // but its neighbours haven't
                    }
                }
            }
        }
    }

    // Euclidean distance calculation for the A* heuristic
    private static double euclidean_heuristic(int source_x, int source_y, int target_x, int target_y) {
        int dx = target_x - source_x;
        int dy = target_y - source_y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static void highlight_edge(Graph graph, Node node, Node neighbour, Color colour) {
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
            Thread.sleep(operation_speed); // Wait the set time between each step so the user can follow what is happening
        } catch (InterruptedException e) {
            System.out.println("Sleep time of " +  operation_speed + "ms failed or was interrupted");
        }
    }
}
