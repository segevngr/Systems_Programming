import sys

from DTO import Activity
from Repository import repo
import  printdb
def main(args):

        inputfilename = args[1]

        with open(inputfilename) as inputfile:

                for line in inputfile:
                    line = line.strip('\n')
                    args = line.split(',')
                    lineProduct_id =args[0]
                    actionQuantity =args[1]

                    prodQuantity = repo.products.findQuantityById(lineProduct_id)
                    updatedQuantity = prodQuantity + int(actionQuantity)

                    if updatedQuantity>=0:
                      repo.products.UpdateQuantity(updatedQuantity,lineProduct_id)
                      currActivity = Activity(lineProduct_id, actionQuantity, args[2], args[3])
                      repo.activities.insert(currActivity)


        printdb.printDB()
if __name__ == '__main__':
    main(sys.argv)

