package moe.plushie.dakimakuramod.common.command;

import moe.plushie.dakimakuramod.common.network.PacketHandler;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerCommand;
import moe.plushie.dakimakuramod.common.network.message.server.MessageServerCommand.ServerCommandType;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandOpenPackFolder extends AbstractCommand {

    @Override
    public String getCommandName() {
        return "openPackFolder";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (player == null) {
            return;
        }
        PacketHandler.NETWORK_WRAPPER.sendTo(new MessageServerCommand(ServerCommandType.OPEN_PACK_FOLDER), player);
    }
}
