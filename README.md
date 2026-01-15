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

On the left hand side of the screen, using Scene2D, there is a variety of elements. There are elements to control the simulation in the following ways:
- Quit the program
- Save the current layout of nodes and edges
- Load a previously saved layout of nodes and edges
- Load a preset grid layout of nodes and edges, where all the edges have a weight of 1
- Create a randomly laid out graph, where the edges between the nodes are sensibly created, like roads between cities
- Reset the current layout of nodes and edges 
- Alter the speed at which the traversals occur
- Change the type of traversal algorithm used
- Change how the traversal occurs <sup>1</sup>
- Enter the start node for the traversals
- Enter the end node for the traversals <sup>2</sup>
- Start the traversal
- Reset the colouring caused by the last traversal run

Additionally, at the top of the UI beneath the quit button, there are some statistics about the scene, such as node and edge count.

If an error is made in any of the options, a suitable error message will be displayed with what the problem is so that it can be fixed, thus allowing for the traversal to be started.

##  Controls

`Left Click` to create a node

`Middle Click` to delete a node

`Right Click` to select 2 different nodes and create an edge between them

`Right Click` 2 nodes with an edge between them to edit its weight, should a suitable traversal algorithm <sup>3</sup> be selected. Once the desired weight has been inputted, press enter to confirm it

`Right Click` then `Middle Click` or press `Backspace` to delete an edge

`SPACE` to start the traversal, if valid conditions for one to start exist

`ESCAPE` to quit the program


## Notes

This program has been created as a project for my A-Level Computer Science NEA. As such, it may not visually be appealing, however this means that it is much more technically advanced in its algorithms and the overall codebase. The colour choices are explained via a key in the bottom left of the screen, which you can mouse over to get a more in depth tooltip.

For the UI I have used a [skin](https://github.com/czyzby/gdx-skins/tree/master/shade) created by [Raymond "Raeleus" Buckley](http://www.badlogicgames.com/forum/viewtopic.php?f=22&t=21568), which can be freely used under the CC BY licence.


1:  The traversals can either run automatically with a delay between each step, be stepped manually or will process with no delay.</br>
2:  In the case of the bidirectional search, the end node is the secondary or reverse start node.</br>
3:  The suitable algorithms aforementioned are Dijkstra's, A*, and Bellman-Ford.
