package moe.plushie.dakimakuramod.common.dakimakura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;

public final class DakiExtractor {
    
    private static final String DAKI_ASSETS_LOCATION = "assets/dakimakuramod/dakis/";
    
    private static final String DAKI_PACK_ANDREWS = "Andrew's Vanilla Mobs";
    private static final String[] DAKI_PACK_ANDREWS_LIST = new String[] {
            "Alex",
            "Bat",
            "Blaze",
            "CaveSpider",
            "Chicken",
            "Cow",
            "Cow2",
            "Creeper",
            "Enderman",
            "Ghast",
            "Horse",
            "IronGolem",
            "Magmacube",
            "MooshroomCow",
            "MooshroomCow2",
            "Ocelot",
            "Pig",
            "Sheep",
            "Sheep2",
            "Silverfish",
            "Skeleton",
            "Slime",
            "Snowman",
            "Spider",
            "Squid",
            "Steve",
            "Villager",
            "Witch",
            "WitherSkeleton",
            "Wolf",
            "Zombie",
            "ZombiePigman"
        };
    
    private DakiExtractor() {}
    
    public static void extractDakis() {
        File packFolder = DakimakuraMod.getProxy().getDakimakuraManager().getPackFolder();
        if (new File(packFolder, "readme.txt").exists()) {
            if (!ConfigHandler.hasUpdated) {
                return;
            }
        }
        extractResource("readme.txt", new File(packFolder, "readme.txt"), true);
        extractDakiPack(packFolder, DAKI_PACK_ANDREWS, DAKI_PACK_ANDREWS_LIST);
    }
    
    private static void extractDakiPack(File packFolder, String packName, String[] packFiles) {
        packFolder = new File(packFolder, packName);
        if (!packFolder.exists()) {
            if (!packFolder.mkdir()) {
                DakimakuraMod.getLogger().error("Failed to make pack folder.");
                return;
            }
        }
        extractResource(packName + "/pack-info.json", new File(packFolder, "pack-info.json"), true);
        for (int i = 0; i < packFiles.length; i++) {
            extractDaki(packFolder, packName, packFiles[i]);
        }
    }
    
    private static void extractDaki(File packFolder, String packName, String name) {
        extractDaki(packFolder, packName, name, "daki-info.json", "front.png", "back.png");
    }
    
    private static void extractDaki(File packFolder, String packName, String name, String... files) {
        File dakiFolder = new File(packFolder, name);
        if (!dakiFolder.exists()) {
            dakiFolder.mkdir();
        }
        for (int i = 0; i < files.length; i++) {
            extractResource(packName + "/" + name + "/" + files[i], new File(dakiFolder, files[i]), true);
        }
    }
    
    private static void extractResource(String source, File target, boolean overwrite) {
        if (target.exists()) {
            if (overwrite) {
                target.delete();
            } else {
                return;
            }
        }
        InputStream input = null;
        FileOutputStream output = null;
        try {
            DakimakuraMod.getLogger().info(String.format("Extracting file '%s' to '%s'.", source, target.getAbsolutePath()));
            input = DakiExtractor.class.getClassLoader().getResourceAsStream(DAKI_ASSETS_LOCATION + source);
            if (input != null) {
                output = new FileOutputStream(target);
                IOUtils.copy(input, output);
                output.flush();
            } else {
                DakimakuraMod.getLogger().error(String.format("Error extracting file '%s'.", source));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }
}
