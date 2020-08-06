import sys
from Repository import repo
from DTO import Supplier, Coffee_stand
from DTO import Product
from DTO import Employee

def main(args):
        repo.create_tables()
        inputfilename = args[1]
        with open(inputfilename) as inputfile:

            for line in inputfile:
                line = line.strip('\n')
                args = line.split(', ')
                orginal2= args[2]
                orginal3 = args[3]
                args[1] = str(args[1]).replace(" ", "")
                args[2] = str(args[2]).replace(" ", "")
                args[3] = str(args[3]).replace(" ", "")
                if args[0] == "C":
                    currCoffee_stand = Coffee_stand(args[1], "'{}'".format(args[2]), args[3])
                    repo.coffee_stands.insert(currCoffee_stand)
                if args[0] == "S":
                    args[3] = orginal3
                    currSupplier = Supplier(args[1],"'{}'".format(args[2]),"'{}'".format(args[3]))
                    repo.suppliers.insert(currSupplier)
                if args[0] == "E":
                    args[4] = str(args[4]).replace(" ", "")
                    currEmployee = Employee(args[1],args[2],args[3],args[4])
                    repo.employees.insert(currEmployee)
                if args[0] == "P":
                    args[2]=orginal2
                    currProduct = Product(args[1], "'{}'".format(args[2]), args[3], 0)
                    repo.products.insert(currProduct)


if __name__ == '__main__':
    main(sys.argv)
