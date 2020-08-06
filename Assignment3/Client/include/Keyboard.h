#ifndef ASSIGNMENT3CLIENT_KEYBOARD_H
#define ASSIGNMENT3CLIENT_KEYBOARD_H

#include "connectionHandler.h"

using namespace std;

class Keyboard {
private:
    ConnectionHandler* handler;
    User *user;
    bool* online;
    bool waitForInput;

public:

    Keyboard(ConnectionHandler *handler, User *user, bool *online);

    void run();

};


#endif //ASSIGNMENT3CLIENT_KEYBOARD_H
