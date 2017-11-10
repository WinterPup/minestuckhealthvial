package com.mraof.minestuck.command;

import com.google.common.collect.Lists;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandSburbSession extends CommandBase	//TODO properly localize all messages related to this command
{
	
	@Override
	public String getName()
	{
		return "sburbSession";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.sburbSession.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 2 || args.length < 3 && args[1].equalsIgnoreCase("add")
				|| args.length != 4 && (args[1].equalsIgnoreCase("landTerrain") || args[1].equalsIgnoreCase("landTitle"))
				|| args.length != 5 && args[1].equalsIgnoreCase("title")
				|| args.length != 6 && args.length != 7 && args[1].equalsIgnoreCase("define"))
			throw new WrongUsageException(this.getUsage(sender));
		String sessionName = args[0];
		String command = args[1];
		
		if(command.equalsIgnoreCase("name"))
		{
			String playerName = args.length < 3 ? getCommandSenderAsPlayer(sender).getName() : args[2];
			SburbHandler.sessionName(server, sender, this, playerName, sessionName);
			
		} else if(command.equalsIgnoreCase("add")/* || command.equalsIgnoreCase("finish")*/)
		{
			String[] params = Arrays.copyOfRange(args, 2, args.length);
			SburbHandler.managePredefinedSession(server, sender, this, sessionName, params, false);//command.equalsIgnoreCase("finish"));
		} else if(command.equalsIgnoreCase("title"))
		{
			String playerName = args[2];
			String classStr = args[3], aspectStr = args[4];
			
			EnumClass titleClass = null;
			EnumAspect titleAspect = null;
			
			try	//Parse class
			{
				for(EnumClass c : EnumClass.values())
					if(c.name().equalsIgnoreCase(classStr))
					{
						titleClass = c;
						break;
					}
				
				if(titleClass == null)
				{
					int classIndex = Integer.parseInt(classStr);
					titleClass = EnumClass.getClassFromInt(classIndex);
				}
			} catch(Exception e)
			{
				throw new WrongUsageException("commands.sburbSession.notClass", classStr);
			}
			try	//Parse aspect
			{
				for(EnumAspect aspect : EnumAspect.values())
					if(aspect.name().equalsIgnoreCase(aspectStr))
					{
						titleAspect = aspect;
						break;
					}
				
				if(titleAspect == null)
				{
					int aspectIndex = Integer.parseInt(aspectStr);
					titleAspect = EnumAspect.getAspectFromInt(aspectIndex);
				}
			} catch(Exception e)
			{
				throw new WrongUsageException("commands.sburbSession.notAspect", aspectStr);
			}
			
			SburbHandler.predefineTitle(server, sender, this, playerName, sessionName, new Title(titleClass, titleAspect));
			
		} else if(command.equalsIgnoreCase("landTerrain"))
		{
			String playerName = args[2];
			
			TerrainLandAspect landAspect = LandAspectRegistry.fromNameTerrain(args[3].toLowerCase());
			if(landAspect == null)
				throw new CommandException("Can't find terrain land aspect by the name %s", args[3]);
			
			SburbHandler.predefineTerrainLandAspect(server, sender, this, playerName, sessionName, landAspect);
			
		} else if(command.equalsIgnoreCase("landTitle"))
		{
			String playerName = args[2];
			
			TitleLandAspect landAspect = LandAspectRegistry.fromNameTitle(args[3].toLowerCase());
			if(landAspect == null)
				throw new CommandException("Can't find title land aspect by the name %s", args[3]);
			
			SburbHandler.predefineTitleLandAspect(server, sender, this, playerName, sessionName, landAspect);
			
		} else if(command.equalsIgnoreCase("define"))
		{
			String playerName = args[2];
			
			String classStr = args[3], aspectStr = args[4];
			
			EnumClass titleClass = null;
			EnumAspect titleAspect = null;
			
			try	//Parse class
			{
				for(EnumClass c : EnumClass.values())
					if(c.name().equalsIgnoreCase(classStr))
					{
						titleClass = c;
						break;
					}
				
				if(titleClass == null)
				{
					int classIndex = Integer.parseInt(classStr);
					titleClass = EnumClass.getClassFromInt(classIndex);
				}
			} catch(Exception e)
			{
				throw new WrongUsageException("commands.sburbSession.notClass", classStr);
			}
			try	//Parse aspect
			{
				for(EnumAspect aspect : EnumAspect.values())
					if(aspect.name().equalsIgnoreCase(aspectStr))
					{
						titleAspect = aspect;
						break;
					}
				
				if(titleAspect == null)
				{
					int aspectIndex = Integer.parseInt(aspectStr);
					titleAspect = EnumAspect.getAspectFromInt(aspectIndex);
				}
			} catch(Exception e)
			{
				throw new WrongUsageException("commands.sburbSession.notAspect", aspectStr);
			}
			TerrainLandAspect terrainLand = null;
			TitleLandAspect titleLand = null;
			
			if(args.length == 7)
			{
				titleLand = LandAspectRegistry.fromNameTitle(args[5].toLowerCase());
				if(titleLand == null)
					throw new CommandException("Can't find title land aspect by the name %s", args[5]);
				terrainLand = LandAspectRegistry.fromNameTerrain(args[6].toLowerCase());
				if(terrainLand == null)
					throw new CommandException("Can't find terrain land aspect by the name %s", args[6]);
			} else
			{
				titleLand = LandAspectRegistry.fromNameTitle(args[5].toLowerCase());
				if(titleLand == null)
				{
					terrainLand = LandAspectRegistry.fromNameTerrain(args[5].toLowerCase());
					if(terrainLand == null)
						throw new CommandException("Can't find any land aspect by the name %s", args[5]);
				}
			}
			
			SburbHandler.predefineTitle(server, sender, this, playerName, sessionName, new Title(titleClass, titleAspect));
			if(titleLand != null)
				SburbHandler.predefineTitleLandAspect(server, sender, this, playerName, sessionName, titleLand);
			if(terrainLand != null)
				SburbHandler.predefineTerrainLandAspect(server, sender, this, playerName, sessionName, terrainLand);
		} else throw new WrongUsageException(this.getUsage(sender));
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if(args.length == 1) return getListOfStringsMatchingLastWord(args, SessionHandler.getSessionNames());
		else if(args.length == 2) return getListOfStringsMatchingLastWord(args, "add", "name", "title", "landTerrain", "landTitle");
		else if(args.length > 2)
		{
			String command = args[1];
			if(command.equalsIgnoreCase("add"))
			{
				boolean remove = args[args.length - 1].startsWith("!");
				if(remove)
					args[args.length - 1] = args[args.length - 1].substring(1);
				List<String> list = IdentifierHandler.getCommandAutocomplete(server, args);
				if(remove && !list.isEmpty())
				{
					List<String> oldList = list;
					list = Lists.<String>newArrayList();
					for(String s : oldList)
						list.add("!" + s);
				}
				return list;
			} else if(command.equalsIgnoreCase("name"))
			{
				if(args.length == 3)
					return IdentifierHandler.getCommandAutocomplete(server, args);
			} else if(command.equalsIgnoreCase("title"))
			{
				if(args.length == 3)
					return IdentifierHandler.getCommandAutocomplete(server, args);
				if(args.length == 4)
					return getListOfStringsMatchingLastWord(args, Arrays.asList(EnumClass.values()));
				if(args.length == 5)
					return getListOfStringsMatchingLastWord(args, Arrays.asList(EnumAspect.values()));
			} else if(command.equalsIgnoreCase("landTerrain"))
			{
				if(args.length == 3)
					return IdentifierHandler.getCommandAutocomplete(server, args);
				if(args.length == 4)
					return getListOfStringsMatchingLastWord(args, LandAspectRegistry.getNamesTerrain());
			} else if(command.equalsIgnoreCase("landTitle"))
			{
				if(args.length == 3)
					return IdentifierHandler.getCommandAutocomplete(server, args);
				if(args.length == 4)
					return getListOfStringsMatchingLastWord(args, LandAspectRegistry.getNamesTitle());
			}
		}
		
		return Collections.<String>emptyList();
	}
}