package net.minecraft.command.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class CommandScoreboard extends CommandBase
{
    private static final String __OBFID = "CL_00000896";

    public String getCommandName()
    {
        return "scoreboard";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.scoreboard.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("objectives"))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                }

                if (args[1].equalsIgnoreCase("list"))
                {
                    this.listObjectives(sender);
                }
                else if (args[1].equalsIgnoreCase("add"))
                {
                    if (args.length < 4)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                    }

                    this.addObjective(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("remove"))
                {
                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                    }

                    this.removeObjective(sender, args[2]);
                }
                else
                {
                    if (!args[1].equalsIgnoreCase("setdisplay"))
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }

                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                    }

                    this.setObjectiveDisplay(sender, args, 2);
                }

                return;
            }

            if (args[0].equalsIgnoreCase("players"))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                }

                if (args[1].equalsIgnoreCase("list"))
                {
                    if (args.length > 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                    }

                    this.listPlayers(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("add"))
                {
                    if (args.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                    }

                    this.setPlayer(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("remove"))
                {
                    if (args.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                    }

                    this.setPlayer(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("set"))
                {
                    if (args.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                    }

                    this.setPlayer(sender, args, 2);
                }
                else
                {
                    if (!args[1].equalsIgnoreCase("reset"))
                    {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }

                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                    }

                    this.resetPlayers(sender, args, 2);
                }

                return;
            }

            if (args[0].equalsIgnoreCase("teams"))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                }

                if (args[1].equalsIgnoreCase("list"))
                {
                    if (args.length > 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                    }

                    this.listTeams(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("add"))
                {
                    if (args.length < 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                    }

                    this.addTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("remove"))
                {
                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                    }

                    this.removeTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("empty"))
                {
                    if (args.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                    }

                    this.emptyTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("join"))
                {
                    if (args.length < 4 && (args.length != 3 || !(sender instanceof EntityPlayer)))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                    }

                    this.joinTeam(sender, args, 2);
                }
                else if (args[1].equalsIgnoreCase("leave"))
                {
                    if (args.length < 3 && !(sender instanceof EntityPlayer))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                    }

                    this.leaveTeam(sender, args, 2);
                }
                else
                {
                    if (!args[1].equalsIgnoreCase("option"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }

                    if (args.length != 4 && args.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }

                    this.setTeamOption(sender, args, 2);
                }

                return;
            }
        }

        throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
    }

    protected Scoreboard getScoreboard()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }

    protected ScoreObjective func_147189_a(String name, boolean edit)
    {
        Scoreboard var3 = this.getScoreboard();
        ScoreObjective var4 = var3.getObjective(name);

        if (var4 == null)
        {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] {name});
        }
        else if (edit && var4.getCriteria().isReadOnly())
        {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] {name});
        }
        else
        {
            return var4;
        }
    }

    protected ScorePlayerTeam func_147183_a(String name)
    {
        Scoreboard var2 = this.getScoreboard();
        ScorePlayerTeam var3 = var2.getTeam(name);

        if (var3 == null)
        {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] {name});
        }
        else
        {
            return var3;
        }
    }

    protected void addObjective(ICommandSender sender, String[] args, int index)
    {
        String var4 = args[index++];
        String var5 = args[index++];
        Scoreboard var6 = this.getScoreboard();
        IScoreObjectiveCriteria var7 = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(var5);

        if (var7 == null)
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] {var5});
        }
        else if (var6.getObjective(var4) != null)
        {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] {var4});
        }
        else if (var4.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] {var4, Integer.valueOf(16)});
        }
        else if (var4.length() == 0)
        {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
        else
        {
            if (args.length > index)
            {
                String var8 = getChatComponentFromNthArg(sender, args, index).getUnformattedText();

                if (var8.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] {var8, Integer.valueOf(32)});
                }

                if (var8.length() > 0)
                {
                    var6.addScoreObjective(var4, var7).setDisplayName(var8);
                }
                else
                {
                    var6.addScoreObjective(var4, var7);
                }
            }
            else
            {
                var6.addScoreObjective(var4, var7);
            }

            notifyOperators(sender, this, "commands.scoreboard.objectives.add.success", new Object[] {var4});
        }
    }

    protected void addTeam(ICommandSender p_147185_1_, String[] p_147185_2_, int p_147185_3_)
    {
        String var4 = p_147185_2_[p_147185_3_++];
        Scoreboard var5 = this.getScoreboard();

        if (var5.getTeam(var4) != null)
        {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] {var4});
        }
        else if (var4.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] {var4, Integer.valueOf(16)});
        }
        else if (var4.length() == 0)
        {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        }
        else
        {
            if (p_147185_2_.length > p_147185_3_)
            {
                String var6 = getChatComponentFromNthArg(p_147185_1_, p_147185_2_, p_147185_3_).getUnformattedText();

                if (var6.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] {var6, Integer.valueOf(32)});
                }

                if (var6.length() > 0)
                {
                    var5.createTeam(var4).setTeamName(var6);
                }
                else
                {
                    var5.createTeam(var4);
                }
            }
            else
            {
                var5.createTeam(var4);
            }

            notifyOperators(p_147185_1_, this, "commands.scoreboard.teams.add.success", new Object[] {var4});
        }
    }

    protected void setTeamOption(ICommandSender p_147200_1_, String[] p_147200_2_, int p_147200_3_)
    {
        ScorePlayerTeam var4 = this.func_147183_a(p_147200_2_[p_147200_3_++]);

        if (var4 != null)
        {
            String var5 = p_147200_2_[p_147200_3_++].toLowerCase();

            if (!var5.equalsIgnoreCase("color") && !var5.equalsIgnoreCase("friendlyfire") && !var5.equalsIgnoreCase("seeFriendlyInvisibles"))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            else if (p_147200_2_.length == 4)
            {
                if (var5.equalsIgnoreCase("color"))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false))});
                }
                else if (!var5.equalsIgnoreCase("friendlyfire") && !var5.equalsIgnoreCase("seeFriendlyInvisibles"))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                }
                else
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(Arrays.asList(new String[]{"true", "false"}))});
                }
            }
            else
            {
                String var6 = p_147200_2_[p_147200_3_++];

                if (var5.equalsIgnoreCase("color"))
                {
                    EnumChatFormatting var7 = EnumChatFormatting.getValueByName(var6);

                    if (var7 == null || var7.isFancyStyling())
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false))});
                    }

                    var4.setNamePrefix(var7.toString());
                    var4.setNameSuffix(EnumChatFormatting.RESET.toString());
                }
                else if (var5.equalsIgnoreCase("friendlyfire"))
                {
                    if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(Arrays.asList(new String[]{"true", "false"}))});
                    }

                    var4.setAllowFriendlyFire(var6.equalsIgnoreCase("true"));
                }
                else if (var5.equalsIgnoreCase("seeFriendlyInvisibles"))
                {
                    if (!var6.equalsIgnoreCase("true") && !var6.equalsIgnoreCase("false"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {var5, joinNiceStringFromCollection(Arrays.asList(new String[]{"true", "false"}))});
                    }

                    var4.setSeeFriendlyInvisiblesEnabled(var6.equalsIgnoreCase("true"));
                }

                notifyOperators(p_147200_1_, this, "commands.scoreboard.teams.option.success", new Object[] {var5, var4.getRegisteredName(), var6});
            }
        }
    }

    protected void removeTeam(ICommandSender p_147194_1_, String[] p_147194_2_, int p_147194_3_)
    {
        Scoreboard var4 = this.getScoreboard();
        ScorePlayerTeam var5 = this.func_147183_a(p_147194_2_[p_147194_3_++]);

        if (var5 != null)
        {
            var4.removeTeam(var5);
            notifyOperators(p_147194_1_, this, "commands.scoreboard.teams.remove.success", new Object[] {var5.getRegisteredName()});
        }
    }

    protected void listTeams(ICommandSender p_147186_1_, String[] p_147186_2_, int p_147186_3_)
    {
        Scoreboard var4 = this.getScoreboard();

        if (p_147186_2_.length > p_147186_3_)
        {
            ScorePlayerTeam var5 = this.func_147183_a(p_147186_2_[p_147186_3_++]);

            if (var5 == null)
            {
                return;
            }

            Collection var6 = var5.getMembershipCollection();

            if (var6.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] {var5.getRegisteredName()});
            }

            ChatComponentTranslation var7 = new ChatComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] {Integer.valueOf(var6.size()), var5.getRegisteredName()});
            var7.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147186_1_.addChatMessage(var7);
            p_147186_1_.addChatMessage(new ChatComponentText(joinNiceString(var6.toArray())));
        }
        else
        {
            Collection var9 = var4.getTeams();

            if (var9.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }

            ChatComponentTranslation var10 = new ChatComponentTranslation("commands.scoreboard.teams.list.count", new Object[] {Integer.valueOf(var9.size())});
            var10.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147186_1_.addChatMessage(var10);
            Iterator var11 = var9.iterator();

            while (var11.hasNext())
            {
                ScorePlayerTeam var8 = (ScorePlayerTeam)var11.next();
                p_147186_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] {var8.getRegisteredName(), var8.func_96669_c(), Integer.valueOf(var8.getMembershipCollection().size())}));
            }
        }
    }

    protected void joinTeam(ICommandSender p_147190_1_, String[] p_147190_2_, int p_147190_3_)
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = p_147190_2_[p_147190_3_++];
        HashSet var6 = new HashSet();
        HashSet var7 = new HashSet();
        String var8;

        if (p_147190_1_ instanceof EntityPlayer && p_147190_3_ == p_147190_2_.length)
        {
            var8 = getCommandSenderAsPlayer(p_147190_1_).getCommandSenderName();

            if (var4.func_151392_a(var8, var5))
            {
                var6.add(var8);
            }
            else
            {
                var7.add(var8);
            }
        }
        else
        {
            while (p_147190_3_ < p_147190_2_.length)
            {
                var8 = getPlayerName(p_147190_1_, p_147190_2_[p_147190_3_++]);

                if (var4.func_151392_a(var8, var5))
                {
                    var6.add(var8);
                }
                else
                {
                    var7.add(var8);
                }
            }
        }

        if (!var6.isEmpty())
        {
            notifyOperators(p_147190_1_, this, "commands.scoreboard.teams.join.success", new Object[] {Integer.valueOf(var6.size()), var5, joinNiceString(var6.toArray(new String[0]))});
        }

        if (!var7.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] {Integer.valueOf(var7.size()), var5, joinNiceString(var7.toArray(new String[0]))});
        }
    }

    protected void leaveTeam(ICommandSender p_147199_1_, String[] p_147199_2_, int p_147199_3_)
    {
        Scoreboard var4 = this.getScoreboard();
        HashSet var5 = new HashSet();
        HashSet var6 = new HashSet();
        String var7;

        if (p_147199_1_ instanceof EntityPlayer && p_147199_3_ == p_147199_2_.length)
        {
            var7 = getCommandSenderAsPlayer(p_147199_1_).getCommandSenderName();

            if (var4.removePlayerFromTeams(var7))
            {
                var5.add(var7);
            }
            else
            {
                var6.add(var7);
            }
        }
        else
        {
            while (p_147199_3_ < p_147199_2_.length)
            {
                var7 = getPlayerName(p_147199_1_, p_147199_2_[p_147199_3_++]);

                if (var4.removePlayerFromTeams(var7))
                {
                    var5.add(var7);
                }
                else
                {
                    var6.add(var7);
                }
            }
        }

        if (!var5.isEmpty())
        {
            notifyOperators(p_147199_1_, this, "commands.scoreboard.teams.leave.success", new Object[] {Integer.valueOf(var5.size()), joinNiceString(var5.toArray(new String[0]))});
        }

        if (!var6.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] {Integer.valueOf(var6.size()), joinNiceString(var6.toArray(new String[0]))});
        }
    }

    protected void emptyTeam(ICommandSender p_147188_1_, String[] p_147188_2_, int p_147188_3_)
    {
        Scoreboard var4 = this.getScoreboard();
        ScorePlayerTeam var5 = this.func_147183_a(p_147188_2_[p_147188_3_++]);

        if (var5 != null)
        {
            ArrayList var6 = new ArrayList(var5.getMembershipCollection());

            if (var6.isEmpty())
            {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] {var5.getRegisteredName()});
            }
            else
            {
                Iterator var7 = var6.iterator();

                while (var7.hasNext())
                {
                    String var8 = (String)var7.next();
                    var4.removePlayerFromTeam(var8, var5);
                }

                notifyOperators(p_147188_1_, this, "commands.scoreboard.teams.empty.success", new Object[] {Integer.valueOf(var6.size()), var5.getRegisteredName()});
            }
        }
    }

    protected void removeObjective(ICommandSender p_147191_1_, String p_147191_2_)
    {
        Scoreboard var3 = this.getScoreboard();
        ScoreObjective var4 = this.func_147189_a(p_147191_2_, false);
        var3.func_96519_k(var4);
        notifyOperators(p_147191_1_, this, "commands.scoreboard.objectives.remove.success", new Object[] {p_147191_2_});
    }

    protected void listObjectives(ICommandSender p_147196_1_)
    {
        Scoreboard var2 = this.getScoreboard();
        Collection var3 = var2.getScoreObjectives();

        if (var3.size() <= 0)
        {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        else
        {
            ChatComponentTranslation var4 = new ChatComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] {Integer.valueOf(var3.size())});
            var4.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147196_1_.addChatMessage(var4);
            Iterator var5 = var3.iterator();

            while (var5.hasNext())
            {
                ScoreObjective var6 = (ScoreObjective)var5.next();
                p_147196_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] {var6.getName(), var6.getDisplayName(), var6.getCriteria().getName()}));
            }
        }
    }

    protected void setObjectiveDisplay(ICommandSender p_147198_1_, String[] p_147198_2_, int p_147198_3_)
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = p_147198_2_[p_147198_3_++];
        int var6 = Scoreboard.getObjectiveDisplaySlotNumber(var5);
        ScoreObjective var7 = null;

        if (p_147198_2_.length == 4)
        {
            var7 = this.func_147189_a(p_147198_2_[p_147198_3_++], false);
        }

        if (var6 < 0)
        {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] {var5});
        }
        else
        {
            var4.setObjectiveInDisplaySlot(var6, var7);

            if (var7 != null)
            {
                notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] {Scoreboard.getObjectiveDisplaySlot(var6), var7.getName()});
            }
            else
            {
                notifyOperators(p_147198_1_, this, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] {Scoreboard.getObjectiveDisplaySlot(var6)});
            }
        }
    }

    protected void listPlayers(ICommandSender p_147195_1_, String[] p_147195_2_, int p_147195_3_)
    {
        Scoreboard var4 = this.getScoreboard();

        if (p_147195_2_.length > p_147195_3_)
        {
            String var5 = getPlayerName(p_147195_1_, p_147195_2_[p_147195_3_++]);
            Map var6 = var4.func_96510_d(var5);

            if (var6.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] {var5});
            }

            ChatComponentTranslation var7 = new ChatComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] {Integer.valueOf(var6.size()), var5});
            var7.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147195_1_.addChatMessage(var7);
            Iterator var8 = var6.values().iterator();

            while (var8.hasNext())
            {
                Score var9 = (Score)var8.next();
                p_147195_1_.addChatMessage(new ChatComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] {Integer.valueOf(var9.getScorePoints()), var9.func_96645_d().getDisplayName(), var9.func_96645_d().getName()}));
            }
        }
        else
        {
            Collection var10 = var4.getObjectiveNames();

            if (var10.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }

            ChatComponentTranslation var11 = new ChatComponentTranslation("commands.scoreboard.players.list.count", new Object[] {Integer.valueOf(var10.size())});
            var11.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            p_147195_1_.addChatMessage(var11);
            p_147195_1_.addChatMessage(new ChatComponentText(joinNiceString(var10.toArray())));
        }
    }

    protected void setPlayer(ICommandSender p_147197_1_, String[] p_147197_2_, int p_147197_3_)
    {
        String var4 = p_147197_2_[p_147197_3_ - 1];
        String var5 = getPlayerName(p_147197_1_, p_147197_2_[p_147197_3_++]);
        ScoreObjective var6 = this.func_147189_a(p_147197_2_[p_147197_3_++], true);
        int var7 = var4.equalsIgnoreCase("set") ? parseInt(p_147197_1_, p_147197_2_[p_147197_3_++]) : parseIntWithMin(p_147197_1_, p_147197_2_[p_147197_3_++], 1);
        Scoreboard var8 = this.getScoreboard();
        Score var9 = var8.getValueFromObjective(var5, var6);

        if (var4.equalsIgnoreCase("set"))
        {
            var9.setScorePoints(var7);
        }
        else if (var4.equalsIgnoreCase("add"))
        {
            var9.increseScore(var7);
        }
        else
        {
            var9.decreaseScore(var7);
        }

        notifyOperators(p_147197_1_, this, "commands.scoreboard.players.set.success", new Object[] {var6.getName(), var5, Integer.valueOf(var9.getScorePoints())});
    }

    protected void resetPlayers(ICommandSender p_147187_1_, String[] p_147187_2_, int p_147187_3_)
    {
        Scoreboard var4 = this.getScoreboard();
        String var5 = getPlayerName(p_147187_1_, p_147187_2_[p_147187_3_++]);
        var4.func_96515_c(var5);
        notifyOperators(p_147187_1_, this, "commands.scoreboard.players.reset.success", new Object[] {var5});
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"objectives", "players", "teams"});
        }
        else
        {
            if (args[0].equalsIgnoreCase("objectives"))
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, new String[] {"list", "add", "remove", "setdisplay"});
                }

                if (args[1].equalsIgnoreCase("add"))
                {
                    if (args.length == 4)
                    {
                        Set var3 = IScoreObjectiveCriteria.INSTANCES.keySet();
                        return getListOfStringsFromIterableMatchingLastWord(args, var3);
                    }
                }
                else if (args[1].equalsIgnoreCase("remove"))
                {
                    if (args.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(args, this.func_147184_a(false));
                    }
                }
                else if (args[1].equalsIgnoreCase("setdisplay"))
                {
                    if (args.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(args, new String[] {"list", "sidebar", "belowName"});
                    }

                    if (args.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(args, this.func_147184_a(false));
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("players"))
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, new String[] {"set", "add", "remove", "reset", "list"});
                }

                if (!args[1].equalsIgnoreCase("set") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove"))
                {
                    if ((args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("list")) && args.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(args, this.getScoreboard().getObjectiveNames());
                    }
                }
                else
                {
                    if (args.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (args.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(args, this.func_147184_a(true));
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("teams"))
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, new String[] {"add", "remove", "join", "leave", "empty", "list", "option"});
                }

                if (args[1].equalsIgnoreCase("join"))
                {
                    if (args.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(args, this.getScoreboard().getTeamNames());
                    }

                    if (args.length >= 4)
                    {
                        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    }
                }
                else
                {
                    if (args[1].equalsIgnoreCase("leave"))
                    {
                        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (!args[1].equalsIgnoreCase("empty") && !args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("remove"))
                    {
                        if (args[1].equalsIgnoreCase("option"))
                        {
                            if (args.length == 3)
                            {
                                return getListOfStringsFromIterableMatchingLastWord(args, this.getScoreboard().getTeamNames());
                            }

                            if (args.length == 4)
                            {
                                return getListOfStringsMatchingLastWord(args, new String[] {"color", "friendlyfire", "seeFriendlyInvisibles"});
                            }

                            if (args.length == 5)
                            {
                                if (args[3].equalsIgnoreCase("color"))
                                {
                                    return getListOfStringsFromIterableMatchingLastWord(args, EnumChatFormatting.getValidValues(true, false));
                                }

                                if (args[3].equalsIgnoreCase("friendlyfire") || args[3].equalsIgnoreCase("seeFriendlyInvisibles"))
                                {
                                    return getListOfStringsMatchingLastWord(args, new String[] {"true", "false"});
                                }
                            }
                        }
                    }
                    else if (args.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(args, this.getScoreboard().getTeamNames());
                    }
                }
            }

            return null;
        }
    }

    protected List func_147184_a(boolean p_147184_1_)
    {
        Collection var2 = this.getScoreboard().getScoreObjectives();
        ArrayList var3 = new ArrayList();
        Iterator var4 = var2.iterator();

        while (var4.hasNext())
        {
            ScoreObjective var5 = (ScoreObjective)var4.next();

            if (!p_147184_1_ || !var5.getCriteria().isReadOnly())
            {
                var3.add(var5.getName());
            }
        }

        return var3;
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return args[0].equalsIgnoreCase("players") ? index == 2 : (!args[0].equalsIgnoreCase("teams") ? false : index == 2 || index == 3);
    }
}
