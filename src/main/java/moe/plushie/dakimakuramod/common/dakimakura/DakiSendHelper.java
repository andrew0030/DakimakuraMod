package moe.plushie.dakimakuramod.common.dakimakura;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerSendTextures;
import moe.plushie.dakimakuramod.proxies.ClientProxy;
import net.minecraft.entity.player.EntityPlayerMP;

public class DakiSendHelper {
    
    private static final HashMap<Daki, byte[]> unfinishedSkins = new HashMap<Daki, byte[]>();
    
    //private static final int MAX_PACKET_SIZE = 2097050 - (97050 * 3);
    private static final int MAX_PACKET_SIZE = 30000;
    
    public static void sendDakiTexturesToClient(EntityPlayerMP player, Daki daki, DakiImageData imageData) {
        
        byte[] totalBytes = new byte[imageData.getTextureFront().length + imageData.getTextureBack().length];
        System.arraycopy(imageData.getTextureFront(), 0, totalBytes, 0, imageData.getTextureFront().length);
        System.arraycopy(imageData.getTextureBack(), 0, totalBytes, imageData.getTextureFront().length, imageData.getTextureBack().length);
        
        ArrayList<MessageServerSendTextures> packetQueue = new ArrayList<MessageServerSendTextures>();
        
        int packetsNeeded = (int) Math.ceil((double)totalBytes.length / (double)MAX_PACKET_SIZE);
        int bytesLeftToSend = totalBytes.length;
        int bytesSent = 0;
        
        for (int i = 0; i < packetsNeeded; i++) {
            boolean lastPacket = i == packetsNeeded - 1;
            byte[] messageData;
            if (lastPacket) {
                messageData = new byte[bytesLeftToSend];
            } else {
                messageData = new byte[MAX_PACKET_SIZE];
            }
            System.arraycopy(totalBytes, bytesSent, messageData, 0, messageData.length);
            MessageServerSendTextures message = new MessageServerSendTextures(daki, imageData.getTextureFront().length, messageData);
            packetQueue.add(message);
            bytesLeftToSend -= messageData.length;
            bytesSent += messageData.length;
        }
        
        //DakimakuraMod.getLogger().info("Sending packets " + packetQueue.size() + " total size " + totalBytes.length);
        //DakimakuraMod.getLogger().info("Front size " +  dakiBufferedImages.getTextureFront().length);
        //DakimakuraMod.getLogger().info("Back size " +  dakiBufferedImages.getTextureBack().length);
        
        for (int i = 0; i < packetQueue.size(); i++) {
            PacketHandler.NETWORK_WRAPPER.sendTo(packetQueue.get(i), player);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void gotDakiTexturePartFromServer(Daki daki, int firstSize, byte[] data) {
        boolean lastPacket = data.length < MAX_PACKET_SIZE;
        byte[] oldSkinData = unfinishedSkins.get(daki);
        
        byte[] newSkinData = null;
        if (oldSkinData != null) {
            newSkinData = ArrayUtils.addAll(oldSkinData, data);
            unfinishedSkins.remove(daki);
        } else {
            newSkinData = data;
        }
        
        if (!lastPacket) {
            unfinishedSkins.put(daki, newSkinData);
        } else {
            byte[] data1 = new byte[firstSize];
            byte[] data2 = new byte[newSkinData.length - firstSize];
            
            //DakimakuraMod.getLogger().info("Got packets total size " + newSkinData.length);
            //DakimakuraMod.getLogger().info("Front size " + data1.length);
            //DakimakuraMod.getLogger().info("Back size " + data2.length);
            
            System.arraycopy(newSkinData, 0, data1, 0, data1.length);
            System.arraycopy(newSkinData, data1.length, data2, 0, data2.length);
            
            DakiImageData imageData = new DakiImageData(data1, data2);

           ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().serverSentTextures(daki, imageData);
        }
    }
}
