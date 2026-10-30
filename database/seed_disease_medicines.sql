-- ============================================================================
-- Medical Diagnosis System (MDS) — Seed Disease to Medicine Mappings
-- Educational linkages with therapeutic guidance
-- ============================================================================

USE `providencedb`;

INSERT INTO `disease_medicines` (`disease_id`, `medicine_id`, `additional_information`) VALUES
-- 1: Common cold
(1, 1, 'Antipyretic and analgesic for headache, fever, and generalized malaise.'),
(1, 4, 'Antihistamine to decrease rhinorrhea and sneezing.'),
(1, 11, 'Expectorant to loosen chest mucus and productive cough.'),
(1, 10, 'Centrally acting antitussive for irritating dry non-productive cough.'),

-- 2: Influenza
(2, 1, 'First-line reduction of high fevers and muscular aches.'),
(2, 2, 'Anti-inflammatory analgesic for severe headache and body soreness (take with food).'),

-- 3: COVID-19
(3, 1, 'Antipyretic for fever and bodily aches; monitor oxygen saturation.'),
(3, 7, 'Hydration support if accompanied by gastroenteritis or reduced oral intake.'),

-- 4: Acute bronchitis
(4, 11, 'Facilitates expectoration of bronchial mucus.'),
(4, 10, 'Symptomatic cough suppression for night-time rest.'),
(4, 8, 'Prescribed bronchodilator for associated wheezing or bronchospasm.'),

-- 5: Pneumonia
(5, 3, 'First-line prescription antimicrobial for susceptible bacterial pneumonia.'),
(5, 1, 'Symptomatic antipyretic for pleuritic discomfort and high temperature.'),

-- 6: Asthma
(6, 8, 'Short-acting rescue bronchodilator for acute wheeze and bronchospasm.'),

-- 7: Allergic rhinitis
(7, 4, 'Non-sedating antihistamine for sneezing, nasal congestion, and itchy eyes.'),
(7, 5, 'Once-daily non-drowsy oral antihistamine for hay fever.'),

-- 8: Sinusitis
(8, 1, 'Analgesic for facial pressure and frontal headache.'),
(8, 2, 'Anti-inflammatory for sinus mucosal congestion and discomfort.'),

-- 9: Pharyngitis
(9, 1, 'Pain relief for severe throat discomfort and odynophagia.'),
(9, 3, 'Prescribed if throat swab confirms Group A beta-hemolytic Streptococcus.'),

-- 10: Tonsillitis
(10, 3, 'Bactericidal antibiotic when bacterial strep tonsillitis is diagnosed.'),
(10, 1, 'Analgesic for swallowing discomfort and fever reduction.'),
(10, 2, 'NSAID for tonsillar and cervical soft-tissue swelling.'),

-- 12: Gastroenteritis
(12, 7, 'Critical first-line therapy: oral electrolyte and fluid repletion.'),
(12, 9, 'Use with caution only in non-invasive watery diarrhea (avoid if high fever/bloody stool).'),
(12, 1, 'Antipyretic for mild abdominal ache and low-grade pyrexia.'),

-- 13: Food poisoning
(13, 7, 'Essential oral electrolyte fluid replacement for dehydration prevention.'),
(13, 1, 'Analgesic for headache and generalized visceral cramping.'),

-- 14: Gastritis
(14, 6, 'Proton pump inhibitor to suppress gastric acid and allow mucosal healing.'),

-- 15: Gastroesophageal reflux disease (GERD)
(15, 6, 'First-line acid suppression therapy for persistent retrosternal heartburn.'),

-- 16: Irritable bowel syndrome (IBS)
(16, 9, 'Antimotility agent for diarrhea-predominant IBS episodes under clinical advice.'),

-- 17: Peptic ulcer disease
(17, 6, 'High-dose acid suppression for duodenal and gastric ulcer re-epithelialization.'),
(17, 3, 'Component of Helicobacter pylori triple eradication antibiotic therapy.'),

-- 22: Migraine
(22, 17, 'Specific 5-HT1 receptor agonist for acute abortive migraine attack relief.'),
(22, 2, 'Early-intervention anti-inflammatory for mild-to-moderate migraine attacks.'),
(22, 1, 'Analgesic combined with antiemetics when NSAIDs are contraindicated.'),

-- 23: Tension headache
(23, 1, 'First-line non-opioid analgesic for bilateral tension cephalalgia.'),
(23, 2, 'NSAID for pericranial muscle tenderness and aching pressure.'),

-- 25: Vertigo
(25, 19, 'Improves microcirculation in the labyrinthine inner ear.'),
(25, 20, 'Antihistamine antiemetic for symptomatic dizziness and nausea relief.'),

-- 27: Urinary tract infection (UTI)
(27, 12, 'Broad-spectrum antibacterial reserved for complicated urinary tract infection.'),
(27, 1, 'Pain relief for burning dysuria and pelvic tenderness.'),

-- 28: Kidney stones
(28, 2, 'NSAID providing renal capsule anti-inflammatory analgesia for ureteric colic.'),
(28, 1, 'Adjunctive analgesic for pain control.'),

-- 29: Type 2 diabetes
(29, 13, 'First-line oral biguanide antihyperglycemic medication taken with meals.'),

-- 30: Hypothyroidism
(30, 14, 'Daily oral synthetic thyroxine (T4) replacement taken on an empty stomach.'),

-- 32: Iron-deficiency anemia
(32, 18, 'Oral elemental iron supplement to restore hemoglobin stores.'),

-- 33: Allergic conjunctivitis
(33, 4, 'Systemic oral antihistamine to reduce conjunctival hyperemia and pruritus.'),
(33, 5, 'Non-sedating antihistamine for ocular allergy relief.'),

-- 35: Dermatitis
(35, 15, 'Mild topical corticosteroid to reduce localized eczematous inflammation.'),
(35, 4, 'Oral antihistamine to manage associated night-time pruritus.'),

-- 36: Urticaria
(36, 4, 'High-affinity H1 receptor blockade for acute and chronic urticarial wheals.'),
(36, 5, 'Non-drowsy antihistamine for day-time urticaria control.'),

-- 37: Fungal skin infection
(37, 16, 'Broad-spectrum topical antifungal cream applied twice daily to skin lesion.'),

-- 38: Otitis media
(38, 3, 'First-line oral antibiotic for acute suppurative bacterial otitis media.'),
(38, 1, 'Analgesic for intense otalgia and fever.'),

-- 40: Osteoarthritis
(40, 1, 'Baseline non-opioid analgesic for chronic joint discomfort.'),
(40, 2, 'Anti-inflammatory for episodic osteoarthritic joint flares.'),

-- 41: Rheumatoid arthritis
(41, 2, 'Symptomatic anti-inflammatory relief during inflammatory joint exacerbations.'),

-- 42: Fibromyalgia
(42, 1, 'Mild non-habit-forming analgesic for musculoskeletal soreness.'),

-- 45: Dehydration
(45, 7, 'Immediate physiological electrolyte and water replacement solution.')
ON DUPLICATE KEY UPDATE
  `additional_information` = VALUES(`additional_information`);
