package com.github.andrew0030.dakimakuramod.dakimakura.client;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.DakiImageData;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class DakiTextureManagerClient implements RemovalListener<Daki, DakiTexture>
{
    private final Cache<Daki, DakiTexture> textureCache;
    private final AtomicInteger textureRequests;
    private final CompletionService<DakiImageData> textureCompletion;
    private final ArrayList<DakiTexture> textureCleanup;

    public DakiTextureManagerClient()
    {
//        this.textureCache = CacheBuilder.newBuilder().removalListener(this).expireAfterAccess(ConfigHandler.cacheTimeClient, TimeUnit.MINUTES).build();
        // TODO add a config option for the length
        this.textureCache = CacheBuilder.newBuilder().removalListener(this).expireAfterAccess(20, TimeUnit.MINUTES).build();
        this.textureRequests = new AtomicInteger(0);
        this.textureCompletion = new ExecutorCompletionService<>(Executors.newFixedThreadPool(1));
        this.textureCleanup = new ArrayList<>();
    }

    /**
     * Gets the {@link DakiTexture} for a given {@link Daki} out of the {@link Cache}, or creates <br/>
     * a new {@link DakiTexture} for the given {@link Daki} and adds it to the {@link Cache}.
     * @param daki The {@link Daki} the {@link DakiTexture} is assigned to
     * @return The {@link DakiTexture} assigned to the given {@link Daki}
     */
    public DakiTexture getTextureForDaki(Daki daki)
    {
        DakiTexture dakiTexture  = null;
        dakiTexture = this.textureCache.getIfPresent(daki);
        if (dakiTexture == null)
        {
            dakiTexture = new DakiTexture(daki);
            this.textureCache.put(daki, dakiTexture);
        }
        return dakiTexture;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.side == LogicalSide.CLIENT && event.type == TickEvent.Type.CLIENT && event.phase == TickEvent.Phase.END)
        {
            this.textureCache.cleanUp();
            Future<DakiImageData> futureDakiImageData = this.textureCompletion.poll();
            if (futureDakiImageData != null)
            {
                try {
                    DakiImageData dakiImageData = futureDakiImageData.get();
                    if (dakiImageData != null)
                    {
                        DakiTexture dakiTexture = this.textureCache.getIfPresent(dakiImageData.getDaki());
                        if (dakiTexture != null)
                            dakiTexture.setBufferedImageFull(dakiImageData.getImageBuffer());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            this.deleteTextures();
        }
    }

    /**
     * Used to clear the {@link DakiTextureManagerClient} cache.
     */
    public void reloadTextures()
    {
        this.textureCache.asMap().clear();
    }

    private void deleteTextures()
    {
        synchronized(this.textureCleanup)
        {
            for (DakiTexture texture : this.textureCleanup)
                if (texture != null)
                    texture.releaseId();
//                    texture.deleteGlTexture();
            this.textureCleanup.clear();
        }
    }

    public AtomicInteger getTextureRequests()
    {
        return this.textureRequests;
    }

    public void serverSentTextures(DakiImageData imageData)
    {
        this.textureRequests.decrementAndGet();
        this.textureCompletion.submit(imageData);
    }

    @Override
    public void onRemoval(RemovalNotification<Daki, DakiTexture> notification)
    {
        //DakimakuraMod.getLogger().info("Removing texture for daki " + notification.getKey().toString());
        synchronized(textureCleanup)
        {
            this.textureCleanup.add(notification.getValue());
        }
    }
}