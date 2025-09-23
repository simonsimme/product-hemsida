# Kod test - tiny web shop

## Task
En enkel fullstack webb app.
* Frontend
* Backend API

Den behöver inte vara hostad, utan det räcker med att det funkar **lokalt**

Du ska sedan kunna gå igenom alla delar av koden och beskriva hur allt fungerar.

Du kan ställa frågor kring uppgiften, men vi ger medvetet oprecis info för att se vad du kan

---

## Krav:

* Frontend
  * Tech-stack
    * Helst React + TypeScript, men använd det du är bekväm med
  * Sidor
  * En home page
    * Enkel struktur, någon titel och bild
  * En samlingssida för produkterna
    * Där bör man kunna se produkterna, som bör displaya
      * Titel
      * Description
      * Någon bild
      * Pris
      * Quantity (dvs stock)
  * En produktsida för varje produkt, där du bara visar upp den enskilda produkten
  * Design
    * Inget krav på snygg design, men ska se ordentlig ut och prydligt

* Backend
  * Någon SQL databas där du hanterar:
    * Produkter
    * Orders
    * Användare
  * Databasen behöver ha vissa optimeringar som en standard "production" webb app ofta har - inga ledtrådar, utan applicera det du kan och anser bör användas
  * API:t bör vara skyddat, dvs inte vem som helst ska kunna ha tillgång
  * Du får implementera fler API "features" som du anser är useful, nödvändiga eller applicable för en webb shop
  * Databasen bör ha skydd mot klassiska "attacker" och sätt som en malicious-actor skulle kunna "förstöra" saker på

## OBS
Du får inte använda "hjälpverktyg" som förenklar dina databas queries eller liknande, utan du ska set it up from scratch.