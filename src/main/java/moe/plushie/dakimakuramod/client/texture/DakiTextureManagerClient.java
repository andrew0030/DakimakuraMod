package moe.plushie.dakimakuramod.client.texture;

import java.util.ArrayList;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiImageData;

@SideOnly(Side.CLIENT)
public class DakiTextureManagerClient implements RemovalListener<Daki, DakiTexture> {
    
    private final Cache<Daki, DakiTexture> textureCache;
    private final AtomicInteger textureRequests;
    private final CompletionService<DakiImageData> textureCompletion;
    private final ArrayList<DakiTexture> textureCleanup;
    
    public DakiTextureManagerClient() {
        textureCache = CacheBuilder.newBuilder().removalListener(this).expireAfterAccess(ConfigHandler.cacheTimeClient, TimeUnit.MINUTES).build();
        textureRequests = new AtomicInteger(0);
        textureCompletion = new ExecutorCompletionService<DakiImageData>(Executors.newFixedThreadPool(1));
        textureCleanup = new ArrayList<DakiTexture>();
        FMLCommonHandler.instance().bus().register(this);
    }
    
    public DakiTexture getTextureForDaki(Daki daki) {
        DakiTexture dakiTexture  = null;
        dakiTexture = textureCache.getIfPresent(daki);
        if (dakiTexture == null) {
            dakiTexture = new DakiTexture(daki);
            textureCache.put(daki, dakiTexture);
        }
        return dakiTexture;
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.side == Side.CLIENT & event.type == Type.CLIENT & event.phase == Phase.END) {
            textureCache.cleanUp();
            Future<DakiImageData> futureDakiImageData = textureCompletion.poll();
            if (futureDakiImageData != null) {
                try {
                    DakiImageData dakiImageData = futureDakiImageData.get();
                    if (dakiImageData != null) {
                        DakiTexture dakiTexture = textureCache.getIfPresent(dakiImageData.getDaki());
                        if (dakiTexture != null) {
                            dakiTexture.setBufferedImageFull(dakiImageData.getBufferedImageFull());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            deleteTextures();
        }
    }
    
    public void reloadTextures() {
        textureCache.asMap().clear();
    }
    
    private void deleteTextures() {
        synchronized(textureCleanup) {
            for (int i = 0; i < textureCleanup.size(); i++) {
                DakiTexture texture = textureCleanup.get(i);
                if (texture != null) {
                    texture.deleteGlTexture();
                }
            }
            textureCleanup.clear();
        }
    }
    
    public AtomicInteger getTextureRequests() {
        return textureRequests;
    }
    
    public void serverSentTextures(DakiImageData imageData) {
        textureRequests.decrementAndGet();
        textureCompletion.submit(imageData);
    }

    @Override
    public void onRemoval(RemovalNotification<Daki, DakiTexture> notification) {
        //DakimakuraMod.getLogger().info("Removing texture for daki " + notification.getKey().toString());
        synchronized(textureCleanup) {
            textureCleanup.add(notification.getValue());
        }
    }
}
