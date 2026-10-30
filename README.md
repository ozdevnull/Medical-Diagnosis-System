# MDS — Medical Diagnosis System

MDS (Medical Diagnosis System) is an educational, symptom-based health assessment and pharmaceutical information application. It helps users explore possible health conditions based on selected symptoms and access information about related diseases and medicines.

**Built with Java 25, JavaFX, FXML, CSS, and MariaDB/MySQL.**

---

## 1. Technology Stack

| Component | Technology |
|---|---|
| Language | Java 25 (OpenJDK) |
| UI Framework | JavaFX 25 |
| UI Design | FXML and CSS |
| Layout | Mobile-style (390 × 844) |
| Build Tool | Apache Maven 3.9+ |
| Database | MariaDB 10.4+ / MySQL 8.0+ |
| Connectivity | JDBC (MariaDB Connector/J 3.3.3) |
| Base Package | `com.mds` |

### Project Structure

```text
com.mds/
├── Main.java
├── AppState.java
├── SceneNavigator.java
├── AnimationUtils.java
│
├── database/
│   └── DatabaseConnection.java
│
├── model/
│   ├── Symptom.java
│   ├── Disease.java
│   ├── Medicine.java
│   ├── DiseaseMatch.java
│   └── User.java
│
├── dao/
│   ├── SymptomDAO.java
│   ├── DiseaseDAO.java
│   ├── MedicineDAO.java
│   └── UserDAO.java
│
├── service/
│   └── DiseaseMatchingService.java
│
└── controller/
    ├── StartController.java
    ├── LoginController.java
    ├── DashboardController.java
    ├── SymptomController.java
    └── ResultsController.java
```

### Architecture

- **Model:** Represents symptoms, diseases, medicines, and users.
- **DAO:** Handles database operations.
- **Service:** Implements disease-matching calculations.
- **Controller:** Manages UI interactions and navigation.
- **Database:** Stores application data and relationships.

---

## 2. Database Setup

### Configuration

| Parameter | Value |
|---|---|
| Database | `providencedb` |
| Host | `localhost` |
| Port | `3306` |
| JDBC URL | `jdbc:mariadb://localhost:3306/providencedb` |
| Username | `root` |
| Password | Blank (default XAMPP configuration) |

### Installation

1. Install and launch XAMPP.
2. Start the **MySQL** service on port `3306`.
3. Open [phpMyAdmin](http://localhost/phpmyadmin/).
4. Create a database named `providencedb`.
5. Select the `utf8mb4_unicode_ci` collation.
6. Import the SQL files in this order:

| SQL File | Description |
|---|---|
| `schema.sql` | Creates six database tables and relationships |
| `seed_symptoms.sql` | Inserts 50 symptoms |
| `seed_diseases.sql` | Inserts 45 diseases |
| `seed_disease_symptoms.sql` | Inserts 290 disease-symptom associations |
| `seed_medicines.sql` | Inserts 20 medicine profiles |
| `seed_disease_medicines.sql` | Inserts 61 disease-medicine associations |
| `seed_users.sql` | Creates the demonstration account |

**Alternative:** Use `database/migrations/001_initial_schema.sql` if it contains the complete initial database setup.

---

## 3. Symptom Database

The application includes **50 predefined symptoms** covering multiple categories:

- General: Fever, chills, fatigue, weakness and sweating.
- Respiratory: Cough, sneezing, sore throat and breathing difficulties.
- Digestive: Nausea, vomiting, abdominal pain and bloating.
- Neurological: Headache, dizziness and light sensitivity.
- Skin and sensory: Rashes, itching, loss of smell and taste.
- Other: Chest pain, joint pain, neck stiffness and urinary symptoms.

These symptoms are linked to diseases through weighted database associations.

---

## 4. Weighted Disease-Matching Algorithm

<details>
<summary><strong>Click to expand the mathematical explanation</strong></summary>

MDS uses a weighted Jaccard-style similarity algorithm to compare selected symptoms against disease-associated symptoms.

### Mathematical Model

Let:

- \(S\) = User-selected symptoms.
- \(D\) = Symptoms associated with a disease.
- \(u(s)\) = User-side symptom weight (default: 5).
- \(a(d,s)\) = Disease association weight (1–5).

**Weighted Intersection:**

\[
I(d)=\sum_{s\in S\cap D}u(s)\times\frac{a(d,s)}{5}
\]

**Weighted Union:**

\[
U(d)=\sum_{s\in S}u(s)+\sum_{s\in D}a(d,s)-I(d)
\]

**Similarity Percentage:**

\[
Match(d)=\frac{I(d)}{U(d)}\times100
\]

If the denominator is zero or negative, the score is set to 0%.

### Example

Selected symptoms: Runny nose, Stuffy nose and Loss of smell.

Each has a user weight of 5, giving a total user weight of 15.

| Calculation | Common Cold | COVID-19 |
|---|---:|---:|
| Disease weight total | 28 | 44 |
| Shared symptom weight | 10 | 5 |
| Weighted union | 33 | 54 |
| Similarity | **30.30%** | **9.26%** |

The system ranks diseases by their calculated similarity scores and displays the highest-ranked results.

*Similarity scores are algorithmic comparisons, not clinical diagnostic probabilities.*

</details>

---

## 5. Application Features

- **Welcome Screen:** Introduces the application.
- **User Authentication:** Login with SHA-256 password hashing and demonstration login.
- **Dashboard:** Central access to application features.
- **Symptom Assessment:** Select from 50 predefined symptoms.
- **Disease Matching:** Weighted comparison against database records.
- **Results:** Displays up to five ranked disease matches.
- **Disease Search:** Explore information about medical conditions.
- **Medicine Search:** View medicine uses, side effects and precautions.

---

## 6. Running the Application

### Prerequisites

- Java JDK 25
- Apache Maven 3.9+
- XAMPP with MariaDB/MySQL
- Configured `providencedb` database

### Commands

**Compile:**
```bash
mvn clean compile
```

**Run Tests:**
```bash
mvn test
```

**Launch:**
```bash
mvn javafx:run
```

Ensure the XAMPP MySQL service is running before launching the application.

---

## 7. Demonstration Credentials

| Field | Value |
|---|---|
| Username | `admin` |
| Password | `1234` |

A Quick-Fill option is also available on the login screen.

---

## 8. Disclaimer

MDS is an educational software project demonstrating Java application development, database integration, and symptom-based disease matching.

The results are generated using predefined symptom associations and mathematical similarity calculations. They are not medically validated diagnoses and should not be used to make treatment decisions. Consult a qualified healthcare professional for medical advice.
