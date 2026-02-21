@echo off
echo Starting JavaChat Client...
echo Connecting to localhost:5000
java -cp out client.ChatClient %1 %2
pause
