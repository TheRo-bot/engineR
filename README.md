# engineR
Java based game engine that's hopefully gonna be dope

To compile this nicely (and exactly how i've done it) you can follow the instructions in the JavaRunner repository :) 

JavaRunner: https://github.com/TheRo-bot/JavaRunner
** CURRENTLY BETA. TO COMPILE:
 - download RUtils (node_rewrite branch) https://github.com/TheRo-bot/RUtils
 - compile RUtils entirely (javac -d <path_to_rutils_output> .\dev\ramar\utils\**.java) (optionally jar it if you know what you're doing)
 - compile engineR with your compiled RUtils code in the PATH (javac -cp <path_to_rutils_output>) 
 - run the Main class: java dev.ramar.e2.Main

WASD to move white, IJKL to move red.
til (`) to open console.

USEFUL COMMANDS:
debug players all highlight
debug players 1 focus // FOCUSES white
debug players 2 focus // FOCUSES red

typicaly, 'list' will tell you what commands you can run, check System.out for the console log, i haven't implemented the console printstream yet sadly!
