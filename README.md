``` markdown
# Commerce en ligne

Application web/API REST de commerce en ligne développée avec **Spring Boot**.  
Elle permet la gestion des produits, achats, ventes, utilisateurs et rôles.

## Description

Ce projet est une application backend Java basée sur Spring Boot permettant de gérer les principales fonctionnalités d’un système de commerce en ligne :

- Gestion des produits
- Gestion des achats
- Gestion des ventes
- Gestion des utilisateurs
- Gestion des rôles
- Persistance des données avec MySQL
- Mapping DTO/Entity avec MapStruct
- Validation des données
- Logs avec Log4j2
- Support Docker pour MySQL, phpMyAdmin et Redis

## Technologies utilisées

- Java 17
- Spring Boot 3.5.10
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring AOP
- Thymeleaf
- MySQL
- Redis
- phpMyAdmin
- Maven
- Lombok
- MapStruct
- Log4j2
- Docker Compose
- JUnit / Spring Boot Test
- H2 Database pour les tests

## Prérequis

Avant de lancer le projet, assurez-vous d’avoir installé :

- Java 17
- Maven
- Docker et Docker Compose
- Git

## Installation

Clonez le dépôt :
```

bash git clone <url-du-repository> cd Commerce-en-ligne```

## Configuration de la base de données

Le projet utilise MySQL par défaut.

La configuration principale se trouve dans :
```

text src/main/resources/application.yml``` 

Configuration par défaut :
```

yaml server: port: 8080
spring: datasource: url: jdbc:mysql://localhost:3306/commerce-db username: user password: user123```

Vous pouvez aussi utiliser des variables d’environnement :
```

bash DB_HOST=localhost DB_NAME=commerce-db DB_USERNAME=user DB_PASSWORD=user123``` 

## Lancement avec Docker

Le fichier `compose.yml` permet de démarrer les services suivants :

- MySQL
- phpMyAdmin
- Redis

Lancez les conteneurs avec :
```

bash docker compose up -d```

Services disponibles :

| Service | URL / Port |
|---|---|
| MySQL | localhost:3306 |
| phpMyAdmin | http://localhost:8085 |
| Redis | localhost:6379 |

Identifiants MySQL par défaut :
```

text Database: commerce-db Username: user Password: user123 Root password: root``` 

## Lancement de l’application

Avec Maven Wrapper :
```

bash ./mvnw spring-boot:run```

Sous Windows :
```

bash mvnw.cmd spring-boot:run``` 

Ou avec Maven installé :
```

bash mvn spring-boot:run```

L’application sera disponible à l’adresse :
```

text http://localhost:8080``` 

## Compilation du projet
```

bash mvn clean package```

Le fichier `.jar` sera généré dans le dossier :
```

text target/``` 

Pour exécuter le fichier généré :
```

bash java -jar target/commerce-en-ligne-0.0.1-SNAPSHOT.jar```

## Endpoints principaux

Base URL :
```

text http://localhost:8080/api/v1``` 

### Produits
```

http GET /produits GET /produits/{id} POST /produits PUT /produits/{id} DELETE /produits/{id}```

### Achats
```

http GET /achats GET /achats/{id} GET /achats/produit/{produitId} POST /achats PUT /achats/{id} DELETE /achats/{id}``` 

### Ventes
```

http GET /ventes GET /ventes/{id} POST /ventes PUT /ventes/{id} DELETE /ventes/{id}```

### Utilisateurs
```

http GET /users GET /users/{id} POST /users PUT /users/{id} DELETE /users/{id}``` 

### Rôles
```

http GET /roles GET /roles/{id} POST /roles PUT /roles/{id} DELETE /roles/{id}```

> Les routes exactes peuvent varier selon les contrôleurs du projet.

## Tests

Pour lancer les tests :
```

bash mvn test```

Le projet utilise notamment :

- Spring Boot Test
- JUnit
- H2 Database pour les tests
- Testcontainers

## Logs

Les logs sont écrits dans :
```

text logs/logs.log``` 

Le niveau de logs web Spring est configuré en `DEBUG`.

## Fonctionnalités principales

- API REST pour les opérations CRUD
- Gestion des données via Spring Data JPA
- Connexion à une base MySQL
- Validation des données entrantes
- Conversion DTO/Entity avec MapStruct
- Gestion centralisée des exceptions
- Logging avec Log4j2
- Support Docker pour l’environnement local
- Interface possible avec Thymeleaf

## Variables d’environnement

| Variable | Description | Valeur par défaut |
|---|---|---|
| DB_HOST | Hôte MySQL | localhost |
| DB_NAME | Nom de la base de données | commerce-db |
| DB_USERNAME | Utilisateur MySQL | user |
| DB_PASSWORD | Mot de passe MySQL | user123 |

## Arrêt des services Docker
```

bash docker compose down```

Pour supprimer également les volumes :
```

bash docker compose down -v``` 

## Auteur

Projet développé dans le cadre d’une application de commerce en ligne.


