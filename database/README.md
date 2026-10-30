# Medical Diagnosis System (MDS) — Database Setup & Architecture

This directory contains the complete SQL schema, migrations, and medically grounded seed data for the **MDS (Medical Diagnosis System)** MariaDB/MySQL database (`providencedb`).

---

## 1. Database Configuration

- **Database Engine**: MariaDB 10.4+ / MySQL 8.0+ (standard in XAMPP)
- **Database Name**: `providencedb`
- **Default Host**: `localhost`
- **Default Port**: `3306`
- **Default User**: `root`
- **Default Password**: *(blank / none by default in XAMPP)*
- **JDBC Connection URL**: `jdbc:mariadb://localhost:3306/providencedb`
- **JDBC Driver**: `org.mariadb.jdbc.Driver` (MariaDB Connector/J 3.3.3+)

---

## 2. Directory Structure

```text
database/
├── schema.sql                   # Creates providencedb and all 6 relational tables & constraints
├── migrations/
│   └── 001_initial_schema.sql  # Standalone migration script for initial deployment
├── seed_symptoms.sql            # Seeds the exact 50 symptoms matching SymptomController.java
├── seed_diseases.sql            # Seeds 45 medically grounded disease profiles (WHO/CDC/NHS)
├── seed_disease_symptoms.sql    # Seeds 290 clinical associations with weights 1–5
├── seed_medicines.sql           # Seeds 20 educational pharmaceutical entries
├── seed_disease_medicines.sql   # Seeds 61 disease-to-therapy educational linkages
├── seed_users.sql               # Seeds administrator user with SHA-256 hashed password
└── README.md                    # Setup, import, and clinical reference guide
```

---

## 3. Database Schema & Tables

### 1. `symptoms` (50 Records)
- `symptom_id` (INT, Primary Key, Auto Increment)
- `symptom_name` (VARCHAR(100), Unique, Not Null) — Exact string match with JavaFX checkbox bindings.
- `description` (TEXT) — Clinical and physiological definition.
- `created_at` (TIMESTAMP)

### 2. `diseases` (45 Records)
- `disease_id` (INT, Primary Key, Auto Increment)
- `disease_name` (VARCHAR(150), Unique, Not Null)
- `description` (TEXT, Not Null)
- `causes` (TEXT) — Etiology, pathogens, and physiological mechanisms.
- `precautions` (TEXT) — Practical lifestyle and clinical management precautions.
- `severity_information` (VARCHAR(100)) — Clinical severity triage rating.
- `reference_source` (VARCHAR(255)) — WHO, CDC, NHS, MedlinePlus citations.
- `created_at` (TIMESTAMP)

### 3. `disease_symptoms` (290 Relationships)
- `disease_id` (INT, Foreign Key referencing `diseases.disease_id`)
- `symptom_id` (INT, Foreign Key referencing `symptoms.symptom_id`)
- `association_weight` (INT, Check 1–5) — Clinical strength of documented association:
  - **5**: Strongly characteristic / pathognomonic / hallmark symptom.
  - **4**: Commonly associated.
  - **3**: Moderately associated.
  - **2**: Occasionally associated.
  - **1**: Less common but recognized.
- Composite Primary Key: `(disease_id, symptom_id)`

### 4. `medicines` (20 Records)
- `medicine_id` (INT, Primary Key, Auto Increment)
- `medicine_name` (VARCHAR(150), Unique, Not Null)
- `description` (TEXT, Not Null)
- `uses` (TEXT) — Approved indications.
- `side_effects` (TEXT) — Documented adverse drug reactions.
- `precautions` (TEXT) — Black box warnings, contraindications, and food/drug interactions.
- `reference_source` (VARCHAR(255))
- `created_at` (TIMESTAMP)

### 5. `disease_medicines` (61 Relationships)
- `disease_id` (INT, Foreign Key referencing `diseases.disease_id`)
- `medicine_id` (INT, Foreign Key referencing `medicines.medicine_id`)
- `additional_information` (TEXT) — Clinical context, timing, and therapeutic caveats.
- Composite Primary Key: `(disease_id, medicine_id)`

### 6. `users` (1+ Records)
- `user_id` (INT, Primary Key, Auto Increment)
- `username` (VARCHAR(50), Unique, Not Null)
- `password_hash` (VARCHAR(255), Not Null) — SHA-256 hexadecimal hash.
- `role` (VARCHAR(30))
- `created_at` (TIMESTAMP)

---

## 4. Setup Instructions

### Method A: Import via XAMPP phpMyAdmin (Web GUI)

1. Open your web browser and navigate to **`http://localhost/phpmyadmin/`**.
2. Click on the **"Databases"** tab in the top navigation bar.
3. In the "Create database" field, enter `providencedb`, choose collation `utf8mb4_unicode_ci`, and click **Create** (if not already created).
4. Select `providencedb` in the left sidebar list.
5. Click the **"Import"** tab in the top navigation bar.
6. Click **"Choose File"** and upload:
   - `database/migrations/001_initial_schema.sql` (Creates all tables)
   - Then upload `database/seed_symptoms.sql`, `database/seed_diseases.sql`, `database/seed_disease_symptoms.sql`, `database/seed_medicines.sql`, `database/seed_disease_medicines.sql`, and `database/seed_users.sql`.
   - Alternatively, open the **SQL** tab and paste the contents of `database/schema.sql` followed by the seed scripts.
7. Click **Go** at the bottom right.
8. Verify that all 6 tables appear with positive row counts.

### Method B: Import via Command Line / PowerShell

From the repository root directory, run:

```powershell
# Using MySQL client from XAMPP
Get-Content database\schema.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_symptoms.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_diseases.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_disease_symptoms.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_medicines.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_disease_medicines.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
Get-Content database\seed_users.sql | & "C:\xampp\mysql\bin\mysql.exe" -u root
```

---

## 5. Verification Query

To verify the database integrity, run the following SQL query:

```sql
USE providencedb;

SELECT 'symptoms' AS `Table`, COUNT(*) AS `Records` FROM symptoms
UNION ALL SELECT 'diseases', COUNT(*) FROM diseases
UNION ALL SELECT 'disease_symptoms', COUNT(*) FROM disease_symptoms
UNION ALL SELECT 'medicines', COUNT(*) FROM medicines
UNION ALL SELECT 'disease_medicines', COUNT(*) FROM disease_medicines
UNION ALL SELECT 'users', COUNT(*) FROM users;
```

**Expected Result:**
- `symptoms`: 50
- `diseases`: 45
- `disease_symptoms`: 290
- `medicines`: 20
- `disease_medicines`: 61
- `users`: 1 (admin)

---

## 6. Medical Reference Sources

All disease profiles, symptoms, and drug summaries are medically grounded and derived from recognized clinical authorities:
- **World Health Organization (WHO)**: Global Clinical Guidelines and Essential Medicines Model List.
- **Centers for Disease Control and Prevention (CDC)**: Infectious disease profiles and surveillance definitions.
- **National Health Service (NHS UK)**: Clinical health A–Z and patient diagnostic guidance.
- **National Library of Medicine (MedlinePlus)**: Medical encyclopedia and drug information.
- **Global Initiative for Asthma (GINA)**: Asthma management standards.
- **American Diabetes Association (ADA)**: Standards of Care in Diabetes.
- **International Headache Society (IHS)**: The International Classification of Headache Disorders (ICHD-3).
