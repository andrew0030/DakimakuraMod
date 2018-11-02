package moe.plushie.dakimakuramod.common.command;

import moe.plushie.dakimakuramod.common.network.PacketHandler;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerCommand;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerCommand.ServerCommandType;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandOpenPackFolder extends AbstractCommand {

    @Override
    public String getCommandName() {
        return "openPackFolder";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] currentCommand) {
        EntityPlayerMP player = getCommandSenderAsPlayer(commandSender);
        if (player == null) {
            return;
        }
        PacketHandler.NETWORK_WRAPPER.sendTo(new MessageServerCommand(ServerCommandType.OPEN_PACK_FOLDER), player);
    }
}
