package moe.plushie.dakimakuramod.common.command;

import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class AbstractCommand extends CommandBase {

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "commands.armourers." + getCommandName() + ".usage";
    }
    
    protected String[] getPlayers() {
        MinecraftServer server = DakimakuraMod.getProxy().getServer();
        return server.getAllUsernames();
    }
}
