# Technician Duty
A school project for Intelligent Systems course.

## What's it about?
Computers don't always work as they should and that's what technicians are here for! If something goes wrong with computer, a technician is assigned to intervene.

## Versions

### v1
Entities act and report their action. Logs are printed in console.

### v2
Simple GUI allowing user to set properties as server/technician/computer count and printing logs.

### v3
Entities are displayed in a grid.

## Model

### ENTITY
- boolean alive
- String name
- Environment environment
- Location location
- boolean reportSelf

### SERVER extends Entity

- List<Technician> technicians
- List<Computer> assignments
- List<AssignmentItem<Computer, Technician>> currentTasks;
- List<Log> logs

### TECHNICIAN extends Entity

- int experience
- boolean available
- Location place
- Computer assignment
- List<WorkLog> workLogs
- Server boss

### COMPUTER extends Entity

- boolean working
- int priority
- boolean assigned
- int age
- List<Log> logs
- Server server