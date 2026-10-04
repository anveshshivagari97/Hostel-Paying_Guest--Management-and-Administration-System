\# Hostel Management and Administration System



A Java-based console application designed to manage hostel operations such as student registration, room allocation, fee payments, expenses, vacancies, and student checkout.



\## Project Overview



The \*\*Hostel Management and Administration System\*\* helps hostel administrators manage day-to-day hostel operations through a simple console-based application.



The application uses \*\*Core Java\*\* for application logic and \*\*JDBC with MySQL\*\* for persistent data storage.



\## Features



\* User signup and login

\* Hostel configuration

\* Student registration and management

\* Room configuration based on sharing type

\* Room capacity and vacancy tracking

\* Student fee payment management

\* Outstanding fee tracking

\* Hostel income tracking

\* Hostel expenditure management

\* Student checkout management

\* Automatic room vacancy restoration after checkout

\* MySQL database persistence

\* Dashboard with hostel summary



\## Technologies Used



\* Java

\* Object-Oriented Programming (OOP)

\* Collections Framework

\* JDBC

\* MySQL

\* SQL

\* Git

\* GitHub

\* Eclipse IDE



\## Project Architecture



```text

Console User Interface

&#x20;       |

&#x20;       v

Hostel Management Application

&#x20;       |

&#x20;       v

StudentDAO

&#x20;       |

&#x20;       v

JDBC

&#x20;       |

&#x20;       v

MySQL Database

```



\## Database Tables



The project uses a MySQL database named:



```text

hostel\_management

```



Main tables:



\* `users`

\* `students`

\* `rooms`

\* `payments`

\* `expenses`



\## Key Modules



\### Student Management



The application allows the administrator to:



\* Register new students

\* View student details

\* Track active students

\* Track student fee information

\* Checkout students



\### Room Management



Rooms can be configured based on:



\* 4-sharing

\* 3-sharing

\* 2-sharing

\* 1-sharing



The system calculates room capacity and vacancy based on the configured sharing type.



\### Fee Management



The system maintains:



\* Total fee

\* Amount paid

\* Outstanding balance

\* Individual payment records



Fee payments are stored in the MySQL database using JDBC transactions.



\### Expense Management



The administrator can record hostel expenses such as:



\* Groceries

\* Vegetables

\* Electricity bills

\* Water bills

\* Maintenance

\* Salaries

\* Other expenses



\### Student Checkout



Before checkout, the system checks whether the student has any outstanding balance.



After successful checkout:



\* Student status is changed to inactive

\* Room occupancy is reduced

\* Room vacancy is restored

\* Checkout status is saved in MySQL



\## Project Structure



```text

MyProjects

|

├── .gitignore

├── README.md

└── src

&#x20;   └── hms

&#x20;       ├── DBConnection.java

&#x20;       ├── HostelManagementAndAdministrationSystem.java

&#x20;       └── StudentDAO.java

```



\## How to Run



\### 1. Requirements



Install:



\* Java JDK

\* MySQL Server

\* MySQL Workbench

\* Eclipse IDE

\* MySQL Connector/J



\### 2. Create the Database



Create a MySQL database named:



```sql

CREATE DATABASE hostel\_management;

USE hostel\_management;

```



Create the required tables using the SQL scripts included in the project setup.



\### 3. Configure Database Connection



Open:



```text

DBConnection.java

```



Update the MySQL username and password:



```java

private static final String USER = "root";

private static final String PASSWORD = "YOUR\_MYSQL\_PASSWORD";

```



\### 4. Add MySQL Connector/J



Add the MySQL Connector/J JAR file to the Eclipse project's build path.



\### 5. Run the Application



Run:



```text

HostelManagementAndAdministrationSystem.java

```



The application starts with the login/signup flow and then provides access to the hostel management dashboard.



\## Concepts Demonstrated



This project demonstrates practical knowledge of:



\* Classes and Objects

\* Encapsulation

\* Static and instance members

\* Collections

\* ArrayList

\* HashMap

\* Exception handling

\* JDBC

\* PreparedStatement

\* ResultSet

\* SQL queries

\* Database transactions

\* CRUD operations

\* Foreign keys

\* MySQL database connectivity

\* Git and GitHub



\## Future Enhancements



Possible future improvements include:



\* Spring Boot REST API

\* Web-based frontend

\* Role-based authentication

\* Online payment integration

\* Advanced reporting

\* Better validation and error handling

\* Deployment as a web application



\## Author



\*\*Anvesh Shivagari\*\*



Java Full Stack Developer — Fresher



