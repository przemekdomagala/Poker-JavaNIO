# Poker-JavaNIO
#### Poker game implemented in Java with usage of NIO Library.

# About:
### It's a poker game for 4 players. Communication between server and client is based on passing tokens such as: PlayerID, ActionType and Action Parameters.
###### Currently game has no GUI and only way to play is with 4 clients.


#### Project Structure:
- Server module contains source code of game server and server manager responsible for answering to clients tokens.
- Client module contains source code of client (player) and is responsible for sending requests and data to game server.
- Model contains business model of game.
- Common is utils module.


Current version of project: Beta
