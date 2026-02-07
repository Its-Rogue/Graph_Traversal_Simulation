package essential_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import structural_classes.Edge;
import structural_classes.Node;
import structural_classes.Priority_Queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Traversals{
    private final static long operation_speed_base = 200;

    public static void dfs(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Node start = data.getGraph().get_node_from_id(data.getStart_node()); // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());     // in the case they are overwritten

        ArrayList<Node> stack = new ArrayList<>();
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>(); // Specifically for traversal colour updating, not needed for algorithm
        Map<Node, Node> previous = new HashMap<>();
        final boolean[] found ={false};

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            stack.add(start);
            discovered.add(start);
            previous.put(start, null);

            while (!stack.isEmpty() && !found[0] && !data.isTraversal_canceled()) {
                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed()));
                    traversal_speed_cache.set(data.getTraversal_speed());
                }

                Node current_node = stack.remove(stack.size() - 1);
                Gdx.app.postRunnable(() -> current_node.setColour(Color.CYAN));

                if (data.Should_step()) {
                    wait_for_step(data); // Wait until the user presses the step button
                }

                for (Node neighbour: current_node.getNeighbours()) {
                    if (data.isTraversal_canceled()) {
                        return; // Stop traversal if the user has pressed the reset traversal button
                    }

                    if (data.getGraph().get_node_from_id(neighbour.getId()) == null) {
                        continue;
                    }

                    Gdx.app.postRunnable(() -> {
                        highlight_edge(data, current_node, neighbour, Color.GRAY);
                        if (neighbour != start && neighbour != end) {
                            neighbour.setColour(Color.GRAY); // Highlight neighbour grey to help visualise steps
                        }
                    });

                    if (data.Should_sleep()) {
                        sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                    } else if (data.Should_step()) {
                        wait_for_step(data); // Wait until the user presses the step button
                    }

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        stack.add(neighbour); // Add neighbour to stack if not already discovered
                        discovered.add(neighbour);
                        previous.put(neighbour, current_node);

                        if (data.isTraversal_canceled()) {
                            return; // Return if the user cancels the traversal by pressing the reset traversal button
                        }

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data, current_node, neighbour, Color.YELLOW);
                            if (neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                            }
                        });

                        if (neighbour.equals(end)) { // Path between start and end node found
                            found[0] = true;
                            break;
                        }
                    } else if (!visited.contains(neighbour)) {
                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data, current_node, neighbour, Color.YELLOW);
                            if (neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                            }
                        });
                    }

                    if (visited.contains(neighbour)) {
                        Gdx.app.postRunnable(() -> {
                            if (neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.ORANGE);
                            }
                            highlight_edge(data, current_node, neighbour, Color.ORANGE);
                        });
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()) {
                    visited.add(current_node); // Help track whether all the neighbours to a node have been visited
                }

                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                if (!current_node.equals(start) && !current_node.equals(end)) {
                    Gdx.app.postRunnable(() -> current_node.setColour(Color.ORANGE)); // Highlight current orange to mark end of processing
                }

                element_highlight(data, visited, discovered, start, end);

                if (found[0]) {
                    break;
                }
            }

            if (found[0]) {
                List<Node> path = reconstruct_path(previous, start, end); // Reconstruct the determined path between the 2 nodes
                highlight_path(data, path, start, end);
            } else {
                data.getError_popup_label().setText("No path found"); // Alert user than no path has been found between start and end node
                data.getError_popup().setVisible(true);
            }

            data.setTraversal_in_progress(false);  // Allow a new traversal to be run after this one has completed
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void bfs(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Node start = data.getGraph().get_node_from_id(data.getStart_node());   // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());       // in the case they are overwritten

        ArrayList<Node> queue = new ArrayList<>(); // Initialise the various lists
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();
        Map<Node, Node> previous = new HashMap<>();
        final boolean[] found ={false};

        new Thread(() -> { // New local render thread to allow for nodes to change colours
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            queue.add(start); // Add the first node to the discovered and queue
            discovered.add(start);
            previous.put(start, null);

            while (!queue.isEmpty() && !found[0] && !data.isTraversal_canceled()) { // Loop through until found or no more nodes in graph
                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed())); // Update operation speed if it has been altered by the user using the slider
                    traversal_speed_cache.set(data.getTraversal_speed());
                }

                Node current_node = queue.remove(0); // Pop the node at the front of the queue
                Gdx.app.postRunnable(() -> current_node.setColour(Color.CYAN));

                if (data.Should_step()) {
                    wait_for_step(data); // Wait until the user presses the step button
                }

                for (Node neighbour: current_node.getNeighbours()) { // Check each neighbour of the current node
                    if (data.isTraversal_canceled()) {
                        return; // Check for cancellation of traversal
                    }

                    if (data.getGraph().get_node_from_id(neighbour.getId()) == null) {
                        continue;
                    }

                    Gdx.app.postRunnable(() -> {
                        highlight_edge(data, current_node, neighbour, Color.GRAY);
                        if (neighbour != start && neighbour != end) {
                            neighbour.setColour(Color.GRAY); // Highlight neighbour grey to help visualise steps
                        }
                    });

                    if (data.Should_sleep()) {
                        sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                    } else if (data.Should_step()) {
                        wait_for_step(data); // Wait until the user presses the step button
                    }

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        discovered.add(neighbour); // Add the neighbour to the queue if not already discovered
                        queue.add(neighbour);
                        previous.put(neighbour, current_node);

                        if (data.isTraversal_canceled()) {
                            return; // Return if the user cancels the traversal by pressing the reset traversal button
                        }

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data, current_node, neighbour, Color.YELLOW);
                            if (neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW);
                            }
                        });

                        if (neighbour.equals(end)) {
                            found[0] = true; // Update to true if neighbour is the desired end node
                            break;
                        }
                    } else if (!visited.contains(neighbour)) {
                        Gdx.app.postRunnable(() ->{
                            highlight_edge(data, current_node, neighbour, Color.YELLOW);
                            if (neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                            }
                        });
                    }

                    if (visited.contains(neighbour)) {
                        Gdx.app.postRunnable(() -> {
                            if (neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.ORANGE);
                            }
                            highlight_edge(data, current_node, neighbour, Color.ORANGE);
                        });
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()) {
                    visited.add(current_node); // Add to visited list
                }

                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                if (!current_node.equals(start) && !current_node.equals(end)) {
                    Gdx.app.postRunnable(() -> current_node.setColour(Color.ORANGE)); // Highlight current orange to mark end of processing
                }

                element_highlight(data, visited, discovered, start, end);

                if (found[0]) {
                    break;
                }
            }

            if (found[0]) {
                List<Node> path = reconstruct_path(previous, start, end); // Reconstruct the determined path between the 2 nodes
                highlight_path(data, path, start, end);
            } else {
                data.getError_popup_label().setText("No path found"); // Alert user than no path has been found between start and end node
                data.getError_popup().setVisible(true);
            }

            data.setTraversal_in_progress(false); // Allow a new traversal to be run after this one has completed
            data.setTraversal_canceled(false);
        }).start(); // Ensure the thread is switched to, instead of main render thread
    }

    public static void bidirectional(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Node forward_start = data.getGraph().get_node_from_id(data.getStart_node());
        Node reverse_start = data.getGraph().get_node_from_id(data.getEnd_node());

        ArrayList<Node> forward_queue = new ArrayList<>(); // Initialise queues, discovered and visited for forward / reverse directions
        ArrayList<Node> reverse_queue = new ArrayList<>();
        Set<Node> forward_discovered = new HashSet<>();
        Set<Node> reverse_discovered = new HashSet<>();
        ArrayList<Node> forward_visited = new ArrayList<>();
        ArrayList<Node> reverse_visited = new ArrayList<>();
        Map<Node, Node> forward_previous = new HashMap<>();
        Map<Node, Node> reverse_previous = new HashMap<>();
        final boolean[] found = {false};
        final Node[] meeting_node = {null};

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                forward_start.setColour(Color.GREEN); // Highlight start nodes their respective colours
                reverse_start.setColour(Color.RED);
            });

            forward_queue.add(forward_start);
            reverse_queue.add(reverse_start);
            forward_discovered.add(forward_start);
            reverse_discovered.add(reverse_start);
            forward_previous.put(forward_start, null);
            reverse_previous.put(reverse_start, null);

            while (!forward_queue.isEmpty() && !reverse_queue.isEmpty() && !found[0] && !data.isTraversal_canceled()) {
                if (data.isTraversal_canceled()) {
                    return;
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed()));
                    traversal_speed_cache.set(data.getTraversal_speed()); // Update traversal speed if the user has changed it
                }

                if (!forward_queue.isEmpty() && !found[0]) { // Forward search
                    if (data.isTraversal_canceled()) {
                        return;
                    }

                    Node forward_current_node = forward_queue.remove(0); // Same logic as a breadth first search
                    Gdx.app.postRunnable(() -> forward_current_node.setColour(Color.CYAN));

                    if (data.Should_step()) {
                        wait_for_step(data); // Wait until the user presses the step button
                    }

                    for (Node neighbour : forward_current_node.getNeighbours()){
                        if (data.isTraversal_canceled()) {
                            return;
                        }

                        if (data.getGraph().get_node_from_id(neighbour.getId()) == null) {
                            continue;
                        }

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data, forward_current_node, neighbour, Color.GRAY);
                            if (neighbour != forward_start && neighbour != reverse_start) {
                                neighbour.setColour(Color.GRAY); // Highlight neighbour grey to help visualise steps
                            }
                        });

                        if (data.Should_sleep()) {
                            sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                        } else if (data.Should_step()) {
                            wait_for_step(data); // Wait until the user presses the step button
                        }

                        if (!forward_discovered.contains(neighbour)) {
                            forward_discovered.add(neighbour);
                            forward_queue.add(neighbour);
                            forward_previous.put(neighbour, forward_current_node);

                            if (data.isTraversal_canceled()) {
                                return; // Return if the user cancels the traversal by pressing the reset traversal button
                            }

                            Gdx.app.postRunnable(() -> {
                                highlight_edge(data, forward_current_node, neighbour, Color.YELLOW);
                                if (neighbour != forward_start && neighbour != reverse_start) {
                                    neighbour.setColour(Color.YELLOW);
                                }
                            });

                            if (reverse_discovered.contains(neighbour)) {
                                found[0] = true;
                                meeting_node[0] = neighbour;
                                break;
                            }
                        } else if (!forward_visited.contains(neighbour)) {
                            Gdx.app.postRunnable(() -> {
                                highlight_edge(data, forward_current_node, neighbour, Color.YELLOW);
                                if (neighbour != forward_start && neighbour != reverse_start) {
                                    neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                                }
                            });
                        }

                        if (forward_visited.contains(neighbour)) {
                            Gdx.app.postRunnable(() -> {
                                if (neighbour != forward_start && neighbour != reverse_start) {
                                    neighbour.setColour(Color.ORANGE);
                                }
                                highlight_edge(data, forward_current_node, neighbour, Color.ORANGE);
                            });
                        }
                    }

                    if (!forward_visited.contains(forward_current_node)) {
                        forward_visited.add(forward_current_node);
                    }

                    if (data.isTraversal_canceled()) {
                        return; // Return if the user cancels the traversal by pressing the reset traversal button
                    }

                    if (!forward_current_node.equals(forward_start) && !forward_current_node.equals(reverse_start)) {
                        Gdx.app.postRunnable(() -> forward_current_node.setColour(Color.ORANGE)); // Highlight current orange to mark end of processing
                    }

                    element_highlight(data, forward_visited, new ArrayList<>(forward_discovered), forward_start, reverse_start);
                }

                if (found[0]) {
                    break; // Do not process reverse search if a continuous path has been found
                }

                if (!reverse_queue.isEmpty()) { // Reverse search
                    if (data.isTraversal_canceled()) {
                        return;
                    }

                    Node reverse_current_node = reverse_queue.remove(0); // Same logic as a breadth first search
                    Gdx.app.postRunnable(() -> reverse_current_node.setColour(Color.CYAN));

                    if (data.Should_step()) {
                        wait_for_step(data); // Wait until the user presses the step button
                    }

                    for (Node neighbour : reverse_current_node.getNeighbours()) {
                        if (data.isTraversal_canceled()){
                            return;
                        }

                        if (data.getGraph().get_node_from_id(neighbour.getId()) == null) {
                            continue;
                        }

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data, reverse_current_node, neighbour, Color.GRAY);
                            if (neighbour != forward_start && neighbour != reverse_start) {
                                neighbour.setColour(Color.GRAY); // Highlight neighbour grey to help visualise steps
                            }
                        });

                        if (data.Should_sleep()) {
                            sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                        } else if (data.Should_step()) {
                            wait_for_step(data); // Wait until the user presses the step button
                        }

                        if (!reverse_discovered.contains(neighbour)) {
                            reverse_discovered.add(neighbour);
                            reverse_queue.add(neighbour);
                            reverse_previous.put(neighbour, reverse_current_node);

                            if (data.isTraversal_canceled()) {
                                return; // Return if the user cancels the traversal by pressing the reset traversal button
                            }

                            Gdx.app.postRunnable(() -> {
                                highlight_edge(data, reverse_current_node, neighbour, Color.YELLOW);
                                if (neighbour != forward_start && neighbour != reverse_start) {
                                    neighbour.setColour(Color.YELLOW);
                                }
                            });

                            if (forward_discovered.contains(neighbour)) {
                                found[0] = true;
                                meeting_node[0] = neighbour;
                                break;
                            }
                        } else if (!reverse_visited.contains(neighbour)) {
                            Gdx.app.postRunnable(() -> {
                                highlight_edge(data, reverse_current_node, neighbour, Color.YELLOW);
                                if (neighbour != forward_start && neighbour != reverse_start) {
                                    neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                                }
                            });
                        }

                        if (reverse_visited.contains(neighbour)) {
                            Gdx.app.postRunnable(() -> {
                                if (neighbour != forward_start && neighbour != reverse_start) {
                                    neighbour.setColour(Color.ORANGE);
                                }
                                highlight_edge(data, reverse_current_node, neighbour, Color.ORANGE);
                            });
                        }
                    }

                    if (!reverse_visited.contains(reverse_current_node)) {
                        reverse_visited.add(reverse_current_node);
                    }

                    if (data.isTraversal_canceled()) {
                        return; // Return if the user cancels the traversal by pressing the reset traversal button
                    }

                    if (!reverse_current_node.equals(reverse_start) && !reverse_current_node.equals(forward_start)) {
                        Gdx.app.postRunnable(() -> reverse_current_node.setColour(Color.ORANGE)); // Highlight current orange to mark end of processing
                    }

                    element_highlight(data, reverse_visited, new ArrayList<>(reverse_discovered), forward_start, reverse_start);
                }

                if (found[0]) {
                    break; // Break if continuous path has been found
                }
            }

            if (found[0]) {
                List<Node> path = new ArrayList<>();
                Node current = meeting_node[0];
                while (current != null) {
                    path.add(0, current);
                    current = forward_previous.get(current);
                }

                current = reverse_previous.get(meeting_node[0]);
                while (current != null) {
                    path.add(current);
                    current = reverse_previous.get(current);
                }

                highlight_path(data, path, forward_start, reverse_start);
            } else {
                data.getError_popup_label().setText("No path found"); // Alert user than no path has been found between start and end node
                data.getError_popup().setVisible(true);
            }

            data.setTraversal_in_progress(false);
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void dijkstra(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Map<Node, Integer> distances = new HashMap<>(); // Maps for distances between nodes, and what node was last accessed before itself
        Map<Node, Node> previous = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        Priority_Queue pq = new Priority_Queue();

        ArrayList<Node> discovered = new ArrayList<>(); // Helper for visualisation

        Node start = data.getGraph().get_node_from_id(data.getStart_node());
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());

        AtomicReference<Boolean> path_found = new AtomicReference<>(false);

        for (Node n: data.getGraph().get_nodes()) {
            distances.put(n, Integer.MAX_VALUE); // Add all nodes to the maps
            previous.put(n, null);
        }

        distances.put(start, 0); // Update the map for the start node
        pq.add(start.getId(), 0);

        new Thread(() -> {
            if (data.isTraversal_canceled()){
                return; // Return if the user cancels the traversal by pressing the reset traversal button
            }

            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the start and end node green and red respectively each frame to ensure they are
                end.setColour(Color.RED);     // Correctly highlighted throughout
            });

            while (!pq.isEmpty()) {
                if (data.isTraversal_canceled()) {
                    return; // Check for cancellation of traversal
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed())); // Update operation speed if it has been altered by the user using the slider
                    traversal_speed_cache.set(data.getTraversal_speed());
                }

                Node current = data.getGraph().get_node_from_id(pq.poll());

                if (visited.contains(current)) {
                    continue; // Skip if the node has been targeted before
                }

                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                Gdx.app.postRunnable(() -> current.setColour(Color.CYAN)); // Set the current node to be cyan

                visited.add(current);

                if (data.Should_sleep()) {
                    sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                } else if (data.Should_step()) {
                    wait_for_step(data); // Wait until the user presses the step button
                }

                for (Edge e: data.getGraph().get_edges(current)) {
                    Node neighbour = e.getTarget(); // Get neighbour from edges of current node
                    if (data.isTraversal_canceled()){
                        return; // Return if the user cancels the traversal by pressing the reset traversal button
                    }

                    if (data.getGraph().get_node_from_id(neighbour.getId()) == null) {
                        continue;
                    }

                    if (visited.contains(neighbour)) {
                        continue;
                    }
                    if (!discovered.contains(neighbour)) {
                        discovered.add(neighbour);
                    }

                    Gdx.app.postRunnable(() -> {
                        highlight_edge(data, current, neighbour, Color.GRAY);
                        if (neighbour != start && neighbour != end) {
                            neighbour.setColour(Color.GRAY); // Highlight neighbour grey to help visualise steps
                        }
                    });

                    if (data.Should_sleep()) {
                        sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                    } else if (data.Should_step()) {
                        wait_for_step(data); // Wait until the user presses the step button
                    }

                    Gdx.app.postRunnable(() -> {
                        highlight_edge(data, current, neighbour, Color.YELLOW);
                        if (neighbour != start && neighbour != end) {
                            neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                        }
                    });

                    int alt = distances.get(current) + e.getWeight(); // Calculate new distance cost

                    if (alt < distances.get(neighbour)) { // Check to see if the new cost is cheaper than the old cost
                        distances.put(neighbour, alt);
                        previous.put(neighbour, current);
                        pq.add(neighbour.getId(), alt); // Update accordingly if true
                    }

                    if (data.isTraversal_canceled()) {
                        return; // Check for cancellation of traversal
                    }
                }

                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                if (current != start && current != end) {
                    Gdx.app.postRunnable(() -> current.setColour(Color.ORANGE)); // Set the current node to orange to mark it as visited at the end of the loop
                }

                ArrayList<Node> visited_list = new ArrayList<>(visited); // Convert set to a list for re-colouring of nodes in graph
                element_highlight(data, visited_list, discovered, start, end); // Reusing the previously used logic

                if (current.equals(end)) {
                    path_found.set(true);
                    break;
                }

                if (data.isTraversal_canceled()) {
                    return; // Check for cancellation of traversal
                }
            }

            if (path_found.get()) {
                List<Node> path = reconstruct_path(previous, start, end); // Reconstruct the determined path between the 2 nodes
                int total_cost = distances.get(end);

                if (total_cost != Integer.MAX_VALUE) {
                    data.getError_popup_label().setText("Path found with cost " + total_cost);
                    data.getError_popup().setVisible(true);
                }

                highlight_path(data, path, start, end);
            } else {
                data.getError_popup_label().setText("No path found");
                data.getError_popup().setVisible(true);
            }

            data.setTraversal_in_progress(false); // Allow a new traversal to be run after this one has completed
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void A_star(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed())); // Cache traversal speed so it can
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());        // be updated at run time

        Map<Node, Integer> g_score = new HashMap<>(); // Initialise maps and priority queue
        Map<Node, Double> f_score = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Priority_Queue pq = new Priority_Queue();

        ArrayList<Node> visited = new ArrayList<>(); // Helpers for traversal visualisation
        ArrayList<Node> discovered = new ArrayList<>();
        Set<Node> closed_set = new HashSet<>();

        Node start = data.getGraph().get_node_from_id(data.getStart_node());
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());

        for (Node n: data.getGraph().get_nodes()) {
            g_score.put(n, Integer.MAX_VALUE); // Add all nodes with default values to maps
            f_score.put(n, Double.MAX_VALUE);
            previous.put(n, null);
        }

        g_score.put(start, 0); // Calculate g_score, f_score for user start node and add them to priority queue
        f_score.put(start, euclidean_heuristic(start.getPosition().getX(), start.getPosition().getY(), end.getPosition().getX(), end.getPosition().getY()));
        pq.add(start.getId(), (int) f_score.get(start).doubleValue());

        new Thread(() -> {
            if (data.isTraversal_canceled()) {
                return; // Return if the user cancels the traversal by pressing the reset traversal button
            }

            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight start and end node green and red respectively
                end.setColour(Color.RED);
            });

            boolean path_found = false; // Flag for identifying whether a valid path exists between the user start and end node

            while (!pq.isEmpty()) {
                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed())); // Update traversal speed if it differs to
                    traversal_speed_cache.set(data.getTraversal_speed());                           // the value that has been cached
                }

                Node current = data.getGraph().get_node_from_id(pq.poll()); // Get node from pq

                if (closed_set.contains(current)) {
                    continue; // Skip if the node has already been visited
                }

                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                Gdx.app.postRunnable(() -> current.setColour(Color.CYAN)); // Highlight current node cyan (based from colour key)

                if (!visited.contains(current)) {
                    visited.add(current); // Add to helper arraylist
                }

                if (data.Should_sleep()) {
                    sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                } else if (data.Should_step()) {
                    wait_for_step(data); // Wait until the user presses the step button
                }

                if (current.equals(end)) {
                    path_found = true; // Break if current node = end node; path found between start and end nodes
                    break;
                }

                for (Edge e: data.getGraph().get_edges(current)) {
                    if (data.isTraversal_canceled()) {
                        return; // Return if the user cancels the traversal by pressing the reset traversal button
                    }

                    Node neighbour = e.getTarget(); // Get neighbour from edges of current node

                    Gdx.app.postRunnable(() -> {
                        highlight_edge(data, current, neighbour, Color.GRAY);
                        if (neighbour != start && neighbour != end) {
                            neighbour.setColour(Color.GRAY); // Highlight neighbour grey to help visualise steps
                        }
                    });

                    if (data.Should_sleep()) {
                        sleep(operation_speed.get()); // Sleep cached amount of time for the current step
                    } else if (data.Should_step()) {
                        wait_for_step(data); // Wait until the user presses the step button
                    }

                    if (closed_set.contains(neighbour)) {
                        if (visited.contains(neighbour)) {
                            Gdx.app.postRunnable(() -> {
                                if (neighbour != start && neighbour != end) {
                                    neighbour.setColour(Color.ORANGE);
                                }
                                highlight_edge(data, current, neighbour, Color.ORANGE);
                            });
                        }
                        continue; // Skip if neighbour has already been visited
                    }

                    int intermediate_g_score = g_score.get(current) + e.getWeight(); // Calculate g_score (old g_score + edge weight)

                    if (!discovered.contains(neighbour)) {
                        discovered.add(neighbour); // Add neighbour to helper arraylist
                    }

                    Gdx.app.postRunnable(() -> {
                        highlight_edge(data, current, neighbour, Color.YELLOW);
                        if (neighbour != start && neighbour != end) {
                            neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                        }
                    });

                    if (intermediate_g_score < g_score.get(neighbour)) { // Update maps if new g_score is less than current g_score for neighbour
                        previous.put(neighbour, current);
                        g_score.put(neighbour, intermediate_g_score);
                        double intermediate_f_score = intermediate_g_score + euclidean_heuristic(neighbour.getPosition().getX(), neighbour.getPosition().getY(), end.getPosition().getX(), end.getPosition().getY());
                        f_score.put(neighbour, intermediate_f_score);
                        pq.add(neighbour.getId(), (int) intermediate_f_score);
                    }
                }

                closed_set.add(current); // Add node to helper class

                if (data.isTraversal_canceled()){
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                if (!current.equals(start) && !current.equals(end)) {
                    Gdx.app.postRunnable(() -> current.setColour(Color.ORANGE)); // Highlight current orange to mark end of processing
                }

                element_highlight(data, visited, discovered, start, end); // Call to overarching highlight function to update all nodes' colours
            }

            if (path_found) {
                List<Node> path = reconstruct_path(previous, start, end); // Generate path based on the previous node of each node between end and start
                int total_cost = g_score.get(end);

                data.getError_popup().setVisible(true);
                data.getError_popup_label().setText("Path found with cost: " + total_cost);

                highlight_path(data, path, start, end);
            } else {
                data.getError_popup_label().setText("No path found"); // Alert user than no path has been found between start and end node
                data.getError_popup().setVisible(true);
            }

            data.setTraversal_in_progress(false); // Update traversal flags to allow for another traversal to be run, and for the user to be able to edit the graph again
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void Bellman_Ford(Runtime_Data data) {
        data.setTraversal_in_progress(true);

        Map<Node, Integer> distances = new HashMap<>(); // Initialise maps for traversal
        Map<Node, Node> previous = new HashMap<>();

        Node start = data.getGraph().get_node_from_id(data.getStart_node());
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());

        AtomicBoolean negative_cycle_detected = new AtomicBoolean(false); // Variables to help with negative cycle detection
        List<Node> cycle_nodes = new ArrayList<>();

        for (Node n: data.getGraph().get_nodes()) {
            distances.put(n, Integer.MAX_VALUE); // Add all nodes to map with default values
            previous.put(n, null);
        }

        distances.put(start, 0); // Add start node with 0 to distances

        new Thread(() -> {
            if (data.isTraversal_canceled()) {
                return; // Return if the user cancels the traversal by pressing the reset traversal button
            }

            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight start and end nodes green and red respectively
                end.setColour(Color.RED);
            });

            for (int i = 0; i < data.getGraph().get_nodes().size() - 1; i++) {
                if (data.isTraversal_canceled()) {
                    return; // Return if the user cancels the traversal by pressing the reset traversal button
                }

                for (Node n: data.getGraph().get_nodes()) { // Check all nodes and neighbours in a graph
                    if (distances.get(n) == Integer.MAX_VALUE) {
                        continue; // Skip nodes with default value
                    }

                    for (Edge e: data.getGraph().get_edges(n)) {
                        Node neighbour = e.getTarget(); // Get neighbours from the edges of the current node
                        int weight = e.getWeight();
                        int alt = distances.get(n) + weight; // Calculate new distance between nodes

                        if (alt < distances.get(neighbour)) {
                            distances.put(neighbour, alt); // Update maps if new distance < old distance
                            previous.put(neighbour, n);
                        }
                    }
                }
            }

            Node negative_cycle_start = null;
            outer:
            for (Node n: data.getGraph().get_nodes()) { // Loop through nodes 1 more time to check for any negative loops by comparing to previously calculated distance
                if (distances.get(n) == Integer.MAX_VALUE) {
                    continue; // Skip nodes with default value
                }

                for (Edge e: data.getGraph().get_edges(n)) {
                    Node neighbour = e.getTarget(); // Get neighbour from edge weight
                    int weight = e.getWeight();

                    if (distances.get(n) + weight < distances.get(neighbour)) { // Check new weight against old, if its less identify a negative cycle
                        negative_cycle_detected.set(true); // Negative cycle detected as new weight is less than the already defined weight
                        negative_cycle_start = neighbour;
                        break outer;
                    }
                }
            }

            if (negative_cycle_detected.get()) { // Highlight the nodes where the negative cycle occurs
                Node current = negative_cycle_start;

                for (int i = 0; i < data.getGraph().get_nodes().size(); i++) {
                    current = previous.get(current);
                    if (current == null) break;
                }

                if (current != null) {
                    Node cycle_node = current; // Collect all nodes that form part of the negative loop
                    do {
                        cycle_nodes.add(current);
                        current = previous.get(current);
                    } while (current != null && !current.equals(cycle_node) && cycle_nodes.size() < data.getGraph().get_nodes().size());
                }

                for (int i= 0; i < cycle_nodes.size(); i++) {
                    Node n = cycle_nodes.get(i);
                    if (!n.equals(start) && !n.equals(end)) {
                        Gdx.app.postRunnable(() -> n.setColour(Color.MAGENTA)); // Highlight nodes that form negative loop MAGENTA
                    }

                    if (i > 0) {
                        Node prev = cycle_nodes.get(i - 1);
                        Gdx.app.postRunnable(() -> highlight_edge(data, n, prev, Color.MAGENTA)); // Highlight edge between nodes MAGENTA
                    }
                }

                data.getError_popup_label().setText("Negative cycle detected"); // Set error message to identify that a negative cycle has been detected
                data.getError_popup().setVisible(true);
            } else {
                List<Node> path = reconstruct_path(previous, start, end); // Reconstruct determined path between nodes

                if (path.isEmpty() || path.size() == 1) {
                    data.getError_popup_label().setText("No path found"); // Ensure a path has actually been found
                    data.getError_popup().setVisible(true);
                } else {
                    int total_cost = distances.get(end);

                    if (total_cost != Integer.MAX_VALUE) {
                        data.getError_popup_label().setText("Path found with cost " + total_cost);
                        data.getError_popup().setVisible(true);
                    }

                    highlight_path(data, path, start, end);
                }
            }

            data.setTraversal_in_progress(false); // Update traversal flags to allow for a new traversal to be run, and for the user to be able to edit the graph
            data.setTraversal_canceled(false);
        }).start();
    }

    private static void element_highlight(Runtime_Data data, ArrayList<Node> visited, ArrayList<Node> discovered, Node start, Node end) {
        for (Node n: visited) {
            if (n != start && n != end && !data.isTraversal_canceled()) {
                boolean all_neighbours_visited = true;
                for (Node neighbour: n.getNeighbours()) {
                    if (!visited.contains(neighbour) && discovered.contains(neighbour)) {
                        all_neighbours_visited = false; // Check to see if each neighbour of every visited node has also been visited
                        break;
                    }
                }

                if (all_neighbours_visited) { // Highlight edge and reverse node purple if all neighbours visited
                    Gdx.app.postRunnable(() -> n.setColour(Color.PURPLE));
                    if (visited.contains(n) && discovered.contains(n)){
                        for (Node neighbour: n.getNeighbours()){
                            Gdx.app.postRunnable(() -> highlight_edge(data, n, neighbour, Color.PURPLE));
                        }
                    }
                }
            }
        }

        // Ensure start and end nodes are properly coloured
        Gdx.app.postRunnable(() -> {
            start.setColour(Color.GREEN);
            end.setColour(Color.RED);
        });
    }

    private static void highlight_path(Runtime_Data data, List<Node> path, Node start, Node end) {
        Node last_node = null; // Cache to allow for edge highlighting of determined path

        for(Node n: path) {
            if (n.equals(start)) { // Set colours accordingly
                Gdx.app.postRunnable(() -> n.setColour(Color.GREEN)); // Highlight nodes accordingly
            } else if (n.equals(end)) {
                Gdx.app.postRunnable(() -> n.setColour(Color.RED));
            } else {
                Gdx.app.postRunnable(() -> n.setColour(Color.SKY));
            }

            if (last_node != null) {
                Node final_last_node = last_node;
                Gdx.app.postRunnable(() -> highlight_edge(data, n, final_last_node, Color.SKY)); // Highlight the edges of the path
            }

            last_node = n; // Update cached last node to highlight the edge between the next node and this one
        }
    }

    // Euclidean distance calculation for the A* heuristic
    private static double euclidean_heuristic(int source_x, int source_y, int target_x, int target_y) {
        int dx = target_x - source_x;
        int dy = target_y - source_y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static List<Node> reconstruct_path(Map<Node, Node> previous, Node start, Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;

        while (current != null) {
            path.add(0, current); // Add the current node to the front of the list
            current = previous.get(current); // Set current to the node that discovered the original current node
        }

        if (path.isEmpty() || !path.get(0).equals(start)) {
            return new ArrayList<>(); // Return a blank path if an invalid or no path is found
        }

        return path;
    }

    private static void highlight_edge(Runtime_Data data, Node node, Node neighbour, Color colour) {
        List<Edge> edge = data.getGraph().get_edges(node);
        for (Edge e: edge) { // Get forward edge and set its colour to the inputted colour
            if (e.getTarget().equals(neighbour)){
                e.setColour(colour);
                break;
            }
        }
        edge = data.getGraph().get_edges(neighbour);
        for (Edge e: edge) { // Get reverse edge and sets its colour to the inputted colour
            if (e.getTarget().equals(node)){
                e.setColour(colour);
                break;
            }
        }
    }

    private static void wait_for_step(Runtime_Data data) {
        data.setStep_button_pressed(false);
        while (!data.isStep_button_pressed() && !data.isTraversal_canceled()) {
            sleep(50); // Avoid halting the render threads unnecessarily while waiting for an input of the button being pressed
        }
    }

    private static void sleep(long operation_speed) {
        try{
            Thread.sleep(operation_speed); // Wait the set time between each step so the user can follow what is happening
        } catch (InterruptedException e) {
            System.out.println("Sleep time of " +  operation_speed + "ms failed or was interrupted");
        }
    }
}