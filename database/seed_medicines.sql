-- ============================================================================
-- Medical Diagnosis System (MDS) — Seed Medicines Directory
-- Educational pharmaceutical reference with indications, side effects & cautions
-- ============================================================================

USE `providencedb`;

INSERT INTO `medicines` (`medicine_id`, `medicine_name`, `description`, `uses`, `side_effects`, `precautions`, `reference_source`) VALUES
(1, 'Paracetamol (Acetaminophen)',
 'First-line antipyretic and non-opioid analgesic agent for mild-to-moderate pain and pyrexia.',
 'Fever reduction, headache, toothache, muscle aches, sore throat, and mild arthritis pain.',
 'Rare at therapeutic doses; rare skin reactions. High doses cause severe hepatotoxicity and acute liver failure.',
 'Do not exceed 4,000 mg (4 g) within 24 hours. Avoid concurrent alcohol intake and check other medications for duplicate paracetamol content.',
 'Clinical Educational Reference / WHO Essential Medicines'),

(2, 'Ibuprofen',
 'Nonsteroidal anti-inflammatory drug (NSAID) with pronounced analgesic, antipyretic, and anti-inflammatory properties.',
 'Relief of inflammatory joint pain, fever, menstrual cramps, dental pain, and soft tissue injuries.',
 'Dyspepsia, gastric ulceration, gastrointestinal bleeding, renal impairment, fluid retention.',
 'Take strictly with or after food. Contraindicated in active peptic ulcer disease, severe heart failure, or late pregnancy.',
 'BNF / WHO Essential Medicines'),

(3, 'Amoxicillin',
 'Moderate-spectrum bactericidal beta-lactam antibiotic of the aminopenicillin class.',
 'Bacterial pharyngitis, acute otitis media, community-acquired pneumonia, and lower respiratory tract infections.',
 'Nausea, diarrhea, mild cutaneous maculopapular rash, oral or vaginal candidiasis.',
 'Verify absence of prior penicillin hypersensitivity or anaphylaxis history. Complete the full prescribed therapeutic course.',
 'WHO Essential Medicines List / CDC'),

(4, 'Cetirizine',
 'Second-generation non-sedating H1-receptor antihistamine for allergic conditions.',
 'Allergic rhinitis (hay fever), allergic conjunctivitis, chronic urticaria, and pruritic skin reactions.',
 'Mild somnolence, dry mouth, headache, fatigue, pharyngitis.',
 'Use caution when driving or operating heavy machinery. Adjust dosage in patients with moderate to severe renal impairment.',
 'FDA / NHS Reference'),

(5, 'Loratadine',
 'Long-acting peripheral histamine H1-receptor antagonist with minimal central nervous system penetration.',
 'Seasonal allergic rhinitis, ocular allergy symptoms, chronic idiopathic urticaria.',
 'Headache, fatigue, dry mouth, gastrointestinal disturbance.',
 'Avoid in severe hepatic impairment without dosage titration. Minimal sedation compared to first-generation antihistamines.',
 'MedlinePlus / NHS'),

(6, 'Omeprazole',
 'Proton pump inhibitor (PPI) that suppresses gastric basal and stimulated acid secretion.',
 'Gastroesophageal reflux disease (GERD), peptic ulcer disease, erosive esophagitis, Zollinger-Ellison syndrome.',
 'Headache, abdominal pain, constipation, flatulence, nausea/vomiting, subacute cutaneous lupus.',
 'Take in the morning 30–60 minutes before breakfast. Prolonged therapy may impair Vitamin B12 and magnesium absorption.',
 'NICE Clinical Guidelines / BNF'),

(7, 'Oral Rehydration Salts (ORS)',
 'Balanced formulation of glucose, sodium chloride, potassium chloride, and trisodium citrate.',
 'Prevention and treatment of clinical dehydration from acute gastroenteritis, cholera, and watery diarrhea.',
 'Hypernatremia if prepared with insufficient water; vomiting if ingested too rapidly.',
 'Dissolve strictly in the exact volume of clean drinking water specified (typically 1 liter). Do not boil prepared solution.',
 'WHO / UNICEF Diarrhoea Treatment Guidelines'),

(8, 'Salbutamol (Albuterol)',
 'Short-acting selective beta-2 adrenergic receptor agonist providing rapid bronchodilation.',
 'Relief of acute bronchospasm in asthma, chronic bronchitis, and exercise-induced asthma.',
 'Fine skeletal muscle tremor (especially hands), palpitations, tachycardia, nervousness, hypokalemia.',
 'Rinse mouth after inhalation. If patient requires relief inhaler more than 2–3 times per week, medical review is essential.',
 'GINA Guidelines / BNF'),

(9, 'Loperamide',
 'Synthetic opioid analogue that acts on opioid mu-receptors in the circular and longitudinal gut wall to slow intestinal motility.',
 'Symptomatic control of acute non-specific diarrhea and chronic diarrhea associated with inflammatory bowel disease.',
 'Abdominal cramps, dizziness, dry mouth, paralytic ileus, toxic megacolon.',
 'Never use in bacterial enteritis with high fever, or if bloody dysenteric stools are present. Rehydration remains primary therapy.',
 'FDA Drug Safety Communication / MedlinePlus'),

(10, 'Dextromethorphan',
 'Centrally acting non-opioid antitussive agent elevating the threshold for cough in the medullary reflex center.',
 'Temporary suppression of non-productive dry, hacking cough without phlegm.',
 'Drowsiness, lightheadedness, nausea, mild stomach upset.',
 'Contraindicated with or within 14 days of monoamine oxidase inhibitors (MAOIs). Do not use for persistent productive wet cough.',
 'MedlinePlus / American College of Chest Physicians'),

(11, 'Guaifenesin',
 'Expectorant that increases hydration and decreases viscosity of tracheobronchial respiratory secretions.',
 'Relief of wet chest congestion and productive cough associated with the common cold and bronchitis.',
 'Nausea, vomiting, diarrhea, headache, cutaneous rash.',
 'Ensure generous oral fluid hydration to facilitate mucus thinning. Consult a physician if cough persists > 7 days or is accompanied by high fever.',
 'MedlinePlus / FDA'),

(12, 'Ciprofloxacin',
 'Broad-spectrum synthetic fluoroquinolone antibacterial agent inhibiting DNA gyrase and topoisomerase IV.',
 'Complicated urinary tract infections, bacterial enteritis, acute bacterial prostatitis, bone and joint infections.',
 'Tendinitis and tendon rupture risk, QT prolongation, peripheral neuropathy, nausea, diarrhea, photosensitivity.',
 'Avoid co-administration with antacids, iron, or dairy products (chelation). Reserve for cases where alternative antibacterials are inappropriate.',
 'FDA Black Box Warning / BNF'),

(13, 'Metformin',
 'Biguanide antihyperglycemic agent reducing hepatic gluconeogenesis and increasing peripheral insulin sensitivity.',
 'First-line pharmacotherapy for Type 2 Diabetes Mellitus.',
 'Metallic taste, diarrhea, nausea, abdominal discomfort, vitamin B12 deficiency; rare risk of lactic acidosis.',
 'Take with meals to minimize gastrointestinal discomfort. Discontinue prior to iodinated contrast radiological procedures.',
 'ADA Standards of Care / WHO'),

(14, 'Levothyroxine',
 'Synthetic crystalline levo-isomer of thyroxine (T4) identical to endogenous human thyroid hormone.',
 'Replacement therapy for primary, secondary, and tertiary hypothyroidism of any etiology.',
 'Symptoms of hyperthyroidism if over-replaced: palpitations, heat intolerance, tremor, insomnia, weight loss.',
 'Take first thing in the morning with plain water at least 30–60 minutes before breakfast or caffeine. Periodic TSH monitoring required.',
 'American Thyroid Association Guidelines'),

(15, 'Hydrocortisone Topical Cream 1%',
 'Low-potency topical glucocorticoid anti-inflammatory, antipruritic, and vasoconstrictive agent.',
 'Symptomatic relief of inflammatory manifestations of dermatitis, eczema, insect bites, and allergic skin rash.',
 'Cutaneous atrophy, telangiectasia, striae, secondary skin infection with prolonged occlusive use.',
 'Apply thinly only to affected skin areas for short durations. Avoid application on broken skin, face, or periorbital tissues without medical guidance.',
 'British National Formulary / MedlinePlus'),

(16, 'Clotrimazole',
 'Broad-spectrum imidazole antifungal agent inhibiting ergosterol synthesis in fungal cell membranes.',
 'Superficial dermatomycoses: tinea pedis (athlete foot), tinea cruris, tinea corporis, and cutaneous candidiasis.',
 'Local cutaneous irritation, stinging, erythema, burning sensation, mild peeling.',
 'Continue application for 1–2 weeks following clinical resolution to prevent fungal recurrence. Keep infected cutaneous areas clean and dry.',
 'CDC Fungal Guidelines / NHS'),

(17, 'Sumatriptan',
 'Selective serotonin 5-HT1B/1D receptor agonist promoting cranial vasoconstriction and inhibiting neuropeptide release.',
 'Acute abortive treatment of migraine attacks with or without aura.',
 'Chest tightness or pressure (prinzmetal sensation), flushing, dizziness, tingling/numbness, neck stiffness sensation.',
 'Take at the onset of migraine headache (not during aura phase). Strictly contraindicated in ischemic heart disease or uncontrolled hypertension.',
 'International Headache Society / FDA'),

(18, 'Ferrous Sulfate',
 'Oral iron salt providing bioavailable elemental iron for hemoglobin synthesis and erythrocyte maturation.',
 'Prevention and treatment of confirmed iron-deficiency anemia.',
 'Dark or black stools, constipation, nausea, abdominal cramping, epigastric distress.',
 'Absorption is enhanced when ingested on an empty stomach or with Vitamin C; take with food if nausea occurs. Keep away from small children.',
 'WHO Guidelines / British Society of Gastroenterology'),

(19, 'Betahistine',
 'Histamine H1 agonist and H3 antagonist that improves microvascular circulation in the inner ear stria vascularis.',
 'Reduction of vertigo frequency and inner ear fullness in vestibular disorders and Ménière disease.',
 'Mild headache, gastric fullness, nausea, allergic skin reactions.',
 'Use cautiously in patients with bronchial asthma or active peptic ulcer disease history.',
 'BNF / MedlinePlus'),

(20, 'Meclizine',
 'First-generation piperazine antihistamine with prominent anticholinergic and central vestibular antiemetic properties.',
 'Management of vestibular vertigo, lightheadedness, and motion sickness symptoms.',
 'Sedation, drowsiness, xerostomia (dry mouth), blurred vision, urinary retention in elderly.',
 'Avoid alcohol or central nervous system sedatives. Use with caution in patients with glaucoma or prostatic hypertrophy.',
 'American Academy of Otolaryngology / FDA')
ON DUPLICATE KEY UPDATE
  `description` = VALUES(`description`),
  `uses` = VALUES(`uses`),
  `side_effects` = VALUES(`side_effects`),
  `precautions` = VALUES(`precautions`),
  `reference_source` = VALUES(`reference_source`);
