@echo off
echo Starting JavaChat Server on port 5000...
java -cp out server.ChatServer %1
pause
