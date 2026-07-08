# Steering Behaviors Project 1 Part 2

Author: Arsalan Riaz
07/07/2026

## Video

[SeekObstacleAvoidance](https://www.dropbox.com/scl/fi/w3bnf9k8jzik8wd78xv8x/recording_2026-07-07_22.51.47.mp4?rlkey=mh9uh1842dj8kzjsyj1fv1o4b&st=g0jwv78f&dl=0)

## Description

For this project, I implemented obstacle avoidance for the seek behavior by adding a raycasting system. Instead of only steering directly toward the target marker, the controller projects multiple rays in front of the car at different angles to detect upcoming obstacles. Each ray moves a small rotated rectangle forward in steps and checks for collisions with the obstacles in the game. If an obstacle is detected, the controller adjusts the steering direction to avoid it while continuing to move toward the target whenever the path is clear.

## Compile

make

## SeekObstacleAvoidance Scenario

make seek

## Keyboard Scenario

make keyboard

## Remove all compile files

make clean
