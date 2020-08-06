
#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include "../include/Converter.h"
#include "../include/StompFrame.h"

using namespace std;

vector<string> Converter::stringToVec(string str, char delim) {
    vector <string> msgLines;
    stringstream ss(str);
    string item;
    while (getline (ss, item, delim))
        msgLines.push_back (item);
    return msgLines;
}

StompFrame* Converter::stringToStompFrame(string msg) {
    StompFrame *frame = new StompFrame();
    vector<string> vec = vector<string>();
    vec = stringToVec(msg, '\n');
    frame->setCommand(vec[0]);
    int i = 1;
    vector<string> header = vector<string>();

    while (!vec[i].empty()) {
        header = stringToVec(vec[i], ':');
        frame-> addHeader(header[0], header[1]);
        i++;
    }
    frame->setFrameBody(vec[i+1]);// not problematic
    return frame;
}

string Converter::StompFrameToString(StompFrame frame) {
    string output = "";
    output = output +frame.getCommand()  +"\n";
    map<string, string> headers = frame.getHeaders();
    for(map<string,string>::iterator it = headers.begin(); it != headers.end(); ++it)
        output = output +it->first +":" +it->second +"\n";
    if(frame.getFrameBody()!= "")
        output = output +"\n" +frame.getFrameBody();
    return output;
}
