package moe.plushie.dakimakuramod.common.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandDakimakura extends CommandBase {
    
    private final ArrayList<AbstractCommand> subCommands;

    public CommandDakimakura() {
        subCommands = new ArrayList<AbstractCommand>();
        subCommands.add(new CommandReload());
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandName() {
        return "dakimakura";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "commands.dakimakura.usage";
    }
    
    private String[] getSubCommandNames() {
        String[] subCommandNames = new String[subCommands.size()];
        for (int i = 0; i < subCommandNames.length; i++) {
            subCommandNames[i] = subCommands.get(i).getCommandName();
        }
        Arrays.sort(subCommandNames);
        return subCommandNames;
    }
    
    private AbstractCommand getSubCommand(String name) {
        for (int i = 0; i < subCommands.size(); i++) {
            if (subCommands.get(i).getCommandName().equals(name)) {
                return subCommands.get(i);
            }
        }
        return null;
    }
    
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, getSubCommandNames());
        }
        if (args.length > 1) {
            String commandName = args[0];
            AbstractCommand command = getSubCommand(commandName);
            if (command != null) {
                return command.getTabCompletionOptions(server, sender, args, pos);
            }
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args == null) {
            throw new WrongUsageException(getCommandUsage(sender), (Object)args);
        }
        if (args.length < 1) {
            throw new WrongUsageException(getCommandUsage(sender), (Object)args);
        }
        String commandName = args[0];
        AbstractCommand command = getSubCommand(commandName);
        if (command != null) {
            command.execute(server, sender, args);
            return;
        }
        throw new WrongUsageException(getCommandUsage(sender), (Object)args);
    }
}
