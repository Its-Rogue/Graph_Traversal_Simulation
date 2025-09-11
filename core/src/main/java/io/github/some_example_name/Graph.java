package io.github.some_example_name;

import java.util.*;

public class Graph {
    public Map<Node, List<Edge>> adj_list;

    public Graph() {
        adj_list = new HashMap<>();
    }

    public void add_node(Node node){
        adj_list.putIfAbsent(node, new ArrayList<>());
    }

    public void add_edge(Node source, Node target, int weight){
        Edge edge = new Edge(source, target, weight);
        adj_list.get(source).add(edge);
        Edge reversed_edge = new Edge(target, source, weight);
        adj_list.get(target).add(reversed_edge);
    }

    public void remove_node(Node node){
        adj_list.values().forEach(edges -> edges.removeIf(e -> e.getTarget().equals(node)));
        adj_list.remove(node);
    }

    public void remove_edge(Node source, Node target){
        adj_list.get(source).removeIf(e -> e.getTarget().equals(target));
        adj_list.get(target).removeIf(e -> e.getSource().equals(source));
    }

    public Set<Node> get_nodes() {
        return adj_list.keySet();
    }

    public List<Edge> get_edges(Node node){
        return adj_list.get(node);
    }
}
