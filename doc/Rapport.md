# Rapport Projet APO 2024/2025 - Sudokus

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

Au début, on a juste fait les classes de bases (Sudoku, Case, etc.).
On ne savait pas encore exactement comment gérer les contraintes et le Solver.
C'est au moment où il a fallu le faire qu'on a décidé des choses qui restaient encore floues.  

Une fois le Solver commencé, un autre membre a commencé le Menu pour pouvoir tester ce qui a été fait beaucoup plus simplement.
Le menu a également permis de revoir les cas d'utilisation et de lister tout ce qui restait à faire.  
La prochaine étape a été l'ajout des logs de résolution, qui ont eu l'air d'avoir ralenti le Solver,
ou alors on ne s'était jamais rendu compte de la lenteur de notre backtracking avant.  

Ce n'est qu'à ce moment qu'on a enfin partagé les classes en packages,
on avait déjà voulu le faire avant mais on n'était pas sûr de comment.
Mais comme on voulait vraiment rendre notre conception plus lisible,
notamment au niveau des contraintes,
on s'y est repris même si un peu trop tard.  

Finalement la dernière chose à faire a été d'ajouter les multidokus.
Le code avait été fait pour qu'il n'y ait "juste" que quelques fonctions à implémenter et que tout marche tout seul après.

## Choix de conception

## Avancement du tronc commun

### Algo de résolution
Les trois types d'algos ont été implémentés et testés sur plusieurs grilles.
### Algo de génération
A été implémenté et testé sur plusieurs tailles de grilles.
### Loguer les opérations de résolution
A été implémenté et testé sur plusieurs grilles et algos.
### Menu textuel
Complètement fonctionnel, sauf pour les multidokus.
### Affichage
Implémenté et testé sur des sudokus, mais pas des multidokus.
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
D'un point de vue conception, il faudrait créer un nouveau package GraphicalUI
et appeler la classe principale de ce package depuis le Main au lieu de TextUI comme actuellement.
### Fichier de configuration et sauvegarde
Ils ont étés intégrés à la conception depuis le début.
Les fichiers de sudokus et de multidokus peuvent être lus et écrits par l'application et sont relativement facilement éditable.
L'application peut écrire et lire des fichiers de logs, mais pas les "importer".
### Rajout de règles de déduction
On peut rajouter des contraintes internes à un sudoku lors de sa création et dans son fichier,
ou en rajouter juste pour la durée d'un algo.
### Grilles avec multiples solutions
Pas réalisé pour l'instant.  
Peut s'implémenter assez facilement en forçant de ne pas s'arrêter quand on trouve une bonne valeur
et en renvoyant une ArrayList<Puzzle> au lieu d'un Puzzle dans la méthode Solver.applyBacktracking().
### Rajout de contrainte
On n'a pas rajouté de nouvelles contraintes explicitement,
mais la conception permet de le faire relativement facilement.  
Il suffit de créer une nouvelle classe implémentant SudokuConstraint dans le package Constraints,
et de l'ajouter à la méthode FilesOperations.readConstraint().
### Résolution par l’humain
Pas réalisé.  
On n'a pas du tout réfléchi à ce point lors de la conception, mais ça doit être possible sans modifier trop de choses.
