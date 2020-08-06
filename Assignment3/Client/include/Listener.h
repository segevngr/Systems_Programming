//
// Created by segevnag@wincs.cs.bgu.ac.il on 15/01/2020.
//

#ifndef ASSIGNMENT3CLIENT_LISTENER_H
#define ASSIGNMENT3CLIENT_LISTENER_H


#include "StompFrame.h"
#include "connectionHandler.h"
#include "User.h"

class Listener {
private:
    ConnectionHandler* handler;
    User* user;
    bool* online;

public:
    Listener(ConnectionHandler *handler, User* user, bool *online);

    void run();

};


#endif //ASSIGNMENT3CLIENT_LISTENER_H
