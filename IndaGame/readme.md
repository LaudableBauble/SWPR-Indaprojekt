Indaprojekt - Sebastian Wigren och Philip Rendén

Det är i huvudsak fyra metoder som är viktiga i spelet; loadContent(), handleInput(), update() och draw(). Alla dessa metoder finns i stort sett i varje klass jag skapat och de tre sistnämnda (handleInput(), update() och draw()) körs i varje uppdateringscykel i spelets main loop, dvs. i Game-klassens updateLoop().

HandleInput() använder sig av InputManager för att ge andra klasser tillgång till input från tangentbord och mus.
Update() använder sig av en timer (GameTimer) för att ge andra klasser tillgång till tid.
Draw() använder sig av Graphics2D som den ursprungligen får från den grundläggande WindowPanel-klassen (som implementerar Canvas och JPanel).

Allt som sker i spelfönstret har ScreenManager koll på. Det är denna klass som bestämmer vilken "screen" som för tillfället är synligt för spelaren. Den huvudmeny man först möter är t.ex en sk. "screen" och det är också den blåa bakgrunden man kommer till genom att trycka på "Play Game" i menyn. Spelaren (Zombien) och bokhyllan är därför instanser i GameplayScreen-klassen.

Både spelaren (Zombien) och bokhyllan är entites (Entity-klassen) och har fått en kropp (Body-klassen). För att kunna röra sig har dessa kroppar lagts in i fysikmotorn (PhysicsSimulator-klassen). Det är genom denna fysikmotor som kollisioner upptäcks. Det är även tack vare fysikmotorn som kroppar kan förflyttas, vilket de gör genom att skapa en kraft (Force-klassen) och skicka in den till fysik simulatorn. Vid varje uppdateringscykel läggs sedan alla uppsamlade krafter till på respektive kropp för att få dem att röra på sig.