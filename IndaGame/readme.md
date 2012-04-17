Indaprojekt - Sebastian Wigren och Philip Rend�n

Det �r i huvudsak fyra metoder som �r viktiga i spelet; loadContent(), handleInput(), update() och draw(). Alla dessa metoder finns i stort sett i varje klass jag skapat och de tre sistn�mnda (handleInput(), update() och draw()) k�rs i varje uppdateringscykel i spelets main loop, dvs. i Game-klassens updateLoop().

HandleInput() anv�nder sig av InputManager f�r att ge andra klasser tillg�ng till input fr�n tangentbord och mus.
Update() anv�nder sig av en timer (GameTimer) f�r att ge andra klasser tillg�ng till tid.
Draw() anv�nder sig av Graphics2D som den ursprungligen f�r fr�n den grundl�ggande WindowPanel-klassen (som implementerar Canvas och JPanel).

Allt som sker i spelf�nstret har ScreenManager koll p�. Det �r denna klass som best�mmer vilken "screen" som f�r tillf�llet �r synligt f�r spelaren. Den huvudmeny man f�rst m�ter �r t.ex en sk. "screen" och det �r ocks� den bl�a bakgrunden man kommer till genom att trycka p� "Play Game" i menyn. Spelaren (Zombien) och bokhyllan �r d�rf�r instanser i GameplayScreen-klassen.

B�de spelaren (Zombien) och bokhyllan �r entites (Entity-klassen) och har f�tt en kropp (Body-klassen). F�r att kunna r�ra sig har dessa kroppar lagts in i fysikmotorn (PhysicsSimulator-klassen). Det �r genom denna fysikmotor som kollisioner uppt�cks. Det �r �ven tack vare fysikmotorn som kroppar kan f�rflyttas, vilket de g�r genom att skapa en kraft (Force-klassen) och skicka in den till fysik simulatorn. Vid varje uppdateringscykel l�ggs sedan alla uppsamlade krafter till p� respektive kropp f�r att f� dem att r�ra p� sig.