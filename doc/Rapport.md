# Rapport Projet APO
## Méthodologie et répartition des rôles
On a essayé de répartir les tâches à peu près équitablement,
en prenant compte la disponibilité de chacun avec les autres projets.
Chacun a contribué sur le code comme sur les diagrammes.
### Membres du groupe :
- Ashley PADAYODI
- Nawal EL KHAL
- Julien CHATAIGNER
### Chronologie
On a d'abord fait des premiers essais de diagrammes de cas d'utilisation et de classes.
C'est là qu'on a discuté des grands choix de conception à adopter.
Après avoir demandé l'avis du prof, on a commencé à coder.
Une fois le code commencé, la javadoc et les diagrammes ont été réalisés au fur et à mesure du code.  
Au début, on a juste fait les classes de bases (Sudoku, Cases, etc.).
On ne savait pas encore exactement comment gérer les contraintes et le Solver.
C'est au moment où il a fallu le faire qu'on a décidé des choses qui restaient encore floues.  
Une fois le Solver commencé, un autre membre a commencé le Menu pour pouvoir tester ce qui a été fait beaucoup plus simplement.
Le menu a également permis de revoir les cas d'utilisation et de lister tout ce qui restait à faire.  
La prochaine étape a été l'ajout des logs de résolution, qui ont eu l'air d'avoir ralenti le Solver,
ou alors on ne s'était jamais rendu compte de la lenteur du backtracking avant.  
Finalement la dernière chose à faire a été d'ajouter les Multidokus.
Le code avait été faite pour qu'il n'y ait "juste" que quelques fonctions à implémenter et que tout marche tout seul après.

## Avancement du tronc commun
### Algo de résolution
Les trois types d'algos ont été implémentés et testés sur plusieurs grilles.
### Algo de génération
A été implémenté et testé sur plusieurs tailles de grilles.
### Loguer les opérations de résolution
A été implémenté et testé sur plusieurs grilles et algos.
### Menu textuel
Complètement fonctionnel, sauf pour les Multidoku.
### Affichage
On peut afficher les sudokus depuis le menu, mais pas encore les multidokus.
### Rentrer un sudoku à la main
Implémenté et testé sur des sudokus, mais pas des multidokus.
## Avancement des extensions
### Environnement de travail
Un repository GitHub a été créé dès le début pour accueillir le projet
et tous les ajouts aux codes sont passés par là.
### Tests unitaires
Pas réalisés.
### Interface graphique
Pas réalisée.
### Fichier de configuration et sauvegarde
Ils ont étés intégrés à la conception depuis le début.
Pour l'instant seulement les fichiers de sudoku sont fonctionnels.
On peut écrire et lire des fichiers de logs, mais pas les "importer".
### Rajout de règles de déduction
A été implémenté, on peut rajouter des contraintes internes à un sudoku lors de sa création et dans son fichier,
ou en rajouter juste pour la durée d'un algo.
### Grilles avec multiples solutions
Pas réalisé pour l'instant.
### Rajout de contrainte
Ça n'a pas été rajouté explicitement, mais la conception permet de le faire relativement facilement.
Il "suffit" de créer une nouvelle classe implémentant SudokuConstraint
et de l'ajouter à la lecture de contraintes dans Menu.java et FilesOperations.java.
### Résolution par l’humain
Pas réalisé.