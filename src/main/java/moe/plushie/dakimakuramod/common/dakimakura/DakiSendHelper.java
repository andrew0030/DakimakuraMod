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
        
        int sizeFront = 0;
        int sizeBack = 0;
        if (imageData.getTextureFront() != null) {
            sizeFront = imageData.getTextureFront().length;
        }
        if (imageData.getTextureBack() != null) {
            sizeBack = imageData.getTextureBack().length;
        }
        
        if (sizeFront == 0 & sizeBack == 0) {
            MessageServerSendTextures message = new MessageServerSendTextures(daki, sizeFront, sizeBack, null);
            PacketHandler.NETWORK_WRAPPER.sendTo(message, player);
            return;
        }
        
        // TODO check if both images are 0
        byte[] totalBytes = new byte[sizeFront + sizeBack];
        if (sizeFront > 0) {
            System.arraycopy(imageData.getTextureFront(), 0, totalBytes, 0, sizeFront);
        }
        if (sizeBack > 0) {
            System.arraycopy(imageData.getTextureBack(), 0, totalBytes, sizeFront, sizeBack);
        }
        
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
            MessageServerSendTextures message = new MessageServerSendTextures(daki, sizeFront, sizeBack, messageData);
            packetQueue.add(message);
            bytesLeftToSend -= messageData.length;
            bytesSent += messageData.length;
        }
        
        for (int i = 0; i < packetQueue.size(); i++) {
            PacketHandler.NETWORK_WRAPPER.sendTo(packetQueue.get(i), player);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static void gotDakiTexturePartFromServer(Daki daki, int sizeFront, int sizeBack, byte[] data) {
        boolean lastPacket = true;
        if (data != null) {
            lastPacket = data.length < MAX_PACKET_SIZE;
        }
        
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
            
            byte[] data1 = null;
            byte[] data2 = null;
            
            try {
                if (sizeFront > 0) {
                    data1 = new byte[sizeFront];
                    System.arraycopy(newSkinData, 0, data1, 0, sizeFront);
                }
                if (sizeBack > 0) {
                    data2 = new byte[sizeBack];
                    System.arraycopy(newSkinData, sizeFront, data2, 0, sizeBack);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            DakiImageData imageData = new DakiImageData(daki, data1, data2);

           ((ClientProxy)DakimakuraMod.getProxy()).getDakiTextureManager().serverSentTextures(imageData);
        }
    }
}
