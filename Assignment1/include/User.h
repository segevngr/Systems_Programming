#ifndef USER_H_
#define USER_H_

#include <vector>
#include <string>
#include <unordered_set>
#include <unordered_map>
class Watchable;
class Session;

class User{
public:
    User(const std::string& name);
    virtual ~User();
    virtual Watchable* getRecommendation(Session& s) = 0;
    std::string getName() const;
    std::vector<Watchable*> get_history() const;
    void setHistory(Watchable* getInHistory);
    void setName(const std::string &name);
    virtual User* clone() const=0;
    std::vector<Watchable*>& getHistoryByReference();
protected:
    std::vector<Watchable*> history;
    int indexHistory=0;
private:
    std::string name;
    void clear();
};


class LengthRecommenderUser : public User {
public:
    LengthRecommenderUser(const std::string& name);
    virtual Watchable* getRecommendation(Session& s);
    bool  isInHistory(Watchable* toCheck);
    virtual User* clone() const;
private:

};

class RerunRecommenderUser : public User {
public:
    RerunRecommenderUser(const std::string& name);
    virtual Watchable* getRecommendation(Session& s);
    virtual User* clone() const;
private:
};

class GenreRecommenderUser : public User {
public:
    GenreRecommenderUser(const std::string& name);
    virtual Watchable* getRecommendation(Session& s);
    virtual User* clone() const;
    bool  isInHistory(Watchable* toCheck);
private:
};

#endif
