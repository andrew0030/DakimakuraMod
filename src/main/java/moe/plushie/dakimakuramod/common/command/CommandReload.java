package moe.plushie.dakimakuramod.common.command;

import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandReload extends AbstractCommand {

    @Override
    public String getName() {
        return "reload";
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        DakimakuraMod.getProxy().getDakimakuraManager().loadPacks(true);
        DakimakuraMod.getProxy().getTextureManagerCommon().clear();
    }
}
