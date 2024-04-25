package com.github.andrew0030.dakimakuramod.dakimakura;

import com.github.andrew0030.dakimakuramod.dakimakura.pack.DakiPackZip;
import com.github.andrew0030.dakimakuramod.dakimakura.pack.IDakiPack;
import com.github.andrew0030.dakimakuramod.dakimakura.pack.DakiPackFolder;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DakiManager
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PACK_FOLDER_NAME = "dakimakura-mod";
    private final File packFolder;
    private final HashMap<String, IDakiPack> dakiPacksMap;

    public DakiManager(File file)
    {
        this.packFolder = new File(file, PACK_FOLDER_NAME);
        this.dakiPacksMap = new HashMap<>();
    }

    public void loadPacks(boolean sendToClients)
    {
        File[] files = this.packFolder.listFiles();
        if (files == null) {
            LOGGER.warn("Failed loading packs, because files are null!");
            return;
        }
        // If files aren't null we clear the packs map and repopulate it
        this.dakiPacksMap.clear();
        for (File file : files) {
            String resourceName = file.getAbsolutePath().substring(this.packFolder.getAbsolutePath().length() + 1);
            if (file.isDirectory()) // Folders
                this.dakiPacksMap.put(resourceName, new DakiPackFolder(resourceName).loadPack());
            if (file.isFile() && file.getName().endsWith(".zip")) // Zip Files
                this.dakiPacksMap.put(resourceName, new DakiPackZip(resourceName).loadPack());
        }
        // Makes the Server send the currently loaded DakiPacks to all clients
        if (sendToClients)
            this.sendDakiListToClients();
    }

    public Daki getDakiFromMap(String packDirName, String dakiDirName)
    {
        IDakiPack dakiPack = this.dakiPacksMap.get(packDirName);
        if (dakiPack != null)
            return dakiPack.getDaki(dakiDirName);
        return null;
    }

    public ArrayList<IDakiPack> getDakiPacksList()
    {
        return new ArrayList<>(this.dakiPacksMap.values());
    }

    public ArrayList<Daki> getDakiList()
    {
        ArrayList<Daki> dakimakuraList = new ArrayList<>();
        for (IDakiPack dakiPack : this.dakiPacksMap.values())
            dakimakuraList.addAll(dakiPack.getDakisInPack());
        Collections.sort(dakimakuraList);
        return dakimakuraList;
    }

    public void setDakiList(ArrayList<IDakiPack> packs)
    {
        this.dakiPacksMap.clear();
        for (IDakiPack pack : packs)
            dakiPacksMap.put(pack.getResourceName(), pack);
    }

    public ArrayList<Daki> getDakisInPack(String packName)
    {
        IDakiPack dakiPack = this.dakiPacksMap.get(packName);
        if (dakiPack != null)
            return dakiPack.getDakisInPack();
        return new ArrayList<>();
    }

    public int getDakiIndexInPack(Daki daki)
    {
        ArrayList<Daki> packList = this.getDakisInPack(daki.getPackDirectoryName());
        for (int i = 0; i < packList.size(); i++)
            if (daki.equals(packList.get(i)))
                return i;
        return -1;
    }

    public IDakiPack getDakiPack(String packName)
    {
        return this.dakiPacksMap.get(packName);
    }

    public File getPackFolder()
    {
        return this.packFolder;
    }

    private void sendDakiListToClients()
    {
        //TODO: add networking logic to this
//        PacketHandler.NETWORK_WRAPPER.sendToAll(new MessageServerSendDakiList());
    }
}