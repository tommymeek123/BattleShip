CS 465 Computer Networks
Western Carolina University
November 2020
Project 3 "BattleShip"
Gatlin Cruz and Tommy Meek

A classic game of Battleship. Multiple players are allowed. Players attack 
their opponents' grids in hopes of eleminating rival armadas. The last player 
standing wins! A list of commands is below:

'/play' to start the game once at least 2 players have joined.

'/attack <username> <[0-9]+> <[0-9]+>' to attack another player's grid.

'/quit' to surrender.

'/show <username>' to display a player's grid to the console. If the player 
shows herself, she will see all her ships. If a player shows an enemy, she 
only sees hits and misses.



usage:

extraction:
tar -xzvf battleship.tar.gz

compilation:
cd BattleShip/
mkdir out/
javac -d out/ src/server/*.java src/client/*.java src/common/*.java

running the server:
java -cp out/ server.BattleServerDriver <port> [gridsize]

running the client:
java -cp out/ client.BattleClientDriver <host> <port> <username>
