package moe.plushie.dakimakuramod.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class AbstractCommand extends CommandBase {

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.armourers." + getName() + ".usage";
	}
    
    protected String[] getPlayers(MinecraftServer server) {
        return server.getOnlinePlayerNames();
    }
}
