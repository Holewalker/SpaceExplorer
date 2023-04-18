# :rocket: Space Explorer :rocket:

App build for checking Rocket :rocket: launches and NASA's Astronomy picture of the day.
## Table of Contents

- [Installation](#installation)
- [Usage](#usage)


## Installation

Clone the github repo and run <code> mvn javafx:run </code>

You WILL need to provide a API key inside the [RESOURCES](src/main/resources) folder. 
Create a config.properties file and add the following key with your api key for NASA API

<code>API_KEY=</code> Your key.
## Usage

Click on <code>get astronomy pic of the day</code> to get the APOD of the date introduced in the datepicker. 
If there is no date present, the current date will be used.

If you right click on the image, you can save it to your system (Low res image is used, not hdres)

Same applies with <code>get launch events</code>, although if there is no date, 
the first 25 records of history will be selected.

Be careful, SpaceDevs API only allows for 15 requests per hour.

You can filter the results by Space Agency and export the data to a CSV file. Zip compressing is in the roadmap.

