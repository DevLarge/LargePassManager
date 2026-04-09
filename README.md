# Oscar Bakkeng prosjekt i Objektorientert programmering - LargePasswordManager. 

## Beskrivelse av app. 

LargePasswordManager er en password manager laget i Java og JavaFX. Målet med appen er å gi brukeren en trygg og oversiktlig måte å lagre passord på, uten at passordene ligger i klartekst på disk. Når man starter appen første gang, opprettes et masterpassord. Dette passordet brukes videre for å låse opp et vault ved innlogging. 

I vault kan brukeren legge til, vise og slette oppføringer (entries). Hver oppføring består av service name, username og password. Oppføringene vises i en liste i brukergrensesnittet, og informasjonen for valgt oppføring vises i eget visningsfelt. Dataene lagres lokalt i filer i brukerens hjemmemappe. 

For lagring brukes en kombinasjon av JSON-format og kryptering. En liste med entries konverteres til tekst, krypteres med masterpassordet og skrives til fil (vault.db). Ved innlasting gjøres det motsatte: filinnhold dekrypteres og bygges opp til objekter igjen.  

## Klassediagram
<img src=class-diagram.png alt="Klassediagram">


## Refleksjon og spørsmål

### 1. Hvilke deler av pensum i emnet dekkes i prosjektet, og på hvilken måte? 
Prosjektet dekker flere deler av pensum i faget. Jeg har brukt klasser og objekter til å organisere appen, blant annet for entries, services, controllers, utility, innkapsling og delegering. Der JavaFX-grensesnittet fungerer som view, controllerne tar imot handlinger, manager og serviceklassene håndterer logikk og databehandling. I tillegg har jeg brukt filer, liste for entries, og JSON for å lagre og lese data. Prosjektet inneholder også håndtering av unntak og enkel testing med JUnit. 

### 2. Dersom deler av pensum ikke er dekket i prosjektet deres, hvordan kunne dere brukt disse delene av pensum i appen? 
En del av pensum som er lite brukt i prosjektet er arv. Koden er hovedsakelig bygd med tanke på at klasser samarbeider ved hjelp av referanser, og ikke med subklasser via `extends`. Det fungerer godt i denne appen, men hvis jeg skulle dekket arv tydeligere kunne jeg laget en felles abstrakt klasse eller et interface for tjenester, for eksempel en generell lagrings- eller krypteringskomponent, og så laget flere implementasjoner. Da ville prosjektet i større grad vist både arv i praksis.

### 3. Hvordan forholder koden deres seg til Model-View-Controller-prinsippet?
Koden følger Model-View-Controller-prinsippet ganske tydelig. View-delen er FXML-filene som viser grensesnittet, mens controller-klassene håndterer brukerinput og koblingen mellom skjerm og logikk. Model-delen er spesielt Entry-objektene og dataene som representerer innholdet i vaultet. I tillegg er mye av logikken flyttet ut i manager og service-klasser, som gir en god ansvarsdeling. Controllerne styrer flyt i UI, managerne håndterer app-logikk, og service-laget tar seg av filbehandling og kryptering. Løsningen er derfor i praksis en MVC-variant med et ekstra lag for bedre struktur. 

### 4. Hvordan har dere gått frem når dere skulle teste appen deres, og hvorfor har dere valgt de testene dere har?
Jeg har testet appen med enhetstester (JUnit) og manuell testing i JavaFX. I enhetstestene fokuserte jeg på de delene som er enklest og viktigst å verifisere automatisk, spesielt kryptering/dekryptering og filhåndtering. Der testet vi blant annet at data faktisk blir kryptert, at dekryptering gir tilbake original tekst med riktig passord, og at nødvendige filer blir opprettet og slettet som forventet.  

I tillegg har vi kjørt manuelle tester av hele appen, for eksempel førstegangsoppsett av masterpassord, innlogging, bytte mellom vinduer, og legg til/slett av entries i vault. Jeg valgte disse testene fordi de dekker både kjernelogikken (sikker lagring av data) og de viktigste handlingene i appen. Målet var å sikre at appen fungerer stabilt i praksis, samtidig som sentrale metoder er kontrollert med automatiske tester. 

## KI-deklarasjon

I denne delen beskrives hvordan KI ble brukt i prosjektet.

### Måtte du bruke KI for å få til prosjektet, eller var det bare tidsbesparende?
Med tanke på å bytte stage trengte jeg hjelp fra KI. 
I tillegg til å få på plass Encryption logikken, som var altfor avansert for meg. Dette går fint fordi selve krypteringen ikke er sentral for å dekke kompetansemålene i faget. 

### Dersom du brukte KI, prøvde du flere modeller? Lekte du deg med hva mulighetene er?
Jeg brukte GitHub Copilot sin Auto modell, som velger ut ifra spørsmålet. Siden tingene den sa til meg fungerte i praksis, så jeg ikke noe poeng i å teste flere måter. 

### Dersom du brukte KI, i hvilken grad? Skrev den koden din, eller ble den brukt mest til debugging og forklaringer?

Ble bare brukt til forklaring, kodeforslag og debugging. Prøvde å fortså alt selv, men encryption algorytmen var vanskelig å forstå. 
