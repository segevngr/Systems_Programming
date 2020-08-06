
from DTO import Employee, Supplier, Coffee_stand, Product, Activity

class Employees:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, employee):
        self._conn.execute("INSERT INTO Employees(id, name, salary, coffee_stand) VALUES(?, ?,? ,?) ", (employee.id,employee.name,employee.salary,employee.coffee_stand))

    def find(self, employee_id):
        c = self._conn.cursor()
        c.execute("""
            SELECT id, name FROM Employees WHERE id = ?
        """, [employee_id])

        return Employee(*c.fetchone())

    def findAll(self):
        c = self._conn.cursor()
        all = c.execute("SELECT * FROM Employees order by id ASC")
        return [Employee(*row) for row in all]
    
    def findAllByName(self):
        c = self._conn.cursor()
        all = c.execute("SELECT * FROM Employees order by name ASC")
        return [Employee(*row) for row in all]




class Suppliers:
    def __init__(self, conn):
        self._conn = conn

    def insert(self,supplier):
        self._conn.execute("INSERT INTO Suppliers(id, name, contact_information) VALUES(?, ?, ?) ", (supplier.id, supplier.name, supplier.contact_information))
    def findAll(self):
        c = self._conn.cursor()
        all = c.execute("SELECT * FROM Suppliers order by id ASC")
        return [Supplier(*row) for row in all]

class Products:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, product):
        self._conn.execute("INSERT INTO Products(id,description, price, quantity) VALUES(?, ?,?,?) ",(product.id,product.description,product.price,product.quantity))

    def findQuantityById(self, id):
        c = self._conn.cursor()
        c.execute("SELECT * FROM Products where id=?", [id])
        return Product(*c.fetchone()).quantity

    def UpdateQuantity(self,quantity,id):
        c = self._conn.cursor()
        c.execute("UPDATE Products SET quantity=? where id=?", [quantity, id])
    def findAll(self):
        c = self._conn.cursor()
        all = c.execute("SELECT * FROM Products order by id ASC")
        return [Product(*row) for row in all]


class Coffee_stands:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, coffee_stand):
        self._conn.execute("INSERT INTO Coffee_stands(id, location, number_of_employees) VALUES(?, ?, ?)", (coffee_stand.id, coffee_stand.location, coffee_stand.number_of_employees))
    def findAll(self):
        c = self._conn.cursor()
        all = c.execute("SELECT * FROM Coffee_stands order by id ASC")
        return [Coffee_stand(*row) for row in all]

class Activities:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, activity):
        self._conn.execute("INSERT INTO Activities(product_id,quantity, activator_id, date) VALUES(?, ?,?,?) ", (activity.product_id,activity.quantity,activity.activator_id,activity.date))
    def findAll(self):
        c = self._conn.cursor()
        all = c.execute("SELECT * FROM Activities order by date")
        return [Activity(*row) for row in all]
