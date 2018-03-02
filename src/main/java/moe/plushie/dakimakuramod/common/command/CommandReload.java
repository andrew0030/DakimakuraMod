package moe.plushie.dakimakuramod.common.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandReload extends AbstractCommand {

    @Override
    public String getCommandName() {
        return "reload";
    }
/*
    @Override
    public void processCommand(ICommandSender commandSender, String[] currentCommand) {
        DakimakuraMod.getProxy().getDakimakuraManager().loadPacks(true);
        DakimakuraMod.getProxy().getTextureManagerCommon().clear();
    }
*/
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // TODO Auto-generated method stub
        
    }
}
