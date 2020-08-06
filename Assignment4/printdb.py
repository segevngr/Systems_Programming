import sys
from Repository import repo
import logicLayer

def printDB():
    print("Activities")
    for line in repo.activities.findAll():
        print(line)

    print("Coffee stands")
    for line in repo.coffee_stands.findAll():
        print(str(line))

    print("Employees")
    for line in repo.employees.findAll():
        print(str(line))

    print("Products")
    for line in repo.products.findAll():
        print(str(line))

    print("Suppliers")
    for line in repo.suppliers.findAll():
        print(str(line))

    print("")
    print("Employees report")
    for line in logicLayer.MakeEmployeesReport():
        print(str(line).replace("'", ""))

    print("")
    
    print("Activities")
    for line in repo.ActivitiesReportJoin():
        print(str(line).replace('"', ""))

    print("")

if __name__ == '__main__':
    printDB()
