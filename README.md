# AndroidMandatory
> hej
This repository contains the source code for my mandatory android assignment on the 4. semester in the computer science study programme at KEA. Below are the most relevant requirements and a brief description of my solution to them.

## Requirements:
### 1. Implement a new feature in the app (not been taught by me in during class) 
For this I decided to implement several things. Firstly a ListView using an ArrayAdapter to present the names of my objects. Secondly an AlertDialog to make the user confirm their selection and thirdly, I built an option to save and load my objects using Google's Gson library, to store my own objects as Json strings in Shared Preferences.

### 2. Send a mail to the teacher being rated with the results
This one was tricky. What I wanted to achieve was the app sending an email automatically to a teacher after they were rated. So far I have been unsuccessfull in building something that doesn't open the Gmail App and inserts the required values. The code is there but commented out.

### 3. Validate input from user
Since I'm only taking RatingBar and ListView inputs in this app (floats & Strings), there wasn't a lot of validation required. I basicaly just cast the view as a button and extract the text or get the numeric value from the RatingBars.

### 4. Clean architecture 
Achived by splitting up views, DAO's & models into seperate packages and trying to follow basic GRASP principles.
