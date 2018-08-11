# ApplicationsDashboard [![Build Status](https://travis-ci.org/72MiguelGomes/ApplicationsDashboard.svg?branch=master)](https://travis-ci.org/72MiguelGomes/ApplicationsDashboard) [![codecov](https://codecov.io/gh/72MiguelGomes/ApplicationsDashboard/branch/master/graph/badge.svg)](https://codecov.io/gh/72MiguelGomes/ApplicationsDashboard)

Application responsible to validate the healthy of services.

## Start Application

This application has 2 modes, the API and the Web view.

### API

Expose a rest API to configure services.

#### Start Command

```bash
cd ApplicationsDashboard-Starter; mvn spring-boot:run
```

### WebView

Application with a web interface

#### Start Command

```bash
cd ApplicationsDashboard-Starter-Web; mvn spring-boot:run
```

## Modules

Quick explanation regarding modules responsibility.

### ApplicationDashboard-Core

Main modules, contains all business logic and where all work flows are defined.

This modules should contains the minimum number of external dependencies possible (frameworks, ...).
And must not depend on any other module on this application.  

### ApplicationDashboard-Database

This module depends on ``ApplicationDashboard-Core`` and must implement the repository interfaces.

The main responsibility is persist and retrieve data. 

### ApplicationDashboard-Api

Module responsible for expose the functionalities throw a REST interface. Must only contains presentation logic.

### ApplicationDashboard-Starter

This module is responsible for glue all modules in order to run the application.

### ApplicationDashboard-Web

Responsible for create server side rendering. Will serve an visual interface for the application.

### ApplicationDashboard-Starter-Web

This module is responsible for glue all modules in order to run the application with the web view interface.
It is very similar to `ApplicationDashboard-Starter` although it replaces the API for the web view.

## Requirements

- Maven
- Java 1.8

#### Lombok Support

This project uses [lombok](https://projectlombok.org/) in order to make entities clean. To make the development easier install 
lombok support on your IDE.