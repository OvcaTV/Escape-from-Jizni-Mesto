# Útěk z Jižního Města

Textová hra v Javě podle Game Design Documentu. Jsi student VŠE, který usnul při hodině v JM357B
a musí se dostat z budovy dřív, než dorazí ranní cvičení.

Potřebuješ **JDK 17 nebo novější**, žádné další knihovny.

## Spuštění

```bash
# přeložení
mkdir -p out
javac --release 17 -encoding UTF-8 -d out $(find src/main -name "*.java")

# hra
java -Dfile.encoding=UTF-8 -cp out cz.vse.escape.Main
```

Ve Windows PowerShellu místo `$(find ...)` použij:
`javac --release 17 -encoding UTF-8 -d out (Get-ChildItem -Recurse src/main -Filter *.java).FullName`
a před spuštěním `chcp 65001`, aby se správně zobrazila čeština.

### Testy

```bash
javac --release 17 -encoding UTF-8 -d out $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -cp out cz.vse.escape.WalkthroughTest
```

Test odehraje obě výhry, všechny prohry, chybové hlášky a ukládání/načítání.

## Ovládání

| Příkaz | Co dělá |
|---|---|
| `jdi [lokace]` | přesun (stačí začátek názvu, když je jednoznačný) |
| `seber [předmět]` | sebrání předmětu z místnosti |
| `pouzij [předmět] [cíl]` | použití předmětu (cíl jde vynechat, pokud je jen jeden možný) |
| `mluv [postava]` | rozhovor |
| `inventar` | seznam věcí u sebe |
| `prozkoumej [předmět]` | znovu popíše místnost / popíše předmět |
| `odejit` | pokus o odchod z budovy hlavními dveřmi |
| `uloz [soubor]`, `nacti [soubor]` | uložení a načtení hry (výchozí `savegame.properties`) |
| `napoveda`, `konec` | nápověda, konec |

V rozhovorech se odpovídá jen písmenem **A / B / C**. Diakritika a velikost písmen nehrají roli.

## Struktura projektu

```
cz.vse.escape
├── Main                 spouštěč, po konci hry nabídne novou hru
├── core                 Game (herní smyčka), GameState, GameFlag, GameFactory, konzole
├── world                Location, Exit, World, WorldFactory (mapa), LocationId, EntryGuard/EntryEvent
├── item                 Item, ItemId, ItemCatalog, ItemCollection
├── interaction          Interactable -> Npc (Mandik, Chlapek, ...) a Prop (VendingMachine, ExitDoor)
├── dialog               rozhovory ABC (Sql kvíz, slušnost u referentek, školník)
├── command              jeden příkaz = jedna třída, CommandRegistry
├── save                 SaveGameRepository (ukládání do .properties)
└── text                 Messages (obecné hlášky), StoryTexts (příběh), TextNormalizer, NameSet, AsciiArt

ascii/                   ASCII portréty postav, jeden soubor na postavu (mandik.txt, chlapek.txt, ...)
```

Zásady: žádná „logika příběhu“ v příkazech (příkazy jen volají postavy a svět), texty příběhu na jednom místě,
stav hry (`GameState`) oddělený od popisu světa (`World`), takže se dá snadno ukládat.

## Jak rozšířit hru

**Nová místnost**
1. Přidej hodnotu do `world/LocationId`.
2. V `WorldFactory.createLocations()` ji zadefinuj (`define(...)`).
3. V `WorldFactory.connectLocations()` ji napoj (`connectTo(cíl, "název", "poznámka v závorce", aliasy...)`).
4. Podmínka vstupu (např. potřebuješ klíč): `addEntryGuard(state -> ...)`;
   událost po vstupu: `addEntryEvent(game -> ...)` – v `addRules()`.

**Nový předmět** – hodnota v `ItemId` a řádek v `ItemCatalog.createStandard()`.
Položit ho do místnosti: `location(...).getItems().add(itemCatalog.get(...))` v `populateLocations()`.

**Nová postava** – třída dědící `Npc` (přepiš `talk`, případně `acceptsItem` + `useItem` pro předávání předmětů),
pak `addInteractable(new ...)` v `WorldFactory.populateLocations()`.
**Nová věc v prostředí** (automat, dveře…) – totéž s třídou dědící `Prop`.

**ASCII portrét postavy** – soubor `ascii/<id>.txt` (čistý text, nejlépe ASCII). Každá `Npc` vrací své `<id>`
v `portraitId()` a portrét se vypíše při každém `mluv`. Hra hledá složku `ascii/` v pracovním adresáři
(jiné umístění: `-Descape.ascii.dir=cesta`), potom v classpath. Chybějící soubor se tiše přeskočí.
Hru je proto potřeba spouštět z kořene projektu (tam, kde je složka `ascii/`).

**Nový rozhovor ABC** – třída dědící `ChoiceConversation` (`showQuestion` + `onOptionChosen`),
spouští se přes `game.startConversation(...)`.

**Nový příkaz** – třída implementující `Command` a jeden řádek v `CommandRegistry.createStandard()`.

**Nový příznak stavu** (co se už stalo) – hodnota v `GameFlag`; ukládá se automaticky.

## Poznámky k implementaci GDD

- Schodiště jsou jednosměrné východy (`schodiště` nahoru z 2. do 3. patra, `schodiště 2` dolů atd.).
- Po darování kávy spícímu studentovi se páčidlo objeví na WC ve 3. patře; návrat do JM357A znamená prohru (Mandík).
- Školník se spustí automaticky po vstupu na „Chodbu do 2. části“.
- Referentky: čtyři otázky, skryté body 1–3, k úspěchu je potřeba alespoň 9 z 12
  (`PolitenessConversation.MINIMUM_POINTS_TO_SUCCEED`).
- Uložit hru jde jen mimo rozhovor.
