#include "../include/StompFrame.h"
#include <map>

using namespace std;

StompFrame::StompFrame() {
    command = "";
    headers = map<string, string>();
    frameBody = "";
}

const string &StompFrame::getCommand() const {
    return command;
}

const string &StompFrame::getFrameBody() const {
    return frameBody;
}

const map<string, string> &StompFrame::getHeaders() const {
    return headers;
}

void StompFrame::setCommand(const string &command) {
    StompFrame::command = command;
}

void StompFrame::setFrameBody(const string &frameBody) {
    StompFrame::frameBody = frameBody;
}

void StompFrame::addHeader(string str1, string str2) {
    StompFrame::headers.insert(pair<string,string>(str1, str2));
}

