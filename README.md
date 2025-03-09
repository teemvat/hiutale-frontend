# Hiutale.app Event management system

This project is an Event Management System built using Java and Maven, created during Metropolia University of Applied Sciences Software Development Project 1 course. The program allows users to manage events, including creating, updating, and deleting events, as well as managing user attendance and favorites.

## Features

- User authentication and session management
- Event creation, update, and deletion
- User attendance tracking
- Favorite events management
- Image handling for events

## Technologies Used

- Java
- Maven
- JavaFX for the user interface
- Gson for JSON parsing
- HttpURLConnection for HTTP requests

## Project Structure

- `src/main/java/controller/api`: Contains API controllers for handling HTTP requests.
- `src/main/java/controller/ui`: Contains UI controllers for handling user interactions.
- `src/main/java/model`: Contains data models for the application.
- `src/main/resources`: Contains FXML files and other resources for the UI.

## Setup and Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/teemvat/event-management-system.git
   cd event-management-system
   ```

2. **Build the project using Maven:**
   ```sh
   mvn clean install
   ```

3. **Run the application:**
  ```sh
  mvn javafx:run
  ```

## Usage

- **Register:** Users can create accounts.
- **Login:** Users can log in using their credentials.
- **Event Management:** Users can create, update, and delete events.
- **Attendance:** Users can mark their attendance for events.
- **Favorites:** Users can add or remove events from their favorites.
- **Image Handling:** Users can upload and view images for events.
- **Notifications:** Users will get notifications for upcoming events.

## Backend

Project backend is in [separate repository](https://github.com/tom9393/hiutale-backend) by [@tom9393](https://github.com/tom9393).
Backend is used via api calls.


## Contributors
Our project team was:
- [@teemvat](https://github.com/teemvat)
- [@mirapery](https://github.com/mirapery)
- [@tom9393](https://github.com/tom9393)

  
