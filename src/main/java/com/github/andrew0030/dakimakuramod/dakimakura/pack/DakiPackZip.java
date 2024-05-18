package com.github.andrew0030.dakimakuramod.dakimakura.pack;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiJsonSerializer;
import com.mojang.logging.LogUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DakiPackZip extends AbstractDakiPack
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public DakiPackZip(String file)
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
        // If the given path is null we return null and don't run further logic
        if(path == null) return null;

        try (ZipFile zipFile = new ZipFile(new File(this.dakiManager.getPackFolder(), this.getResourceName()), ZipFile.OPEN_READ)) {
            ZipEntry zipEntry = zipFile.getEntry(path);
            if (zipEntry != null) {
                try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                    return IOUtils.toByteArray(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean resourceExists(String path)
    {
        try (ZipFile zipFile = new ZipFile(new File(this.dakiManager.getPackFolder(), this.getResourceName()), ZipFile.OPEN_READ)) {
            return zipFile.getEntry(path) != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Loads and stores each {@link Daki} found inside the Daki Pack
     * @return The same {@link DakiPackZip} instance, but with its dakiMap populated
     */
    public DakiPackZip loadPack()
    {
        LOGGER.info(String.format("Loading Pack: '%s'", this.getResourceName()));
        try (ZipFile zipFile = new ZipFile(new File(this.dakiManager.getPackFolder(), this.getResourceName()), ZipFile.OPEN_READ)) {
            int depth = this.findDepth(zipFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            List<ZipEntry> entryList = Collections.list(entries).stream()
                    .filter(ZipEntry::isDirectory)
                    .sorted(Comparator.comparing(ZipEntry::getName))
                    .collect(Collectors.toList());
            for (int i = 0; i < entryList.size(); i++) {
                ZipEntry zipEntry = entryList.get(i);
                if (this.getDepth(zipEntry.getName()) == depth)
                    this.loadDaki(zipFile, zipEntry, (i == entryList.size() - 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Finds the maximum depth within the given {@link ZipFile}
     * @param zipFile The {@link ZipFile} to check
     * @return The maximum depth of the given {@link ZipFile}
     */
    private int findDepth(ZipFile zipFile)
    {
        int maxDepth = 0;
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements())
        {
            ZipEntry zipEntry = entries.nextElement();
            int entryDepth = this.getDepth(zipEntry.getName());
            maxDepth = Math.max(maxDepth, entryDepth);
        }
        return maxDepth;
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

    private void loadDaki(ZipFile zipFile, ZipEntry zipEntry, boolean lastInPack)
    {
        String logPrefix = lastInPack ? " \\ " : " | ";
        String dakiName = zipEntry.getName().substring(0, zipEntry.getName().length() - 1);
        String[] split = dakiName.split("/");
        ZipEntry dakiJsonZipEntry = zipFile.getEntry(zipEntry.getName() + "daki-info.json");
        if (dakiJsonZipEntry != null)
        {
            try (InputStream inputStream = zipFile.getInputStream(dakiJsonZipEntry)) {
                String dakiJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                Daki daki = DakiJsonSerializer.deserialize(dakiJson, this.getResourceName(), dakiName);
                if (daki != null)
                {
                    LOGGER.info(String.format(logPrefix + "Loading Dakimakura: '%s'", split[split.length - 1]));
                    this.addDaki(daki);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            LOGGER.info(String.format(logPrefix + "Loading Dakimakura Without Json: '%s' (No Json)", dakiName));
            this.addDaki(new Daki(this.getResourceName(), dakiName));
        }
    }
}