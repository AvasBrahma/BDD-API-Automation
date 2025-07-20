<img width="1906" height="750" alt="image" src="https://github.com/user-attachments/assets/462c0fc8-cd17-4dcf-81cc-65af88db89e0" /># 🧪 BDD-API-Automation Framework

This is a lightweight and flexible **API automation framework** built using **Java**, **BDD (Cucumber)**, and **TestNG**. It supports comprehensive HTTP operations and validations for both **JSON** and **XML** API responses.

---

## 🚀 Features

- ✅ Built using **Java** and **TestNG** for robust test execution
- ✅ **BDD Cucumber** for human-readable feature files
- ✅ Support for all standard **HTTP Methods**: `GET`, `POST`, `PUT`, `DELETE`, `PATCH`
- ✅ **org.json** for easy **JSON key-value** validations
- ✅ **org.w3c.dom** or **javax.xml** for flexible **XML tag validations**
- ✅ Lightweight and easily extendable
- ✅ Ideal for RESTful service testing
- ✅ Structured logs and outputs for better traceability

---

## 📁 Project Structure
```bash
project-root/
├── src/
│   ├── test/java/stepdefinitions/    # Cucumber step definitions
│   ├── test/java/utils/              # Utility classes for API, JSON, XML
│   └── test/resources/               # Feature files and config
│
├── test-output/                      # TestNG HTML reports
├── target/                           # Compiled build output
├── config.properties                 # Environment/configuration settings
├── testng.xml                        # TestNG suite configuration
├── pom.xml                           # Maven dependencies and build setup
└── README.md                         # Project documentation

## 🧰 Tools & Libraries

- **Java 11+**
- **Cucumber (BDD)**
- **TestNG**
- **org.json** for JSON parsing/validation
- **org.w3c.dom / javax.xml** for XML parsing/validation
- **Maven** for dependency management

---

## 🔧 How to Use

### 1. Clone the Repository
```bash
git clone https://github.com/AvasBrahma/BDD-API-Automation.git
