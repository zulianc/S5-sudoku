# Format des puzzles
On a à chaque fois une ligne "header" suivi d'une ou plusieurs lignes de valeurs.  
Tous les headers sont obligatoires, mais certains n'ont pas forcément besoin de valeurs.  
Pour vous aider, il y a plusieurs fichiers d'exemple dans data/tests.
### Tous les indices dans les fichiers et dans le menu textuel commencent à 1 au lieu de 0 dans le code, pour faciliter la lecture et l'écriture

## Format d'un sudoku : 
```
puzzleType:
sudoku
useCustomPlacements:
//
useCustomSymbols:
//
size:
//
values:
//
placements:
//
symbols:
//
additionalConstraints:
//
end
```
- ``useCustomPlacements`` doit être rempli avec la valeur `true` ou `false`  
- ``useCustomSymbols`` doit être rempli avec la valeur `true` ou `false`  
- ``size`` doit être rempli par un entier représentant le côté du sudoku (classiquement 9)  
- ``values`` doit être rempli par 'taille' lignes de 'taille' entier, de 1 à 'taille', séparés par un espace  
- ``placements`` suit le même format que ``values``, et doit être rempli uniquement si ``useCustomPlacements`` est `true`, par les numéros des blocs auxquels appartiennent les cases, de 1 à 'taille'  
- ``symbols`` doit être rempli uniquement si ``useCustomSymbols`` est `true`, par 'taille' lignes représentant les symboles remplaçant les chiffres de 1 à 'taille'  
- ``additionalConstraints`` contient entre 0 et autant de contraintes que l'on veut, voir la partie sur le format des contraintes

## Format d'un multidoku :
```
puzzleType:
multidoku
useCustomSymbols:
//
sudokusCount:
//
/* pour chaque sudoku dans multidoku */
sudokuLine:
//
sudokuColumn:
//
/* insérer informations sudokus */
/* fin pour */
symbols:
//
additionalConstraints:
//
end
```
- La partie dans laquelle on insère les sudokus comporte exactement les mêmes champs qu'un fichier de sudoku
- ``sudokusCount``, ``sudokuLine``, et ``sudokuColumn`` sont tous des entier  
- ``sudokuLine`` et ``sudokuColumn`` référent à la place du sudoku sur la grille du multidoku  
- Pour ``additionalConstraints``, il y a un format légèrement différent pour les multidokus  

# Format des contraintes
Une contrainte s'écrit sur une ligne, elle est composées de 3 parties :  
``(contraintType)." ".(caseHasConstraint)." ".((caseToCompareTo)." ")*`` .   
Les types de contraintes actuellement reconnues par l'application sont : `=` et `!=`.  
On définit chaque case par sa position, donc `(ligne)." ".(colonne)`.  
Si c'est une contrainte de multidoku, on ajoute également la position du sudoku auquel appartient la case.  
Exemple : ``!= 1 1 1 2 2 1`` stipule que la case (1, 1) doit avoir une valeur différente des cases (1, 2) et (2, 1).
### Le format des contraintes est le même dans les fichiers et dans le menu textuel

# Format des logs
Les logs sont enregistrés dans des fichiers à part des puzzles.  
Un fichier de log représente simplement une suite d'instructions sur un puzzle,
peu importe les valeurs qui auraient étés contenus dans ce puzzle auparavant.  
Ils suivent les formats suivants, suivant si ce sont des logs sur un sudoku ou un multidoku :
```
algorithm:
//
puzzleType:
sudoku
size:
//
logs:
//
end
```
```
algorithm:
//
puzzleType:
multidoku
sudokusSize:
//
sudokusCount:
//
/* pour chaque sudoku dans multidoku */
sudokuLine:
//
sudokuColumn:
//
/* fin pour */
logs:
//
end
```
- Les champs autres que ``logs`` sont les mêmes que dans les fichiers de sudokus/multidokus.
- Chaque log est constitué comme suit : ``(placement case)."->".(nouvelle valeur case)``.  
``(placement case)`` suit le même format que pour les contraintes, c'est-à-dire  
``((line du sudoku)." ".(colonne du sudoku)." ")?.(ligne de la case)." ".(colonne de la case)." "``.  
où on rajoute la position du sudoku dans le multidoku si c'est une contrainte sur un multidoku.  
Exemple : ``1 1 2 3 -> 2`` signifie qu'on a donné la valeur 2 à la case (2, 3) du sudoku à la position (1, 1).
- On n'ajoute un log que si la valeur de la case a changée, et loguer "0" signifie
qu'on annule toutes les opérations jusqu'au dernier changement de valeur de cette case (backtrack).
- Quand on logue plusieurs fois sur la même case à la suite,
cela signifie qu'on teste plusieurs valeurs et que celles d'avant n'étaient donc pas bonnes.
