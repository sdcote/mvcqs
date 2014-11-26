Overview
--------
The purpose of this project is to enable the quick creation of a web 
application using the Spring framework.

It was created to address the frequently re-occurring need for a quick and 
simple web service or a platform independent UI to perform some simple task.

Unlike the BOSS project which is a template for back office services, this 
template is intended to provide a front-end service environment with an 
emphasis on HTML via MVC.

It is a fully operational web application with authentication, authorization 
and auditing built in.

The system uses a responsive design for phones, tablets and desktop access, 
courtesy of Bootstrap.

Login profile management is also included. 

Project Structure
-----------------
src - all source code
	main/java - backend java source code
	main/webapp/css,img,js,static - static content
	main/webapp/WEB-INF/views - template sources that generic dynamic views
	test/ - test code
	
To build a deployable web app archive (.war):
---------------------------------------------
./gradlew build
See build/libs/webapp-tmpl.war

To run the web app:
-------------------
./gradlew jettyRun
Point your preferred web browser at http://localhost/mvcqs

Easier still, just execute the *run* command to build and run the project in 
a matter of seconds.