-- Script d'insertion dans la base de données ENCHERES_DB
--   type :      SQL Server 2022
--

-- Adresses Campus ENI Ecole Nantes
INSERT INTO ADRESSES (rue,code_postal,ville, adresse_eni) VALUES ('2B RUE BENJAMIN FRANKLIN','44800','SAINT HERBLAIN',1);
DECLARE @AddNantesFranklin AS INTEGER;
SET @AddNantesFranklin = SCOPE_IDENTITY();

INSERT INTO ADRESSES (rue,code_postal,ville, adresse_eni) VALUES ('3 RUE MICKAËL FARADAY','44800','SAINT HERBLAIN', 1);
DECLARE @AddNantesFaraday AS INTEGER;
SET @AddNantesFaraday = SCOPE_IDENTITY();

-- Adresse Campus ENI Ecole Rennes
INSERT INTO ADRESSES (rue,code_postal,ville, adresse_eni) VALUES ('8 RUE LÉO LAGRANGE','35131','CHARTRES DE BRETAGNE', 1);
DECLARE @AddRennes AS INTEGER;
SET @AddRennes = SCOPE_IDENTITY();
-- Adresse Campus ENI Ecole Quimper
INSERT INTO ADRESSES (rue,code_postal,ville, adresse_eni) VALUES ('2 RUE GEORGES PERROS','29000','QUIMPER', 1);
-- Adresse Campus ENI Ecole Niort
INSERT INTO ADRESSES (rue,code_postal,ville, adresse_eni) VALUES ('19 AVENUE LÉO LAGRANGE','79000','NIORT', 1);

-- Catégories
INSERT INTO CATEGORIES (libelle) VALUES ('Ameublement');
DECLARE @CatMeuble AS INTEGER;
SET @CatMeuble = SCOPE_IDENTITY();

INSERT INTO CATEGORIES (libelle) VALUES ('Informatique');
DECLARE @CatInfo AS INTEGER;
SET @CatInfo = SCOPE_IDENTITY();

INSERT INTO CATEGORIES (libelle) VALUES ('Sport & Loisir');

INSERT INTO CATEGORIES (libelle) VALUES ('Vêtement');
DECLARE @CatVet AS INTEGER;
SET @CatVet = SCOPE_IDENTITY();

-- chiffrement des mots de passe avec {bcrypt}
-- L'administrateur pour les tests des formateurs
DECLARE @CoachAdmin VARCHAR(30);
SET @CoachAdmin = 'coach_admin';
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe, administrateur,	no_adresse) VALUES (@CoachAdmin,'COACH', 'Eni', 'coach@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', 1, @AddNantesFranklin);-- mot_de_passe = Pa$$w0rd

-- Utilisateur toto pour les tests des formateurs
DECLARE @CoachToto VARCHAR(30);
SET @CoachToto = 'coach_toto';
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe, administrateur,	no_adresse) VALUES (@CoachToto,'Toto', 'Eni', 'toto@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', 0, @AddNantesFaraday);-- mot_de_passe = Pa$$w0rd

-- Utilisateur titi pour les tests des formateurs
DECLARE @CoachTiti VARCHAR(30);
SET @CoachTiti = 'coach_titi';
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe,	no_adresse) VALUES (@CoachTiti,'Titi', 'Eni', 'titi@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', @AddRennes);-- mot_de_passe = Pa$$w0rd

-- Utilisateur tata pour les tests des formateurs
DECLARE @CoachTata VARCHAR(30);
SET @CoachTata = 'coach_tata';
INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, mot_de_passe, administrateur,	no_adresse) VALUES (@CoachTata,'Tata', 'Eni', 'tata@campus-eni.fr', '{bcrypt}$2a$10$tKtwPcqKLa6Hw8nnGr96LeN.hxzhjXGzQe1DuN6hjlnRGJgiTqcMu', 0, @AddNantesFaraday);-- mot_de_passe = Pa$$w0rd


-- 4 articles de tests sans enchère
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial,  id_utilisateur , no_categorie , no_adresse_retrait)
	VALUES ('PC ENI', 'Un PC de salle de cours', DATEADD (DAY , 1 , GETDATE() ), DATEADD (DAY , 15 , GETDATE() ), 1, @CoachToto, @CatInfo,@AddNantesFaraday);
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial,  id_utilisateur , no_categorie , no_adresse_retrait)
	VALUES ('Disque Dur', 'Disque dur externe', DATEADD (DAY , -1 , GETDATE() ), DATEADD (DAY , 20 , GETDATE() ), 1, 1, @CoachToto, @CatInfo, @AddNantesFaraday);
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial,  id_utilisateur , no_categorie , no_adresse_retrait)
	VALUES ('PC Gamer pour travailler', 'Un PC de Gamer avec une RAM de 36Go', DATEADD (DAY , 1 , GETDATE() ), DATEADD (DAY , 15 , GETDATE() ), 2, @CoachToto, @CatInfo,@AddNantesFranklin);
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial,  id_utilisateur , no_categorie , no_adresse_retrait)
	VALUES ('Baskets blanches (femme)', 'Chaussures pour le sport ou tous les jours', DATEADD (DAY , -1 , GETDATE() ), DATEADD (DAY , 10 , GETDATE() ), 1, 1, @CoachTiti, @CatVet, @AddRennes);


DECLARE @ArticleEnchereEnCours AS INTEGER;
SET @ArticleEnchereEnCours = SCOPE_IDENTITY();
DECLARE @Offre AS INTEGER;
SET @Offre = 2;
-- 2 enchères sur 1 même article
	INSERT INTO ENCHERES (id_utilisateur , no_article, date_enchere,montant_enchere) VALUES (@CoachTiti,@ArticleEnchereEnCours,  DATEADD (DAY , -2 , GETDATE() ), @Offre);
	--Mise à jour article 
	UPDATE ARTICLES_A_VENDRE SET prix_vente = @Offre WHERE no_article = @ArticleEnchereEnCours;
	-- Mise à jour des points pour l'acquéreur
	UPDATE UTILISATEURS SET credit = (SELECT credit FROM UTILISATEURS WHERE pseudo = @CoachTiti) - @Offre  WHERE pseudo = @CoachTiti;
	-- enchère
	INSERT INTO ENCHERES (id_utilisateur , no_article, date_enchere,montant_enchere) VALUES (@CoachTata,@ArticleEnchereEnCours,  DATEADD (DAY , -1 , GETDATE() ), @Offre + 1);
	--Mise à jour article 
	UPDATE ARTICLES_A_VENDRE SET prix_vente = @Offre + 1  WHERE no_article = @ArticleEnchereEnCours;
	-- Mise à jour des points pour l'acquéreur
	UPDATE UTILISATEURS SET credit = (SELECT credit FROM UTILISATEURS WHERE pseudo = @CoachTata) - (@Offre+1)  WHERE pseudo = @CoachTata;
	
-- 1 article avec enchère clôturée + 1 enchère faite
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial, prix_vente,  id_utilisateur , no_categorie , no_adresse_retrait)
	VALUES ('Chaise', 'Chaise de bureau', DATEADD (DAY , -3 , GETDATE() ),DATEADD (DAY , -1 , GETDATE() ), 2,1,2, @CoachToto, @CatMeuble,@AddNantesFranklin);
DECLARE @ArticleEnchereCloturee AS INTEGER;
SET @ArticleEnchereCloturee = SCOPE_IDENTITY();
	-- enchère
	INSERT INTO ENCHERES (id_utilisateur , no_article, date_enchere,montant_enchere) VALUES (@CoachTiti,@ArticleEnchereCloturee,  DATEADD (DAY , -2 , GETDATE() ), @Offre);
	-- Mise à jour point acquéreur
	UPDATE UTILISATEURS SET credit = (SELECT credit FROM UTILISATEURS WHERE pseudo = @CoachTiti) - @Offre  WHERE pseudo = @CoachTiti;
	--Mise à jour article 
	UPDATE ARTICLES_A_VENDRE SET prix_vente = @Offre WHERE no_article = @ArticleEnchereCloturee;
	-- Mise à jour des points pour du vendeur
	UPDATE UTILISATEURS SET credit = (SELECT credit FROM UTILISATEURS WHERE pseudo = @CoachToto) + @Offre  WHERE pseudo = @CoachToto;
	
-- 1 article avec enchère annulée
INSERT INTO ARTICLES_A_VENDRE (nom_article, description, date_debut_encheres, date_fin_encheres, statut_enchere, prix_initial,  id_utilisateur , no_categorie , no_adresse_retrait)
	VALUES ('Bureau', 'Bureau d''ordinateur', DATEADD (DAY , +2 , GETDATE() ), DATEADD (DAY , +16 , GETDATE() ),100, 1, @CoachTiti, @CatMeuble,@AddNantesFranklin);

INSERT INTO articles (
    no_article, nom_article, description, photo, date_debut_encheres,
    date_fin_encheres, statut_enchere, prix_initial, prix_vente,
    id_utilisateur, no_categorie, no_adresse_retrait
) VALUES
      (1, 'PC ENI 2', 'Un PC de salle de cours', 'uploads/photos/article_1_7541ed7b-d87e-4feb-9392-26cbadf3c43e.jpg', '2025-06-11', '2025-06-25', 1, 1, 999, 'coach_toto', 2, 2),
      (2, 'Disque Dur', 'Disque dur externe', 'uploads/photos/article_2_39e8a415-1fe0-4962-b5c1-adca0a92bba1.jpg', '2025-06-09', '2025-06-30', 1, 1, 4, 'coach_toto', 2, 2),
      (3, 'PC Gamer pour travailler', 'Un PC de Gamer avec une RAM de 36Go', 'uploads/photos/article_3_49fa4a4e-891f-460a-aefc-743a11fcf838.jpg', '2025-06-11', '2025-06-25', 1, 2, 3, 'coach_toto', 2, 1),
      (4, 'Baskets blanches', 'Chaussures pour le sport ou tous les jours', 'uploads/photos/article_4_fe98afac-9b6d-4761-bc0a-079fe103994b.png', '2025-06-09', '2025-06-20', 1, 2, 10, 'coach_titi', 4, 3),
      (5, 'Chaise', 'Chaise de bureau', 'uploads/photos/article_5_ff107f0b-b6d7-4d51-aea5-a752d546b583.jpg', '2025-06-07', '2025-06-10', 2, 1, 2, 'coach_toto', 1, 1),
      (6, 'Bureau', 'Bureau d''ordinateur', 'uploads/photos/article_6_4b6cad53-f3cd-443a-bc2c-d1f57d65b4d8.jpg', '2025-06-12', '2025-06-26', 1, 1, 9, 'coach_titi', 1, 1),
      (7, 'Jouet d''enfant', 'Parfait pour les enfants de 3 à 8 ans.', 'uploads/photos/article_7_4468c2ca-5c8e-4808-ba72-29d70e3b5a75.jpg', '2025-06-17', '2025-06-18', 2, 1, 2, 'admin', 3, 17),
      (8, 'Ballon de football', 'Ballon de la coupe du monde 2020', 'uploads/photos/article_8_4c01d02c-1956-40a0-92cc-16d7d8d5ea75.jpg', '2025-06-18', '2025-06-25', 1, 1, 2, 'matteo', 4, 19),
      (9, 'Tambour', 'Si vous voulez commencer la musique c''est ce qu''il vous faut', 'uploads/photos/article_9_d9d7a604-fbe0-4932-a4d1-63cc7893edec.jpg', '2025-06-17', '2025-06-17', 2, 1, 5, 'admin', 2, 17),
      (10, 'Trampolinee', 'trampoline pour enfants', 'uploads/photos/article_10_d54ea371-dc17-4672-9ce1-c1c6101e40d4.jpg', '2025-06-18', '2025-06-20', 1, 2, 0, 'admin', 3, 17),
      (11, 'Magazine', 'magazine de meubles', 'uploads/photos/article_11_8302117e-9785-4668-ae82-f8014c83cf17.jpg', '2025-06-18', '2025-06-20', 1, 5, 6, 'admin2', 1, 21),
      (17, 'Chien', 'Pour un magnifique cadeau de Noël.', 'uploads/photos/article_17_4c611cac-c837-4477-8cb5-95aef852a6a0.jpg', '2025-06-19', '2025-06-20', 1, 5, 0, 'administrateur', 3, 23);
