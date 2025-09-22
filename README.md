# Graph Traversal Simulation

This is a Graph Traversal Simulator, that uses LibGDX as a framework for visualising the graph.

The simulator allows for the creation of a custom graph with weighted edges, and a series of different traversal algorithms. These include: 
- Depth First
- Breadth First
- Dijkstra's
- A*
- Minimum Spanning Tree

## UI

On the left hand side of the screen, using Scene2D, there is a variety of elements. There are buttons to control the simulation in the following ways:
- Clear the graph
- Choose the type of graph traversal
- Choose the start and end node for finding the shortest path
- Quit the program

Additionally, at the top of the UI, there are some statistics about the scene, such as node count and FPS.

##  Controls

`Left Click` to create a node

`Middle Click` to delete a node

`Right Click` to select 2 different nodes and create an edge between them

`Right Click` then `Middle Click` or press `Backspace` to delete an edge

`ESCAPE` to quit the program
