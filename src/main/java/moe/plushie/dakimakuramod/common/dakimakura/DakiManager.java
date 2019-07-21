package moe.plushie.dakimakuramod.common.dakimakura;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import moe.plushie.dakimakuramod.common.dakimakura.pack.DakiPackFolder;
import moe.plushie.dakimakuramod.common.dakimakura.pack.DakiPackZipFile;
import moe.plushie.dakimakuramod.common.dakimakura.pack.IDakiPack;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerSendDakiList;

public class DakiManager {
    
    private static final String PACK_FOLDER_NAME = "dakimakura-mod";
    
    private final File packFolder;
    private final HashMap<String, IDakiPack> dakiPacksMap;
    
    public DakiManager(File file) {
        packFolder = new File(file, PACK_FOLDER_NAME);
        dakiPacksMap = new HashMap<String, IDakiPack>();
    }

    public void loadPacks(boolean sendToClients) {
        dakiPacksMap.clear();
        for (File file : packFolder.listFiles()) {
            String resourceName = file.getAbsolutePath().substring(packFolder.getAbsolutePath().length() + 1, file.getAbsolutePath().length());
            if (file.isDirectory()) {
                dakiPacksMap.put(resourceName, new DakiPackFolder(resourceName).loadPack());
            }
            if (file.isFile() & file.getName().endsWith(".zip")) {
                dakiPacksMap.put(resourceName, new DakiPackZipFile(resourceName).loadPack());
            }
        }
        if (sendToClients) {
            sendDakiListToClients();
        }
    }
    
    public Daki getDakiFromMap(String packDirName, String dakiDirName) {
        IDakiPack dakiPack = dakiPacksMap.get(packDirName);
        if (dakiPack != null) {
            return dakiPack.getDaki(dakiDirName);
        }
        return null;
    }
    
    public ArrayList<IDakiPack> getDakiPacksList() {
        ArrayList<IDakiPack> packsList = new ArrayList<IDakiPack>();
        for (IDakiPack dakiPack : dakiPacksMap.values()) {
            packsList.add(dakiPack);
        }
        return packsList;
    }
    
    public ArrayList<Daki> getDakiList() {
        ArrayList<Daki> dakimakuraList = new ArrayList<Daki>();
        for (IDakiPack dakiPack : dakiPacksMap.values()) {
            dakimakuraList.addAll(dakiPack.getDakisInPack());
        }
        Collections.sort(dakimakuraList);
        return dakimakuraList;
    }
    
    public void setDakiList(ArrayList<IDakiPack> packs) {
        dakiPacksMap.clear();
        for (IDakiPack pack : packs) {
            dakiPacksMap.put(pack.getResourceName(), pack);
        }
    }
    
    public ArrayList<Daki> getDakisInPack(String packName) {
        IDakiPack dakiPack = dakiPacksMap.get(packName);
        if (dakiPack != null) {
            return dakiPack.getDakisInPack();
        }
        return new ArrayList<Daki>();
    }
    
    public int getDakiIndexInPack(Daki daki) {
        ArrayList<Daki> packList = getDakisInPack(daki.getPackDirectoryName());
        for (int i = 0; i < packList.size(); i++) {
            if (daki.equals(packList.get(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public IDakiPack getDakiPack(String packName) {
        return dakiPacksMap.get(packName);
    }
    
    public File getPackFolder() {
        return packFolder;
    }
    
    private void sendDakiListToClients() {
        PacketHandler.NETWORK_WRAPPER.sendToAll(new MessageServerSendDakiList());
    }
}
