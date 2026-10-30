# MDS — Medical Diagnosis System

MDS (Medical Diagnosis System) is an educational symptom-based health triage and pharmaceutical exploration assistant built with **Java 25**, **JavaFX**, **FXML**, and **MariaDB / MySQL (XAMPP)**.

---

## 1. Technology Stack & Architecture

- **Language**: Java 25 (OpenJDK / Eclipse Adoptium)
- **UI Framework**: JavaFX (v25-ea+1)
- **UI Structure**: FXML declarative layout with CSS styling
- **Form Factor**: Fixed mobile portrait layout (390 × 844)
- **Build System**: Apache Maven 3.9+
- **Database**: MariaDB 10.4+ / MySQL 8.0+ via XAMPP
- **JDBC Driver**: MariaDB Connector/J (`org.mariadb.jdbc:mariadb-java-client:3.3.3`)
- **Base Package**: `com.mds`

### Package Structure

```text
com.mds/
├── Main.java                        # JavaFX application entry point
├── AppState.java                    # Thread-safe session state singleton
├── SceneNavigator.java              # Centralized FXML page transitions
├── AnimationUtils.java              # Polished UI micro-animations
│
├── database/
│   └── DatabaseConnection.java      # Centralized MariaDB JDBC connection manager
│
├── model/
│   ├── Symptom.java                 # Symptom entity model
│   ├── Disease.java                 # Disease entity model with association weights
│   ├── Medicine.java                # Educational pharmaceutical model
│   ├── DiseaseMatch.java            # Weighted similarity match result model
│   └── User.java                    # User authentication model
│
├── dao/
│   ├── SymptomDAO.java              # Symptom database operations
│   ├── DiseaseDAO.java              # Disease retrieval, searching, and symptom mappings
│   ├── MedicineDAO.java             # Medicine search and disease linkages
│   └── UserDAO.java                 # SHA-256 authentication with demo fallback
│
├── service/
│   └── DiseaseMatchingService.java  # Weighted Jaccard-style clinical matching engine
│
└── controller/
    ├── StartController.java         # Welcome / landing screen controller
    ├── LoginController.java         # Authentication controller (SHA-256 + demo)
    ├── DashboardController.java     # Dashboard (Triage, Disease Search, Medicine Search)
    ├── SymptomController.java       # 50-symptom clinical checklist controller
    └── ResultsController.java       # 5-card ranked differential assessment controller
```

---

## 2. Database Configuration & Setup

### Database Parameters
- **Database**: `providencedb`
- **Host**: `localhost`
- **Port**: `3306`
- **JDBC URL**: `jdbc:mariadb://localhost:3306/providencedb`
- **Username**: `root`
- **Password**: *(blank / default XAMPP)*

### Setting Up the Database in XAMPP

1. Launch **XAMPP Control Panel** and start the **MySQL** module (Port 3306).
2. Open your browser and navigate to phpMyAdmin at `http://localhost/phpmyadmin/`.
3. Create a new database named `providencedb` (Collation: `utf8mb4_unicode_ci`).
4. Import the provided SQL files located in the `database/` folder:
   - `database/schema.sql` (Creates all 6 tables and relational constraints)
   - `database/seed_symptoms.sql` (Seeds all 50 symptoms)
   - `database/seed_diseases.sql` (Seeds 45 medically grounded conditions)
   - `database/seed_disease_symptoms.sql` (Seeds 290 symptom-disease relationships)
   - `database/seed_medicines.sql` (Seeds 20 educational pharmaceutical profiles)
   - `database/seed_disease_medicines.sql` (Seeds 61 disease-medicine linkages)
   - `database/seed_users.sql` (Seeds `admin` user with SHA-256 hashed password)

Alternatively, run the single migration script:
- `database/migrations/001_initial_schema.sql`

Or execute via PowerShell:
```powershell
Get-Content database\schema.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_symptoms.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_diseases.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_disease_symptoms.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_medicines.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_disease_medicines.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_users.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
```

---

## 3. Standard 50 Symptoms

The database seeds the exact 50 symptoms used in `SymptomController.java`:

| # | Symptom Name | # | Symptom Name |
|---|--------------|---|--------------|
| 1 | Fever | 26 | Sweating |
| 2 | Chills | 27 | Skin rash |
| 3 | Headache | 28 | Itching |
| 4 | Fatigue | 29 | Red eyes |
| 5 | Weakness | 30 | Watery eyes |
| 6 | Dizziness | 31 | Loss of smell |
| 7 | Nausea | 32 | Loss of taste |
| 8 | Vomiting | 33 | Ear pain |
| 9 | Loss of appetite | 34 | Frequent urination |
| 10 | Abdominal pain | 35 | Painful urination |
| 11 | Diarrhea | 36 | Increased thirst |
| 12 | Constipation | 37 | Swelling |
| 13 | Cough | 38 | Palpitations |
| 14 | Dry cough | 39 | Blurred vision |
| 15 | Phlegm | 40 | Difficulty sleeping |
| 16 | Sore throat | 41 | Anxiety/restlessness |
| 17 | Runny nose | 42 | Yellowing of skin/eyes |
| 18 | Stuffy nose | 43 | Dark urine |
| 19 | Sneezing | 44 | Pale stools |
| 20 | Shortness of breath | 45 | Neck stiffness |
| 21 | Chest pain | 46 | Sensitivity to light |
| 22 | Wheezing | 47 | Hair loss |
| 23 | Muscle pain | 48 | Sore muscles |
| 24 | Joint pain | 49 | Heartburn |
| 25 | Back pain | 50 | Bloating |

---

## 4. Weighted Matching Algorithm

The matching engine uses a clinical weighted Jaccard-style similarity metric:

### Mathematical Definition
- $S$ = Symptoms selected by the user.
- $D$ = Symptoms associated with a candidate disease.
- $u(s)$ = User-side importance weight of symptom $s$ (default = $5.0$ on the clinical 1–5 scale).
- $a(d,s)$ = Documented disease association weight, between 1 and 5.

### Step A: Weighted Intersection
For shared symptoms $s \in (S \cap D)$:
$$I(d) = \sum_{s \in (S \cap D)} \left[ u(s) \times \frac{a(d,s)}{5} \right]$$

### Step B: Weighted Union
$$U(d) = \sum_{s \in S} u(s) + \sum_{s \in D} a(d,s) - I(d)$$

### Step C: Similarity Percentage
$$\text{Match}(d) = \left[ \frac{I(d)}{U(d)} \right] \times 100\%$$

Zero denominators are safely caught ($U(d) \le 0 \implies \text{Match}(d) = 0\%$).

---

## 5. Worked Numerical Example

Consider a patient selecting 3 symptoms:
1. **Runny nose** ($u = 5.0$)
2. **Stuffy nose** ($u = 5.0$)
3. **Loss of smell** ($u = 5.0$)

Total user weight $\sum u(s) = 5.0 + 5.0 + 5.0 = 15.0$.

### Candidate 1: Common Cold
Associated symptoms in `providencedb`:
- Runny nose ($a=5$)
- Stuffy nose ($a=5$)
- Sneezing ($a=5$)
- Sore throat ($a=4$)
- Cough ($a=3$)
- Fatigue ($a=2$)
- Headache ($a=2$)
- Watery eyes ($a=2$)

Total disease weights $\sum a(d,s) = 5+5+5+4+3+2+2+2 = 28.0$.

- **Shared symptoms**: Runny nose ($a=5$), Stuffy nose ($a=5$).
- **Weighted Intersection**:
  $$I(d) = \left(5.0 \times \frac{5}{5}\right) + \left(5.0 \times \frac{5}{5}\right) = 5.0 + 5.0 = 10.0$$
- **Weighted Union**:
  $$U(d) = 15.0 + 28.0 - 10.0 = 33.0$$
- **Match Score**:
  $$\text{Match}(d) = \frac{10.0}{33.0} \times 100\% = 30.30\% \approx 30\%$$

### Candidate 2: COVID-19
Associated symptoms include:
- Loss of smell ($a=5$), Dry cough ($a=5$), Fever ($a=5$), Loss of taste ($a=5$), Fatigue ($a=4$), Shortness of breath ($a=4$), Muscle pain ($a=3$), Headache ($a=3$), Sore throat ($a=3$), Chills ($a=3$), Diarrhea ($a=2$), Chest pain ($a=2$).
Total disease weights $\sum a(d,s) = 44.0$.

- **Shared symptoms**: Loss of smell ($a=5$).
- **Weighted Intersection**:
  $$I(d) = 5.0 \times \frac{5}{5} = 5.0$$
- **Weighted Union**:
  $$U(d) = 15.0 + 44.0 - 5.0 = 54.0$$
- **Match Score**:
  $$\text{Match}(d) = \frac{5.0}{54.0} \times 100\% = 9.26\% \approx 9\%$$

**Ranking Result**:
1. **Common cold** (30% similarity) — 2 hallmark symptoms matched.
2. **COVID-19** (9% similarity) — 1 symptom matched.

---

## 6. How to Run and Test

### Compile the Application
```bash
mvn clean compile
```

### Run Unit and Integration Tests
```bash
mvn test
```

### Launch the JavaFX Application
```bash
mvn javafx:run
```

### Demonstration Credentials
- **Username**: `admin`
- **Password**: `1234`
*(Quick-Fill button available on login page)*
