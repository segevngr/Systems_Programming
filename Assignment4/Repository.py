import sqlite3
import atexit
from DAO import Employees
from DAO import Suppliers
from DAO import Products
from DAO import Coffee_stands
from DAO import Activities
import os
class _Repository(object):
    def __init__(self):
        self._dbcon = sqlite3.connect('moncafe.db')
        self.employees = Employees(self._dbcon)
        self.suppliers = Suppliers(self._dbcon)
        self.products = Products(self._dbcon)
        self.coffee_stands = Coffee_stands(self._dbcon)
        self.activities = Activities(self._dbcon)

    def _close(self):
        self._dbcon.commit()
        self._dbcon.close()

    def create_tables(self):
        self._close()
        os.remove('moncafe.db')
        self.__init__()

        self._dbcon.execute("""CREATE TABLE Employees(
                          id      INTEGER     PRIMARY KEY ,
                          name    TEXT        NOT NULL ,
                          salary  TEXT        REAL NOT NULL,
                          coffee_stand INTEGER REFERENCES Coffee_stand(id));""")
        self._dbcon.execute("""CREATE TABLE Suppliers(
                                      id              INTEGER PRIMARY KEY,
                                      name       TEXT    NOT NULL,
                                      contact_information TEXT
                                      );""")
        self._dbcon.execute("""CREATE TABLE Products(
                                     id              INTEGER PRIMARY KEY,
                                     description TEXT   NOT NULL,
                                     price     REAL  NOT NULL ,
                                     quantity   INTEGER NOT NULL);""")
        self._dbcon.execute("""CREATE TABLE Coffee_stands(
                                                id              INTEGER PRIMARY KEY,
                                                location TEXT   NOT NULL,
                                                number_of_employees     INTEGER);""")
        self._dbcon.execute("""CREATE TABLE Activities(
                          product_id              INTEGER INTEGER REFERENCES Product(id),
                          quantity       INTEGER NOT NULL,
                          activator_id   INTEGER NOT NULL,
                          date DATE  NOT  NULL 
                          );""")
    def FirstEmployeesReportJoin(self,employeeId) :
        c = self._dbcon.cursor()
        c.execute("SELECT location FROM Coffee_stands INNER JOIN Employees ON Coffee_stands.id = Employees.Coffee_stand where Employees.id=?",[employeeId])
        return c.fetchone()
    def SecondEmployeesReportJoin(self,employeeId):
        c = self._dbcon.cursor()
        c.execute("SELECT Activities.quantity,price FROM Activities INNER JOIN Products ON Activities.product_id = Products.id where activator_id=?",[employeeId])
        return c.fetchall()
    def ActivitiesReportJoin(self):
        c = self._dbcon.cursor()
        c.execute("SELECT date,Products.description, Activities.quantity, Employees.name ,Suppliers.name FROM Activities LEFT JOIN Products ON  Activities.product_id = Products.id  "
                  "LEFT JOIN Employees ON Activities.activator_id= Employees.id LEFT JOIN Suppliers ON Suppliers.id= activator_id order By date ASC ")
        return c.fetchall()


repo = _Repository()
atexit.register(repo._close)