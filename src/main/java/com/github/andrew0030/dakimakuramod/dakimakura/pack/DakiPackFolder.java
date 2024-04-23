package com.github.andrew0030.dakimakuramod.dakimakura.pack;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.mojang.logging.LogUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class DakiPackFolder extends AbstractDakiPack
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public DakiPackFolder(String folder)
    {
        super(folder);
    }

    @Override
    public String getName()
    {
        return this.getResourceName();
    }

    @Override
    public byte[] getResource(String path)
    {
        FileInputStream inputStream = null;
        byte[] data = null;
        try
        {
            inputStream = new FileInputStream(new File(this.dakiManager.getPackFolder(), this.getResourceName() + "/" + path));
            data = IOUtils.toByteArray(inputStream);
            inputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
        return data;
    }

    @Override
    public boolean resourceExists(String path)
    {
        return new File(this.dakiManager.getPackFolder(), this.getResourceName() + "/" + path).exists();
    }

    public DakiPackFolder loadPack()
    {
        File dir = new File(this.dakiManager.getPackFolder(), this.getResourceName());
        LOGGER.info("Loading Pack: " + dir.getName());
        File[] files = dir.listFiles();
        for (File file : files)
            if (file.isDirectory())
                this.loadDaki(dir, file);
        return this;
    }

    private void loadDaki(File packDir, File dakieDir)
    {
        LOGGER.info("Loading Dakimakura: " + dakieDir.getName());
        File dakiFile = new File(dakieDir, "daki-info.json");
        if (dakiFile.exists())
        {
            String dakiJson = this.readStringFromFile(dakiFile);
//            if (!StringUtil.isNullOrEmpty(dakiJson)) {
//                Daki dakimakura = DakiJsonSerializer.deserialize(dakiJson, packDir.getName(), dakieDir.getName());
//                if (dakimakura != null) {
//                    this.addDakiToMap(dakimakura);
//                }
//            }
        }
        else
        {
            this.addDakiToMap(new Daki(packDir.getName(), dakieDir.getName()));
        }
    }

    private void addDakiToMap(Daki daki)
    {
        this.addDaki(daki);
    }

    private void writeStringToFile(File file, String data)
    {
        try
        {
            FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String readStringFromFile(File file)
    {
        String data = null;
        try
        {
            data = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }
}