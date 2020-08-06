#ifndef SESSION_H_
#define SESSION_H_

#include "../include/json.hpp"
#include <fstream>

#include <vector>
#include <unordered_map>
#include <string>
#include "Action.h"

class User;
class Watchable;

class Session{
public:
    Session(const std::string &configFilePath);
    virtual ~Session();
    void start();
//Copy Constructor
    Session(const Session &other);
//Copy Assignment Operatorauto
     Session& operator=(const Session &other);
//Move Constructor
    Session(Session &&other);
//Move Assignment Operator
    Session& operator=(Session &&other);

    std::unordered_map<std::string, User *> &getUserMap();
    std::string getWord(int input);
    std::vector<BaseAction *> &getActionsLog();
    void setActiveUser(User *activeUser);
    User *getActiveUser() const;
    const std::vector<Watchable *> &getContent() const;
    void setId(int id);
    int getId() const;

private:
    std::vector<Watchable*> content;
    std::vector<BaseAction*> actionsLog;
    std::unordered_map<std::string,User*> userMap;
    User* activeUser;

    void clear();
public:
    std::string input;
    int id;
};
#endif
