//
// Created by levyak@wincs.cs.bgu.ac.il on 15/01/2020.
//

#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <thread>
#include "../include/connectionHandler.h"
#include "../include/User.h"
#include "../include/StompFrame.h"
#include "../include/Keyboard.h"
#include "../include/Listener.h"
#include "../include/Converter.h"

using namespace boost;
using namespace std;


int main(int argc, char *argv[]) {

    ConnectionHandler *handler = nullptr;
    User *user = nullptr;
    Converter *converter = new Converter;

    while (handler == nullptr) {
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        vector<string> input = converter->stringToVec(line, ' ');

        if (input[0] == "login") {

            vector<string> hostPort = converter->stringToVec(input[1], ':');

            for (auto it = hostPort.begin(); it != hostPort.end(); ++it)
                cout << *it << '\n';

            string host = hostPort[0];
            short port = atoi(hostPort[1].c_str());

            handler = new ConnectionHandler(host, port);

            if (!handler->connect()) {
                cerr << "Could not connect to server";
                return 1;
            }

            string username = input[2];
            string password = input[3];
            string hostname = boost::asio::ip::host_name();

            StompFrame *connectFrame = new StompFrame();
            connectFrame->setCommand("CONNECT");
            connectFrame->addHeader("accept-version", "1.2");
            connectFrame->addHeader("host", hostname);
            connectFrame->addHeader("login", username);
            connectFrame->addHeader("passcode", password);
            string connectString = converter->StompFrameToString(*connectFrame);


            if (!handler->sendLine(connectString)) {
                cout << "Disconnected. Exiting...\n" << endl;
                break;
            }

            user = new User(username);
        }
    }
    string answer;


    if (handler->getLine(answer)) {
        StompFrame *answerFrame = converter->stringToStompFrame(answer);

        if (answerFrame->getCommand() == "CONNECTED") {
            cout << "Login successful\n";
            bool *online = new bool(true);
            Keyboard *keyboard = new Keyboard(handler, user, online);
            Listener *fromServer = new Listener(handler, user, online);
            thread keyboardThread(&Keyboard::run, keyboard);
            thread fromServerThread(&Listener::run, fromServer);
            keyboardThread.join();
            fromServerThread.join();
            delete(user);
            delete(handler);

        } else if (answerFrame->getCommand() == "ERROR"){
            cerr << answerFrame->getFrameBody() << endl;
        }
        //TODO: delete objects

        return 0;
    }
}