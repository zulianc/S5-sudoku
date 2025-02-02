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

Une fois le Solver commencé, un autre membre a commencé le Menu et le stockage dans les fichiers
pour pouvoir tester ce qui a été fait beaucoup plus simplement.
Le menu a également permis de revoir les cas d'utilisation et de lister tout ce qui restait à faire.

La prochaine étape a été l'ajout des logs de résolution, qui ont eu l'air d'avoir ralenti le Solver,
ou alors on ne s'était jamais rendu compte de la lenteur de notre backtracking avant.
Ce n'est qu'à ce moment qu'on a enfin partagé les classes en packages,
on avait déjà voulu le faire avant mais on n'était pas sûr de comment.
Mais comme on voulait vraiment rendre notre conception plus lisible,
notamment au niveau des contraintes,
on s'y est repris même si un peu trop tard.  

Finalement la dernière chose à faire a été d'ajouter les multidokus.
La conception avait été faite pour qu'il n'y ait "juste" que quelques fonctions à implémenter et que tout marche tout seul après,
donc ça n'a pas été super compliqué, mais on a quand même rencontré quelques problèmes.

## Choix de conception

On a choisi que les sudokus soient créés de façon à ce qu'ils soient toujours le plus valide possible.
C'est pour ça que les cases sont créées dans le sudoku qu'on ne peut pas en remplacer une par une nouvelle.
C'est également pour ça qu'on ne peut pas modifier sa valeur à volonté et qu'on a dû rajouter des attributs pour essayer ou cacher une valeur.
Tout au long du projet, on n'a jamais eu de problèmes avec l'intégrité des grilles du coup,
mais ça a sûrement ralenti les algos, car on copie la grille entière à chaque fois qu'on backtrack.
Ça aurait certainement pû être réalisé plus intelligemment, mais c'est le plus simple qu'on a trouvé.

Pour modéliser les multidokus, on a imaginé une espèce de super-grille, sur laquelle on viendrait placer des sudokus.
C'est dans ce but que l'on a créé une classe PlacedSudoku,
qui permet de lier un sudoku à un placement sur une de ces super-grilles.
Comme c'est géré actuellement, on autorise à avoir un multidoku ou les grilles ne se touchent pas sur des blocs entiers,
ou même des multidokus où aucune grille ne se touche.
On n'a cependant pas autorisé à avoir des grilles de tailles différentes, car ça aurait causé toute sorte de problèmes.
Au final, un multidoku n'est qu'une liste de sudoku avec des contraintes d'égalité sur les cases se trouvant au même endroit.

Pour pouvoir faciliter l'intégration de contraintes supplémentaires,
on a décidé de simplifier au maximum la notion de ce qu'est une contrainte, dans notre cas,
c'est juste un objet qui dit à une case quelles valeurs elle peut avoir ou ne pas avoir en fonction d'autre chose.
Cela fait qu'on peut facilement ajouter des contraintes entre n'importe quelle case et n'importe quoi d'autre,
que ce soit d'autres cases ou des valeurs brutes (ce qu'on n'a pas fait, mais rien dans notre conception l'empêche).

On a voulu pouvoir faire des grilles de n'importe quelle forme, c'est-à-dire pas forcément avec des blocs carrés,
c'est donc pour ça que l'on a une classe Bloc pour savoir quelle forme ils ont.
Pour plus de praticité cependant on a inclus le fait de créer des grilles avec des formes de blocs par défaut.
Ce choix allait aussi dans le sens de la généricité des contraintes,
vu que les contraintes n'ont aucunement besoin de savoir la forme de la grille.

Pour pouvoir faire une application plus générique, on a décidé de créer deux interfaces :
sur les grilles (Puzzle) et sur les contraintes (SudokuConstraint),
cela permet de généraliser les algos, notamment pour le Solver,
qui au final ne fait que prendre une liste de cases et des contraintes sur des cases.
Cela veut dire qu'on pourrait techniquement accepter d'autres types de grilles que les sudokus et les multidokus,
mais il faudrait changer pas mal de choses et on n'a pas vraiment pris ça en compte, car ce n'était pas le but.

On a décidé de créer les classes Solver et FilesOperations comme des classes abstraites,
car elles sont un peu comme des classes outils qui procurent seulement des méthodes sur les grilles.
Comme les fichiers ont été inclus à la conception depuis le début,
la création d'une classe servant uniquement à gérer les fichiers s'est faite naturellement pour pouvoir mieux séparer les fonctionnalités.

L'application justement est totalement contenue dans une classe Menu.
Pour pouvoir laisser la possibilité d'ajouter une interface graphique facilement,
on a décidé de complètement séparer tout le fonctionnement interne de l'interface avec l'utilisateur.
Ainsi aucune classe en dehors du package TextUI utilise le package TextUI ni n'affiche quoi que ce soit à l'utilisateur.
Une nouvelle interface graphique pourra s'implémenter en créant un nouveau package (par exemple GraphicalUI),
qui pourra utiliser les méthodes fournies par FilesOperations et Solver,
sans avoir à modifier quoi que ce soit dans le cœur de l'application.

Dans ce sens, c'est également Menu qui récupère la plupart des exceptions lancées par le reste de l'application.
On a décidé que c'était plus logique comme ça, car déjà la logique interne ne se bloque jamais ainsi,
et ensuite en fonction de comment on veut interagir avec l'utilisateur, on peut vouloir gérer les exceptions différemment,
et c'est seulement l'interface qui doit pouvoir afficher des messages à l'utilisateur.
On a essayé au maximum de rajouter toutes les exceptions possibles à la Javadoc et qu'elles soient toujours récupérées quelque part.
Les seules exceptions pas documentées sont celles de type accès à un indice incorrect de tableau, mais elles sont normalement récupérées avec le reste.

## Avancement du tronc commun

### Algo de résolution
Les trois types d'algos ont été implémentés et testés sur plusieurs grilles.
### Algo de génération
A été implémenté et testé sur plusieurs tailles de grilles.
### Loguer les opérations de résolution
A été implémenté et testé sur plusieurs grilles et algos.
### Menu textuel
Complètement fonctionnel.
### Affichage
Implémenté et testé sur des sudokus et des multidokus.
### Rentrer un sudoku à la main
Implémenté et testé sur des sudokus et des multidokus.

## Avancement des extensions

### Environnement de travail
Un repository git a été créé dès le début pour accueillir le projet
et tous les ajouts aux codes sont passés par là.
Le lien du repo est le suivant : https://github.com/zulianc/S5-sudoku.
### Tests unitaires
Pas réalisés.
### Interface graphique
Pas réalisée.  
Cependant, comme on a fait attention à ce qu'aucune classe ne dépende de TextUI,
il n'y aurait pas de difficulté en plus que de créer l'interface en elle-même.
### Fichier de configuration et sauvegarde
Ils ont étés intégrés à la conception depuis le début.
Les fichiers de sudokus et de multidokus peuvent être lus et écrits par l'application et sont relativement facilement éditable.
L'application peut écrire et lire des fichiers de logs, mais l'importation de logs n'a pas été réalisée.
### Rajout de règles de déduction
On peut rajouter des contraintes internes à un sudoku lors de sa création (ou dans son fichier de sauvegarde),
ou également en rajouter juste pour la durée d'un algo.
### Grilles avec multiples solutions
Pas réalisé.
### Rajout de contrainte
On n'a pas rajouté de nouveaux types de contraintes explicitement,
mais la conception permet de le faire relativement facilement.
Il suffit de créer une nouvelle classe implémentant SudokuConstraint dans le package Constraints,
et de l'ajouter à la méthode FilesOperations.readConstraint().
### Résolution par l’humain
Pas réalisée.
