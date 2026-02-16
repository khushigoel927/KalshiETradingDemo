# KaslshiETradingDemo
An automated trading system built in **Java** that interacts with live event markets using the Kalshi API.  
The system monitors real-time NCAA game data and executes trades based on configurable decision rules and risk limits.

This project focuses on reliability, structured decision-making, and handling fast-changing live data — challenges similar to those found in financial trading systems.

---

##  Features

- Connects to the **Kalshi Demo Trading API**
- Fetches open NCAA events and associated milestones
- Monitors real-time score and game updates
- Executes trades automatically based on predefined conditions
- Handles API authentication and request signing
- Implements safeguards to limit trading exposure
- Continuously polls multiple concurrent events

---

##  Tech Stack

- **Java**
- REST APIs
- Unirest HTTP Client
- `org.json` for JSON parsing
- Kalshi Demo Trading API

---

## System Overview

The trading workflow:

1. Fetch open NCAA events from the Kalshi API.
2. Retrieve active milestones associated with each event.
3. Poll live data for score and game updates.
4. Apply decision logic based on score differential and time remaining.
5. Execute trades automatically when conditions are met.
6. Enforce exposure limits across markets.

The system is designed to run continuously and handle incomplete or changing API responses safely.

---

## Authentication

The project uses Kalshi API authentication with:

- API Key ID
- RSA-PSS request signing
- Timestamp-based validation

Credentials are stored in this repository are only for the demo version of Kalshi, not for actual trading

---

## Setup & Installation

### Clone the Repository

```bash
git clone https://github.com/yourusername/your-repo-name.git
cd your-repo-name
```

---

## API Configuration

### Demo Key (Included)

The repository is currently configured to use a **Kalshi Demo API key**.

**Demo base URL:**

```
https://demo-api.kalshi.co/trade-api/v2/
```

The demo environment allows you to test trading logic without placing real-money trades.

---

## Using Your Own API Key (Recommended)

If you would like to use your own API credentials (demo or production), follow these steps:

### Step 1: Generate API Keys

1. Log in to your Kalshi account.
2. Navigate to **Settings → API Keys**.
3. Create a new API key pair.
4. Download the **private RSA key (.pem file)**.
5. Copy the **API Key ID**.

---

### Step 2: Store Your Private Key Securely

Save your private key locally, for example:

```
~/kalshi/kalshi_private_key.pem
```

 **Never commit your private key to GitHub.**

Add this to your `.gitignore` file:

```gitignore
*.pem
```

---

### Step 3: Update `KalshiConfig.java`

Modify the following fields:

```java
public static final String API_KEY_ID = "YOUR_API_KEY_ID";
public static final String PRIVATE_KEY_PATH = "/absolute/path/to/your/kalshi_private_key.pem";
```

For production trading:

```java
public static final String BASE_URL = "https://api.elections.kalshi.com/trade-api/v2/";
```

For demo trading:

```java
public static final String BASE_URL = "https://demo-api.kalshi.co/trade-api/v2/";
```

---

## Build & Run

Using Maven:

```bash
mvn clean install
mvn exec:java
```

Or run directly from your IDE by executing:

```
NCAABettingBot.java
```

---

## Notes

- Ensure your private key path is absolute.
- Confirm your system clock is accurate (API requests require valid timestamps).
- If switching to production, double-check your risk limits before running.

