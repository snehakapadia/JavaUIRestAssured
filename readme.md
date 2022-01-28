I have created a BDD Cucumber framework for the given tasks.
You can go to config.properties and mention the browser that you want to run tests on, for now I have configured Chrome and Firefox.
Below are paths for important folders:
Feature Files: FeatureFiles
StepDefinations: src/test/java/TestAutomation/TestAutomation
TestRunner: src/test/java/TestAutomation/TestAutomation
I have pom.xml for dependency injection. You can find the pom.xml in the root directory of the project.
Execution Reports will be generated in 'target/cucumber/index.html' (The report is a basic cucumber html report, it can be beautified through plugins).

To Run the tests:
1. Git clone the project on your machine to the desired location.
2. On the Terminal, go to the root directory of the project and execute 'mvn test'. Optionally, we can also execute the TestRunner.java class to execute the tests.

Note: The tests will run in headless mode. If you want to execute the tests in head mode you need to change line number 88/line number 100 (based on the browser selected from config.properties) in Stepdefinations.java to 'options.addArguments("start-maximized"); '
Also, Username and Password as of now I have driven through feature file, however we can take from an external source and also encrypt/decrypt the password as well. I havent done it now due to time constraints.

The backend related are failing as I keep getting 404 response, even after a successful post response. I tried the same manually and manually also I get 404 sometimes and for the same data I get 200 after sometime. There seems to be some problem with the endpoints.
I have choosen BDD Cucumber framework to do both frontend & backend tasks as it will be easy to maintain the code if its in the same codebase.
Since I had a limited amount of time, I had to hardcode a few stuff. These things can be parameterised from an external source and also the data in json file can be parameterised.

Front testcases:

1. Check the basic search functionality of Wikipedia page.
2. Check the advanced search functionality of Wikipedia page using Not These words functionality.
I thought these 2 functionalities are most important functionality and they should work every time, hence I have decided to automate these functionalities and in future we can have these functionalities to execute after every deployment.