//
// Created by omrigil@wincs.cs.bgu.ac.il on 14/01/2020.
//

#ifndef ASSIGNMENT3CLIENT_STOMPFRAME_H
#define ASSIGNMENT3CLIENT_STOMPFRAME_H
#include <iostream>
#include <string>
#include <map>

using namespace std;

class StompFrame {
private:
    string command;
    map<string, string> headers;
    string frameBody;
public:

    StompFrame();

    const string &getCommand() const;

    const string &getFrameBody() const;

    const map<string, string> &getHeaders() const;

    void setCommand(const string &command);

    void setFrameBody(const string &frameBody);

    void addHeader(string str1, string str2);

};


#endif //ASSIGNMENT3CLIENT_STOMPFRAME_H
