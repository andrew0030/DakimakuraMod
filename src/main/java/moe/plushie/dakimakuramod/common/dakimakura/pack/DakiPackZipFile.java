package moe.plushie.dakimakuramod.common.dakimakura.pack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Charsets;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiJsonSerializer;

public class DakiPackZipFile extends AbstractDakiPack {

    public DakiPackZipFile(String file) {
        super(file);
    }
    
    @Override
    public String getName() {
        return getResourceName().substring(0, getResourceName().length() - 4);
    }
    
    @Override
    public byte[] getResource(String path) {
        ZipFile zipFile = null;
        InputStream inputStream = null;
        byte[] data = null;
        try {
            zipFile = new ZipFile(new File(dakiManager.getPackFolder(), getResourceName()), ZipFile.OPEN_READ);
            ZipEntry zipEntry = zipFile.getEntry(path);
            if (zipEntry != null) {
                inputStream = zipFile.getInputStream(zipEntry);
                data = IOUtils.toByteArray(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(zipFile);
        }
        return data;
    }
    
    @Override
    public boolean resourceExists(String path) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(new File(dakiManager.getPackFolder(), getResourceName()), ZipFile.OPEN_READ);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.getName().equals(path)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zipFile);
        }
        return false;
    }
    
    public DakiPackZipFile loadPack() {
        DakimakuraMod.getLogger().info("Loading Pack: " + getResourceName());
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(new File(dakiManager.getPackFolder(), getResourceName()), ZipFile.OPEN_READ);
            int depth = findDepth(zipFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.isDirectory() & getDepth(zipEntry.getName()) == depth) {
                    loadDaki(zipFile, zipEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zipFile);
        }
        return this;
    }
    
    private int findDepth(ZipFile zipFile) {
        int depth = 0;
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            depth = Math.max(depth, getDepth(zipEntry.getName()));
        }
        return depth;
    }
    
    private int getDepth(String str) {
        return StringUtils.countMatches(str, "/") - 1;
    }
    
    private void loadDaki(ZipFile zipFile, ZipEntry zipEntry) {
        String dakiName = zipEntry.getName().substring(0, zipEntry.getName().length() - 1);
        String[] split = dakiName.split("/");
        dakiName = split[split.length - 1];
        
        dakiName = zipEntry.getName().substring(0, zipEntry.getName().length() - 1);
        //DakimakuraMod.getLogger().info("Loading Dakimakura: " + dakiName);
        
        ZipEntry dakiJsonZipEntry = zipFile.getEntry(zipEntry.getName() + "daki-info.json");
        if (dakiJsonZipEntry != null) {
            InputStream inputStream = null;
            try {
                inputStream = zipFile.getInputStream(dakiJsonZipEntry);
                String dakiJson = IOUtils.toString(inputStream, Charsets.UTF_8);
                Daki daki = DakiJsonSerializer.deserialize(dakiJson, getResourceName(), dakiName);
                if (daki != null) {
                    addDaki(daki);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        } else {
            addDaki(new Daki(getResourceName(), dakiName));
        }
    }
}
