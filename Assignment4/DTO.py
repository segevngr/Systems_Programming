
class Employee(object):
    def __init__(self, id, name, salary, coffee_stand):
        self.id = id
        self.name = name
        self.salary = salary
        self.coffee_stand = coffee_stand

    def __str__(self):
        return "({})".format(str(self.id) + ", " + str(self.name)  + ", " + str(self.salary) + ", " + str (self.coffee_stand))


class Supplier(object):
    def __init__(self, id, name, contact_information):
        self.id = id
        self.name = name
        self.contact_information = contact_information

    def __str__(self):
        return "({})".format(str(self.id) + ", " + str(self.name)  + ", " + str(self.contact_information))


class Product(object):
    def __init__(self, id, description, price, quantity):
        self.id = id
        self.description = description
        self.price = price
        self.quantity = quantity

    def __str__(self):
        return "({})".format(str(self.id) + ", " + str(self.description)  + ", " + str(self.price) + ", " + str (self.quantity))

class Coffee_stand(object):
    def __init__(self, id, location, number_of_employees):
        self.id = id
        self.location = location
        self.number_of_employees = number_of_employees

    def __str__(self):
        return "({})".format(str(self.id) + ", " + str(self.location) + ", " + str(self.number_of_employees))

class Activity(object):
    def __init__(self, product_id, quantity, activator_id, date):
        self.product_id = product_id
        self.quantity = quantity
        self.activator_id = activator_id
        self.date = date

    def __str__(self):
        return "({})".format(
            str(self.product_id) + ", " + str(self.quantity) + ", " + str(self.activator_id) + ", " + str(self.date))