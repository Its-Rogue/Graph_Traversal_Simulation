# Graph Traversal Simulation

This is a Graph Traversal Simulator, that uses LibGDX as a framework for visualising the graph.

The simulator allows for the creation of a custom graph with weighted edges, and a series of different traversal algorithms. These include: 
- Depth First
- Breadth First
- Bidirectional
- Dijkstra's
- A*
- Bellman-Ford

## UI

On the left hand side of the screen, using Scene2D, there is a variety of elements. There are buttons to control the simulation in the following ways:
- Clear the graph
- Choose the type of graph traversal
- Choose the start and end node for finding the shortest path
- Quit the program

Additionally, at the top of the UI, there are some statistics about the scene, such as node and edge count.

If an error is made in any of the options, a suitable error message will be displayed with what the problem is so that it can be fixed, thus allowing for the traversal to be started.

##  Controls

`Left Click` to create a node

`Middle Click` to delete a node

`Right Click` to select 2 different nodes and create an edge between them

`Right Click + Left Control` to edit the weight of an already existing edge, should a suitable traversal algorithm<sup>1</sup> be presently selected

`Right Click` then `Middle Click` or press `Backspace` to delete an edge

`ESCAPE` to quit the program


## Notes

This program has been created as a project for my A-Level Computer Science NEA. As such, it may not visually be appealing, however it is meant to be used<br>
as such is more technically advanced rather than graphically. The colour choices are explained via a key in the bottom left of the screen.

For the UI I have used a [skin](https://github.com/czyzby/gdx-skins/tree/master/shade) created by [Raymond "Raeleus" Buckley](http://www.badlogicgames.com/forum/viewtopic.php?f=22&t=21568), which can be freely used under the CC BY license.


1:  The suitable algorithms aforementioned are Dijkstra's, A*, and Bellman-Ford.
