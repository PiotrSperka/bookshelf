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
2. Create `application.properties` file and set required settings.
3. Start application. By default, it will be available at port 8080.
4. Follow the creator to create first user.
5. Now you can add more users and start filling up your books' database.

## Configuration
Create `application.properties` file and set configuration:
```
# Put here your application URL
application.url=http://127.0.0.1:8080
application.password.secret=put-here-random-32-characters

# Database configuration - update to your needs
quarkus.datasource.db-kind=mariadb
quarkus.datasource.username=bookshelf
quarkus.datasource.password=some-random-password-to-db
quarkus.datasource.jdbc.url=jdbc:mariadb://bookshelf-db:3306/bookshelf-db
quarkus.datasource.jdbc.max-size=16
quarkus.hibernate-orm.database.generation=update

# Mailer - enter your proper configuration here
quarkus.mailer.from=mailer@yourdomain.com
quarkus.mailer.host=mail.yourdomain.com
quarkus.mailer.login=REQUIRED
quarkus.mailer.password=your-password
quarkus.mailer.port=587
quarkus.mailer.start-tls=REQUIRED
quarkus.mailer.username=mailer@yourdomain.com
quarkus.mailer.mock=false
```

## Docker
Create files and directories:
```
+bookshelf:
  +bin:
    -bookshelf.jar - application JAR
    -application.properties - configuration described above
    -run.sh
  -dockerfile
  -docker-compose.yml
```
run.sh:
```
#!/bin/bash
java -Dquarkus-profile=prod -Dsmallrye.config.locations="/opt/app/" -jar bookshelf.jar
```

dockerfile:
```
FROM eclipse-temurin:17
WORKDIR "/opt/app"
CMD ["sh", "run.sh"]
```

docker-compose.yml:
```
version: '3.1'

services:
  bookshelf:
    build: .
    restart: always
    expose:
      - 8080
    volumes:
      - ./bin:/opt/app

  bookshelf-db:
    image: mariadb:latest
    restart: always
    command: --max_binlog_size=104857600 --expire_logs_days=14
    environment:
      MYSQL_DATABASE: bookshelf-db
      MYSQL_USER: bookshelf
      MYSQL_PASSWORD: some-random-password-to-db
      MYSQL_RANDOM_ROOT_PASSWORD: '1'
      MARIADB_AUTO_UPGRADE: '1'
    volumes:
      - ./mysql:/var/lib/mysql
```

Start the container by running `docker-compose up -d` from `bookshelf` directory.

## Changelog

### v1.0.4
* Updated to Quarkus 3.0.4.Final
* Moved password secret to property
* Added mailer
* Added ability to reset password via e-mail
* Added ability to edit record via double-clicking it
* Added validation to user add/edit dialog
* Added first-time creator (installer)
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