@tag
Feature: Testing a basic scenario in Test Automation
		
	Scenario: Checking the basic search on Wikipedia
		Given User navigates to "Wikipedia" webpage
		When User enters "Wikipedia_Search" as "automation testing"
		And User clicks on "Search_Button" button
		Then User "can" see "Search_Result_text"
		And User verifies the basic search results
		When User closes the browser
	
	Scenario: Checking the basic search on Wikipedia
		Given User navigates to "Wikipedia" webpage
		When User enters "Wikipedia_Search" as "automation testing"
		And User clicks on "Advanced_Search" button
		When User enters "Not_These_Words" as "GUI"
		And User clicks on "Search_Button" button
		Then User verifies the advanced search results
		When User closes the browser
		

	Scenario: User Adds a pet & validates if its added
		When User adds a pet to the store
		And User gets the pet by the id
		
	Scenario: User Adds a pet & deletes the pet
		When User adds a pet to the store
		#And User gets the pet by the id
		Then User deletes the pet
		
	Scenario: User Adds a pet & places an order
		When User adds a pet to the store
		#And User gets the pet by the id
		And User places an order
		Then User gets the order by id and validates the order

	Scenario: User adds a user from backend
		When User adds a user
		Then User validates if the user is "added"
		When User logins in to the system
		And User logs out from the system

	Scenario: User adds a user and then updates its data
		When User adds a user
		And User updates the user data
		Then User validates if the user is "updated"