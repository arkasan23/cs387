# Project 2 Pathfinding

Author: Arsalan Riaz
07/21/2026

## Video

[Pathfinding Video](https://www.dropbox.com/scl/fi/pi44zrjy9c2zx8wupq2hn/recording_2026-07-20_15.15.20.mp4?rlkey=0qlm2y3j57m2e1ceayx9iks8c&st=c5jo9uw5&dl=0)

## Description

For this project, I implemented the A* pathfinding algorithm in the AStar class to allow units in the game to navigate around obstacles and reach their destinations. The algorithm uses open and closed lists, calculates movement and heuristic costs to determine the shortest path, and checks each possible move using the game's collision detection to avoid blocked tiles. Once the goal is reached, the path is reconstructed from the goal back to the start and returned in the correct order, excluding the starting position and including the goal. If no valid path exists, the algorithm returns null. I tested the implementation with both human controlled units and AI versus AI matches to ensure units could navigate the maps correctly.

## Compile

make

## Run Human Vs AI Scenario

make run

## Run Archers AI vs Archers AI Scenario

make archers

## Run Footmen AI vs Footmen AI Scenario

make footmen

## Run Footmen AI vs Archers AI Scenario

make ai
