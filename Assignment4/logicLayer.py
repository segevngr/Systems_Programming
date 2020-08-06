from Repository import repo

def MakeEmployeesReport():
    table = []
    e = repo.employees.findAllByName()
    for Employee in e:
        employeeId = Employee.id
        employeeName = Employee.name
        employeeSalary = Employee.salary
        buildingNumberLine = repo.FirstEmployeesReportJoin(employeeId)
        buildingNumber = buildingNumberLine[0]
        tableOfJoin = repo.SecondEmployeesReportJoin(employeeId)
        totalSalesIncome = 0
        for line in tableOfJoin:
            totalSalesIncome += (-1) * line[0] * line[1]
        row = str(employeeName) + " " + str(employeeSalary) + " " + str(buildingNumber) + " " + str(totalSalesIncome)
        table.append(row)
    return table




