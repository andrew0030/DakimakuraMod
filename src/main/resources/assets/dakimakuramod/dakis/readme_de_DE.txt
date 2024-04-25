Dieses liesmich wird eine kurze Übersicht darüber geben wie du deine eigenen Dakimakura Bilder, der Mod hinzufügen kannst.



Schnellstart
--------------
1. Mache einen Daki-paket Ordner im Dakimakura Mod Ordner in dem alle deine Dakimakuras gespeichert werden.
2. Mache einen Ordner für dein Dakimakura in dem Ordner deines Daki-pakets.
3. Kopiere die Bilder für dein Dakimakura in den Ordner und achte darauf, dass die Bilder front.png und back.png oder front.jpg und back.jpg benannt sind.
4. Starte dein Spiel und dein Dakimakura sollte im Kreativ inventar gelistet sein.

Deine Ordnerstruktur sollte so aussehen.
<minecraft instance>
	dakimakura-mod
		<dein Daki-paket>
			<dein Dakimakura>
				front.png
				back.png.

Wenn du noch immer mit der Formatierung verwirrt bist, schaue dir den "Vanilla Mobs" Daki-packet ordner an, der mit der Mod installiert kommt.



Bild Details
--------------
Dakimakura's im Spiel sind 50cm x 150cm sodass Bilder die das Verhältnis 1:3 haben am besten funktionieren.
Die standard config Einstellungen erlauben nur eine Bildgröße von 1024*515 pro Seite, größere Bilder werden runter skaliert um VRAM zu sparen. Es ist empfohlen, dass du deine Bilder so groß oder kleiner hältst.
Die meisten Bildformate werden akzeptiert, allerdings ist empfohlen Bildformate, ohne Verdichtung wie BMP nicht zu verwenden, da sie für den Klient lange zu laden brauchen werden.



Erweiterte Nutzung
--------------
Eine pack-info.json Datei kann dem Paket Ordner hinzugefügt werden, um mehr Informationen zu deinem Paket anzuzeigen. (alle Elemente sind optional)
{
	"name":"Daki-packet Name",
	"author":"Author Name",
	"credits":["Person 1", "Person 2"],
	"version":"1.0",
	"website":"https://github.com/RiskyKen/DakimakuraMod"
}

Eine daki-info.json Datei kann in jeder dakimakura Datei hinzugefügt werden. (alle Elemente sind optional)
{
	"name": "Dakimakura Name",
	"author": "Author Name",
	"image-front": "front.png",
	"image-back": "back.png",
	"flavour-text": "Dieser text wird unter der dakimakura Gegenstandsbeschreibung angezeigt",
	"smooth":true
}

name: Name des Dakimakura. Ohne wird der Ordnername verwendet.
author: Name das Authors. Wird nicht angezeigt, wenn keiner gegeben wird.
image-front: Datei Name des vorderen Bilds. Ohne Angabe wird die Mod nach front.png, front.jpg oder front.jpeg suchen.
image-back: Datei Name des hinteren Bilds. Ohne Angabe wird die Mod nach back.png, back.jpg oder back.jpeg suchen.
flavour-text: Kurzer Text der unter der Gegenstandsbeschreibung angezeigt wird. Wird nicht angezeigt, wenn keiner gegeben wird.
smooth: Sollte das Bild geglättet werden, wenn es verformt wird. Normalerweise ist es besser für fotorealistische Bilder true zu verwenden und für PixelArt false. Voreinstellung ist true.



Tipps & Tricks
--------------
Der Befehl "/dakimakura reload" kann genutzt werden, um alle Dakimakura Packete neu zu laden, ohne das Spiel neu zu starten.
Der Befehl "/dakimakura openPackFolder" kann genutzt werden, um den Ordner mit den Dakimakura Bildern zu öffnen.
Die festgeschriebene maximal Größe für Dakimakura Bilder ist 4096*8192 für jede Seite. Größere Bilder zu verwenden ist nur Platzverschwendung.
Den dakimakura-mod Ordner zu löschen wird dazu führen, dass "Vanilla Mobs" Daki-packet zurückgesetzt wird.
Wenn du auf einem Server spielst müssen neue Dakimakuras nur auf dem server installiert werden, sie werden automatisch an den Klient gesendet, wenn er verbindet.



Kontakt
--------------
Bei Fragen oder Problemen schau bitte auf dem Discord Server vorbei https://discord.gg/5Z3KKvU