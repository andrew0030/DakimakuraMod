package com.github.andrew0030.dakimakuramod.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DakiSendHelper
{
    private static final HashMap<Daki, byte[]> unfinishedSkins = new HashMap<>();
    //private static final int MAX_PACKET_SIZE = 2097050 - (97050 * 3);
    private static final int MAX_PACKET_SIZE = Short.MAX_VALUE;

    /**
     * Called on <strong>Server Side</strong> to send {@link Daki} texture parts.
     * @param daki The {@link Daki} the texture part belongs to
     * @param imageData The {@link DakiImageData} that will be used to load the texture data
     */
    public static void sendDakiTexturesToClient(ServerPlayer serverPlayer, Daki daki, DakiImageData imageData)
    {
        byte[] textureFront = imageData.getTextureFront();
        byte[] textureBack = imageData.getTextureBack();
        // Determines the sizes of front and back textures
        int sizeFront = textureFront != null ? textureFront.length : 0;
        int sizeBack = textureBack != null ? textureBack.length : 0;
        // If there is no texture data to send, we return
        if (sizeFront == 0 && sizeBack == 0) {
            MessageServerSendTextures message = new MessageServerSendTextures(daki, sizeFront, sizeBack, null);
            PacketHandler.NETWORK_WRAPPER.sendTo(message, serverPlayer);
            return;
        }
        // Combines front and back textures into a single byte array
        byte[] totalBytes = new byte[sizeFront + sizeBack];
        if (sizeFront > 0)
            System.arraycopy(textureFront, 0, totalBytes, 0, sizeFront);
        if (sizeBack > 0)
            System.arraycopy(textureBack, 0, totalBytes, sizeFront, sizeBack);
        // Determines the number of packets needed
        int packetsNeeded = (int) Math.ceil((double) totalBytes.length / MAX_PACKET_SIZE);
        // Sends packets
        int offset = 0;
        for (int i = 0; i < packetsNeeded; i++) {
            int length = Math.min(MAX_PACKET_SIZE, totalBytes.length - offset);
            byte[] messageData = new byte[length];
            System.arraycopy(totalBytes, offset, messageData, 0, length);
            MessageServerSendTextures message = new MessageServerSendTextures(daki, sizeFront, sizeBack, messageData);
            PacketHandler.NETWORK_WRAPPER.sendTo(message, serverPlayer);
            offset += length;
        }
    }

    /**
     * Called on <strong>Client Side</strong> to receive {@link Daki} texture parts.
     * @param daki The {@link Daki} the texture part belongs to
     * @param sizeFront The size of the Front Texture byte array
     * @param sizeBack The size of the Back texture byte array
     * @param data The received data in bytes
     */
    public static void gotDakiTexturePartFromServer(Daki daki, int sizeFront, int sizeBack, byte[] data)
    {
        // Whether this texture part is the last. This is also true, if there aren't more than one.
        boolean isLast = data == null || data.length < MAX_PACKET_SIZE;
        // We get the previously received texture data, and combine it with the current texture part.
        byte[] oldSkinData = unfinishedSkins.get(daki);
        byte[] newSkinData = oldSkinData != null ? Arrays.copyOf(oldSkinData, oldSkinData.length + data.length) : data;

        try {
            byte[] data1 = sizeFront > 0 ? Arrays.copyOfRange(newSkinData, 0, sizeFront) : null;
            byte[] data2 = sizeBack > 0 ? Arrays.copyOfRange(newSkinData, sizeFront, sizeFront + sizeBack) : null;

            DakiImageData imageData = new DakiImageData(daki, data1, data2);
            DakimakuraModClient.getDakiTextureManager().serverSentTextures(imageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If the daki texture part we received wasn't the last, we add its data to our unfinishedSkins map,
        // otherwise we remove it from the unfinishedSkins map, if it was present.
        if (!isLast) {
            unfinishedSkins.put(daki, newSkinData);
        } else {
            unfinishedSkins.remove(daki);
        }
    }
}