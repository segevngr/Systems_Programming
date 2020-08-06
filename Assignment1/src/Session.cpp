#include <iostream>
#include "../include/Session.h"
#include "../include/Watchable.h"
#include "../include/User.h"
#include <vector>
#include <sstream>

using namespace std;
//Constructor
Session::Session(const std::string &configFilePath):content(), actionsLog(), userMap(), activeUser()
,input(),id() {
    // read JSON file
    using json = nlohmann::json;
    std::ifstream in(configFilePath);
    json j;
    in >> j;
    int watchableID = 1;
    for (int unsigned i = 0; i <j["movies"].size(); ++i) {
        string movieName(j["movies"][i]["name"]);
        int length(j["movies"][i]["length"]);
        vector<string> tags = j["movies"][i]["tags"];
        Watchable* watchMovie = new Movie(watchableID,movieName,length,tags);
        content.push_back(watchMovie);
        watchableID = watchableID + 1;
    }

    for (int unsigned k = 0; k < j["tv_series"].size(); ++k) {
        string episodeName(j["tv_series"][k]["name"]);
        int length(j["tv_series"][k]["episode_length"]);
        vector<string> tags = j["tv_series"][k]["tags"];
        for (int unsigned i = 0; i < j["tv_series"][k]["seasons"].size(); ++i) {
            int numOfEpisodes = j["tv_series"][k]["seasons"][i];
            for (int l = 0; l < numOfEpisodes; ++l) {
                Watchable* watchEpisode = new Episode(watchableID,episodeName,length,i+1,l+1,tags);
                content.push_back(watchEpisode);
                watchableID = watchableID+1;
            }
        }
    }
    activeUser = new LengthRecommenderUser("default");
    userMap["default"] = activeUser;
}
//Destructor
Session::~Session() {
    clear();
}
void Session::clear() {
    for(Watchable* watch : content){
        delete watch;
    }
    content.clear();
    for(BaseAction* action : actionsLog){
        delete action;
    }
    actionsLog.clear();
    for(auto USER : userMap){
        delete USER.second;
    }
    userMap.clear();
    activeUser = nullptr;
}
//Copy Constructor
Session::Session(const Session &other):content(), actionsLog(), userMap(), activeUser()
,input(),id() {
    for(Watchable* w : other.content){
        content.push_back(w->clone());
    }
    for(BaseAction* ba : other.actionsLog){
        actionsLog.push_back(ba->clone());
    }
    for (auto & USER : other.userMap){
        string name = USER.first;
        userMap.insert({name,USER.second->clone()});
        for(auto & w : USER.second->getHistoryByReference()){
            string name = USER.first;
            userMap.find(name)->second->getHistoryByReference().push_back(content[w->getId()]);
        }
    }
    string userName = other.activeUser->getName();
    activeUser = userMap.at(userName);
}
//Move Constructor
Session::Session(Session &&other):content(other.content), actionsLog(other.actionsLog), userMap(other.userMap), activeUser(other.activeUser)
,input(other.input),id(other.id){
    clear();
}
//Copy Assignment Operator
Session &Session::operator=(const Session &other){
    if(this!= &other){
        clear();
        activeUser = userMap[other.activeUser->getName()];
        for (auto USER = other.userMap.begin(); USER != other.userMap.end(); ++USER){
            string name = USER->first;
            userMap.insert({name,USER->second->clone()});
            for(auto & w : USER->second->getHistoryByReference()){
                string name = USER->first;
                userMap.find(name)->second->getHistoryByReference().push_back(content[w->getId()]);
            }
        }
        for (int unsigned i = 0; i < other.actionsLog.size(); ++i) {
            BaseAction* BA = other.actionsLog[i]->clone();
            actionsLog.push_back(BA);
        }
        for (int unsigned j = 0; j < other.content.size(); ++j) {
            Watchable* W = other.content[j]->clone();
            content.push_back(W);
        }
    }
    return *this;
}
//Move Assignment Operator
Session &Session::operator=(Session &&other) {
    if(this!= &other){
        clear();
        content = other.content;
        actionsLog = other.actionsLog;
        userMap = other.userMap;
        activeUser = other.activeUser;
    }
    return *this;
}

void Session::start() {
    cout << "SPLFLIX is now on!" <<endl;
    getline(std::cin,input);
    BaseAction* classUser;
    while(input!="exit"){
        string action = getWord(1);
        if(action == "createuser"){
            classUser = new CreateUser;
            classUser->act(*this);
        }
        if(action == "changeuser"){
            classUser = new ChangeActiveUser;
            classUser->act(*this);
        }
        if(action == "deleteuser"){
            classUser = new DeleteUser;
            classUser->act(*this);
        }
        if(action == "dupuser"){
            classUser = new DuplicateUser;
            classUser->act(*this);
        }
        if(action == "content"){
            classUser = new PrintContentList;
            classUser->act(*this);
        }
        if(action == "watchhist"){
            classUser = new PrintWatchHistory;
            classUser->act(*this);
        }
        if(action == "watch"){
            string id = this->getWord(2);
            stringstream(id) >> this->id;
            classUser = new Watch;
            classUser->act(*this);
        }
        if(action == "log"){
            classUser = new PrintActionsLog;
            classUser->act(*this);
        }
        if(action == "exit"){
            classUser = new Exit;
            classUser->act(*this);
        }
        getline(std::cin,input);
    }
}


unordered_map<std::string, User*> &Session::getUserMap(){
    return userMap;
}

std::string Session::getWord(int wordNum) {
    int space;
    string word = input;
    while(wordNum!=1){
        space = word.find(" ");
        word = word.substr(space+1);
        wordNum--;
    }
    int space2 = word.find(" ");
    if(unsigned (space2) != (std::string::npos)) {
        word = word.substr(0, space2);
    }
    return word;
}

vector<BaseAction *> &Session::getActionsLog(){
    return actionsLog;
}

void Session::setActiveUser(User *activeUser) {
    Session::activeUser = activeUser;
}

const vector<Watchable *> &Session::getContent() const {
    return content;
}

User *Session::getActiveUser() const {
    return activeUser;
}

int Session::getId() const {
    return id;
}

void Session::setId(int id) {
    Session::id = id;
}














