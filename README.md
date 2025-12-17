# Graph Traversal Simulation

This is a Graph Traversal Simulator, that uses LibGDX as a framework for visualising the graph.

The simulator allows for the creation of a custom graph with weighted edges, and a series of different traversal algorithms. These include: 
- Depth First Search
- Breadth First Search
- Bidirectional Search
- Dijkstra's Algorithm
- A* Traversal
- Bellman-Ford Traversal

## UI

On the left hand side of the screen, using Scene2D, there is a variety of elements. There are buttons to control the simulation in the following ways:
- Clear the graph
- Save or load a layout of nodes and edges
- Choose whether to automatically run the traversals or step through them
- Choose the speed at which the traversals will be automatically run
- Choose the type of graph traversal
- Choose the start and end node for finding the shortest path
- Generate either a random graph or a set graph for ease of viewing the traversal algorithms
- Quit the program

Additionally, at the top of the UI, there are some statistics about the scene, such as node and edge count.

If an error is made in any of the options, a suitable error message will be displayed with what the problem is so that it can be fixed, thus allowing for the traversal to be started.

##  Controls

`Left Click` to create a node

`Middle Click` to delete a node

`Right Click` to select 2 different nodes and create an edge between them

`Right Click` 2 nodes with an edge between them to edit its weight, should a suitable traversal algorithm<sup>1</sup> be presently selected

`Right Click` then `Middle Click` or press `Backspace` to delete an edge

`ESCAPE` to quit the program


## Notes

This program has been created as a project for my A-Level Computer Science NEA. As such, it may not visually be appealing, however this means that it is much more technically advanced in its algorithms and the overall codebase. The colour choices are explained via a key in the bottom left of the screen, which you can mouse over to get a more in depth tooltip.

For the UI I have used a [skin](https://github.com/czyzby/gdx-skins/tree/master/shade) created by [Raymond "Raeleus" Buckley](http://www.badlogicgames.com/forum/viewtopic.php?f=22&t=21568), which can be freely used under the CC BY license.


1:  The suitable algorithms aforementioned are Dijkstra's, A*, and Bellman-Ford.
