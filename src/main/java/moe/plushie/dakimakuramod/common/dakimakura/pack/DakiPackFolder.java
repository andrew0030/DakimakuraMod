package moe.plushie.dakimakuramod.common.dakimakura.pack;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiJsonSerializer;
import net.minecraft.util.StringUtils;

public class DakiPackFolder extends AbstractDakiPack {
    
    public DakiPackFolder(String folder) {
        super(folder);
    }
    
    @Override
    public String getName() {
        return getResourceName();
    }
    
    @Override
    public byte[] getResource(String path) {
        FileInputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(new File(dakiManager.getPackFolder(), getResourceName() + "/" + path));
            data = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return data;
    }
    
    @Override
    public boolean resourceExists(String path) {
        return new File(dakiManager.getPackFolder(), getResourceName() + "/" + path).exists();
    }
    
    public DakiPackFolder loadPack() {
        File dir = new File(dakiManager.getPackFolder(), getResourceName());
        DakimakuraMod.getLogger().info("Loading Pack: " + dir.getName());
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                loadDaki(dir, files[i]);
            }
        }
        return this;
    }
    
    private void loadDaki(File packDir, File dakieDir) {
        //DakimakuraMod.getLogger().info("Loading Dakimakura: " + dakieDir.getName());
        File dakiFile = new File(dakieDir, "daki-info.json");
        if (dakiFile.exists()) {
            String dakiJson = readStringFromFile(dakiFile);
            if (!StringUtils.isNullOrEmpty(dakiJson)) {
                Daki dakimakura = DakiJsonSerializer.deserialize(dakiJson, packDir.getName(), dakieDir.getName());
                if (dakimakura != null) {
                    addDakiToMap(dakimakura);
                }
            }
        } else {
            addDakiToMap(new Daki(packDir.getName(), dakieDir.getName()));
        }
    }
    
    private void addDakiToMap(Daki daki) {
        addDaki(daki);
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
}
