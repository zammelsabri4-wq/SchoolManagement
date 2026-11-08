# Guide de test Postman

Démarrer l'application puis tester chaque endpoint avec `http://localhost:8081`.

Exemple de séquence conseillée :
1. POST `/api/classes` pour créer une classe.
2. POST `/api/enseignants` pour créer un enseignant.
3. POST `/api/etudiants` avec `classe: {"id": 1}`.
4. POST `/api/cours` avec `enseignant: {"id": 1}` et `classe: {"id": 1}`.
5. POST `/api/inscriptions` avec l'étudiant et le cours.
6. POST `/api/notes` avec l'inscription.
7. Tester GET, PUT et DELETE pour chaque ressource.

Dans Postman, choisir Body > raw > JSON pour les requêtes POST et PUT.
