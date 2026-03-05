# Movie Recommendation and Tracker System

A modular Java application for movie management and personalized recommendations, developed by Group 9 for the Java Programming Coursework at XJTLU.

# Key Highlights
* OOP-Centric Design: Strictly implemented Encapsulation, Modularity, and Separation of Concerns.
* Efficiency: Utilized `HashMap` for movie storage to ensure near O(1) retrieval performance.
* Comprehensive Features: Includes user authentication, watchlist/history tracking, and a custom recommendation engine.
* Ethical Awareness: Evaluated potential algorithm bias ("Filter Bubbles") and proposed privacy protection measures.

##  System Architecture
The system consists of 9 interdependent core classes designed for high maintainability:
* User & Credentials: Manages profiles and watchlist/history data.
* MovieDatabase: Encapsulates movie data retrieval and manipulation.
* RecommendationEngine: Core logic for generating user-specific suggestions.
* FileHandlers: Manages persistent storage via CSV files.

##  Recommendation Logic
The system implements a `RecommendationEngine` that analyzes:
1.  User Viewing History: Suggests movies based on past interactions.
2.  Genre Preferences: Matches movies with user-defined interests.
3.  Global Ratings: Leverages average ratings for high-quality discovery.

##  Ethical Considerations & Solutions
We conducted a thorough ethical evaluation of the system:
* Privacy: Proposed encryption for plain-text passwords in CSV files to mitigate data leakage.
* Algorithm Bias: Identified risks of "over-personalization" and suggested diversity-promoting features.
* Transparency: Proposed explainable AI (XAI) features to help users understand why a movie was recommended.

##  How to Run
1.  Clone: `git clone https://github.com/[YourUsername]/Movie-Recommendation-System.git`
2.  Setup: Import the project into **IntelliJ IDEA** or **Eclipse**.
3.  Data: Ensure the movie and user CSV files are in the designated directory.
4.  Launch: Run `Main.java`.

---
Course Code: CPT111 | Institution: Xi'an Jiaotong-Liverpool University (XJTLU)
