# ApplicationsDashboard [![Build Status](https://travis-ci.org/72MiguelGomes/ApplicationsDashboard.svg?branch=master)](https://travis-ci.org/72MiguelGomes/ApplicationsDashboard) [![codecov](https://codecov.io/gh/72MiguelGomes/ApplicationsDashboard/branch/master/graph/badge.svg)](https://codecov.io/gh/72MiguelGomes/ApplicationsDashboard)

Application responsible to validate the healthy of services.

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

## Requirements

- Maven
- Java 1.8

#### Lombok Support

This project uses [lombok](https://projectlombok.org/) in order to make entities clean. To make the development easier install 
lombok support on your IDE.