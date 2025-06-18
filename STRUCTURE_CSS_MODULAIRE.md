# Structure CSS Modulaire - ENI-Enchères

## Vue d'ensemble

Le projet ENI-Enchères utilise maintenant une architecture CSS modulaire avec un fichier CSS spécifique pour chaque page, organisé de manière claire et maintenable.

## Structure des fichiers CSS

```
src/main/resources/static/css/
├── layout.css      # Styles généraux et layout principal
├── connexion.css   # Page de connexion
├── compte.css      # Page de création de compte
├── encheres.css    # Page de listing des enchères
├── detail.css      # Page de détail d'enchère
├── vente.css       # Page de nouvelle vente
├── profil.css      # Pages de profil utilisateur
├── home.css        # Page d'accueil
├── error.css       # Pages d'erreur
└── photo.css       # Page d'ajout de photo
```

## Fonctionnement

### 1. CSS Général (`layout.css`)
- Contient les variables CSS personnalisées (couleurs ENI, ombres, etc.)
- Styles du header et footer
- Overrides Bootstrap
- Classes utilitaires communes
- Animations globales

### 2. CSS Spécifiques par page
Chaque page a son propre fichier CSS contenant :
- Styles spécifiques aux éléments de cette page
- Animations et transitions dédiées
- Media queries responsive
- Styles de validation et interaction

### 3. Système d'inclusion automatique
Le template `layout.html` inclut automatiquement le CSS spécifique grâce au paramètre `cssSpecifique` :

```html
<html data-th-fragment="layout(contenu,titre,cssSpecifique)">
    <!-- CSS général toujours inclus -->
    <link rel="stylesheet" data-th-href="@{/css/layout.css}">
    
    <!-- CSS spécifique inclus conditionnellement -->
    <link data-th-if="${cssSpecifique != null}" 
          rel="stylesheet" 
          data-th-href="@{'/css/' + ${cssSpecifique} + '.css'}">
</html>
```

### 4. Utilisation dans les templates
Chaque template spécifie son CSS via le paramètre `cssSpecifique` :

```html
<html data-th-replace="~{layout::layout(contenu=~{::main},titre=~{::title},cssSpecifique='connexion')}">
```

## Avantages de cette approche

### ✅ Performance
- Seul le CSS nécessaire est chargé par page
- Réduction de la taille des fichiers à télécharger
- Pas de CSS inutile

### ✅ Maintenabilité
- Code organisé et facile à localiser
- Modifications isolées par page
- Réduction des conflits de styles

### ✅ Évolutivité
- Facile d'ajouter de nouvelles pages
- Structure claire pour les nouveaux développeurs
- Possibilité d'optimiser chaque page individuellement

### ✅ Cohérence
- Variables CSS communes dans `layout.css`
- Style ENI préservé sur toutes les pages
- Bootstrap comme base commune

## Variables CSS communes

Définies dans `layout.css` et utilisables dans tous les fichiers :

```css
:root {
    --eni-primary: #667eea;
    --eni-secondary: #764ba2;
    --eni-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    --eni-light: #f8f9fa;
    --eni-dark: #343a40;
    --eni-shadow: 0 2px 10px rgba(0,0,0,0.1);
    --eni-shadow-lg: 0 5px 20px rgba(0,0,0,0.1);
    --eni-border-radius: 0.375rem;
}
```

## Exemples d'utilisation

### Page de connexion (`connexion.css`)
```css
.connexion-main {
    min-height: calc(100vh - 200px);
    display: flex;
    align-items: center;
    justify-content: center;
}

.form-container {
    background: white;
    border-radius: var(--eni-border-radius);
    box-shadow: var(--eni-shadow-lg);
}
```

### Page d'enchères (`encheres.css`)
```css
.encheres-card {
    transition: all 0.3s ease;
    border: none;
    box-shadow: var(--eni-shadow);
}

.encheres-card:hover {
    transform: translateY(-5px);
    box-shadow: var(--eni-shadow-lg);
}
```

## Responsive Design

Chaque fichier CSS inclut ses propres media queries adaptées :

```css
/* Mobile */
@media (max-width: 768px) {
    .vente-container {
        margin: 1rem;
        padding: 1.5rem;
    }
}
```

## Ajout d'une nouvelle page

Pour ajouter une nouvelle page :

1. Créer le fichier CSS : `src/main/resources/static/css/ma-page.css`
2. Ajouter les styles spécifiques à la page
3. Dans le template HTML, ajouter : `cssSpecifique='ma-page'`

Exemple :
```html
<html data-th-replace="~{layout::layout(contenu=~{::main},titre=~{::title},cssSpecifique='ma-page')}">
```

## Migration depuis l'ancien système

✅ **Terminé** : Migration des 11 fichiers CSS originaux vers 9 fichiers modulaires
✅ **Terminé** : Mise à jour de tous les templates HTML
✅ **Terminé** : Conservation de l'identité visuelle ENI
✅ **Terminé** : Intégration complète avec Bootstrap 5.3.2

## Fichiers convertis

| Template | CSS Spécifique | Description |
|----------|----------------|-------------|
| `layout.html` | `layout.css` | Styles généraux et layout |
| `view-connexion.html` | `connexion.css` | Page de connexion |
| `view-creer-compte.html` | `compte.css` | Création de compte |
| `view-enchere.html` | `encheres.css` | Liste des enchères |
| `view-detail.html` | `detail.css` | Détail d'enchère |
| `view-nouvelle-vente.html` | `vente.css` | Nouvelle vente |
| `view-profil.html` | `profil.css` | Profil utilisateur |
| `home.html` | `home.css` | Page d'accueil |
| `error.html` | `error.css` | Pages d'erreur |
| `view-ajout-photo.html` | `photo.css` | Ajout de photo |

Cette architecture offre une base solide et modulaire pour le développement et la maintenance du projet ENI-Enchères. 