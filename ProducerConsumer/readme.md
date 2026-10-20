# Applicazione Producer-Consumer

**AI utilizzate (percentuale di utilizzo):**

* `<Gemini 30%>`

---

## Descrizione del progetto

Questo progetto dimostra il pattern Producer-Consumer utilizzando due applicazioni Spring Boot separate, containerizzate con Docker e orchestrate tramite Docker Compose.

* **ProducerApplication:** Un backend API RESTful che gestisce i dati dei prodotti. Offre operazioni CRUD, si connette a un database PostgreSQL per la persistenza e include funzionalità di ricerca e filtraggio.
* **ConsumerApplication:** Un front-end web che consuma l'API fornita dalla ProducerApplication. Mostra le informazioni sui prodotti in un'interfaccia web user-friendly realizzata con Thymeleaf.

Il Consumer non accede mai direttamente al database: tutti i dati vengono recuperati attraverso le REST API del Producer.

Il flusso di una richiesta è:

```
Browser → Consumer (:8082) → HTTP/REST → Producer (:8081) → JDBC/JPA → PostgreSQL
```

---

## Architettura

Il sistema è composto da tre servizi principali orchestrati da Docker Compose:

* **db (Database PostgreSQL):**
  * Un container PostgreSQL che funge da archivio dati per l'applicazione producer.
  * I dati vengono salvati in un volume Docker denominato `postgres-data`.

* **producer (ProducerApplication):**
  * Un'applicazione Spring Boot che funge da server API di backend.
  * Espone endpoint REST per la gestione dei prodotti.
  * Si connette al servizio `db` per la persistenza dei dati tramite Spring Data JPA.
  * È in ascolto sulla porta **8081**.
  * Le operazioni di scrittura (POST, PUT, DELETE) sono protette da Autenticazione Base (Basic Authentication).

* **consumer (ConsumerApplication):**
  * Un'applicazione Spring Boot che fornisce un'interfaccia web.
  * Utilizza il `RestClient` di Spring per recuperare i dati dal servizio producer.
  * Esegue il rendering dei dati dei prodotti tramite template Thymeleaf.
  * Consente agli utenti di visualizzare un elenco di prodotti, consultare i dettagli di un prodotto e cercare i prodotti per nome e categoria.
  * È in ascolto sulla porta **8082**.

```
                  Docker Network
┌──────────────────────────────────────────────┐
│  ┌──────────────┐  HTTP   ┌──────────────┐   │
│  │   Consumer   │ ──────> │   Producer   │   │
│  │    :8082     │         │    :8081     │   │
│  └──────────────┘         └──────┬───────┘   │
│                                  │ JDBC      │
│                           ┌──────v───────┐   │
│                           │  PostgreSQL  │   │
│                           └──────┬───────┘   │
│                           ┌──────v───────┐   │
│                           │Docker Volume │   │
│                           │postgres-data │   │
│                           └──────────────┘   │
└──────────────────────────────────────────────┘
```

---

## Stack Tecnologico

* **Backend & Frontend:** Java 21, Spring Boot, Spring Web MVC, Spring Data JPA, Spring Security
* **Database:** PostgreSQL
* **Motore di Template:** Thymeleaf
* **Client HTTP:** RestClient di Spring
* **Strumento di Build:** Maven
* **Containerizzazione:** Docker, Docker Compose

---

## Requisiti necessari

Per eseguire il progetto con Docker:

* Docker
* Docker Compose

Per compilare ed eseguire le applicazioni in locale, senza Docker (opzionale):

* Java 21
* Maven
* Git
* Un'istanza PostgreSQL raggiungibile

---

## Accesso alle Applicazioni

Una volta che i container sono in esecuzione, puoi accedere ai servizi:

* **Applicazione Web Consumer:** Apri il browser web e vai su: `http://localhost:8082/products`
* **API REST Producer:** L'API è disponibile all'indirizzo `http://localhost:8081/api`. Puoi utilizzare strumenti come `curl` o Postman per interagire con essa.

---

## Configurazione

### Configurazione del database (PostgreSQL)

Il database è eseguito nel container `db` (servizio `db` in Docker Compose) e utilizza un'immagine PostgreSQL. I dati vengono conservati nel volume Docker `postgres-data`, quindi sopravvivono alla rimozione dei container.

All'interno della rete Docker il database è raggiungibile con l'hostname `db` (e non con `localhost`).

### Configurazione del Producer

Il Producer riceve i parametri di connessione al database tramite variabili d'ambiente, così la stessa applicazione può essere eseguita sia in locale sia in Docker senza modificare il codice:

| Variabile | Descrizione |
| --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC del database (in Docker punta al servizio `db`) |
| `SPRING_DATASOURCE_USERNAME` | Utente del database |
| `SPRING_DATASOURCE_PASSWORD` | Password del database |

Il Producer è in ascolto sulla porta **8081**.

### Configurazione del Consumer

Il Consumer conosce l'indirizzo del Producer tramite una variabile d'ambiente, in modo che l'URL non sia scritto nel codice Java:

| Variabile | Descrizione |
| --- | --- |
| `PRODUCER_URL` | URL base del Producer (in Docker: `http://producer:8081`, in locale: `http://localhost:8081`) |

All'interno della rete Docker `localhost` non indica il container Producer: per questo motivo si utilizza il nome del servizio (`producer`) come hostname.

Il Consumer è in ascolto sulla porta **8082**.

---

## Configurazione Docker

### Immagini Docker utilizzate

| Servizio | Immagine |
| --- | --- |
| db | Immagine ufficiale `postgres` (`<versione>`) |
| producer | Immagine costruita dal `Dockerfile` del Producer (`xuaqni/producer-api:<tag>`) |
| consumer | Immagine costruita dal `Dockerfile` del Consumer (`xuaqni/consumer-web:<tag>`) |

### Rete

Docker Compose crea automaticamente una rete condivisa tra i tre servizi. I container comunicano tra loro usando i nomi dei servizi come hostname:

* `consumer` → `producer:8081`
* `producer` → `db:5432`

Il Consumer non utilizza il database.

### Volume

Il servizio `db` utilizza il volume persistente `postgres-data` per conservare i dati del database. Dopo `docker-compose down` i container vengono rimossi ma i dati rimangono nel volume (a meno di usare l'opzione `-v`).

Per verificare i volumi:

```bash
docker volume ls
```

---

## Comandi principali

### Build e avvio

```bash
# Compila e avvia tutti i servizi
docker-compose up --build

# Avvio in background
docker-compose up -d
```

### Stop

```bash
# Ferma i container senza rimuoverli
docker-compose stop

# Ferma e rimuove i container (il volume dei dati rimane)
docker-compose down
```

### Riavvio

```bash
docker-compose start
```

### Log

```bash
docker-compose logs
docker-compose logs producer
docker-compose logs consumer
docker-compose logs -f producer
```

### Verifica

```bash
docker ps
docker network ls
docker volume ls
```

---

## Endpoint delle API (ProducerApplication)

Tutti gli endpoint hanno come prefisso `/api/products`.

| Metodo | Endpoint | Descrizione | Autenticazione |
| --- | --- | --- | --- |
| GET | `/` | Recupera l'elenco di tutti i prodotti. | Nessuna |
| GET | `/{id}` | Recupera un singolo prodotto tramite il suo ID. | Nessuna |
| GET | `/search` | Cerca i prodotti. Supporta i parametri di query: `name`, `category` e `priceOrder` (`asc` o `desc`). | Nessuna |
| POST | `/` | Crea un nuovo prodotto. Il corpo della richiesta deve contenere i dettagli del prodotto. | Basic Auth |
| PUT | `/{id}` | Aggiorna un prodotto esistente tramite il suo ID. Il corpo della richiesta deve contenere i dettagli aggiornati del prodotto. | Basic Auth |
| DELETE | `/{id}` | Elimina un prodotto tramite il suo ID. | Basic Auth |

---

## Funzionalità aggiuntive implementate

* **Autenticazione semplice con Spring Security (Basic Auth):** le operazioni di scrittura (POST, PUT, DELETE) richiedono autenticazione, mentre le letture (GET) sono pubbliche.
* **Ricerca e filtraggio** dei prodotti per nome e categoria, con ordinamento per prezzo (`priceOrder`), combinabili tra loro.
* **Containerizzazione completa** con Docker e Docker Compose, con volume persistente per il database.

---

## Autenticazione

Per gli endpoint che richiedono l'autenticazione, utilizza le seguenti credenziali di Autenticazione Base (Basic Auth):

* **Nome utente:** `user`
* **Password:** `password123`

Esempio di utilizzo di `curl` per creare un prodotto:

```bash
curl -X POST http://localhost:8081/api/products \
-u user:password123 \
-H "Content-Type: application/json" \
-d '{
    "name": "New Gadget",
    "description": "A shiny new gadget.",
    "price": 99.99,
    "category": "Electronics",
    "quantity": 50
}'
```

---

## Repository Docker Hub

* Producer: `https://hub.docker.com/r/xuaqni/producer-api`
* Consumer: `https://hub.docker.com/r/xuaqni/consumer-web`

---

## Procedura di rilascio

1. Compilare i progetti e costruire le immagini:

```bash
docker-compose build
```

2. Effettuare il login a Docker Hub:

```bash
docker login
```

3. Taggare le immagini con la versione:

```bash
docker tag producer-api xuaqni/producer-api:1.0
docker tag consumer-web xuaqni/consumer-web:1.0
```

4. Pubblicare le immagini:

```bash
docker push xuaqni/producer-api:1.0
docker push xuaqni/consumer-web:1.0
```

5. Nel file Compose, sostituire `build: ./producer` e `build: ./consumer` con `image: xuaqni/producer-api:1.0` e `image: xuaqni/consumer-web:1.0`.

6. Sulla macchina di destinazione (servono solo Docker, Docker Compose, il file Compose e l'accesso a Docker Hub, non il codice sorgente):

```bash
docker-compose pull
docker-compose up -d
```

7. Verificare il funzionamento su `http://localhost:8082/products`.