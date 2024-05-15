package com.github.andrew0030.dakimakuramod.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.netwok.NetworkUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.HashMap;

public class DakiSendHelper
{
    private static final HashMap<Pair<Daki, Integer>, byte[]> unfinishedSkins = new HashMap<>();
    private static final int MAX_PACKET_SIZE = Short.MAX_VALUE;

    /**
     * Called on <strong>Server Side</strong> to send {@link Daki} texture parts.
     * @param serverPlayer The player that will receive the data
     * @param daki The {@link Daki} the texture part belongs to
     * @param imageData The {@link DakiImageData} that will be used to load the texture data
     */
    public static void sendDakiTexturesToClient(ServerPlayer serverPlayer, Daki daki, DakiImageData imageData)
    {
        int sizeFront = imageData.getTextureFront() != null ? imageData.getTextureFront().length : 0;
        int sizeBack = imageData.getTextureBack() != null ? imageData.getTextureBack().length : 0;

        if (sizeFront == 0 && sizeBack == 0)
        {
            NetworkUtil.sendTextures(serverPlayer, daki, sizeFront, sizeBack, 1, 0, null);
            return;
        }

        byte[] totalBytes = new byte[sizeFront + sizeBack];
        if (sizeFront > 0)
            System.arraycopy(imageData.getTextureFront(), 0, totalBytes, 0, sizeFront);
        if (sizeBack > 0)
            System.arraycopy(imageData.getTextureBack(), 0, totalBytes, sizeFront, sizeBack);


        int packetsNeeded = (int) Math.ceil((double) totalBytes.length / MAX_PACKET_SIZE);
        int bytesSent = 0;

        for (int i = 0; i < packetsNeeded; i++)
        {
            boolean lastPacket = (i == packetsNeeded - 1);
            int packetSize = lastPacket ? (totalBytes.length - bytesSent) : MAX_PACKET_SIZE;
            byte[] messageData = new byte[packetSize];

            System.arraycopy(totalBytes, bytesSent, messageData, 0, packetSize);
            NetworkUtil.sendTextures(serverPlayer, daki, sizeFront, sizeBack, packetsNeeded, i, messageData);

            bytesSent += packetSize;
        }
    }

    /**
     * Called on <strong>Client Side</strong> to receive {@link Daki} texture parts.
     * @param daki The {@link Daki} the texture part belongs to
     * @param sizeFront The size of the Front Texture byte array
     * @param sizeBack The size of the Back texture byte array
     * @param packetsNeeded The total amount of packets required to create the {@link DakiImageData}
     * @param idx The index of the current packet, used to ensure data is processed in the correct order
     * @param data The received data in bytes
     */
    public static void gotDakiTexturePartFromServer(Daki daki, int sizeFront, int sizeBack, int packetsNeeded, int idx, byte[] data)
    {
        boolean lastPacket = packetsNeeded == 1 || packetsNeeded == DakiSendHelper.getPacketCountForDaki(daki) + 1;

        if (!lastPacket)
        {
            DakiSendHelper.unfinishedSkins.put(new Pair<>(daki, idx), data);
            return;
        }

        byte[] totalData = DakiSendHelper.assembleData(daki, sizeFront + sizeBack, data, packetsNeeded);
        byte[] dataFront = (sizeFront > 0) ? Arrays.copyOfRange(totalData, 0, sizeFront) : null;
        byte[] dataBack = (sizeBack > 0) ? Arrays.copyOfRange(totalData, sizeFront, sizeFront + sizeBack) : null;

        DakiImageData imageData = new DakiImageData(daki, dataFront, dataBack);
        DakimakuraModClient.getDakiTextureManager().serverSentTextures(imageData);
    }

    /** Converts the given data, and all the cached data (if present) into one array, while maintaining data order. */
    private static byte[] assembleData(Daki daki, int totalSize, byte[] lastPacketData, int packetsNeeded)
    {
        byte[] totalData = new byte[totalSize];
        int index = 0;
        for (int i = 0; i < packetsNeeded - 1; i++)
        {
            Pair<Daki, Integer> key = new Pair<>(daki, i);
            byte[] dataChunk = unfinishedSkins.remove(key);
            if (dataChunk == null)
                throw new IllegalStateException("Attempted to retrieve non existing texture data from cache for key: [" + daki.toString() + ", index=" + i + "]");
            System.arraycopy(dataChunk, 0, totalData, index, dataChunk.length);
            index += dataChunk.length;
        }
        System.arraycopy(lastPacketData, 0, totalData, index, lastPacketData.length);
        return totalData;
    }

    /** The amount of data packets that have already been cached for a given daki. */
    private static int getPacketCountForDaki(Daki daki)
    {
        int count = 0;
        for (Pair<Daki, Integer> key : unfinishedSkins.keySet())
            if (key.getFirst().equals(daki))
                count++;
        return count;
    }
}