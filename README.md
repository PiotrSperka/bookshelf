# Bookshelf: The book collection database

This is a simple web application that allows you to keep track of your books collection.
Can be used by multiple users with different credentials.

## Most important frameworks and libraries
* Java 17
* Quarkus 3
* React
* MUI

## How to run
1. Download provided JAR file. Alternatively you can build bookcase-server project using Maven.
2. Create application.properties file and set required settings.
3. Start application. By default, it will be available at port 8080.
4. Create default admin user by running endpoint /api/auth/init. If users table is empty, this will create user named "admin" with password "ChangeMe!".
5. Log in, go to settings and change password. Now you can add more users and start filling up your books database.

## Configuration
TODO

## Docker
TODO

## Changelog

### v1.0.4
* Updated to Quarkus 3.0.4.Final
* Moved password secret to property
* Added mailer
* Added ability to reset password via e-mail
* Added ability to edit record via double-clicking it
* Minor improvements

### v1.0.3
* Slight webfront refactoring and reformatting
* Refactored Java package name
* Added validation when adding and editing book
* Improved admin panel
* Added ability to view and restore deleted books
* Minor improvements

### v1.0.2
* Updated to Quarkus 2.16.7
* Added ability to disable user
* Fixed responsiveness on mobile devices
* Added background to login form
* Fixed multiple calls to backend when page was refreshed
* Minor fixes

### v1.0.1
* Fixed book grid refresh after logging in
* Added JWT blacklist (logout), and refresh
* Added simple log to database
* Added log viewer in admin panel
* Book service and resource refactoring