-- ============================================================================
-- Medical Diagnosis System (MDS) — Seed Disease to Symptom Mappings
-- Clinical Association Weights:
--   5: Strongly characteristic / defining
--   4: Commonly associated
--   3: Moderately associated
--   2: Occasionally associated
--   1: Less common but recognized
-- ============================================================================

USE `providencedb`;

INSERT INTO `disease_symptoms` (`disease_id`, `symptom_id`, `association_weight`) VALUES
-- 1: Common cold
(1, 17, 5), -- Runny nose
(1, 18, 5), -- Stuffy nose
(1, 19, 5), -- Sneezing
(1, 16, 4), -- Sore throat
(1, 13, 3), -- Cough
(1, 4, 2),  -- Fatigue
(1, 3, 2),  -- Headache
(1, 30, 2), -- Watery eyes

-- 2: Influenza
(2, 1, 5),  -- Fever
(2, 2, 5),  -- Chills
(2, 23, 5), -- Muscle pain
(2, 4, 5),  -- Fatigue
(2, 3, 4),  -- Headache
(2, 48, 4), -- Sore muscles
(2, 14, 4), -- Dry cough
(2, 5, 4),  -- Weakness
(2, 26, 3), -- Sweating
(2, 16, 3), -- Sore throat
(2, 18, 2), -- Stuffy nose
(2, 9, 2),  -- Loss of appetite

-- 3: COVID-19
(3, 1, 5),  -- Fever
(3, 14, 5), -- Dry cough
(3, 31, 5), -- Loss of smell
(3, 32, 5), -- Loss of taste
(3, 4, 4),  -- Fatigue
(3, 20, 4), -- Shortness of breath
(3, 23, 3), -- Muscle pain
(3, 3, 3),  -- Headache
(3, 16, 3), -- Sore throat
(3, 2, 3),  -- Chills
(3, 11, 2), -- Diarrhea
(3, 21, 2), -- Chest pain

-- 4: Acute bronchitis
(4, 13, 5), -- Cough
(4, 15, 5), -- Phlegm
(4, 4, 3),  -- Fatigue
(4, 16, 3), -- Sore throat
(4, 20, 3), -- Shortness of breath
(4, 22, 3), -- Wheezing
(4, 21, 2), -- Chest pain
(4, 1, 2),  -- Fever

-- 5: Pneumonia
(5, 1, 5),  -- Fever
(5, 13, 5), -- Cough
(5, 15, 5), -- Phlegm
(5, 20, 5), -- Shortness of breath
(5, 21, 4), -- Chest pain
(5, 2, 4),  -- Chills
(5, 4, 4),  -- Fatigue
(5, 5, 3),  -- Weakness
(5, 26, 3), -- Sweating

-- 6: Asthma
(6, 22, 5), -- Wheezing
(6, 20, 5), -- Shortness of breath
(6, 13, 4), -- Cough
(6, 14, 4), -- Dry cough
(6, 21, 3), -- Chest pain
(6, 40, 3), -- Difficulty sleeping
(6, 41, 2), -- Anxiety/restlessness

-- 7: Allergic rhinitis
(7, 19, 5), -- Sneezing
(7, 17, 5), -- Runny nose
(7, 18, 5), -- Stuffy nose
(7, 28, 4), -- Itching
(7, 30, 4), -- Watery eyes
(7, 29, 4), -- Red eyes
(7, 4, 2),  -- Fatigue

-- 8: Sinusitis
(8, 3, 5),  -- Headache
(8, 18, 5), -- Stuffy nose
(8, 17, 4), -- Runny nose
(8, 15, 3), -- Phlegm
(8, 16, 3), -- Sore throat
(8, 1, 2),  -- Fever
(8, 4, 2),  -- Fatigue
(8, 33, 2), -- Ear pain

-- 9: Pharyngitis
(9, 16, 5), -- Sore throat
(9, 1, 3),  -- Fever
(9, 3, 3),  -- Headache
(9, 4, 2),  -- Fatigue
(9, 18, 2), -- Stuffy nose

-- 10: Tonsillitis
(10, 16, 5), -- Sore throat
(10, 1, 4),  -- Fever
(10, 33, 3), -- Ear pain
(10, 2, 3),  -- Chills
(10, 3, 3),  -- Headache
(10, 4, 2),  -- Fatigue
(10, 9, 2),  -- Loss of appetite

-- 11: Tuberculosis
(11, 13, 5), -- Cough
(11, 15, 5), -- Phlegm
(11, 26, 5), -- Sweating (night sweats)
(11, 9, 4),  -- Loss of appetite
(11, 4, 4),  -- Fatigue
(11, 5, 4),  -- Weakness
(11, 21, 3), -- Chest pain
(11, 20, 3), -- Shortness of breath
(11, 1, 3),  -- Fever

-- 12: Gastroenteritis
(12, 11, 5), -- Diarrhea
(12, 8, 5),  -- Vomiting
(12, 7, 5),  -- Nausea
(12, 10, 4), -- Abdominal pain
(12, 9, 3),  -- Loss of appetite
(12, 5, 3),  -- Weakness
(12, 1, 2),  -- Fever
(12, 2, 2),  -- Chills
(12, 50, 2), -- Bloating

-- 13: Food poisoning
(13, 7, 5),  -- Nausea
(13, 8, 5),  -- Vomiting
(13, 11, 5), -- Diarrhea
(13, 10, 5), -- Abdominal pain
(13, 5, 4),  -- Weakness
(13, 2, 3),  -- Chills
(13, 1, 2),  -- Fever
(13, 3, 2),  -- Headache

-- 14: Gastritis
(14, 10, 5), -- Abdominal pain
(14, 7, 4),  -- Nausea
(14, 9, 4),  -- Loss of appetite
(14, 50, 4), -- Bloating
(14, 49, 3), -- Heartburn
(14, 8, 2),  -- Vomiting

-- 15: Gastroesophageal reflux disease (GERD)
(15, 49, 5), -- Heartburn
(15, 21, 3), -- Chest pain
(15, 50, 3), -- Bloating
(15, 14, 3), -- Dry cough
(15, 16, 2), -- Sore throat
(15, 40, 2), -- Difficulty sleeping

-- 16: Irritable bowel syndrome (IBS)
(16, 10, 5), -- Abdominal pain
(16, 50, 5), -- Bloating
(16, 11, 4), -- Diarrhea
(16, 12, 4), -- Constipation
(16, 41, 2), -- Anxiety/restlessness

-- 17: Peptic ulcer disease
(17, 10, 5), -- Abdominal pain
(17, 49, 4), -- Heartburn
(17, 7, 3),  -- Nausea
(17, 50, 3), -- Bloating
(17, 9, 3),  -- Loss of appetite
(17, 8, 2),  -- Vomiting

-- 18: Constipation
(18, 12, 5), -- Constipation
(18, 50, 4), -- Bloating
(18, 10, 3), -- Abdominal pain
(18, 9, 2),  -- Loss of appetite

-- 19: Gallstones
(19, 10, 5), -- Abdominal pain
(19, 7, 4),  -- Nausea
(19, 8, 3),  -- Vomiting
(19, 42, 3), -- Yellowing of skin/eyes
(19, 43, 3), -- Dark urine
(19, 44, 3), -- Pale stools
(19, 1, 2),  -- Fever

-- 20: Hepatitis
(20, 42, 5), -- Yellowing of skin/eyes
(20, 43, 5), -- Dark urine
(20, 44, 5), -- Pale stools
(20, 4, 4),  -- Fatigue
(20, 9, 4),  -- Loss of appetite
(20, 7, 3),  -- Nausea
(20, 10, 3), -- Abdominal pain
(20, 24, 2), -- Joint pain
(20, 1, 2),  -- Fever

-- 21: Appendicitis
(21, 10, 5), -- Abdominal pain
(21, 7, 4),  -- Nausea
(21, 8, 4),  -- Vomiting
(21, 9, 4),  -- Loss of appetite
(21, 1, 3),  -- Fever
(21, 12, 2), -- Constipation
(21, 11, 2), -- Diarrhea

-- 22: Migraine
(22, 3, 5),  -- Headache
(22, 46, 5), -- Sensitivity to light
(22, 7, 4),  -- Nausea
(22, 8, 3),  -- Vomiting
(22, 6, 3),  -- Dizziness
(22, 39, 3), -- Blurred vision

-- 23: Tension headache
(23, 3, 5),  -- Headache
(23, 48, 3), -- Sore muscles
(23, 23, 3), -- Muscle pain
(23, 4, 3),  -- Fatigue
(23, 40, 2), -- Difficulty sleeping

-- 24: Meningitis
(24, 45, 5), -- Neck stiffness
(24, 46, 5), -- Sensitivity to light
(24, 1, 5),  -- Fever
(24, 3, 5),  -- Headache
(24, 7, 4),  -- Nausea
(24, 8, 4),  -- Vomiting
(24, 6, 3),  -- Dizziness
(24, 5, 3),  -- Weakness

-- 25: Vertigo
(25, 6, 5),  -- Dizziness
(25, 7, 4),  -- Nausea
(25, 8, 3),  -- Vomiting
(25, 38, 2), -- Palpitations
(25, 41, 2), -- Anxiety/restlessness

-- 26: Peripheral neuropathy
(26, 5, 4),  -- Weakness
(26, 48, 3), -- Sore muscles
(26, 23, 3), -- Muscle pain
(26, 39, 2), -- Blurred vision

-- 27: Urinary tract infection (UTI)
(27, 35, 5), -- Painful urination
(27, 34, 5), -- Frequent urination
(27, 43, 3), -- Dark urine
(27, 10, 3), -- Abdominal pain
(27, 1, 2),  -- Fever
(27, 2, 2),  -- Chills

-- 28: Kidney stones
(28, 25, 5), -- Back pain
(28, 35, 4), -- Painful urination
(28, 34, 4), -- Frequent urination
(28, 7, 4),  -- Nausea
(28, 8, 3),  -- Vomiting
(28, 43, 3), -- Dark urine
(28, 1, 2),  -- Fever

-- 29: Type 2 diabetes
(29, 34, 5), -- Frequent urination
(29, 36, 5), -- Increased thirst
(29, 4, 4),  -- Fatigue
(29, 39, 3), -- Blurred vision
(29, 5, 3),  -- Weakness
(29, 9, 2),  -- Loss of appetite

-- 30: Hypothyroidism
(30, 4, 5),  -- Fatigue
(30, 5, 4),  -- Weakness
(30, 12, 4), -- Constipation
(30, 47, 4), -- Hair loss
(30, 23, 3), -- Muscle pain
(30, 37, 3), -- Swelling
(30, 40, 2), -- Difficulty sleeping

-- 31: Hyperthyroidism
(31, 38, 5), -- Palpitations
(31, 26, 4), -- Sweating
(31, 41, 4), -- Anxiety/restlessness
(31, 40, 4), -- Difficulty sleeping
(31, 5, 3),  -- Weakness
(31, 34, 3), -- Frequent urination
(31, 47, 3), -- Hair loss

-- 32: Iron-deficiency anemia
(32, 4, 5),  -- Fatigue
(32, 5, 5),  -- Weakness
(32, 6, 4),  -- Dizziness
(32, 20, 4), -- Shortness of breath
(32, 38, 3), -- Palpitations
(32, 3, 3),  -- Headache
(32, 47, 2), -- Hair loss

-- 33: Allergic conjunctivitis
(33, 29, 5), -- Red eyes
(33, 30, 5), -- Watery eyes
(33, 28, 5), -- Itching
(33, 37, 3), -- Swelling
(33, 46, 2), -- Sensitivity to light

-- 34: Infective conjunctivitis
(34, 29, 5), -- Red eyes
(34, 30, 4), -- Watery eyes
(34, 46, 3), -- Sensitivity to light
(34, 37, 3), -- Swelling
(34, 28, 2), -- Itching

-- 35: Dermatitis
(35, 27, 5), -- Skin rash
(35, 28, 5), -- Itching
(35, 37, 2), -- Swelling
(35, 29, 2), -- Red eyes

-- 36: Urticaria
(36, 27, 5), -- Skin rash
(36, 28, 5), -- Itching
(36, 37, 4), -- Swelling

-- 37: Fungal skin infection
(37, 27, 5), -- Skin rash
(37, 28, 5), -- Itching

-- 38: Otitis media
(38, 33, 5), -- Ear pain
(38, 1, 4),  -- Fever
(38, 3, 3),  -- Headache
(38, 40, 3), -- Difficulty sleeping
(38, 18, 2), -- Stuffy nose

-- 39: Otitis externa
(39, 33, 5), -- Ear pain
(39, 28, 4), -- Itching
(39, 37, 3), -- Swelling

-- 40: Osteoarthritis
(40, 24, 5), -- Joint pain
(40, 48, 3), -- Sore muscles
(40, 37, 3), -- Swelling
(40, 25, 3), -- Back pain

-- 41: Rheumatoid arthritis
(41, 24, 5), -- Joint pain
(41, 37, 5), -- Swelling
(41, 4, 4),  -- Fatigue
(41, 5, 3),  -- Weakness
(41, 48, 3), -- Sore muscles
(41, 1, 2),  -- Fever

-- 42: Fibromyalgia
(42, 23, 5), -- Muscle pain
(42, 48, 5), -- Sore muscles
(42, 4, 5),  -- Fatigue
(42, 40, 4), -- Difficulty sleeping
(42, 3, 3),  -- Headache
(42, 41, 3), -- Anxiety/restlessness
(42, 24, 3), -- Joint pain

-- 43: Insomnia
(43, 40, 5), -- Difficulty sleeping
(43, 4, 5),  -- Fatigue
(43, 41, 4), -- Anxiety/restlessness
(43, 3, 3),  -- Headache
(43, 6, 2),  -- Dizziness

-- 44: Anxiety disorders
(44, 41, 5), -- Anxiety/restlessness
(44, 38, 4), -- Palpitations
(44, 40, 4), -- Difficulty sleeping
(44, 26, 3), -- Sweating
(44, 20, 3), -- Shortness of breath
(44, 6, 3),  -- Dizziness
(44, 4, 3),  -- Fatigue

-- 45: Dehydration
(45, 36, 5), -- Increased thirst
(45, 43, 5), -- Dark urine
(45, 6, 4),  -- Dizziness
(45, 5, 4),  -- Weakness
(45, 4, 4),  -- Fatigue
(45, 3, 3),  -- Headache
(45, 38, 3)  -- Palpitations
ON DUPLICATE KEY UPDATE
  `association_weight` = VALUES(`association_weight`);
