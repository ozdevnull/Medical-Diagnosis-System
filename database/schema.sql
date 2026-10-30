-- ============================================================================
-- Medical Diagnosis System (MDS) — Database Schema
-- Target Engine: MariaDB / MySQL (XAMPP)
-- Database Name: providencedb
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `providencedb`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `providencedb`;

-- ----------------------------------------------------------------------------
-- Table: symptoms
-- Stores the standardized list of 50 clinical symptoms
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `symptoms` (
  `symptom_id` INT AUTO_INCREMENT PRIMARY KEY,
  `symptom_name` VARCHAR(100) NOT NULL UNIQUE,
  `description` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_symptom_name` (`symptom_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Table: diseases
-- Stores medically grounded disease profiles and clinical guidance
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `diseases` (
  `disease_id` INT AUTO_INCREMENT PRIMARY KEY,
  `disease_name` VARCHAR(150) NOT NULL UNIQUE,
  `description` TEXT NOT NULL,
  `causes` TEXT,
  `precautions` TEXT,
  `severity_information` VARCHAR(100) DEFAULT 'Moderate',
  `reference_source` VARCHAR(255) DEFAULT 'WHO / CDC / NHS / MedlinePlus',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_disease_name` (`disease_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Table: disease_symptoms
-- Maps diseases to symptoms with clinical association weights (1 to 5)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `disease_symptoms` (
  `disease_id` INT NOT NULL,
  `symptom_id` INT NOT NULL,
  `association_weight` INT NOT NULL CHECK (`association_weight` BETWEEN 1 AND 5),
  PRIMARY KEY (`disease_id`, `symptom_id`),
  INDEX `idx_ds_symptom` (`symptom_id`),
  CONSTRAINT `fk_ds_disease`
    FOREIGN KEY (`disease_id`) REFERENCES `diseases` (`disease_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ds_symptom`
    FOREIGN KEY (`symptom_id`) REFERENCES `symptoms` (`symptom_id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Table: medicines
-- Educational pharmaceutical directory with usage and safety warnings
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `medicines` (
  `medicine_id` INT AUTO_INCREMENT PRIMARY KEY,
  `medicine_name` VARCHAR(150) NOT NULL UNIQUE,
  `description` TEXT NOT NULL,
  `uses` TEXT,
  `side_effects` TEXT,
  `precautions` TEXT,
  `reference_source` VARCHAR(255) DEFAULT 'WHO / BNF / MedlinePlus',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_medicine_name` (`medicine_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Table: disease_medicines
-- Maps conditions to associated educational therapies
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `disease_medicines` (
  `disease_id` INT NOT NULL,
  `medicine_id` INT NOT NULL,
  `additional_information` TEXT,
  PRIMARY KEY (`disease_id`, `medicine_id`),
  INDEX `idx_dm_medicine` (`medicine_id`),
  CONSTRAINT `fk_dm_disease`
    FOREIGN KEY (`disease_id`) REFERENCES `diseases` (`disease_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_dm_medicine`
    FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Table: users
-- User accounts with password hashing (SHA-256)
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` INT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `role` VARCHAR(30) DEFAULT 'user',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
