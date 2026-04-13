# 🎓 SmartCampus — Plateforme de Gestion Académique Microservices

> Plateforme académique complète construite par l'équipe **FullStackers**, sur une architecture **microservices** avec Spring Boot, Angular, Keycloak, RabbitMQ et Docker.

---

## 📑 Table des matières

- [Vue d'ensemble](#-vue-densemble)
- [Architecture Microservices](#-architecture-microservices)
  - [Notion de Microservices](#notion-de-microservices)
  - [Config Server](#config-server)
  - [Eureka — Service Discovery](#eureka--service-discovery)
  - [API Gateway](#api-gateway)
  - [Keycloak — Authentification & Autorisation](#keycloak--authentification--autorisation)
- [Services Métier](#-services-métier)
  - [User Service](#1-user-service)
  - [Formation & Certificat Service](#2-formation--certificat-service)
  - [Cours Service](#3-cours-service)
  - [Classe Service](#4-classe-service)
  - [Notes Service](#5-notes-service)
  - [Absence Service](#6-absence-service)
  - [Réservation Service](#7-réservation-service)
  - [Réclamation Service](#8-réclamation-service)
- [Communication inter-services](#-communication-inter-services)
- [Stack Technique](#-stack-technique)
- [Lancer le projet](#-lancer-le-projet)
- [Docker & Déploiement](#-docker--déploiement)
- [Frontend Angular](#-frontend-angular)
- [Contributeurs](#-contributeurs)

---

## 🌐 Vue d'ensemble

**SmartCampus** est une plateforme académique conçue pour digitaliser et automatiser la gestion des établissements d'enseignement supérieur. Elle couvre l'ensemble du cycle étudiant : inscription, suivi des cours, gestion des notes, absences, réservations de salles, et traitement des réclamations.

La plateforme est construite selon le paradigme **microservices**, garantissant une scalabilité indépendante de chaque module, une maintenance simplifiée, et une haute disponibilité.

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT ANGULAR                           │
│                    (localhost:4200)                              │
└──────────────────────────┬──────────────────────────────────────┘
                           │ HTTP
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API GATEWAY  :9000                            │
│              (Routage + Auth OAuth2/Keycloak)                    │
└──────┬───────┬────────┬────────┬────────┬────────┬─────────────┘
       │       │        │        │        │        │
       ▼       ▼        ▼        ▼        ▼        ▼
   :8090   :8086    :8082    :8083    :8084    :8085  ...
  [User] [Form.]  [Cours] [Classe] [Notes] [Absence]

       ┌──────────────────────────────────────┐
       │          INFRASTRUCTURE              │
       │  Eureka :8761 | Config Server :8888  │
       │  RabbitMQ :5672 | Keycloak :8080     │
       │  MongoDB | MySQL                     │
       └──────────────────────────────────────┘
```

---

## 🏗️ Architecture Microservices

### Notion de Microservices

Une architecture **microservices** décompose une application monolithique en un ensemble de **services indépendants**, chacun responsable d'un domaine métier précis.

Chaque microservice possède sa **propre base de données** (MongoDB ou MySQL selon le besoin), expose une **API REST** indépendante, et est **déployé, scalé et maintenu séparément**. Les services communiquent entre eux via **HTTP (Feign)** ou **messagerie asynchrone (RabbitMQ)**.

**Avantages dans ce projet :**

| Propriété | Bénéfice |
|---|---|
| Indépendance | Un bug dans `absence-service` n'affecte pas `notes-service` |
| Scalabilité | Scaler uniquement les services sous charge |
| Technologies mixtes | MongoDB pour certains services, MySQL pour d'autres |
| Déploiement continu | Chaque service a son propre pipeline Docker |

---

### Config Server

**Port :** `8888`

Le **Config Server** (Spring Cloud Config) centralise toutes les configurations des microservices dans un dépôt Git. Au démarrage, chaque service récupère sa configuration depuis ce serveur, ce qui évite de dupliquer les propriétés dans chaque service. Les fichiers de configuration sont versionnés dans un repo Git dédié et peuvent être rechargés à chaud sans redémarrer les services.

---

### Eureka — Service Discovery

**Port :** `8761` | Dashboard : `http://localhost:8761`

**Eureka** (Spring Cloud Netflix) est le **registre de services**. Chaque microservice s'enregistre automatiquement au démarrage avec son nom et son adresse. Les autres services peuvent alors le découvrir sans connaître son IP ou son port exact, ce qui rend l'architecture robuste aux changements d'infrastructure.

Sans Eureka, les URLs sont codées en dur et fragiles. Avec Eureka, chaque service est appelé par son nom logique et la résolution est dynamique.

---

### API Gateway

**Port :** `9000`

La **Gateway** (Spring Cloud Gateway) est le **point d'entrée unique** de la plateforme. Elle reçoit toutes les requêtes du frontend Angular et les route vers le bon microservice.

**Responsabilités :**
- **Routage** des requêtes vers les services cibles via Eureka
- **Authentification** via Keycloak (OAuth2 Resource Server)
- **CORS** centralisé pour tous les services
- **Rate limiting** et logging global

> ⚠️ **État actuel :** La Gateway est désactivée en mode développement. Le frontend pointe directement vers `localhost:8090`. Elle sera réactivée avec Keycloak OAuth2.

---

### Keycloak — Authentification & Autorisation

**Port :** `8080` | Realm : `esprit` | Client : `angular-client`

**Keycloak** est le serveur d'identité open-source utilisé pour gérer l'**authentification et les autorisations** de toute la plateforme via le protocole **OAuth2 / OpenID Connect**.

**Configuration prévue :**

| Paramètre | Valeur |
|---|---|
| Realm | `esprit` |
| Client Angular | `angular-client` |
| Grant Type | Authorization Code + PKCE |
| Rôles | `ROLE_ADMIN`, `ROLE_ETUDIANT`, `ROLE_ENSEIGNANT` |

Le flux d'authentification fonctionne comme suit : l'utilisateur se connecte via Angular sur Keycloak, obtient un **Access Token JWT**, puis le transmet à chaque requête vers la Gateway. Chaque microservice valide ce token localement via la clé publique JWKS, sans contacter Keycloak à chaque appel.

---

## 📦 Services Métier

### 1. User Service

**Port :** `8090` | **Base :** MongoDB | **Docker :** `meddhia1rom/user-service`

Service central de gestion des utilisateurs de la plateforme.

**Entités :** `User` (id, nom, prénom, email, rôle, dateInscription)

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/register` | Inscription d'un nouvel utilisateur |
| `POST` | `/api/users/login` | Authentification |
| `GET` | `/api/users/{id}` | Récupérer un utilisateur |
| `GET` | `/api/users` | Lister tous les utilisateurs |
| `PUT` | `/api/users/{id}` | Modifier un utilisateur |
| `DELETE` | `/api/users/{id}` | Supprimer un utilisateur |

**Événements publiés :** À chaque inscription, un `UserCreatedEvent` est publié vers l'exchange RabbitMQ `formation-certificat.exchange`, déclenchant l'auto-enrôlement dans les formations par défaut.

---

### 2. Formation & Certificat Service

**Port :** `8086` | **Base :** MongoDB | **Docker :** `meddhia1rom/formation-certificat-service1`

Gère les formations proposées aux étudiants et les certificats délivrés à la complétion.

**Entités :** `Formation` (id, titre, description, duree, niveau), `Certificat` (id, userId, formationId, dateObtention, statut)

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/formations` | Lister toutes les formations |
| `POST` | `/api/formations` | Créer une formation |
| `GET` | `/api/formations/{id}` | Détail d'une formation |
| `GET` | `/api/certificats/user/{userId}` | Certificats d'un étudiant |
| `POST` | `/api/certificats` | Délivrer un certificat |

**Événements consommés :** Le `UserEventConsumer` écoute le `UserCreatedEvent` et enrôle automatiquement le nouvel utilisateur dans les formations par défaut.

---

### 3. Cours Service

**Port :** `8082` | **Base :** MySQL

Gère le catalogue des cours, leur contenu, et leur affectation aux classes.

**Entités :** `Cours` (id, titre, description, enseignantId, classeId, credits, semestre)

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/cours` | Lister tous les cours |
| `POST` | `/api/cours` | Créer un cours |
| `GET` | `/api/cours/{id}` | Détail d'un cours |
| `GET` | `/api/cours/classe/{classeId}` | Cours d'une classe |
| `GET` | `/api/cours/enseignant/{id}` | Cours d'un enseignant |
| `PUT` | `/api/cours/{id}` | Modifier un cours |
| `DELETE` | `/api/cours/{id}` | Supprimer un cours |

---

### 4. Classe Service

**Port :** `8083` | **Base :** MySQL

Gère les classes, les promotions, et l'affectation des étudiants.

**Entités :** `Classe` (id, nom, niveau, annee, capacite), `Inscription` (id, etudiantId, classeId, dateInscription)

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/classes` | Lister toutes les classes |
| `POST` | `/api/classes` | Créer une classe |
| `GET` | `/api/classes/{id}` | Détail d'une classe |
| `POST` | `/api/classes/{id}/inscrire` | Inscrire un étudiant |
| `GET` | `/api/classes/{id}/etudiants` | Étudiants d'une classe |
| `DELETE` | `/api/classes/{id}/etudiants/{etudiantId}` | Désinscrire un étudiant |

---

### 5. Notes Service

**Port :** `8084` | **Base :** MySQL

Gère la saisie, la consultation et le calcul des notes des étudiants par matière et session.

**Entités :** `Note` (id, etudiantId, coursId, valeur, session, type, dateCreation)

**Types de notes :** `DS` (Devoir Surveillé), `EXAM` (Examen final), `TP` (Travaux Pratiques), `PROJET`

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/notes` | Saisir une note |
| `GET` | `/api/notes/etudiant/{id}` | Notes d'un étudiant |
| `GET` | `/api/notes/cours/{id}` | Notes d'un cours |
| `GET` | `/api/notes/moyenne/{etudiantId}/{coursId}` | Moyenne calculée |
| `PUT` | `/api/notes/{id}` | Modifier une note |
| `DELETE` | `/api/notes/{id}` | Supprimer une note |

**Calcul de moyenne :** La moyenne finale est calculée automatiquement selon les coefficients : DS (30%) + TP (20%) + EXAM (50%).

---

### 6. Absence Service

**Port :** `8085` | **Base :** MySQL

Suivi et justification des absences des étudiants par cours et séance.

**Entités :** `Absence` (id, etudiantId, coursId, date, justifiee, motif)

**Statuts :** `INJUSTIFIEE`, `EN_ATTENTE`, `JUSTIFIEE`

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/absences` | Enregistrer une absence |
| `GET` | `/api/absences/etudiant/{id}` | Absences d'un étudiant |
| `GET` | `/api/absences/cours/{id}` | Absences par cours |
| `PUT` | `/api/absences/{id}/justifier` | Justifier une absence |
| `GET` | `/api/absences/etudiant/{id}/count` | Compteur d'absences |

**Règle métier :** Au-delà de **3 absences injustifiées** dans un cours, une alerte est générée automatiquement.

---

### 7. Réservation Service

**Port :** `8087` | **Base :** MySQL

Gestion des réservations de salles, laboratoires et équipements pédagogiques.

**Entités :** `Salle` (id, nom, capacite, type, equipements), `Reservation` (id, salleId, utilisateurId, dateDebut, dateFin, statut, motif)

**Statuts :** `EN_ATTENTE`, `CONFIRMEE`, `ANNULEE`, `TERMINEE`

**Types de salles :** `COURS`, `TP`, `CONFERENCE`, `INFORMATIQUE`

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/salles` | Lister les salles disponibles |
| `POST` | `/api/reservations` | Créer une réservation |
| `GET` | `/api/reservations/{id}` | Détail d'une réservation |
| `GET` | `/api/reservations/utilisateur/{id}` | Réservations d'un utilisateur |
| `PUT` | `/api/reservations/{id}/confirmer` | Confirmer (admin) |
| `PUT` | `/api/reservations/{id}/annuler` | Annuler une réservation |
| `GET` | `/api/salles/disponibles` | Salles libres sur un créneau |

---

### 8. Réclamation Service

**Port :** `8088` | **Base :** MongoDB

Gestion du cycle de vie des réclamations soumises par les étudiants (notes, absences, administratif…).

**Entités :** `Reclamation` (id, etudiantId, type, description, statut, dateCreation, dateTraitement, reponse)

**Types :** `NOTE`, `ABSENCE`, `INSCRIPTION`, `ADMINISTRATIF`, `AUTRE`

**Statuts :** `SOUMISE`, `EN_COURS`, `RESOLUE`, `REJETEE`

**Endpoints REST :**

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/reclamations` | Soumettre une réclamation |
| `GET` | `/api/reclamations/etudiant/{id}` | Réclamations d'un étudiant |
| `GET` | `/api/reclamations` | Toutes les réclamations (admin) |
| `PUT` | `/api/reclamations/{id}/traiter` | Traiter et répondre |
| `GET` | `/api/reclamations/statut/{statut}` | Filtrer par statut |
| `DELETE` | `/api/reclamations/{id}` | Supprimer une réclamation |

---

## 🔗 Communication inter-services

### Communication synchrone — Feign Client

Utilisée pour les appels directs nécessitant une réponse immédiate. Un service déclare un client Feign annoté avec le nom du service cible enregistré dans Eureka. Spring Cloud se charge de la résolution d'adresse et de l'équilibrage de charge automatiquement.

### Communication asynchrone — RabbitMQ

Utilisée pour les événements découplés où une réponse immédiate n'est pas nécessaire. Lorsqu'un utilisateur s'inscrit, `user-service` publie un `UserCreatedEvent` vers l'exchange `formation-certificat.exchange`. Le `UserEventConsumer` dans `formation-certificat-service` consomme cet événement et procède à l'auto-enrôlement.

**Exchanges et Queues déclarés :**

| Exchange | Queue | Binding Key | Consommateur |
|---|---|---|---|
| `formation-certificat.exchange` | `formation.user.queue` | `user.created` | `UserEventConsumer` |

---

## 🛠️ Stack Technique

| Couche | Technologie |
|---|---|
| **Frontend** | Angular 17, TypeScript, Bootstrap |
| **Backend** | Spring Boot 3.x / 4.x, Java 17/21 |
| **Sécurité** | Keycloak 23, OAuth2, JWT |
| **Infrastructure** | Spring Cloud (Eureka, Gateway, Config) |
| **Messagerie** | RabbitMQ 3.x |
| **Base de données** | MongoDB 7, MySQL 8 |
| **Conteneurisation** | Docker, Docker Compose |
| **Registry Docker** | Docker Hub (`meddhia1rom`) |
| **Build** | Maven |

---

## 🚀 Lancer le projet

### Prérequis

- Java 17+
- Node.js 18+ et npm
- Docker Desktop
- Maven 3.8+

### Ordre de démarrage recommandé

L'ordre de démarrage est important pour garantir que chaque service trouve ses dépendances. Il faut respecter la séquence suivante : **Config Server** en premier, puis **Eureka**, ensuite les **services métier** (dans n'importe quel ordre entre eux), et enfin l'**API Gateway** en dernier.

Le frontend Angular se démarre indépendamment et pointe vers le port `8090` en mode développement, ou vers le port `9000` (Gateway) en mode production.

---

## 🐳 Docker & Déploiement

### Images disponibles sur Docker Hub (`meddhia1rom`)

| Image | Statut |
|---|---|
| `meddhia1rom/formation-certificat-service1` | ✅ Disponible |
| `meddhia1rom/user-service` | 🔄 À builder |
| `meddhia1rom/api-gateway` | 🔄 À builder |

### Docker Compose

Un fichier `docker-compose.yml` global est prévu pour orchestrer l'ensemble des services, bases de données et infrastructure en une seule commande.

---

## 🖥️ Frontend Angular

**Port :** `4200` | **API cible :** `http://localhost:8090/api` (mode direct) / `http://localhost:9000/api` (via Gateway)

Le frontend est une **Single Page Application (SPA)** Angular communiquant avec les microservices via HTTP REST. Il implémente une gestion des rôles côté client avec des guards Angular protégeant les routes selon le profil de l'utilisateur connecté.

**Modules Angular :**
- `AuthModule` — Login, JWT interceptor, Guard
- `UserModule` — Profil et gestion des comptes
- `FormationModule` — Catalogue et inscriptions
- `NotesModule` — Consultation des notes et moyennes
- `AbsenceModule` — Suivi des présences
- `ReservationModule` — Réservation de salles
- `ReclamationModule` — Soumission et suivi des réclamations

---

## 👨‍💻 Contributeurs

Projet réalisé par l'équipe **FullStackers**.

| Nom |
|---|
| Fedi Ben Khalifa |
| Ali Bouaine |
| Mohamed Dhia Romdhane |
| Fedi Mbarek Abidi |
| Firas Nefzi |
| Malek Fridhi |

---

> **SmartCampus** by FullStackers — Architecture Microservices · Spring Boot · Angular · Keycloak · RabbitMQ · Docker
