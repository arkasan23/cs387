# Project 4 Procedural Content Generation

Author: Arsalan Riaz
08/28/2026

## Video

[Content Generation Video](https://www.dropbox.com/scl/fi/om69ny097zopi5bjhanc5/recording_2026-08-28_22.54.04.mp4?rlkey=9e2yqsf1cmdl180rkvnmcuu1p&st=i0g8ogmk&dl=0)

## Description

For this project, I made a procedural map generator for S3 using a 2D Cellular Automata algorithim. The generator is written in Python and creates a new map.xml file that the S3 game can load. It starts off by making a grid with grass, trees, and water placed kinda randomly. After that, the cellular automata goes through the map a few times and checks the cells around each spot. I used the Moore neighborhood, so it looks at the 8 cells around the current one. Depending on how many trees or water tiles are nearby, the cell can stay the same or change. Doing this a few times makes the map look more natural instead of just looking like a bunch of random tiles everywhere.

I also added some extra stuff to make sure the map is actually playable and not just random. The map is mirrored, so one side is copied to the other side. This makes it more fair since both players get about the same terrain. Each player starts with a town hall, a peasant, and a gold mine close to them. I also added two more gold mines closer to the middle of the map so both players have a reason to move out and expand. After the terrain is generated, the program clears space around the bases, units, and gold mines so trees or water dont spawn on top of anything important. It also makes sure there is enough open area for players to build stuff near their starting base.

The generator also does some testing after it makes the map. It checks if each player can actually reach their gold mine, the extra gold mines, and the other players base. This is important because the Cellular Automata can sometimes make big groups of trees or water that completly block off parts of the map. If the generated map fails one of these checks, the program just throws that version away and generates another one. I also used a random seed, so if I use the same seed again it will generate the exact same map. If I change the seed, it makes a different map, which keeps it varied while still being reproducable. I changed Main.java so the game loads maps/map.xml by default, and I also updated the Makefile so I can generate the map, compile the game, and run it with a few simple commands.

## Generate Map

make map

## Generate Map with Seed

make map SEED=12345

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
