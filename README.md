# 🗂️ Chat Application using DragonFlyDB

This is a simple chat application using **WebSocket** and **DragonFly DB** (Redis-compatible). The application supports
running multiple instances simultaneously on different ports and communicates with DragonFly DB for storing messages.

---

## 🚀 Features

- 💬 Real-time chat using WebSocket
- 🗃️ Stores chat messages in DragonFly DB
- ⚙️ Can run multiple instances on different ports

---

## ⚙️ How to Run Locally

Follow these steps after cloning the repository:

### 1️⃣Clone the Repository

Open your terminal and run:

```bash
git clone https://github.com/Kapilan1998/Chat-Application-using-DragonFlyDB.git
cd Chat-Application-using-DragonFlyDB
```

### 2️⃣ Configure application.properties
Before starting the application, check the src/main/resources/application.properties  file:

⬤ What to do:

➜ Running locally on your machine:

Uncomment the localhost line and comment out the dragonfly line:
```bash
spring.data.redis.host=localhost
#spring.data.redis.host=dragonfly
```
This ensures your local application connects to a local DragonFly DB instance (or a Redis-compatible DB running locally).

➜Running via Docker image:

Keep the dragonfly line uncommented and localhost commented:
```bash
#spring.data.redis.host=localhost
spring.data.redis.host=dragonfly
```
then start to build docker image from 4️⃣th instruction and go on

### 3️⃣ Start DragonFly DB using Docker Compose

Before starting the chat application, make sure the DragonFly DB container is up. Without it, the Redis connection will
fail.

```bash
docker compose up -d
```
then move to step 8️⃣

### 4️⃣ Build Docker Image for the Chat Application

From the project root, build the Docker image:

```bash
docker build -t chat-image:1.0 .
```

### 5️⃣ Create Docker Network

Create a network to allow communication between containers:

```bash
docker network create chat-net
```

### 6️⃣ Connect DragonFly DB to the Network

Connect the DragonFly DB container to the network:

```bash
docker network connect chat-net dragonfly
```

### 7️⃣ Run Chat Application Containers

You can run one or more instances of the chat application on different ports:

```bash
docker run -d --name chat-server-1 --network chat-net -p 4001:8082 chat-image:1.0
docker run -d --name chat-server-2 --network chat-net -p 4002:8082 chat-image:1.0
```

This allows multiple instances to run in parallel and communicate with DragonFly DB.

### 8️⃣ Access the Chat Application

Open your browser and access the running instances:

Instance 1:

```bash
http://localhost:4001
```

Instance 2:

```bash
http://localhost:4002
```
