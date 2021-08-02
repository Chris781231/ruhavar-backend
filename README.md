# Ruhavár backend applikáció

Ez az applikáció egy eladó napi adminisztrációját könnyíti meg. Segítségével nyomon követhetőek az eladók, vásárlók adatai, a vásárlások, az eladások, valamint különféle statisztikák. A háttérben JPA(Hibernate) adatbázis gondoskodik a tárolásról.

## Elkészül funkciók

### Vásárlók

- név
- város
- lakcím
- telefonszámok listája
- email-cím

### Telefonszámok
- típus (pl. mobil, vezetékes)
- szám

####Vásárlók tábla funkciói:
- mentés
- módosítás (egyedi azonosító alapján)
- listázás (egyedi azonosító alapján, névtöredék alapján vagy teljes listát)
- törlés (egyedi azonosító alapján vagy teljes lista)

####Vásárlóhoz tartozó telefonszámlista funkciói:
- mentés
- telefonszám módosítása (egyedi azonosító alapján, csak a szám módosítása)
- listázás (egyedi azonosító alapján vagy teljes lista)
- törlés (egyedi azonosító alapján vagy teljes lista)
