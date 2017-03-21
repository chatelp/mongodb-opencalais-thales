# POC BigData pour Thales

##Objectif du projet

Analyse de contenus textuels de type Big Data (ici dumps Wikipedia complet), en base stockage dans MongoDB, via les services Web d'analyse d'OpenCalais.

Réunit les étapes de la chaine de traitement Big Data :- Le cumul des informations dans une base de données- Le traitement des données via des requêtes
- L’analyse des données
- La visualisation des donnéesAutres éléments :

- Système de requêtes graphique guidant l’utilisateur (ne requiert pas d’apprentissage).- Possibilité de modifier manuellement la requête - Utilisation de la bibliothèque Jena


##Etape 1

Lancer mongodb dans un invite de commande Windows.
Par exemple: C:\mongodb\mongod.exe --dbpath C:
\mongodata

##Etape 2

Lancer l'application BigData

##Etape 3 : Connexion (par défaut):
Host: localhost
Port: 27017
Nom d'utilisateur: [Laisser vide]
Mot de passe: [Laisser vide]
Base de données: [Ce que vous voulez, la base sera créee à la volée si elle n'existe pas]

Importer un dump Wikipedia (téléchargeable à cette adresse: [http://dumps.wikimedia.org/frwiki/](http://dumps.wikimedia.org/frwiki/))
Onglet "Fichiers" -> "Importer"-> "Dump Wikipedia"

##Etape 4
A la fin de l'importation (ou apr&s avoir avoir arrété l'importation manuellement), fermer et relancer le logiciel.

Informations diverses:

- Penser à être connecté à Internet pour l'analyse via OpenCalais,
- Avant de lancer une analyse, appliquer un filtre sur les données à analyser,
- Se référer à la documentation mongoDB pour supprimer des donn�es: http://docs.mongodb.org/manual/applications/delete/
Pour éviter de passer par un shell, il est possible d'utiliser des logiciels permettant la suppression (i.e: MongoExplorer)