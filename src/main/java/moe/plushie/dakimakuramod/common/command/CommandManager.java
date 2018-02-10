package moe.plushie.dakimakuramod.common.command;

import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandManager extends AbstractCommand {

    @Override
    public String getCommandName() {
        return "manager";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] currentCommand) {
        if (commandSender instanceof EntityPlayer) {
            ((EntityPlayer)commandSender).openGui(DakimakuraMod.getInstance(), 0, commandSender.getEntityWorld(), 0, 0, 0);
        }
    }
}
