package moe.plushie.dakimakuramod.common.dakimakura;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class DakiTextureManagerCommon implements Runnable {
    
    private final Cache<Daki, DakiImageData> dakiImageCache;
    private final ArrayList<WaitingClient> waitingClients;
    private final ArrayList<Daki> dakiLoadQueue;
    private volatile Thread threadTextureManager = null;
    
    public DakiTextureManagerCommon() {
        dakiImageCache = CacheBuilder.newBuilder().expireAfterAccess(ConfigHandler.cacheTimeServer, TimeUnit.MINUTES).build();
        waitingClients = new ArrayList<WaitingClient>();
        dakiLoadQueue = new ArrayList<Daki>();
    }
    
    public void serverStarted() {
        threadTextureManager = new Thread(this, "Dakimakura Mod texture manager thread");
        threadTextureManager.start();
    }
    
    public void serverStopped() {
        threadTextureManager = null;
    }
    
    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        DakimakuraMod.getLogger().info("Starting texture manager thread.");
        while (threadTextureManager == thisThread) {
            try {
                thisThread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            processDakiQueue();
        }
        DakimakuraMod.getLogger().info("Stopped texture manager thread.");
    }
    
    private void processDakiQueue() {
        Daki loadDaki = null;
        synchronized (dakiLoadQueue) {
            if (dakiLoadQueue.size() > 0) {
                loadDaki = dakiLoadQueue.get(0);
            }
        }
        if (loadDaki != null) {
            loadDakiTextures(loadDaki);
        }
    }
    
    private void loadDakiTextures(Daki daki) {
        DakiImageData imageData = new DakiImageData(daki);
        
        synchronized (dakiImageCache) {
            dakiImageCache.put(daki, imageData);
        }
        synchronized (dakiLoadQueue) {
            dakiLoadQueue.remove(daki);
        }
        checkAndSendToClients();
    }
    
    public int size() {
        synchronized (dakiImageCache) {
            return dakiImageCache.asMap().size();
        }
    }
    
    public void clear() {
        synchronized (dakiImageCache) {
            dakiImageCache.asMap().clear();
        }
        synchronized (dakiLoadQueue) {
            dakiLoadQueue.clear();
        }
        synchronized (waitingClients) {
            waitingClients.clear();
        }
    }
    
    public void onClientRequestTexture(EntityPlayerMP playerEntity, Daki daki) {
        synchronized (dakiImageCache) {
            if (dakiImageCache.asMap().containsKey(daki)) {
                sendTextureToClient(playerEntity, daki, dakiImageCache.getIfPresent(daki));
            } else {
                synchronized (waitingClients) {
                    waitingClients.add(new WaitingClient(playerEntity, daki));
                }
                synchronized (dakiLoadQueue) {
                    if (!dakiLoadQueue.contains(daki)) {
                        dakiLoadQueue.add(daki);
                    }
                }
            }
        }
    }
    
    private void checkAndSendToClients() {
        synchronized (dakiImageCache) {
            synchronized (waitingClients) {
                for (int i = 0; i < waitingClients.size(); i++) {
                    WaitingClient waitingClient = waitingClients.get(i);
                    DakiImageData imageData = dakiImageCache.getIfPresent(waitingClient.getDaki());
                    if (imageData != null) {
                        sendTextureToClient(waitingClient.getPlayerEntity(), waitingClient.getDaki(), imageData);
                        waitingClients.remove(i);
                    }
                }
            }
        }
    }
    
    private void sendTextureToClient(EntityPlayerMP playerEntity, Daki daki, DakiImageData imageData) {
        //DakimakuraMod.getLogger().info("Sending daki to client " + playerEntity.getDisplayName() + " " + daki);
        DakiSendHelper.sendDakiTexturesToClient(playerEntity, daki, imageData);
    }
    
    public static class WaitingClient {
        
        private EntityPlayerMP playerEntity;
        private Daki daki;
        
        public WaitingClient(EntityPlayerMP playerEntity, Daki daki) {
            this.playerEntity = playerEntity;
            this.daki = daki;
        }
        
        public EntityPlayerMP getPlayerEntity() {
            return playerEntity;
        }
        
        public Daki getDaki() {
            return daki;
        }
    }
}
