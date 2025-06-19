-- Script de création de la base de données ENCHERES_DB
--   type :      SQL Server 2022
--

/*
drop table ENCHERES;
drop table ARTICLES_A_VENDRE;
drop table CATEGORIES;
drop table UTILISATEURS;
drop table ADRESSES;
*/

CREATE TABLE ADRESSES (
    no_adresse   INTEGER IDENTITY(1,1) NOT NULL,
    rue              VARCHAR(100) NOT NULL,
    code_postal      VARCHAR(10) NOT NULL,
    ville            VARCHAR(50) NOT NULL,
	adresse_eni		bit NOT NULL default 0 -- Indique si c'est une adresse ENI Ecole
);
ALTER TABLE ADRESSES ADD CONSTRAINT adresse_pk PRIMARY KEY (no_adresse);


CREATE TABLE CATEGORIES (
    no_categorie   INTEGER IDENTITY(1,1) NOT NULL,
    libelle        VARCHAR(30) NOT NULL
);
ALTER TABLE CATEGORIES ADD CONSTRAINT categorie_pk PRIMARY KEY (no_categorie);
ALTER TABLE CATEGORIES ADD CONSTRAINT uc_categories UNIQUE (libelle);

CREATE TABLE UTILISATEURS (
    pseudo           VARCHAR(30) NOT NULL,
    nom              VARCHAR(40) NOT NULL,
    prenom           VARCHAR(50) NOT NULL,
    email            VARCHAR(100) NOT NULL,
    telephone        VARCHAR(15),
    mot_de_passe     VARCHAR(68) NOT NULL,
    credit           INTEGER NOT NULL default 10,
    administrateur   bit NOT NULL default 0,
	no_adresse		 INTEGER NOT NULL 
);
ALTER TABLE UTILISATEURS ADD CONSTRAINT uc_utilisateurs UNIQUE (pseudo,email);
ALTER TABLE UTILISATEURS ADD CONSTRAINT utilisateur_pk PRIMARY KEY (pseudo);
ALTER TABLE UTILISATEURS
    ADD CONSTRAINT utilisateur_adresse_fk FOREIGN KEY ( no_adresse ) REFERENCES ADRESSES ( no_adresse )
ON DELETE NO ACTION 
    ON UPDATE no action ;

CREATE TABLE ARTICLES_A_VENDRE (
    no_article                    INTEGER IDENTITY(1,1) NOT NULL,
    nom_article                   VARCHAR(30) NOT NULL,
    description                   VARCHAR(300) NOT NULL,
	photo					  	  INTEGER NULL,
	date_debut_encheres           DATE NOT NULL,
    date_fin_encheres             DATE NOT NULL,
	statut_enchere				  INTEGER NOT NULL default 0, -- 0 : PAS COMMENCEE, 1 : EN COURS, 2 : CLOTUREE, 3 : LIVREE,  100 : ANNULEE
    prix_initial                  INTEGER NOT NULL,
    prix_vente           		  INTEGER,
    id_utilisateur                VARCHAR(30) NOT NULL,
    no_categorie                  INTEGER NOT NULL,	
	no_adresse_retrait		 	  INTEGER NOT NULL 
);
ALTER TABLE ARTICLES_A_VENDRE ADD CONSTRAINT articles_vendus_pk PRIMARY KEY (no_article);

ALTER TABLE ARTICLES_A_VENDRE
    ADD CONSTRAINT articles_vendus_categories_fk FOREIGN KEY ( no_categorie )
        REFERENCES categories ( no_categorie )
ON DELETE NO ACTION 
ON UPDATE no action ;

ALTER TABLE ARTICLES_A_VENDRE
    ADD CONSTRAINT ventes_utilisateur_fk FOREIGN KEY ( id_utilisateur )
        REFERENCES utilisateurs ( pseudo )
ON DELETE NO ACTION 
    ON UPDATE no action ;
ALTER TABLE ARTICLES_A_VENDRE
    ADD CONSTRAINT encheres_adresse_fk FOREIGN KEY ( no_adresse_retrait ) REFERENCES ADRESSES ( no_adresse )
ON DELETE NO ACTION 
    ON UPDATE no action ;

CREATE TABLE ENCHERES (
    id_utilisateur   VARCHAR(30) NOT NULL,
    no_article       INTEGER NOT NULL,
    montant_enchere  INTEGER NOT NULL,
	date_enchere     datetime NOT NULL
);
-- Pour permettre de lister les enchères et de gérer le cas où 1 acquéreur enchérit plusieurs fois. 
-- La clef primaire repose sur 3 colonnes
ALTER TABLE ENCHERES ADD CONSTRAINT enchere_pk PRIMARY KEY (id_utilisateur, no_article, montant_enchere);
ALTER TABLE ENCHERES
    ADD CONSTRAINT encheres_articles_vendus_fk FOREIGN KEY ( no_article )
        REFERENCES ARTICLES_A_VENDRE ( no_article )
ON DELETE NO ACTION 
    ON UPDATE no action ;
ALTER TABLE ENCHERES
    ADD CONSTRAINT encheres_utilisateurs_encherisseur_fk FOREIGN KEY ( id_utilisateur )
        REFERENCES UTILISATEURS ( pseudo )
ON DELETE NO ACTION 
    ON UPDATE no action ;

ALTER TABLE ARTICLES_A_VENDRE
ALTER COLUMN photo VARCHAR(MAX);


