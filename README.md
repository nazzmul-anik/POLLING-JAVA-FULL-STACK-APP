# 🗳️ Polling Application

A full-stack **Polling Application** built with **Java, Spring Boot, MySQL, React, and Material UI**, designed with clean architecture, secure authentication, and scalable REST APIs.

This project demonstrates modern backend engineering practices (JWT security, layered architecture, DTO pattern) combined with a responsive and user-friendly frontend.

---

<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/ced079ea-2301-4f7a-9640-50df6a6693ee" />


## 🚀 Tech Stack

### 🔧 Backend

* Java 25
* Spring Boot
* Spring Security (JWT Authentication)
* Spring Data JPA (Hibernate)
* MySQL
* Java Mail Sender
* Lombok

### 🎨 Frontend

* React.js
* Material UI (MUI)
* Axios

---

## ✨ Features

### 🔐 Authentication & Authorization

* User Registration & Login
* JWT-based authentication
* Secure API endpoints

### 🗳️ Poll Management

* Create a poll (authenticated users)
* View all polls
* View **My Polls** (user-specific)
* Delete own polls

### ❤️ Engagement

* Like a poll
* Comment on polls
* Vote on poll options

### 📩 Email Integration

* Automatic email notification when a poll is created (via Java Mail Sender)

---

## 🏗️ Backend Architecture

The backend follows a clean layered architecture:

```
Controller → Service → Repository → Database
            ↓
           DTO
```

### Key Design Practices

* DTO pattern for API communication
* Service layer abstraction
* Exception handling (recommended to extend)
* Stateless authentication using JWT

---

## 📡 API Endpoints

### Poll APIs

| Method | Endpoint                    | Description                |
| ------ | --------------------------- | -------------------------- |
| POST   | `/api/user/polls/`          | Create a poll              |
| GET    | `/api/user/polls/`          | Get all polls              |
| GET    | `/api/user/polls/my-polls`  | Get logged-in user's polls |
| GET    | `/api/user/polls/{id}`      | Get poll details           |
| DELETE | `/api/user/polls/{id}`      | Delete poll                |
| GET    | `/api/user/polls/like/{id}` | Like a poll                |
| POST   | `/api/user/polls/comment`   | Add comment                |
| POST   | `/api/user/polls/vote`      | Vote on poll               |

---

## 🔑 Authentication Flow

1. User registers
2. User logs in
3. Backend generates JWT token
4. Token is sent in headers for secured requests

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 📬 Email Notification Flow

* When a user creates a poll:

  * Backend triggers Java Mail Sender
  * Email is sent to the logged-in user

---

## 🧩 Sample Controller (PollController)

```java
@RestController
@RequestMapping("/api/user/polls")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PollController {

    private final PollService pollService;

    @PostMapping("/")
    public ResponseEntity<?> postPoll(@RequestBody PollDTO pollDTO){
        PollDTO creatdPollDTO = pollService.postPoll(pollDTO);
        return new ResponseEntity<>(creatdPollDTO, HttpStatus.CREATED);
    }   
}
```

---

## ⚙️ Setup Instructions

### 🔹 Backend

```bash
git clone https://github.com/nazzmul-anik/polling-app.git
cd polling-app/backend
```

Run the application:

```bash
mvn spring-boot:run
```

---

### 🔹 Frontend UI Picture

🔹 Dashboard

<img width="958" height="464" alt="image" src="https://github.com/user-attachments/assets/0c9b8ce3-3aec-41c2-940c-e0da75d49eb8" />

🔹 Create Poll Page

<img width="950" height="473" alt="image" src="https://github.com/user-attachments/assets/ad86fab5-34e9-4120-91bd-017dfd090baf" />

🔹 Signu Up Page

<img width="950" height="380" alt="image" src="https://github.com/user-attachments/assets/3c2b39f9-9495-4672-ac2f-aa1e8a82fc12" />

🔹 Email Confirmation

<img width="959" height="462" alt="image" src="https://github.com/user-attachments/assets/9657aae9-2c3c-407a-8c14-329e8d2b8790" />

🔹 Like & Comment Section

<img width="957" height="451" alt="image" src="https://github.com/user-attachments/assets/b118d2c9-f1c3-4e54-b6d1-0529efa19517" />



---


## 👨‍💻 Author

**Md Nazmul Hasan Anik**

* Backend Developer (Spring Boot)
* Passionate about scalable system design and clean code

---

⭐ If you find this project useful, consider giving it a star!
