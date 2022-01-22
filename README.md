# engineR
Java based game engine that's hopefully gonna be dope

To compile this nicely (and exactly how i've done it) you can follow the instructions in the JavaRunner repository :) 

** Current work is dedicated to the demo_combat branch, i'm making a combat demo babyyyyy

JavaRunner: https://github.com/TheRo-bot/JavaRunner
** CURRENTLY BETA. TO COMPILE:
 - download RUtils (node_rewrite branch) https://github.com/TheRo-bot/RUtils
 - combine the two /dev/... files together from engineR and RUtils 
 - compile: `javac ./dev/ramar/e2/Main.java`
 - run: `java dev.ramar.e2.Main`
   ** NOTE: make sure your console has access to a window manager (powershell for windows, you know what you're doing with \*nix systems)

WASD to move white, IJKL to move red.
til (\`) to open console.

USEFUL COMMANDS:
debug players all highlight
debug players 1 focus // FOCUSES white
debug players 2 focus // FOCUSES red

typically, 'list' will tell you what commands you can run, check System.out for the console log, i haven't implemented the console printstream yet sadly!
