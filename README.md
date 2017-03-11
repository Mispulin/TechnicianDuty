# Technician Duty
A school project for Intelligent Systems course.

##What's it about?
Computers don't always work as they should and that's what technicians are here for! If something goes wrong with computer, a technician is assigned to repair or replace it.

##Model

###TECHNICIAN
Working technician is blue, not working is grey.

- skillLevel (affects time needed to repair)
- isAvailable (if he's repairing or free)
- age
- workLog (what is he doing, for how long etc)

###COMPUTER
Working computer is green, not working properly is red.

- state (working or not working)
- priority
- difficulty (affect time needed for repair)
- isAssigned (is somebody coming to work on it)
- age