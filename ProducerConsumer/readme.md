# Applicazione Producer-Consumer

Questo progetto dimostra il pattern Producer-Consumer utilizzando due applicazioni Spring Boot separate, containerizzate con Docker e orchestrate tramite Docker Compose.

* **ProducerApplication:** Un backend API RESTful che gestisce i dati dei prodotti. Offre operazioni CRUD, si connette a un database PostgreSQL per la persistenza e include funzionalità di ricerca e filtraggio.
* **ConsumerApplication:** Un front-end web che consuma l'API fornita dalla ProducerApplication. Mostra le informazioni sui prodotti in un'interfaccia web user-friendly realizzata con Thymeleaf.

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



---

## Stack Tecnologico

* **Backend & Frontend:** Java 21, Spring Boot, Spring Web MVC, Spring Data JPA, Spring Security
* **Database:** PostgreSQL
* **Motore di Template:** Thymeleaf
* **Strumento di Build:** Maven
* **Containerizzazione:** Docker, Docker Compose

---

## Prerequisiti

* Docker
* Docker Compose

---

## Per Iniziare

Per eseguire l'intero stack applicativo, segui questi passaggi:

1. Clona il repository:
```bash
git clone https://github.com/Xu-aqni/school-java.git

```


2. Spostati nella directory del progetto:
```bash
cd school-java/ProducerConsumer

```


3. Compila ed esegui i servizi utilizzando Docker Compose:
```bash
docker-compose up --build

```



Questo comando effettuerà le seguenti operazioni:

* Creerà le immagini Docker sia per la `ProducerApplication` che per la `ConsumerApplication`.
* Avvierà i container per il database PostgreSQL, l'API del producer e l'applicazione web consumer.
* Stabilirà le connessioni di rete tra i container.

---

## Accesso alle Applicazioni

Una volta che i container sono in esecuzione, puoi accedere ai servizi:

* **Applicazione Web Consumer:** Apri il browser web e vai su: `http://localhost:8082/products`
* **API REST Producer:** L'API è disponibile all'indirizzo `http://localhost:8081/api`. Puoi utilizzare strumenti come `curl` o Postman per interagire con essa.

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