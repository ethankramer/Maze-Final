# Maze-Final
A random maze generator with solution.

The file maze.java is a simple maze generator that takes advantage of path compression and a Union-by-Height function to
efficiently generate random (square) mazes of sizes 10x10, 20x20, and 30x30 with the solution to the maze shown by a path
of 'X'. The mazes are generated in such a way that there will always only be one unique solution, and there are no loops
(or cycles) in the maze. The solution is found using depth-first search on the generated maze, by traversing each possible
path, eventually finding the one correct path that goes from the entrance ('Start') to the exit ('End').

After the initial mazes (10x10, 20x20, and 30x30) are generated and displayed, the console will prompt the user for another
input. This input will offer the user to generate another square maze using the user's input.
   
   - For example, if the user inputs 57, the program will generate a 57x57 maze and display it with the solution.
   
After the user's input and maze is shown, the program will finish.

The summary file provided gives an example of what the mazes of each size (10x10, 20x20, and 30x30) will look like once displayed.
