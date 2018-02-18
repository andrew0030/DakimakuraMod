package moe.plushie.dakimakuramod.client.texture;

import java.util.HashMap;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiImageData;

@SideOnly(Side.CLIENT)
public class DakiTextureManagerClient {
    
    private final HashMap<Daki, DakiTexture> textureMap;
    private final AtomicInteger textureRequests;
    private final CompletionService<DakiImageData> textureCompletion;
    
    public DakiTextureManagerClient() {
        textureMap = new HashMap<Daki, DakiTexture>();
        textureRequests = new AtomicInteger(0);
        textureCompletion = new ExecutorCompletionService<DakiImageData>(Executors.newFixedThreadPool(1));
        FMLCommonHandler.instance().bus().register(this);
    }
    
    public DakiTexture getTextureForDaki(Daki daki) {
        DakiTexture dakiTexture  = null;
        synchronized (textureMap) {
            dakiTexture = textureMap.get(daki);
            if (dakiTexture == null) {
                dakiTexture = new DakiTexture(daki);
                textureMap.put(daki, dakiTexture);
            }
        }
        return dakiTexture;
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.side == Side.CLIENT & event.type == Type.CLIENT & event.phase == Phase.END) {
            Future<DakiImageData> futureDakiImageData = textureCompletion.poll();
            if (futureDakiImageData != null) {
                try {
                    DakiImageData dakiImageData = futureDakiImageData.get();
                    if (dakiImageData != null) {
                        synchronized (textureMap) {
                            DakiTexture dakiTexture = textureMap.get(dakiImageData.getDaki());
                            if (dakiTexture != null) {
                                dakiTexture.setBufferedImageFull(dakiImageData.getBufferedImageFull());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void reloadTextures() {
        synchronized (textureMap) {
            deleteTextures();
        }
    }
    
    private void deleteTextures() {
        Daki[] keys = (Daki[]) textureMap.keySet().toArray(new Daki[textureMap.size()]);
        for (int i = 0; i < keys.length; i++) {
            DakiTexture dakiTexture = textureMap.remove(keys[i]);
            dakiTexture.deleteGlTexture();
        }
    }
    
    public AtomicInteger getTextureRequests() {
        return textureRequests;
    }
    
    public void serverSentTextures(DakiImageData imageData) {
        textureRequests.decrementAndGet();
        textureCompletion.submit(imageData);
    }
}
