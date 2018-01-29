package moe.plushie.dakimakuramod.common.command;

import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.command.ICommandSender;

public class CommandReload extends AbstractCommand {

    @Override
    public String getCommandName() {
        return "reload";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] currentCommand) {
        DakimakuraMod.dakimakuraManager.loadPacks();
    }
}
