package moe.plushie.dakimakuramod.common.dakimakura;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.util.StringUtils;

public class DakiManager {
    
    private final File packFolder;
    private final HashMap<String, Daki> dakiMap;
    
    public DakiManager(File file) {
        packFolder = new File(file, "dakimakura-mod");
        if (!packFolder.exists()) {
            packFolder.mkdir();
        }
        dakiMap = new HashMap<String, Daki>();
        loadPacks();
    }
    
    public void loadPacks() {
        dakiMap.clear();
        File[] files = packFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                loadPack(files[i]);
            }
        }
    }
    
    private void loadPack(File dir) {
        DakimakuraMod.logger.info("Loading Pack: " + dir.getName());
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                loadDaki(dir, files[i]);
            }
        }
    }
    
    private void loadDaki(File packDir, File dakieDir) {
        DakimakuraMod.logger.info("Loading Dakimakura: " + dakieDir.getName());
        File dakiFile = new File(dakieDir, "daki-info.json");
        if (dakiFile.exists()) {
            String dakiJson = readStringFromFile(dakiFile);
            if (!StringUtils.isNullOrEmpty(dakiJson)) {
                Daki dakimakura = DakiSerializer.deserialize(dakiJson, packDir.getName(), dakieDir.getName());
                if (dakimakura != null) {
                    addDakiToMap(packDir.getName(), dakieDir.getName(), dakimakura);
                }
            }
        }
    }
    
    private void addDakiToMap(String packDirName, String dakiDirName, Daki daki) {
        dakiMap.put(packDirName + ":" + dakiDirName, daki);
    }
    
    public Daki getDakiFromMap(String packDirName, String dakiDirName) {
        return dakiMap.get(packDirName + ":" + dakiDirName);
    }
    
    private void writeStringToFile(File file, String data) {
        try {
            FileUtils.writeStringToFile(file, data, Charsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String readStringFromFile(File file) {
        String data = null;
        try {
            data = FileUtils.readFileToString(file, Charsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public ArrayList<Daki> getDakimakuraList() {
        ArrayList<Daki> dakimakuraList = new ArrayList<Daki>();
        for (int i = 0; i < dakiMap.size(); i++) {
            String key = (String) dakiMap.keySet().toArray()[i];
            Daki daki = dakiMap.get(key);
            if (daki != null) {
                dakimakuraList.add(daki);
            }
        }
        return dakimakuraList;
    }
    
    public File getPackFolder() {
        return packFolder;
    }
}
