# 🏆 ENI-Enchere - Plateforme d'Enchères en Ligne

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen?style=for-the-badge&logo=spring)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.2-purple?style=for-the-badge&logo=bootstrap)
![SQL Server](https://img.shields.io/badge/SQL%20Server-Database-red?style=for-the-badge&logo=microsoft-sql-server)

*Une application moderne d'enchères en ligne développée avec Spring Boot*

</div>

## 📋 Table des Matières

- [🎯 Présentation](#-présentation)
- [🚀 Fonctionnalités](#-fonctionnalités)
- [🏗️ Architecture](#%EF%B8%8F-architecture)
- [💻 Technologies](#-technologies)
- [📦 Installation](#-installation)
- [🔧 Configuration](#-configuration)
- [🎨 Interface Utilisateur](#-interface-utilisateur)
- [🔐 Sécurité](#-sécurité)
- [📱 Responsive Design](#-responsive-design)
- [🔄 Refactoring CSS](#-refactoring-css)
- [📂 Structure du Projet](#-structure-du-projet)
- [🚀 Démarrage](#-démarrage)
- [🧪 Tests](#-tests)
- [📈 Améliorations Apportées](#-améliorations-apportées)

## 🎯 Présentation

**ENI-Enchere** est une plateforme d'enchères en ligne moderne permettant aux utilisateurs de :
- Créer et gérer leurs comptes utilisateur
- Mettre en vente leurs articles avec photos
- Participer aux enchères d'autres utilisateurs
- Administrer la plateforme (pour les administrateurs)

Le projet a été entièrement **refactorisé** avec une architecture modulaire, un design moderne utilisant Bootstrap 5, et une sécurité renforcée avec Spring Security.

## 🚀 Fonctionnalités

### 👤 Gestion des Utilisateurs
- ✅ **Inscription** avec validation complète des données
- ✅ **Connexion/Déconnexion** sécurisée
- ✅ **Profil utilisateur** avec modification des informations
- ✅ **Gestion du crédit** pour les enchères
- ✅ **Système de rôles** (Utilisateur/Administrateur)

### 🛍️ Gestion des Articles
- ✅ **Création d'articles** avec description détaillée
- ✅ **Upload de photos** avec validation et redimensionnement
- ✅ **Catégorisation** des articles
- ✅ **Définition des prix** et durée d'enchère
- ✅ **Gestion du retrait** des articles

### 💰 Système d'Enchères
- ✅ **Enchères en temps réel** avec validation
- ✅ **Historique des enchères** par utilisateur
- ✅ **Notification des gagnants**
- ✅ **Gestion des crédits** automatique
- ✅ **Filtrage avancé** des enchères

### 🔧 Administration
- ✅ **Gestion globale des enchères**
- ✅ **Modération des articles**
- ✅ **Statistiques détaillées**
- ✅ **Gestion des utilisateurs**

## 🏗️ Architecture

Le projet suit une **architecture en couches** bien structurée :

```
src/main/java/eni/ecole/enienchere/
├── 🎮 controller/          # Contrôleurs Spring MVC
├── 💼 bll/                 # Business Logic Layer (Services)
├── 🗄️ dal/                 # Data Access Layer (DAO)
├── 📦 bo/                  # Business Objects (Entités)
├── 🔐 SecurityConfiguration.java
└── 🚀 EniEnchereApplication.java
```

### 🎮 Contrôleurs (Controller Layer)
- **EnchereController** - Gestion des enchères et du listing
- **UtilisateurController** - Authentification et profils
- **NouvelleVenteController** - Création d'articles
- **AjoutPhotoController** - Gestion des photos
- **GestionVendeurController** - Interface vendeur
- **HomeController** - Page d'accueil
- **CustomErrorController** - Gestion des erreurs

### 💼 Services (Business Logic Layer)
- **ArticleService** - Logique métier des articles
- **UtilisateurService** - Gestion des utilisateurs
- **EnchereService** - Logique des enchères
- **PhotoService** - Traitement des images
- **CategorieService** - Gestion des catégories

### 🗄️ DAO (Data Access Layer)
- **ArticleDAO** - Accès aux données des articles
- **UtilisateurDAO** - Persistance des utilisateurs  
- **EnchereDAO** - Gestion des enchères en base
- **PhotoDAO** - Stockage des photos
- **CategorieDAO** - Données des catégories
- **AdresseDAO** - Gestion des adresses

### 📦 Entités (Business Objects)
- **Utilisateur** - Entité utilisateur avec Spring Security
- **ArticleAVendre** - Article mis en vente
- **Enchere** - Enchère sur un article
- **Categorie** - Catégorie d'article
- **Adresse** - Adresse de retrait

## 💻 Technologies

### Backend
| Technologie | Version | Usage |
|-------------|---------|-------|
| **Java** | 17 | Langage principal |
| **Spring Boot** | 3.4.5 | Framework principal |
| **Spring Security** | 6.x | Authentification & autorisation |
| **Spring MVC** | 6.x | Architecture web |
| **Thymeleaf** | 3.x | Moteur de templates |
| **JDBC** | - | Accès aux données |
| **SQL Server** | - | Base de données |
| **Gradle** | - | Gestionnaire de build |

### Frontend
| Technologie | Version | Usage |
|-------------|---------|-------|
| **Bootstrap** | 5.3.2 | Framework CSS |
| **HTML5** | - | Structure des pages |
| **CSS3** | - | Styles personnalisés |
| **JavaScript** | - | Interactions client |

### Outils de Développement
- **Spring Boot DevTools** - Rechargement à chaud
- **Gradle Wrapper** - Build reproductible
- **JUnit 5** - Tests unitaires
- **Spring Security Test** - Tests de sécurité

## 📦 Installation

### Prérequis
- ☕ **Java 17** ou version supérieure
- 🗄️ **SQL Server** (local ou distant)
- 🔧 **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### Étapes d'Installation

1. **Cloner le repository**
```bash
git clone <repository-url>
cd ENI-Enchere
```

2. **Configuration de la base de données**
```sql
-- Créer la base de données
CREATE DATABASE ENCHERE;
```

3. **Modifier la configuration**
```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:sqlserver://localhost;databasename=ENCHERE
spring.datasource.username=votre_username
spring.datasource.password=votre_password
```

4. **Lancer l'application**
```bash
# Avec Gradle Wrapper
./gradlew bootRun

# Ou sur Windows
gradlew.bat bootRun
```

## 🔧 Configuration

### 📂 Configuration des Uploads
```properties
# Configuration des photos
app.upload.dir=uploads/photos
app.photo.max-size=5242880  # 5MB
app.photo.allowed-types=image/jpeg,image/jpg,image/png
```

### 🔐 Configuration de Sécurité
```properties
# Session timeout (5 minutes)
server.servlet.session.timeout=5m
```

### 🗄️ Configuration Base de Données
```properties
spring.datasource.url=jdbc:sqlserver://localhost;databasename=ENCHERE;integratedSecurity=false;encrypt=false;trustServerCertificate=false
spring.datasource.username=sa
spring.datasource.password=Pa$$w0rd
```

## 🎨 Interface Utilisateur

### 🎯 Design System
L'application utilise un **design system cohérent** basé sur Bootstrap 5 avec des couleurs personnalisées ENI :

```css
:root {
    --eni-primary: #667eea;      /* Bleu ENI */
    --eni-secondary: #764ba2;    /* Violet ENI */
    --eni-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    --eni-light: #f8f9fa;
    --eni-dark: #343a40;
    --eni-shadow: 0 2px 10px rgba(0,0,0,0.1);
}
```

### 📱 Pages Principales

#### 🏠 Page d'Accueil (`home.html`)
- Design moderne avec hero section
- Boutons d'action mis en valeur
- Navigation intuitive

#### 🔐 Authentification
- **Connexion** (`view-connexion.html`) - Formulaire Bootstrap stylisé
- **Inscription** (`view-creer-compte.html`) - Validation côté client et serveur

#### 💰 Enchères
- **Liste des enchères** (`view-enchere.html`) - Cartes Bootstrap avec filtres
- **Détail d'enchère** (`view-detail.html`) - Layout responsive en 2 colonnes
- **Gestion vendeur** (`view-gestion-vendeur.html`) - Interface d'administration

#### 👤 Profil Utilisateur
- **Affichage profil** (`view-profil.html`) - Cards Bootstrap
- **Modification profil** (`view-profil-modif.html`) - Formulaires validés
- **Mon profil** (`view-mon-profil.html`) - Vue personnelle

#### 🛍️ Vente
- **Nouvelle vente** (`view-nouvelle-vente.html`) - Formulaire complet
- **Ajout photo** (`view-ajout-photo.html`) - Upload avec prévisualisation

## 🔐 Sécurité

### 🔑 Authentification
- **Spring Security 6** pour l'authentification
- **BCrypt** pour le hachage des mots de passe
- **Sessions sécurisées** avec timeout configurable

### 🛡️ Autorisation
```java
// Configuration des rôles
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/gestion/**").hasRole("USER")
.requestMatchers("/", "/encheres", "/detail/**").permitAll()
```

### 🔒 Protection CSRF
- Protection CSRF activée sur tous les formulaires
- Tokens automatiques dans les templates Thymeleaf

### 📁 Upload Sécurisé
- Validation des types de fichiers (JPEG, PNG uniquement)
- Limitation de taille (5MB maximum)
- Stockage sécurisé hors du webroot

## 📱 Responsive Design

### 📐 Breakpoints Bootstrap
- **Mobile** : < 768px
- **Tablette** : 768px - 992px  
- **Desktop** : > 992px

### 🎯 Composants Adaptatifs
- **Navigation** - Hamburger menu sur mobile
- **Grilles** - Colonnes adaptatives 
- **Formulaires** - Optimisés pour le tactile
- **Cartes** - Empilage vertical sur mobile

## 🔄 Refactoring CSS

### 🎯 Objectifs Atteints
- ✅ **Consolidation** - 11 fichiers CSS → Architecture modulaire
- ✅ **Bootstrap 5** - Framework moderne et responsive
- ✅ **Performance** - Optimisation du chargement
- ✅ **Maintenabilité** - Code organisé et documenté

### 📂 Nouvelle Architecture CSS
```
src/main/resources/static/css/
├── layout.css      # 🎨 Styles généraux et variables
├── connexion.css   # 🔐 Page de connexion
├── compte.css      # 👤 Création de compte
├── encheres.css    # 💰 Liste des enchères
├── detail.css      # 🔍 Détail d'enchère
├── vente.css       # 🛍️ Nouvelle vente
├── profil.css      # 👤 Profil utilisateur
├── home.css        # 🏠 Page d'accueil
├── error.css       # ❌ Pages d'erreur
└── photo.css       # 📸 Upload de photos
```

### 🔧 Système d'Inclusion Automatique
Le template principal inclut automatiquement le CSS spécifique :
```html
<link data-th-if="${cssSpecifique != null}" 
      rel="stylesheet" 
      data-th-href="@{'/css/' + ${cssSpecifique} + '.css'}">
```

## 📂 Structure du Projet

```
ENI-Enchere/
├── 📋 build.gradle                     # Configuration Gradle
├── ⚙️ settings.gradle                  # Paramètres du projet
├── 📖 README.md                        # Documentation (ce fichier)
├── 📚 REFACTORING_CSS_BOOTSTRAP.md     # Doc refactoring Bootstrap
├── 📚 STRUCTURE_CSS_MODULAIRE.md       # Doc architecture CSS
├── 📚 CHANGEMENTS_CSS.md               # Historique des changements
├── 🗂️ src/
│   ├── 🎯 main/
│   │   ├── ☕ java/eni/ecole/enienchere/
│   │   │   ├── 🎮 controller/          # Contrôleurs Spring MVC
│   │   │   ├── 💼 bll/                 # Services (Business Logic)
│   │   │   ├── 🗄️ dal/                 # DAO (Data Access)
│   │   │   ├── 📦 bo/                  # Entités métier
│   │   │   ├── 🔐 SecurityConfiguration.java
│   │   │   └── 🚀 EniEnchereApplication.java
│   │   └── 📁 resources/
│   │       ├── ⚙️ application.properties
│   │       ├── 🎨 static/
│   │       │   ├── 🎨 css/             # Feuilles de style
│   │       │   ├── 📸 images/          # Images statiques
│   │       │   └── 💻 js/              # Scripts JavaScript
│   │       └── 📄 templates/           # Templates Thymeleaf
│   └── 🧪 test/                        # Tests unitaires
├── 📸 uploads/                         # Photos uploadées
├── 🗂️ build/                          # Fichiers compilés
└── 🔧 gradle/                         # Wrapper Gradle
```

## 🚀 Démarrage

### 🔥 Démarrage Rapide
```bash
# 1. Cloner le projet
git clone <repository-url>

# 2. Naviguer vers le dossier
cd ENI-Enchere

# 3. Configurer la base de données (voir application.properties)

# 4. Lancer l'application
./gradlew bootRun
```

### 🌐 Accès à l'Application
- **URL** : `http://localhost:8080`
- **Page d'accueil** : Interface moderne avec navigation
- **Connexion** : Créer un compte ou utiliser un compte existant

### 👥 Comptes de Test
Selon votre base de données, vous pourrez :
- Créer un nouveau compte utilisateur
- Accéder aux fonctionnalités d'enchère
- Tester l'upload de photos

## 🧪 Tests

### 🔬 Types de Tests
- **Tests unitaires** avec JUnit 5
- **Tests d'intégration** Spring Boot
- **Tests de sécurité** Spring Security Test

### ▶️ Exécution des Tests
```bash
# Lancer tous les tests
./gradlew test

# Tests avec rapport détaillé
./gradlew test --info
```

## 📈 Améliorations Apportées

### 🎨 Interface Utilisateur
- ✅ **Design moderne** avec Bootstrap 5.3.2
- ✅ **Responsive design** sur tous les appareils
- ✅ **Identité visuelle ENI** préservée
- ✅ **Accessibilité** améliorée (contraste, navigation)
- ✅ **Animations** fluides et professionnelles

### 🏗️ Architecture
- ✅ **Refactoring complet** de l'architecture CSS
- ✅ **Modularité** - Un CSS par page
- ✅ **Variables CSS** pour la cohérence
- ✅ **Performance** optimisée

### 🔐 Sécurité
- ✅ **Spring Security 6** moderne
- ✅ **Protection CSRF** complète
- ✅ **Upload sécurisé** des photos
- ✅ **Validation** côté client et serveur

### 📱 Fonctionnalités
- ✅ **Gestion complète** des enchères
- ✅ **Upload de photos** avec validation
- ✅ **Interface d'administration**
- ✅ **Gestion des erreurs** centralisée

### 🚀 Performance
- ✅ **Chargement optimisé** des ressources
- ✅ **CSS modulaire** par page
- ✅ **Images optimisées**
- ✅ **Cache approprié**

---

<div align="center">

### 🎓 Développé dans le cadre de la formation ENI École

**Technologies modernes • Design responsive • Architecture robuste**

*Pour toute question ou amélioration, n'hésitez pas à contribuer !*

</div> 