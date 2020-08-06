CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread
# All Targets
all: Client

# Tool invocations
# Executable "main" depends on the files inside bin folder
Client: bin/main.o bin/User.o bin/connectionHandler.o bin/Keyboard.o bin/Listener.o bin/StompFrame.o bin/Book.o bin/Converter.o
	@echo 'Building target: Client'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/Client bin/main.o bin/User.o bin/connectionHandler.o bin/Keyboard.o bin/Listener.o bin/StompFrame.o bin/Book.o bin/Converter.o -lboost_system -lpthread
	@echo 'Finished building target: Client'
	@echo ' '

# Depends on the source and header files
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/connectionHandler.o src/connectionHandler.cpp
bin/User.o: src/User.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/User.o src/User.cpp
bin/Keyboard.o: src/Keyboard.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Keyboard.o src/Keyboard.cpp
bin/main.o: src/main.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/main.o src/main.cpp
bin/Listener.o: src/Listener.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Listener.o src/Listener.cpp
bin/StompFrame.o: src/StompFrame.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/StompFrame.o src/StompFrame.cpp
bin/Book.o: src/Book.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Book.o src/Book.cpp
bin/Converter.o: src/Converter.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Converter.o src/Converter.cpp

#Clean the build directory
clean:
	rm -f bin/*
