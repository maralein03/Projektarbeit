# CRUD & Rollen-Management

## Rollen-Beschreibung

### ROLE_READ (Lernender)
- Kann Aufgaben **einsehen**
- Kann Aufgaben **annehmen** (Accept)
- Kann **Status ändern**
- Kann **Fragen stellen** zu Aufgaben

### ROLE_UPDATE (Ausbilder)
- Kann **neue Aufgaben erstellen**
- Kann **Aufgaben löschen**
- Kann **Aufgaben bearbeiten/aktualisieren**
- Kann **das System verwalten**

---

## REST API - Vollständiges CRUD

### **TODOS LESEN** (TodoController)

#### 1. Alle Todos auflisten
```
GET /api/todos
```
- **Rolle**: ROLE_READ (Lernender)
- **Beschreibung**: Listet alle vorhandenen Todos auf
- **Response**: Array aller Todos oder leeres Array

#### 2. Einzelnes Todo abrufen
```
GET /api/todos/{id}
```
- **Rolle**: ROLE_READ (Lernender)
- **Beschreibung**: Gibt ein spezifisches Todo nach ID zurück
- **Parameter**: `id` (Long) - ID des Todos
- **Response**: Todo oder 404 Not Found

---

### **TODOS ERSTELLEN** (AdminController)

#### 3. Neues Todo erstellen
```
POST /api/todos
```
- **Rolle**: ROLE_UPDATE (Ausbilder)
- **Beschreibung**: Erstellt ein neues Todo
- **Request Body**:
```json
{
  "title": "Aufgabentitel",
  "description": "Detaillierte Beschreibung (min. 200 Zeichen)",
  "assignedTo": "Name der Person",
  "status": "OPEN"
}
```
- **Response**: Neu erstelltes Todo mit ID (Status: 201 Created)

---

### **TODOS AKTUALISIEREN** (TodoController)

#### 4. Todo bearbeiten
```
PUT /api/todos/{id}
```
- **Rolle**: ROLE_UPDATE (Ausbilder)
- **Beschreibung**: Aktualisiert ein vorhandenes Todo vollständig
- **Parameter**: `id` (Long) - ID des Todos
- **Request Body**:
```json
{
  "title": "Neuer Titel",
  "description": "Neue Beschreibung (min. 200 Zeichen)",
  "assignedTo": "Neue Person",
  "status": "IN_PROGRESS"
}
```
- **Response**: Aktualisiertes Todo oder 404 Not Found

---

### **TODOS LÖSCHEN** (AdminController)

#### 5. Todo löschen
```
DELETE /api/todos/{id}
```
- **Rolle**: ROLE_UPDATE (Ausbilder)
- **Beschreibung**: Löscht ein Todo komplett aus dem System
- **Parameter**: `id` (Long) - ID des Todos
- **Response**: 204 No Content (bei Erfolg) oder 404 Not Found

---

## STATUS-MANAGEMENT (StatusController)

### 6. Aufgabe annehmen
```
PATCH /api/todos/{id}/accept
```
- **Rolle**: ROLE_READ (Lernender)
- **Beschreibung**: Nimmt eine Aufgabe an und setzt Status auf IN_PROGRESS
- **Parameter**: 
  - `id` (Path) - ID des Todos
  - `name` (Query) - Name der Person, die annimmt
- **Beispiel**: `PATCH /api/todos/1/accept?name=Max`
- **Response**: Aktualisiertes Todo mit neuer Zuweisung und Status

### 7. Status ändern
```
PATCH /api/todos/{id}/status
```
- **Rolle**: ROLE_READ (Lernender)
- **Beschreibung**: Ändert den Status eines Todos
- **Parameter**:
  - `id` (Path) - ID des Todos
  - `status` (Query) - Neuer Status (OPEN, IN_PROGRESS, DONE, QUESTION)
- **Beispiel**: `PATCH /api/todos/1/status?status=DONE`
- **Response**: Aktualisiertes Todo mit neuem Status

---

## FRAGEN-MANAGEMENT (QuestionController)

### 8. Frage zu einem Todo stellen
```
POST /api/todos/{id}/questions
```
- **Rolle**: ROLE_READ (Lernender)
- **Beschreibung**: Stellt eine neue Frage zu einem Todo
- **Parameter**: `id` (Path) - ID des Todos
- **Request Body**:
```json
{
  "content": "Meine Frage zum Todo...",
  "author": "Name des Fragestellers"
}
```
- **Response**: Neu erstellte Frage mit ID (Status: 201 Created)

### 9. Fragen zu einem Todo auflisten
```
GET /api/todos/{id}/questions
```
- **Rolle**: ROLE_READ (Lernender)
- **Beschreibung**: Listet alle Fragen zu einem spezifischen Todo auf
- **Parameter**: `id` (Path) - ID des Todos
- **Response**: Array aller Fragen zu diesem Todo

---

## Status-Werte

```
OPEN          - Aufgabe ist neu und unbearbeitet
IN_PROGRESS   - Aufgabe ist in Bearbeitung
DONE          - Aufgabe ist abgeschlossen
QUESTION      - Es gibt eine offene Frage zur Aufgabe
```

---

## Sicherheit & Authentifizierung

- **Auth-Mechanismus**: OAuth2 mit JWT (Keycloak)
- **Token-Format**: JWT
- **Rollen-Quelle**: Keycloak `realm_access.roles`
- **Authentifizierung erforderlich**: Ja (außer Swagger UI)
- **CORS**: Konfigurierbar nach Bedarf

### Swagger-UI
- **URL**: `http://localhost:8081/swagger-ui.html`
- **Zugang**: Öffentlich (keine Authentifizierung nötig)
- **Beschreibung**: Interaktive API-Dokumentation zum Testen der Endpunkte

---

## Zusammenfassung der CRUD-Operationen

| Operation | HTTP-Methode | Endpoint | Rolle | Controller |
|-----------|--------------|----------|-------|-----------|
| **C**reate | POST | `/api/todos` | ROLE_UPDATE | AdminController |
| **R**ead All | GET | `/api/todos` | ROLE_READ | TodoController |
| **R**ead One | GET | `/api/todos/{id}` | ROLE_READ | TodoController |
| **U**pdate | PUT | `/api/todos/{id}` | ROLE_UPDATE | TodoController |
| **D**elete | DELETE | `/api/todos/{id}` | ROLE_UPDATE | AdminController |
| Status Change | PATCH | `/api/todos/{id}/status` | ROLE_READ | StatusController |
| Accept Task | PATCH | `/api/todos/{id}/accept` | ROLE_READ | StatusController |
| Add Question | POST | `/api/todos/{id}/questions` | ROLE_READ | QuestionController |
| Get Questions | GET | `/api/todos/{id}/questions` | ROLE_READ | QuestionController |

---

## Validierung & Fehlerbehandlung

### Todo-Validierung
- **ID**: Muss positiv oder Null sein (Auto-Generation)
- **Beschreibung**: Mindestens 200 Zeichen erforderlich
- **Status**: Automatisch auf OPEN gesetzt, wenn nicht angegeben
- **CreatedAt**: Automatisch beim Erstellen gesetzt

### Fehlerhafte Anfragen
- **400 Bad Request**: Bei ungültigen Eingabedaten
- **401 Unauthorized**: Bei fehlender oder ungültiger Authentifizierung
- **403 Forbidden**: Wenn die Rolle nicht berechtigt ist
- **404 Not Found**: Wenn die Ressource nicht existiert

---

## Verwendungsbeispiele

### Als LERNENDER (ROLE_READ)
```bash
# Alle Todos anschauen
curl -H "Authorization: Bearer <JWT>" http://localhost:8081/api/todos

# Einzelnes Todo anschauen
curl -H "Authorization: Bearer <JWT>" http://localhost:8081/api/todos/1

# Aufgabe annehmen
curl -X PATCH -H "Authorization: Bearer <JWT>" \
  http://localhost:8081/api/todos/1/accept?name=Max

# Status ändern
curl -X PATCH -H "Authorization: Bearer <JWT>" \
  http://localhost:8081/api/todos/1/status?status=DONE

# Frage stellen
curl -X POST -H "Authorization: Bearer <JWT>" \
  -H "Content-Type: application/json" \
  -d '{"content":"Wie mache ich das?","author":"Max"}' \
  http://localhost:8081/api/todos/1/questions

# Fragen anschauen
curl -H "Authorization: Bearer <JWT>" http://localhost:8081/api/todos/1/questions
```

### Als AUSBILDER (ROLE_UPDATE)
```bash
# Neues Todo erstellen
curl -X POST -H "Authorization: Bearer <JWT>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Neue Aufgabe","description":"Das ist eine sehr lange Beschreibung mit mindestens 200 Zeichen...","assignedTo":"Max","status":"OPEN"}' \
  http://localhost:8081/api/todos

# Todo aktualisieren
curl -X PUT -H "Authorization: Bearer <JWT>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Aktualisiert","description":"Neue, längere Beschreibung mit mindestens 200 Zeichen...","assignedTo":"Anna","status":"IN_PROGRESS"}' \
  http://localhost:8081/api/todos/1

# Todo löschen
curl -X DELETE -H "Authorization: Bearer <JWT>" \
  http://localhost:8081/api/todos/1
```
