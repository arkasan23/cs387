# Project 3 Super Mario

Author: Arsalan Riaz
08/09/2026

## Video

[Mario Video](https://www.dropbox.com/scl/fi/ohflfyh5nyivpj60k698v/recording_2026-08-09_20.48.27.mp4?rlkey=zapn7cgsl05dqaat6bh9351yy&st=nhokndu2&dl=0)

## Description

For this project, I implemented a Mario controller that is driven by a Behavior Tree rather than by one large hard-coded decision method. Mario can move toward visible coins and collectible items, hit nearby question blocks, shoot shootable enemies when Fire Mario is able to fire, attempt to stomp Goombas and Koopas, jump over gaps and obstacles, and avoid dangerous entities such as Spikies. When none of these higher priority behaviors applies, the tree falls back to moving or sprinting to the right so that Mario can continue toward the end of the level. The Behavior Tree is stored outside of the Java controller in src/behavior/mario_bt.xml. The XML contains a priority selector at the root and a set of sequence branches underneath it. The highest-priority branches handle danger and gaps, followed by combat, collection, question blocks, obstacles, jumping, and normal movement. A graphical version of the tree is included in behavior_tree.png. The original src/MyAgent.java was replaced with the Behavior Tree agent entry point. The important code added for the assignment can be found in src/MyAgent.java, the entire src/behavior directory.

## Run

./mario MyAgent -level 6
