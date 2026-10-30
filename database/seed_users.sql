-- ============================================================================
-- Medical Diagnosis System (MDS) — Seed Demonstration Users
-- Hashed password for demo user 'admin' (password: '1234') using SHA-256
-- SHA-256('1234') = 03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4
-- ============================================================================

USE `providencedb`;

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `role`) VALUES
(1, 'admin', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'administrator')
ON DUPLICATE KEY UPDATE
  `password_hash` = VALUES(`password_hash`),
  `role` = VALUES(`role`);
