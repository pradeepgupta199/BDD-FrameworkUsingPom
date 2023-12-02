Feature: Login

Scenario: Search for the cheapest flight
Given I visit https://www.makemytrip.com/ website 
And I do a signup to login into the  account
And I click FLIGHTS
And I select from London City Arpt
And I select to Dubai Intl 
And I select Return Trip

#For the below date select, please use the the date picker And I select departure date 2
# weeks from today's date And I select return date 2 weeks from departure date

And I select 2 Adult
And I select 2 Child and I select the Premium economy class
When I click SEARCH button

And I filter by the following flight carrier
|Emirates|
|British Airways|

And I click on BOOK NOW with the cheapest price
Then I am taken to booking page