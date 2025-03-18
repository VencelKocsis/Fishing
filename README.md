Specifikáció:
Horgász verseny applikáció készítése. Az applikációval a felhasználó regisztrálni, bejelentkezni tud saját fiókjába. A felhasználó firebase adatbázisba tud felvenni új fogás adatait és képet illeszteni hozzá. 
A felhasználó a többi felhasználó által létrehozott fogásokat láthatja listázva a képernyőn, vagy szűrni tud a sajátjára. 


Dokumentáció:

Alkalmazás felépítése:
A Fishing alkalmazás MVI architektúra felépítéssel és az általánosan használt fájlstruktúra szervezésével készült. 
Az alkalmazásnak van egy data és domain rétege. A data rétegben két elkülöníthető rész megfigyelhető; az authentikáció és a fogás. 

Mind az authentikáció és fogás kezelésére van <>Service interface, amit megvalósít a Firebase<>Service, ahol az alkalmazás a Firebase authentikációjával, adatbázisával és tárhelyével interaktál.
Itt taláható még az adatmodell, amit a firebase adatbázisábal tárolunk.

A domain szinten van megvalósítva a Dependecy Injection, az authentikáció, fogás, firebase UseCase-k.
A domain adatmodell amit az applikáció a felhasználó felé prezentál a ui rétegen keresztül.

A feature packageben megtalálhatóak a képernyők és hozzá tartozó viewmodel-k. A ViewModel-ek állapotokon keresztül változtatják a hozzájuk tartozó képernyő megjelenítését.
A ViewModel-k usecase-eken keresztül hívják meg a Firebase adatbázison kívánt elvégzendő feladatokat.

Végül a Navigation package-ben található a NavGraph, aminek segítségével, tud az alkalmazás a képernyők közötti navigációra. 

Alkalmazás felhasználói útmutatója:
Az alkalmazás megnyitásakor a felhasználót az Authentication képernyő fogadja, ahol a felhasználónak új fiókot regisztrálni, vagy meglévő fiókkal bejelentkezni van lehetősége. 
Vagy ha meglévő fiókjához elfelejtette jelszavát, akkokr password recovery email-ben megújíthatja azt. 
Bejelentkezve a felhasználó a fogásokat listázó képernyőre tud navigálni, előtte is odanavigálhat, viszont nem fognak betölteni az adatbázisban tárolt fogások, a felhasználó bejelentkezése előtt.

A fogásokat listázó képernyőn a felhasználó szűrni tud a listázott fogásokra, hogy csak a sajátját lássa, vagy az összes fogást az adatbázisból.
Egy fogásra kattintva a felhasználó megtekintheti a fogás adatait: név, faj, súly, hossz, fogásról készült kép. 
A felhasználó a fogások lisázása képernyőn található floating action gombbal létre tudja hozni saját fogását. 
Új fogás létrehozásakor a felhasználó megadja az előbb felsorolt adatokat, és feltölt a galériájából egy képet. A pipa ikonnal rendelkező floating action gombra kattintva
a kiválasztott kép feltöltésre kerül a Firebase Storage-ba és a fogás adatait beilleszti az adatbázisba az alkalmazás.
Egy fogást létrehozása után szerkeszteni, illetve törölni is lehet.
