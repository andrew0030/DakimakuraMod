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
        int meta = 0;
        DakimakuraMod.logger.info("1 - meta: " + meta + " bytes: " + Integer.toBinaryString(meta));
        
        meta = meta | (1 << 0);
        meta = meta | (1 << 0);
        DakimakuraMod.logger.info("2 - meta: " + meta + " bytes: " + Integer.toBinaryString(meta));
        
        for (int i = 0; i < 4; i++) {
            meta = meta | (1 << i);
        }
        DakimakuraMod.logger.info("3 - meta: " + meta + " bytes: " + Integer.toBinaryString(meta));
        
        if (commandSender instanceof EntityPlayer) {
            ((EntityPlayer)commandSender).openGui(DakimakuraMod.instance, 0, commandSender.getEntityWorld(), 0, 0, 0);
        }
    }
}
