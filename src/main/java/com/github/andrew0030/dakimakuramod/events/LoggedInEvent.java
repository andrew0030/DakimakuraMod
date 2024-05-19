package com.github.andrew0030.dakimakuramod.events;

import com.github.andrew0030.dakimakuramod.netwok.NetworkUtil;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

public class LoggedInEvent
{
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
        {
            LOGGER.info(String.format("Sending daki list to %s", serverPlayer.getName().getString()));
            NetworkUtil.sendDakiList(serverPlayer);
        }
    }
}