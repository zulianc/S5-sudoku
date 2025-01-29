# Rapport Projet APO
## Méthodologie et répartition des rôles
### Membres du groupe :
- Ashley PADAYODI
- Nawal EL KHAL
- Julien CHATAIGNER
### Répartition des rôles :
On a réparti les tâches à peu près équitablement,
en fonction de la disponibilité de chacun avec les autres projets.
On a discuté pour les grands choix de conception
et chacun a bossé sur le code comme sur les diagrammes.

On a d'abord fait des premiers essais de diagrammes de cas d'utilisation et de classes.
Après avoir demandé l'avis du prof, on a commencé à coder.
La première étape a été les classes de bases (Sudoku, Cases, etc.).
Puis, on a commencé le menu et le solver. C'est à ce moment-là qu'on a décidé de
l'implantation des choses qui restaient encore floues.
Les dernières grosses étapes ont été l'implémentation des multidoku, la génération de grille et les logs des opérations.

Une fois le code commencé, la javadoc et les diagrammes ont été réalisés au fur et à mesure du code.
## Avancement du tronc commun
### Algo de résolution
Les 3 types d'algos ont été implémentés et testés sur plusieurs grilles.
### Algo de génération
Pas encore implémenté.
### Loguer les opérations de résolution
Pas encore implémenté.
### Menu textuel
En cours de création.
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
### Rajout de règles de déduction
A été implémenté, on peut rajouter des contraintes à un sudoku lors de sa création et dans son fichier,
ou en rajouter juste pour la durée d'un algo.
### Grilles avec multiples solutions
Pas réalisé pour l'instant.
### Rajout de contrainte
N'a pas été rajouté explicitement, mais la conception permet de le faire facilement.
Il suffit de créer une nouvelle classe implémentant SudokuConstraint
et de rajouter son symbole dans les fonctions de FilesOperations et Menu.
### Résolution par l’humain
Pas réalisé.