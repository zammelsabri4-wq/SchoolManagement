# School Management System — DS1 Spring Boot

Application REST de gestion scolaire développée avec Spring Boot, Spring Data JPA et MySQL.

## Technologies
- Java 21
- Spring Boot 3.5.7
- Spring Web
- Spring Data JPA / Hibernate
- MySQL
- Bean Validation
- Maven
- Postman (tests manuels)

## Modèle métier : 6 entités
1. `Etudiant` : nom, prénom, email, classe.
2. `Enseignant` : nom, prénom, email.
3. `Classe` : nom, niveau.
4. `Cours` : titre, volume horaire, enseignant, classe.
5. `Inscription` : date, étudiant, cours.
6. `Note` : valeur sur 20, commentaire, date, inscription.

## Relations principales
- Une classe possède plusieurs étudiants (`Classe` 1 — N `Etudiant`).
- Une classe peut proposer plusieurs cours (`Classe` 1 — N `Cours`).
- Un enseignant peut assurer plusieurs cours (`Enseignant` 1 — N `Cours`).
- Un étudiant peut avoir plusieurs inscriptions (`Etudiant` 1 — N `Inscription`).
- Un cours peut avoir plusieurs inscriptions (`Cours` 1 — N `Inscription`).
- Une inscription peut avoir plusieurs notes (`Inscription` 1 — N `Note`).

## Installation
1. Installer un JDK 21 et MySQL.
2. Créer la base :
   ```sql
   CREATE DATABASE school_db;
   ```
3. Ouvrir le projet dans IntelliJ IDEA via `pom.xml`.
4. Modifier `src/main/resources/application.properties` pour indiquer votre identifiant et mot de passe MySQL.
5. Laisser Maven télécharger les dépendances.
6. Exécuter `SchoolManagementApplication`.

Hibernate crée/met à jour les tables automatiquement grâce à `spring.jpa.hibernate.ddl-auto=update`.

## API REST

Toutes les entités disposent des opérations suivantes :

| Ressource | URL |
|---|---|
| Étudiants | `/api/etudiants` |
| Enseignants | `/api/enseignants` |
| Classes | `/api/classes` |
| Cours | `/api/cours` |
| Inscriptions | `/api/inscriptions` |
| Notes | `/api/notes` |

Pour chaque ressource :
- `GET /api/{ressource}` : lister
- `GET /api/{ressource}/{id}` : consulter un élément
- `POST /api/{ressource}` : créer
- `PUT /api/{ressource}/{id}` : modifier
- `DELETE /api/{ressource}/{id}` : supprimer

## Requêtes personnalisées
- `GET /api/etudiants/recherche?nom=Ali` n'est pas exposé par défaut ; la méthode repository `findByNomContainingIgnoreCase` est disponible.
- `CoursRepository.findByEnseignantId(id)` : cours d'un enseignant.
- `InscriptionRepository.findByEtudiantId(id)` : inscriptions d'un étudiant.
- D'autres méthodes sont présentes dans les repositories.

Pour présenter et tester ces requêtes dans Postman, ajouter au besoin des endpoints dédiés dans les contrôleurs.

## Exemples Postman

### Créer une classe
`POST http://localhost:8081/api/classes`
```json
{
  "nom": "L3 BIS A",
  "niveau": 3
}
```

### Créer un enseignant
`POST http://localhost:8081/api/enseignants`
```json
{
  "nom": "Ben Ali",
  "prenom": "Sami",
  "email": "sami.benali@example.com"
}
```

### Créer un étudiant lié à la classe 1
`POST http://localhost:8081/api/etudiants`
```json
{
  "nom": "Trabelsi",
  "prenom": "Amine",
  "email": "amine.trabelsi@example.com",
  "classe": { "id": 1 }
}
```

### Créer un cours lié à l'enseignant 1 et à la classe 1
`POST http://localhost:8081/api/cours`
```json
{
  "titre": "Bases de données",
  "volumeHoraire": 42,
  "enseignant": { "id": 1 },
  "classe": { "id": 1 }
}
```

### Créer une inscription liée à l'étudiant 1 et au cours 1
`POST http://localhost:8081/api/inscriptions`
```json
{
  "dateInscription": "2026-11-06",
  "etudiant": { "id": 1 },
  "cours": { "id": 1 }
}
```

### Créer une note liée à l'inscription 1
`POST http://localhost:8081/api/notes`
```json
{
  "valeur": 16.5,
  "commentaire": "Bon travail",
  "dateEvaluation": "2026-11-06",
  "inscription": { "id": 1 }
}
```

## Codes HTTP
- `200 OK` : lecture ou modification réussie.
- `201 Created` : création réussie.
- `204 No Content` : suppression réussie.
- `400 Bad Request` : données invalides.
- `404 Not Found` : ressource inexistante.
- `409 Conflict` : violation d'une contrainte de base de données.

## Remarque pédagogique
Ce projet constitue une base de travail. Avant la remise, vérifiez les règles métier, ajoutez les endpoints de recherche personnalisée souhaités, testez les cas d'erreur dans Postman, exportez la collection Postman et préparez-vous à expliquer chaque classe.
