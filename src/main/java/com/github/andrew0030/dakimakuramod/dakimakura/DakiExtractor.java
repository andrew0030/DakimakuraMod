package com.github.andrew0030.dakimakuramod.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.mojang.logging.LogUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.*;

public final class DakiExtractor
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String DAKI_ASSETS_LOCATION = "assets/" + DakimakuraMod.MODID + "/dakis/";
    private static final String[] README_LIST = new String[] {"en_GB", "de_DE"};
    private static final String DEFAULT_DAKI_PACK = "Vanilla Mobs";
    private static final String[] DEFAULT_DAKI_LIST = new String[] {"Allay", "Axolotl", "Camel", "Horse"};

    /** Creates the daki folder in the game directory, and then extracts the default dakis and README's into it */
    public static void extractDakis()
    {
        File packFolder = DakimakuraMod.getDakimakuraManager().getPackFolder();
//        if (packFolder.exists())
//            if (!ConfigHandler.hasUpdated)//TODO investigate hasUpdated and maybe replace this
//                return;

        // If the dakimakura folder doesn't exist we create it
        if (!packFolder.exists())
            packFolder.mkdirs();

        // I assume this is code that handles deleting a README named "readme.txt"
        // This was probably added because the README was translated and a new naming system was added
        // While I doubt this is needed at this point, just to be safe I will leave it here
        File oldReadme = new File(packFolder, "readme.txt");
        if (oldReadme.exists()) { try { oldReadme.delete(); } catch (Exception ignored) {} }

        // After creating the folder if needed, we extract the README's and default dakis
        DakiExtractor.extractReadmeFiles(packFolder);
        DakiExtractor.extractDakiPack(packFolder, DEFAULT_DAKI_PACK, DEFAULT_DAKI_LIST);
    }

    private static void extractReadmeFiles(File packFolder)
    {
        for (String langCode : README_LIST)
        {
            String readmeName = "readme_" + langCode + ".txt";
            DakiExtractor.extractResource(readmeName, new File(packFolder, readmeName), true);
        }
    }

    private static void extractDakiPack(File packFolder, String packName, String[] dakiNames)
    {
        packFolder = new File(packFolder, packName);
        // If the currently generating daki pack folder doesn't exist we attempt to create it
        if (!packFolder.exists() && !packFolder.mkdir()) {
            LOGGER.error("Failed to make default pack folder for [" + packName + "]");
            return;
        }
        // If the folder was created successfully or already existed, we extract the "pack-info.json" and all the dakis
        DakiExtractor.extractResource(packName + "/pack-info.json", new File(packFolder, "pack-info.json"), true);
        for (String dakiName : dakiNames)
            DakiExtractor.extractDakiFiles(packFolder, packName, dakiName, true, "daki-info.json", "front.png", "back.png");
    }

    /**
     * Creates a Dakimakura Folder, and all the given Dakimakura files. (In this case Json, front.png, back.png)
     * @param packFolder The daki pack folder this daki will be in
     * @param packName The daki pack name
     * @param name The daki name
     * @param overwrite Whether this should overwrite existing files
     * @param files The files that will be extracted
     */
    private static void extractDakiFiles(File packFolder, String packName, String name, boolean overwrite, String... files)
    {
        File dakiFolder = new File(packFolder, name);
        // If the daki folder doesn't already exist we create it
        if (!dakiFolder.exists())
            dakiFolder.mkdir();
        // Creates all the given files in the daki folder
        for (String file : files)
            DakiExtractor.extractResource(packName + "/" + name + "/" + file, new File(dakiFolder, file), overwrite);
    }

    private static void extractResource(String source, File target, boolean overwrite)
    {
        // If we don't want to overwrite existing files and the file already exists we return
        if (!overwrite && target.exists())
            return;
        try (InputStream input = DakiExtractor.class.getClassLoader().getResourceAsStream(DAKI_ASSETS_LOCATION + source);
             OutputStream output = new BufferedOutputStream(new FileOutputStream(target))) {
            if (input != null)
            {
                LOGGER.info(String.format("Extracting file '%s' to '%s'.", source, target.getAbsolutePath()));
                IOUtils.copy(input, output);
                output.flush();
            }
            else
            {
                LOGGER.error(String.format("Error extracting file '%s'.", source));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}