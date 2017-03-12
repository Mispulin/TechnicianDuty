# Technician Duty
A school project for Intelligent Systems course.

##What's it about?
Computers don't always work as they should and that's what technicians are here for! If something goes wrong with computer, a technician is assigned to intervene.

##Model

###ENTITY
- alive
- name
- environment
- location
- reportSelf

###TECHNICIAN extends Entity

- experience
- available
- place
- assignment
- workLogs
- boss

###COMPUTER extends Entity

- working
- priority
- assigned
- age
- logs
- server

###SERVER extends Entity

- technicians
- assignments
- logs