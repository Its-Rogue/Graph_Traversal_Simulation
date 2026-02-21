# Graph Traversal Simulation

This is a Graph Traversal Simulator, that uses [LibGDX](https://libgdx.com/) as a framework for visualising the graph.

The simulator allows for the creation of a custom graph with weighted or unweighted edges, and a series of different traversal algorithms. These include:
- Depth First Search
- Breadth First Search
- Bidirectional Search
- Dijkstra's Algorithm
- A* Traversal
- Bellman-Ford Traversal

## UI

On the left hand side of the screen, using Scene2D, there is a variety of elements. There are elements to control the simulation in the following ways:
- `Quit`: Quit the program
- `Save Current Layout`: Save the current layout of nodes and edges in the scene
- `Load Saved Layout`: Load a previously saved layout of nodes and edges
- `Create a Gridded Graph`: Load a preset grid layout of nodes and edges, where all the edges have a weight of 1
- `Create a Random Graph`: Create a randomly laid out graph, where the edges between the nodes are sensibly created, like roads between cities
- `Reset Graph`: Reset the current layout of nodes and edges
- `Traversal Speed Slider`: Alter the speed at which the traversals occur
- `Traversal Type Drop-Down`: Change the type of traversal algorithm used
- `Traversal Progress Drop-Down`: Change how the traversal occurs <sup>1</sup>
- `Start Node Text Field`: Enter the start node for the traversals
- `End Node Text Field`: Enter the end node for the traversals <sup>2</sup>
- `Start Traversal`: Start the traversal
- `Reset Traversal`: Reset the colouring caused by the last traversal run
- `Step Traversal`: Step the selected traversal currently running by 1 step

Additionally, at the top of the UI beneath the quit button, there are some statistics about the scene, such as node and edge count.

If an error is made in any of the options, a suitable error message will be displayed with what the problem is so that it can be fixed, thus allowing for the traversal to be started.

##  Controls

`Left Click` to create a node

`Middle Click` or `Backspace` to delete a node

`Right Click` to select a node
- `Left Click` a position in the scene to move the selected node to that position
- `Middle Click` or `Backspace` another node that shares an edge to delete the edge
- `Right Click` the same node again to change its label
- `Right Click` another node to create an edge between the 2 nodes
  - If an edge already exists between the 2 nodes, you will be able to change the edge's weight, provided the selected traversal supports edge weights<sup>3</sup>


`Space` to start the selected traversal, if valid conditions for one to start exist, or step through the traversal if the "Stepped" traversal progress option is chosen

`Escape` to quit the program

##  Download

To download this project, open a Command Prompt window in your desired directory and input the following command: <br>

```
git clone https://github.com/Its-Rogue/Graph_Traversal_Simulation
```

## Notes

This program has been created as a project for my A-Level Computer Science NEA. As such, it is more algorithmically focussed rather than visually, however the colour choices are chosen to be as contrasting from one another as possible. These colour choices are explained via a key in the bottom left of the screen, which you can mouse over to get a more in depth hint.

For the UI I have used a scene2D UI [skin](https://github.com/czyzby/gdx-skins/tree/master/shade) created by [Raymond "Raeleus" Buckley](http://www.badlogicgames.com/forum/viewtopic.php?f=22&t=21568), which can be freely used under the CC BY licence.


1:  The traversals can either run automatically with a delay between each step, defined by the Traversal Speed slider, can be stepped manually or will process with no delay between steps of the chosen traversal.</br>
2:  In the case of the bidirectional search, the end node is the secondary or reverse start node.</br>
3:  The suitable algorithms aforementioned are Dijkstra's, A*, and Bellman-Ford, where Bellman-Ford can also take inputs of negative integers.
