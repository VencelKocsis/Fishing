Specifikáció:
Horgász verseny applikáció készítése. Az applikációval a felhasználó regisztrálni, bejelentkezni tud saját fiókjába. A felhasználó firebase adatbázisba tud felvenni új fogás adatait és képet illeszteni hozzá. 
A felhasználó a többi felhasználó által létrehozott fogásokat láthatja listázva a képernyőn, vagy szűrni tud a sajátjára. 


Dokumentáció:
A Fishing alkalmazás MVI architektúra felépítéssel és az általánosan használt fájlstruktúra szervezésével készült. 
Az alkalmazásnak van egy data és domain rétege. A data rétegben két elkülöníthető rész megfigyelhető; az authentikáció és a fogás. 

Mind az authentikáció és fogás kezelésére van <>Service interface, amit megvalósít a Firebase<>Service, ahol az alkalmazás a Firebase authentikációjával, adatbázisával és tárhelyével interaktál.
Itt taláható még az adatmodell, amit a firebase adatbázisábal tárolunk.

A domain szinten van megvalósítva a Dependecy Injection, az authentikáció, fogás, firebase UseCase-k.
A domain adatmodell amit az applikáció a felhasználó felé prezentál a ui rétegen keresztül.

A feature packageben megtalálhatóak a képernyők és hozzá tartozó viewmodel-k. A ViewModel-ek állapotokon keresztül változtatják a hozzájuk tartozó képernyő megjelenítését.
A ViewModel-k usecase-eken keresztül hívják meg a Firebase adatbázison kívánt elvégzendő feladatokat.

Végül a Navigation package-ben található a NavGraph, aminek segítségével, tud az alkalmazás a képernyők közötti navigációra. 

