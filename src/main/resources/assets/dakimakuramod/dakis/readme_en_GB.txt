This readme will give a quick overview on adding your own dakimakura images to the mod.



Quick Start
--------------
1. Inside the dakimakura-mod directory create a pack directory that will hold all your dakimakura's.
2. Create a directory for your dakimakura inside the pack directory.
3. Copy the images for you dakimakura into the folder making sure they are named front.png and back.png or front.jpg and back.jpg.
4. Start the game and your dakimakura should be listed in the Dakimakura Mod creative tab.

The directory structure should look like this.
<minecraft instance>
	dakimakura-mod
		<your pack>
			<your dakimakura>
				front.png
				back.png.

If you are still confused about the formatting check the "Vanilla Mobs" pack directory that comes with the mod.



Image Details
--------------
Dakimakura's in game are 50cm x 150cm so images that are a 1:3 ratio will work best.
The default config settings only allow an image size of 1024*515 per side, larger images will be scaled down to save VRAM. It is recommended that you keep images at or under this size.
Most image formats will be accepted however image formats without compression like BMP, are not recommended as they will take a long time for the client to load.



Advanced Usage
--------------
A pack-info.json file can be created inside the pack directory to display more information about your pack. (all elements are optional)
{
	"name":"Pack Name",
	"author":"Author Name",
	"credits":["Person 1", "Person 2"]
	"version":"1.0",
	"website":"https://github.com/RiskyKen/DakimakuraMod"
}

A daki-info.json file can be created inside each dakimakura directory. (all elements are optional)
{
	"name": "Dakimakura Name",
	"author": "Author Name",
	"image-front": "front.png",
	"image-back": "back.png",
	"flavour-text": "This text will show at the bottom of the dakimakura's tool-tip",
	"smooth":true
}

name: Name of the Dakimakura. If not added the directory name is used.
author: Name of the author. Will not be shown if none is provided.
image-front: File name of the front image. If not provided the mod will look for front.png, front.jpg or front.jpeg
image-back: File name of the back image. If not provided the mod will look for back.png, back.jpg or back.jpeg
flavour-text: Flavour text that will be shown at the bottom of the item tool-tip. Will not be shown if none is provided.
smooth: Should the image be smoothed when resized. Normally it's better to use true for photo realistic images and false for pixel art. Default is true.



Tips & Ticks
--------------
The command "/dakimakura reload" can be used to reload all dakimakura packs without restarting the game.
The command "/dakimakura openPackFolder" can be used to open the folder that holds the dakimakura images.
The hard coded max size for dakimakura images is 4096*8192 for each side. Using bigger images is just a waste of space.
Deleting the dakimakura-mod folder will cause the "Vanilla Mobs" pack to be reset.
If you are playing on a server new dakimakura's only need to be installed on the server, they will be sent to the clients when they connect.



Contact
--------------
If you have any question or issues please come by the discord server https://discord.gg/5Z3KKvU
