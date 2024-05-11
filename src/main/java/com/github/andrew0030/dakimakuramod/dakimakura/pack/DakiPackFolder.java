package com.github.andrew0030.dakimakuramod.dakimakura.pack;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiJsonSerializer;
import com.mojang.logging.LogUtils;
import net.minecraft.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        // If the given path is null we return null and don't run further logic
        if(path == null) return null;

        try (FileInputStream inputStream = new FileInputStream(new File(this.dakiManager.getPackFolder(), this.getResourceName() + "/" + path))) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean resourceExists(String path)
    {
        return new File(this.dakiManager.getPackFolder(), this.getResourceName() + "/" + path).exists();
    }

    /**
     * Loads and stores each {@link Daki} found inside the Daki Pack
     * @return The same {@link DakiPackFolder} instance, but with its dakiMap populated
     */
    public DakiPackFolder loadPack()
    {
        LOGGER.info(String.format("Loading Pack: '%s'", this.getName()));
        File dir = new File(this.dakiManager.getPackFolder(), this.getResourceName());
        File[] files = dir.listFiles();
        if (files == null || files.length == 0)
        {
            LOGGER.warn(String.format("No Files found in Pack: '%s'", this.getName()));
            return this;
        }
        for (File file : files)
            if (file.isDirectory())
                this.loadDaki(dir, file);
        return this;
    }

    private void loadDaki(File packDir, File dakiDir)
    {
        File dakiFile = new File(dakiDir, "daki-info.json");
        if (dakiFile.exists())
        {
            String dakiJson = this.readStringFromFile(dakiFile);
            if (!StringUtil.isNullOrEmpty(dakiJson))
            {
                Daki dakimakura = DakiJsonSerializer.deserialize(dakiJson, packDir.getName(), dakiDir.getName());
                if (dakimakura != null)
                {
                    LOGGER.info(String.format("Loading Dakimakura: '%s'", dakiDir.getName()));
                    this.addDaki(dakimakura);
                }
            }
        }
        else
        {
            // TODO test this fallback generation and maybe move the logger above up or add a new one here
            this.addDaki(new Daki(packDir.getName(), dakiDir.getName()));
        }
    }

    /**
     * Helper method to easily write a {@link String} to a given {@link File}
     * @param file The {@link File} that will be written to
     * @param data The {@link String} that will be written to the {@link File}
     */
    private void writeStringToFile(File file, String data)
    {
        try {
            FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to easily read a {@link String} from a given {@link File}
     * @param file The {@link File} that will be read
     * @return A {@link String} with the contents of the given {@link File}
     */
    private String readStringFromFile(File file)
    {
        String data = null;
        try {
            data = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}