# 📚 SmartCampus — Angular Frontend

Réécriture complète du frontend React en **Angular 17** (standalone components).

---

## 🗂️ Structure du projet

```
smartcampus-angular/
├── src/
│   ├── app/
│   │   ├── services/
│   │   │   └── course.service.ts          # Appels HTTP (GET, POST, PUT, DELETE, chatbot)
│   │   ├── components/
│   │   │   ├── search-bar/                # Barre de recherche réactive
│   │   │   ├── course-list/               # Liste, ajout, édition, suppression de cours
│   │   │   └── chatbot/                   # Widget chatbot flottant
│   │   ├── app.component.ts               # Composant racine
│   │   └── app.config.ts                  # HttpClient + Router
│   ├── main.ts                            # Bootstrap
│   ├── index.html
│   └── styles.css                         # Variables CSS globales + fonts
├── angular.json
├── tsconfig.json
├── tsconfig.app.json
└── package.json
```

---

## ⚡ Installation & démarrage

### Prérequis
- Node.js >= 18
- Angular CLI : `npm install -g @angular/cli`

### Lancer le projet

```bash
# 1. Installer les dépendances
npm install

# 2. Démarrer le serveur de développement
ng serve

# L'application est disponible sur http://localhost:4200
```

> ⚠️ Le backend Spring Boot doit tourner sur `http://localhost:8083`

---

## 🔗 Endpoints backend utilisés

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | `/courses` | Récupérer tous les cours |
| GET | `/courses/search?title=...` | Rechercher par titre |
| POST | `/courses` | Ajouter un cours |
| PUT | `/courses/:id` | Modifier un cours |
| DELETE | `/courses/:id` | Supprimer un cours |
| POST | `/courses/chatbot` | Envoyer un message au chatbot |

---

## 🔄 Correspondances React → Angular

| React | Angular |
|-------|---------|
| `axios` | `HttpClient` Angular |
| `useState` | Propriétés de classe |
| `useEffect` + `[]` | `ngOnInit()` |
| `props` / callbacks | `@Input()` / `@Output()` |
| `courseApi.js` | `CourseService` (injectable) |
| Composants `.jsx` | Composants standalone Angular 17 |
| `{ onResults }` prop | `(results)="onSearchResults($event)"` |

---

## 🎨 Design

- **Fonts** : DM Sans + Playfair Display (Google Fonts)
- **Couleurs** : Variables CSS (--primary, --text, --border, etc.)
- **Animations** : fadeIn, slideUp, bounce (CSS pur)
- Design identique au projet React d'origine

---

## 🏗️ Build production

```bash
ng build
# Output dans dist/smartcampus-angular/
```
