Code Structure:


1. Initialize array lists.

2. Read CIM EQ profile into Node List.

3. Read CIM SSH profile into Node List.

4. Extract EQ profile node list into database.

5. Extract SSH profile node list into database.

6. Augment EQ objects with SSH data.

7. Create Y-Bus matrix.

8. Build SQL database (OBS: Comment this line to work without SQL for debugging purposes!).

9. Print Y-Bus matrix.

10. Return Y-Bus list to display it in the GUI.

Main Java File:
GUI.java (front-end of the Assignment_I.java class)



XML files used:
 
Reduced model:

	- Assignment_EQ_reduced.xml
	
- Assignment_SSH_reduced.xml

2. 

Reduced modified model:

	- Assignment_EQ_reduced.xml

	- Assignment_SSH_reduced_mod.xml

2. 
Extended model:

	- MicroGridTestConfiguration_T1_BE_EQ_V2.xml

	- MicroGridTestConfiguration_T1_BE_SSH_V2.xml



Link to video:
https://youtu.be/lInBSSg9ZGs



Included there is also a stand-alone runnable java archive (runnable jar).
	