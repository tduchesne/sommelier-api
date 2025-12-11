# ARCHITECTURE TECHNIQUE DÉTAILLÉE - SOMMELIER NUMÉRIQUE

Ce document est destiné aux développeurs souhaitant comprendre la mécanique interne du projet. Il détaille les choix d'implémentation, les flux de données et les patterns utilisés.

## 1. Vue d'ensemble (Big Picture)

Le projet suit une architecture **Client-Serveur REST** classique.

*   **Client (Mobile)** : React Native (Expo). Il ne stocke rien de permanent. Il demande des données à l'API et les affiche.
*   **Serveur (API)** : Spring Boot. Il contient toute la logique métier ("Business Logic"). Il sécurise l'accès aux données.
*   **Base de Données** : PostgreSQL. Elle stocke la vérité (Vins, Plats, Relations).

---

## 2. BACKEND (Spring Boot)

Le code est structuré en **couches (Layers)**. Une requête traverse ces couches dans cet ordre précis :

`Client` -> `Controller` -> `Service` -> `Repository` -> `Database`

### A. Structure des Dossiers (`src/main/java/com/sommelier/api`)

1.  **`model` (ou `entity`)** : Les objets qui représentent les tables de la BD.
   *   *Exemple :* `Vin.java` contient les annotations `@Entity` et `@Id`. C'est le miroir Java de la table SQL `vin`.
2.  **`repository`** : L'interface de communication avec la BD.
   *   *Magie Spring Data :* On étend `JpaRepository`. On n'écrit presque pas de SQL. Spring génère les requêtes `findAll`, `save`, `delete` automatiquement.
3.  **`service`** : Le cerveau. La logique métier.
   *   *Rôle :* C'est ici qu'on vérifie si un vin existe avant de le modifier, ou qu'on calcule des stats. Le Controller ne doit pas contenir de logique complexe, il délègue au Service.
4.  **`controller`** : Le guichetier (Point d'entrée HTTP).
   *   *Rôle :* Il reçoit les requêtes (GET /vins), valide les paramètres basiques, appelle le Service, et renvoie la réponse JSON (200 OK, 404 Not Found).

### B. Point Clé : Le Filtrage Dynamique (`VinSpecification.java`)

C'est la partie la plus technique du backend.
Pour permettre une recherche combinée (Prix + Couleur + Région), nous n'écrivons pas une énorme requête SQL avec des `IF`.

Nous utilisons l'API **JPA Criteria**.
*   **Concept :** On construit la clause `WHERE` de manière programmatique.
*   **Fonctionnement :** On crée une liste de `Predicates` (conditions). Si le paramètre `couleur` est présent, on ajoute un prédicat `criteriaBuilder.equal(root.get("couleur"), couleur)`.
*   À la fin, on fait un `AND` de tous les prédicats.

---

## 3. FRONTEND (React Native / Expo)

L'application utilise **Expo Router** (basé sur des fichiers) plutôt que React Navigation classique.

### A. Structure des Dossiers

1.  **`app/`** : Chaque fichier ici devient un écran.
   *   `app/(tabs)/index.tsx` : L'écran d'accueil (Liste des vins).
   *   `app/_layout.tsx` : Le "Wrapper" global. C'est ici qu'on charge les polices, qu'on gère le thème (Dark Mode) et la navigation globale.
2.  **`components/`** : Les briques LEGO réutilisables.
   *   `WineCard.tsx` : Le composant qui affiche un seul vin. Il reçoit les données via des `props`.
   *   `FilterModal.tsx` : La modale complexe pour choisir prix/région.
3.  **`constants/`** : Les couleurs, styles partagés.

### B. Point Clé : Gestion d'État (State Management)

Dans `index.tsx`, nous utilisons le Hook `useState` pour gérer les filtres :

```typescript
const [filters, setFilters] = useState({
    minPrix: null,
    maxPrix: null,
    couleur: null,
    region: null,
    search: '',
    menuType: null, 
    allergenes: []  
});