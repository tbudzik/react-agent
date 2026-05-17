# Spring Boot CRUD Backend

Backend aplikacji CRUD Components & Fields oparty na Spring Boot z bazą danych H2 z persystencją do pliku.

## Wymagania

- Java 17+
- Maven 3.6+

## Uruchomienie

```bash
cd backend
mvn spring-boot:run
```

Serwer będzie dostępny pod adresem `http://localhost:8080`.

## API Endpoints

### Komponenty

- `GET /api/components` — lista wszystkich komponentów
- `GET /api/components/{id}` — szczegóły komponentu
- `POST /api/components` — utworzenie nowego komponentu
- `PUT /api/components/{id}` — aktualizacja komponentu
- `DELETE /api/components/{id}` — usunięcie komponentu

### Pola komponentu

- `GET /api/components/{componentId}/fields` — lista pól dla komponentu
- `POST /api/components/{componentId}/fields` — dodanie nowego pola
- `PUT /api/components/{componentId}/fields/{fieldId}` — aktualizacja pola
- `DELETE /api/components/{componentId}/fields/{fieldId}` — usunięcie pola

## Baza danych H2

Baza danych jest przechowywana w pliku `./data/crud-db.h2.db`.

Konsola H2 dostępna pod: `http://localhost:8080/h2-console`

- **URL JDBC**: `jdbc:h2:file:./data/crud-db`
- **User**: `sa`
- **Password**: (puste)

## Struktura projektu

```
backend/
├── pom.xml
└── src/main/java/com/example/crud/
    ├── CrudBackendApplication.java
    ├── model/
    │   ├── ComponentEntity.java
    │   └── FieldEntity.java
    ├── repository/
    │   ├── ComponentRepository.java
    │   └── FieldRepository.java
    ├── controller/
    │   └── ComponentController.java
    └── dto/
        ├── ComponentDTO.java
        └── FieldDTO.java
```
