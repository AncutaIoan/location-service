# 📍 Location Service

This is a small project to explore and experiment with the following modern and powerful technologies:

- **PostGIS**: Spatial database extension for PostgreSQL.
- **Redisson (Redis)**: Reactive, distributed caching backed by Redis.
- **Kafka**: Distributed event streaming platform.
- **GraphQL**: Query language for APIs.

---

## 📚 Table of Contents

1. [Technologies Used](#technologies-used)
2. [Getting Started](#getting-started)
3. [Setup and Installation](#setup-and-installation)
4. [Usage](#usage)
5. [Resources](#resources)

---

## 🚀 Technologies Used

- **[PostGIS](https://postgis.net/)**  
  A spatial database extender for PostgreSQL, enabling geographic object support and powerful spatial queries.

- **[Redisson (Redis)](https://github.com/redisson/redisson)**  
  A high-performance reactive Redis client for Java that brings distributed caching, locking, and more.  
  🔄 *Replaces Infinispan to leverage Redis's proven stability, broad ecosystem, and lightweight setup for distributed caching.*

- **[Kafka](https://kafka.apache.org/)**  
  A distributed event streaming platform for building robust, scalable real-time data pipelines.

- **[GraphQL](https://graphql.org/)**  
  A powerful query language and runtime for APIs offering fine-grained control over fetched data.

---

## 🏁 Getting Started

### ✅ Prerequisites

Ensure you have the following installed:

- Java 21 or higher
- [Apache Kafka](https://kafka.apache.org/downloads)
- [PostgreSQL with PostGIS](https://postgis.net/install/)
- [Redis](https://redis.io/download) (Standalone or Docker)

> ⚠️ Note: Redisson handles connectivity to Redis, so make sure Redis is running and reachable.

---

## ⚙️ Setup and Installation

> _Coming soon..._  
This section will include full setup scripts, configuration guides, and Docker-based setup for local development.

---

## 🧪 Usage

1. **Run Kafka**  
   Start your Kafka broker(s) and ensure required topics are created.

2. **Start Redis**  
   Either install Redis locally or run it via Docker:
   ```bash
   docker run -p 6379:6379 redis
   ```

3. **Configure Redisson**  
   Redisson handles distributed cache behavior through reactive access patterns and TTL-based expiry.  
   Use `application.yml` to configure host, port, and authentication.

4. **Query via GraphQL**  
   Interact with the service via GraphQL to pull enriched data using Kafka events, spatial queries from PostGIS, and cached results from Redis.

---

## 📘 Resources

- **[PostGIS Documentation](https://postgis.net/documentation/)**
- **[Redisson Documentation](https://github.com/redisson/redisson/wiki)**
- **[Apache Kafka Documentation](https://kafka.apache.org/documentation/)**
- **[GraphQL Documentation](https://graphql.org/learn/)**

---

## ✨ Why the Change from Infinispan to Redis?

While Infinispan is a powerful data grid, Redis brings:

- A lighter operational footprint
- Built-in support for advanced data structures
- Seamless integration with Redisson's reactive APIs
- Superior ecosystem and deployment simplicity (especially with Docker)
- Improved developer experience for cache invalidation, distributed locks, and pub/sub

This makes Redis a more accessible and scalable choice for the caching and synchronization layer in this project.

---
