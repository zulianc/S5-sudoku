# S5-sudoku
## Architecture
- src : code source
  - package Constraints : regroupe les classes représentant des contraintes
  - package Grids : regroupe les classes représentant des grilles
  (soit les sudokus et les multidokus et leurs classes constitutives)
  - package Operations : regroupe les classes qui font des opérations sur les grilles
  (ici résoudre et stocker dans des fichiers)
  - package TextUI : regroupe les classes qui procurent une interface textuelle avec l'utilisateur
- doc : documentation (contient les diagrammes UML et le rapport, et un dossier où générer la javadoc)
- data : où sont stockés les sudokus sous forme de fichiers
(les dossiers sudokus et multidoku sont directement utilisés par l'application
et le dossier tests contient juste des exemples et ne sont pas accessibles depuis l'application)

Pour démarrer le projet, lancez la classe Main dans le dossier src.

Pour utiliser les sudokus et multidokus de test, copiez-les dans le bon dossier.  
Le fichier data/FormatFichiers.md contient le format des sudokus, multidokus, logs et contraintes.

Pour plus d'informations sur la méthodologie, les choix de conceptions et les extensions réalisées,  
voir le fichier doc/Rapport.md.
