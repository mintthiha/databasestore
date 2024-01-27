https://gitlab.com/mintthiha/database2project

- How to setup the database.
    In order to setup the database, the user will just need to run the script that is named 'setup.sql'. This file will automatically create everything needed to run the Java side of the application. It will create the tables, all the packages, triggers and object types. This file will also insert the raw data.

    By any chance, you wish to delete the database, the 'remove.sql' file will delete everything the file 'setup.sql' has created, bringing everything back to 0!

- How to compile and run the Java application.
    In order to do this, it is important to know that this java application SHOULD be able to compile right away without compiler errors. To run the application, the user will need to run the main class, which is the 'App.java' file. The user will be asked afterwards for their username and password, to successfully login into the database. Using the numbers on the keyboard, the user may choose which action to perform. Within those actions, the user will be able to choose actions again, until the program will be able to call a procedure/function to please the user wish. The program is run with a do while loop, so the user may do as many actions as they want before quitting the program.

- ERD assumptions:
    * Foreign keys are procedurally identified by their unique fields. Example: Customer by their email, Product by their name, etc. 
    * The program only works in Canada, so there are no countries table (It is also because all the raw data is in Canada only).
    * The price is assigned to a specific product, so all the stores will have the same minimal price.
    * Cities, Provinces, and Stores are made in separate tables because in future cases they would require their own fields.


Made by Dmitriy Kim (2232931) and Thiha Min Thein (2245136).