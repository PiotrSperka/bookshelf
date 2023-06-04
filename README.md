# Bookshelf: The book collection database

This is simple web application that allows you to keep track of your books collection.

### How to run
1. Download provided JAR file. Alternatively you can build bookcase-server project using Maven.
2. Create application.properties file and set required settings.
3. Start application. By default, it will be available at port 8080.
4. Create default admin user by running endpoint /api/auth/init. If users table is empty, this will create user named "admin" with password "ChangeMe!".
5. Log in, go to settings and change password. Now you can add more users and start filling up your books database.