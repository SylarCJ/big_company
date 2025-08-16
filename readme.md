# Big Company Analysis

This project analyzes an organizational structure from a CSV file.  
It checks two main rules:

1. **Manager Salary Rule**
    - A manager must earn **at least 20% more** than the average salary of their direct subordinates.
    - A manager must earn **no more than 50% more** than that average.

2. **Reporting Line Rule**
    - An employee cannot have more than **4 managers between them and the CEO**.
    - If exceeded, the program reports how many extra levels exist.

---

## 🚀 Build & Run

### Build with Maven
```bash
mvn clean package
```

### Run the Application
```bash
java -jar target/big-company-analysis-1.0-SNAPSHOT.jar target/classes/employees.csv
```
---

## 🧪 Running Tests
To run the tests, use the following command:
```bash
mvn test
```
---
## 📄 CSV File Format
The CSV file should have the following format:
```
id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,30000,124
305,Brett,Hardleaf,34000,300
```
---
## 📊 Example Output

When running the application with the provided CSV file, you will see output similar to this:

```
Manager Salary Rule:
- Manager: Joe Doe, Average Subordinate Salary: $46,000, Manager Salary: $60,000
  - Rule: Compliant
- Manager: Martin Chekov, Average Subordinate Salary: $32,000, Manager Salary: $45,000
  - Rule: Compliant

Reporting Line Rule:
- Employee: Brett Hardleaf, Reporting Line: 4 levels to CEO
  - Rule: Compliant
- Employee: Alice Hasacat, Reporting Line: 2 levels to CEO
  - Rule: Compliant
```
---

## Other Scenarios
You can test the application with different CSV files to see how it handles various organizational structures.
Feel free to modify the `employees.csv` file to create different scenarios and test the rules.

The CSV file for testing is provided below:
```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,90000,
124,Martin,Chekov,40000,123
125,Bob,Ronstad,40000,123
300,Alice,Hasacat,30000,124
305,Brett,Hardleaf,34000,300
126,Bruce,Wayne,40000,125
127,Clark,Kent,40000,126
128,Barry,Alen,30000,127
129,Arthur,Curry,34000,128
```

The output for this scenario will be:
```
[123, Joe, Doe, 90000, ]
[124, Martin, Chekov, 40000, 123]
[125, Bob, Ronstad, 40000, 123]
[300, Alice, Hasacat, 30000, 124]
[305, Brett, Hardleaf, 34000, 300]
[126, Martin, Ronstad, 40000, 125]
[127, Bob, Chekov, 40000, 126]
[128, Alice, Hardleaf, 30000, 127]
[129, Brett, Hasacat, 34000, 128]
Managers with salary less than 120% of average of direct reportees:
Employee{id='300', firstName='Alice', lastName='Hasacat', salary=30000.0, managerId='124'}: lesser 10800.00
Employee{id='125', firstName='Bob', lastName='Ronstad', salary=40000.0, managerId='123'}: lesser 8000.00
Employee{id='126', firstName='Martin', lastName='Ronstad', salary=40000.0, managerId='125'}: lesser 8000.00
Employee{id='128', firstName='Alice', lastName='Hardleaf', salary=30000.0, managerId='127'}: lesser 10800.00
Managers with salary more than 150% of average of direct reportees:
Employee{id='123', firstName='Joe', lastName='Doe', salary=90000.0, managerId='null'}: more 30000.00
Employees with reporting lines exceeding 4 levels:
Employee{id='129', firstName='Brett', lastName='Hasacat', salary=34000.0, managerId='128'}: exceeds by 1
```
---

## 📚 Dependencies
- Java 17 or higher
- Maven
- JUnit for unit testing
---

