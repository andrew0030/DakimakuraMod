package moe.plushie.dakimakuramod.common.dakimakura;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.DakiSendHelper;
import net.minecraft.entity.player.EntityPlayerMP;


public class DakiTextureManagerCommon implements Runnable {
    
    private final Cache<Daki, DakiBufferedImages> dakiImageCache;
    private final ArrayList<WaitingClient> waitingClients;
    private final ArrayList<Daki> dakiLoadQueue;
    private volatile Thread threadTextureManager = null;
    
    public DakiTextureManagerCommon() {
        dakiImageCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();
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
        DakimakuraMod.getLogger().info("aad.");
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
        DakiBufferedImages bufferedImages = new DakiBufferedImages(daki);
        
        synchronized (dakiImageCache) {
            dakiImageCache.put(daki, bufferedImages);
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
                    DakiBufferedImages bufferedImages = dakiImageCache.getIfPresent(waitingClient.getDaki());
                    if (bufferedImages != null) {
                        sendTextureToClient(waitingClient.getPlayerEntity(), waitingClient.getDaki(), bufferedImages);
                        waitingClients.remove(i);
                    }
                }
            }
        }
    }
    
    private void sendTextureToClient(EntityPlayerMP playerEntity, Daki daki, DakiBufferedImages dakiBufferedImages) {
        DakimakuraMod.getLogger().info("Sending daki to client " + playerEntity.getDisplayName() + " " + daki);
        DakiSendHelper.sendDakiTexturesToClient(playerEntity, daki, dakiBufferedImages);
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
    
    public static class DakiBufferedImages {
        
        private Daki daki;
        private byte[] textureFront;
        private byte[] textureBack;
        
        public DakiBufferedImages(Daki daki) {
            this.daki = daki;
            load();
        }
        
        public DakiBufferedImages(Daki daki, byte[] textureFront, byte[] textureBack) {
            this.daki = daki;
            this.textureFront = textureFront;
            this.textureBack = textureBack;
        }
        
        public boolean load() {
            DakimakuraMod.getLogger().info("Loading daki from disk: " + daki);
            boolean valid = true;
            File dir = DakimakuraMod.getProxy().getDakimakuraManager().getPackFolder();
            
            dir = new File(dir, daki.getPackDirectoryName());
            dir = new File(dir, daki.getDakiDirectoryName());
            
            File fileFront = new File(dir, daki.getImageFront());
            File fileBack = new File(dir, daki.getImageBack());
            
            InputStream inputstream = null;
            try {
                inputstream = new FileInputStream(fileFront);
                textureFront = IOUtils.toByteArray(inputstream);
                inputstream.close();
                
                inputstream = new FileInputStream(fileBack);
                textureBack = IOUtils.toByteArray(inputstream);
                inputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
                valid = false;
            } finally {
                IOUtils.closeQuietly(inputstream);
            }
            return valid;
        }
        
        public byte[] getTextureFront() {
            return textureFront;
        }
        
        public byte[] getTextureBack() {
            return textureBack;
        }
    }
}
