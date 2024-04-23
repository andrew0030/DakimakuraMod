package com.github.andrew0030.dakimakuramod.dakimakura.pack;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.mojang.logging.LogUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DakiPackZipFile extends AbstractDakiPack
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public DakiPackZipFile(String file)
    {
        super(file);
    }

    @Override
    public String getName()
    {
        return this.getResourceName().substring(0, this.getResourceName().length() - 4);
    }

    @Override
    public byte[] getResource(String path)
    {
        ZipFile zipFile = null;
        InputStream inputStream = null;
        byte[] data = null;
        try
        {
            zipFile = new ZipFile(new File(this.dakiManager.getPackFolder(), this.getResourceName()), ZipFile.OPEN_READ);
            ZipEntry zipEntry = zipFile.getEntry(path);
            if (zipEntry != null)
            {
                inputStream = zipFile.getInputStream(zipEntry);
                data = IOUtils.toByteArray(inputStream);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(zipFile);
        }
        return data;
    }

    @Override
    public boolean resourceExists(String path)
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(new File(this.dakiManager.getPackFolder(), this.getResourceName()), ZipFile.OPEN_READ);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements())
            {
                ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.getName().equals(path))
                    return true;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(zipFile);
        }
        return false;
    }

    public DakiPackZipFile loadPack()
    {
//        DakimakuraMod.getLogger().info("Loading Pack: " + getResourceName());
        ZipFile zipFile = null;
        try
        {
            zipFile = new ZipFile(new File(this.dakiManager.getPackFolder(), this.getResourceName()), ZipFile.OPEN_READ);
            int depth = this.findDepth(zipFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements())
            {
                ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.isDirectory() && this.getDepth(zipEntry.getName()) == depth)
                    loadDaki(zipFile, zipEntry);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(zipFile);
        }
        return this;
    }

    private int findDepth(ZipFile zipFile)
    {
        int depth = 0;
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements())
        {
            ZipEntry zipEntry = entries.nextElement();
            depth = Math.max(depth, this.getDepth(zipEntry.getName()));
        }
        return depth;
    }

    private int getDepth(String str)
    {
        return this.countOccurrences(str, '/') - 1;
    }

    private int countOccurrences(String str, char delimiter)
    {
        int count = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == delimiter)
                count++;
        return count;
    }

    private void loadDaki(ZipFile zipFile, ZipEntry zipEntry)
    {
        String dakiName = zipEntry.getName().substring(0, zipEntry.getName().length() - 1);
        String[] split = dakiName.split("/");
        dakiName = split[split.length - 1];

        dakiName = zipEntry.getName().substring(0, zipEntry.getName().length() - 1);
        // DakimakuraMod.getLogger().info("Loading Dakimakura: " + dakiName);

        ZipEntry dakiJsonZipEntry = zipFile.getEntry(zipEntry.getName() + "daki-info.json");
        if (dakiJsonZipEntry != null)
        {
            InputStream inputStream = null;
            try {
                inputStream = zipFile.getInputStream(dakiJsonZipEntry);
                String dakiJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//                Daki daki = DakiJsonSerializer.deserialize(dakiJson, getResourceName(), dakiName);
//                if (daki != null)
//                    this.addDaki(daki);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                IOUtils.closeQuietly(inputStream);
            }
        }
        else
        {
            this.addDaki(new Daki(getResourceName(), dakiName));
        }
    }
}