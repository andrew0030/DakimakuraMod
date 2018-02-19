package moe.plushie.dakimakuramod.common.dakimakura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import moe.plushie.dakimakuramod.DakimakuraMod;

public final class DakiExtractor {
    
    private static final String DAKI_ASSETS_LOCATION = "assets/dakimakuramod/dakis/";
    
    private DakiExtractor() {}
    
    public static void extractDakis() {
        File packFolder = DakimakuraMod.getProxy().getDakimakuraManager().getPackFolder();
        extractResource("readme.txt", new File(packFolder, "readme.txt"), false);
        packFolder = new File(packFolder, "Official Pack");
        if (!packFolder.exists()) {
            if (!packFolder.mkdir()) {
                DakimakuraMod.getLogger().error("Failed to make pack folder.");
            }
        }
        
        extractDaki(packFolder, "Creeper");
    }
    
    private static void extractDaki(File packFolder, String name) {
        extractDaki(packFolder, name, "daki-info.json", "front.png", "back.png");
    }
    
    private static void extractDaki(File packFolder, String name, String... files) {
        File dakiFolder = new File(packFolder, name);
        if (!dakiFolder.exists()) {
            dakiFolder.mkdir();
            for (int i = 0; i < files.length; i++) {
                extractResource(name + "/" + files[i], new File(dakiFolder, files[i]), true);
            }
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
            DakimakuraMod.getLogger().info(String.format("Extracting file %s. to %s", source, target.getAbsolutePath()));
            input = DakiExtractor.class.getClassLoader().getResourceAsStream(DAKI_ASSETS_LOCATION + source);
            if (input != null) {
                output = new FileOutputStream(target);
                IOUtils.copy(input, output);
                output.flush();
            } else {
                DakimakuraMod.getLogger().error(String.format("Error extracting file %s.", source));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }
}
