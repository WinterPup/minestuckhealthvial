package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.editmode.ServerEditHandler;

public class ClientEditPacket extends MinestuckPacket
{
	
	int username = -1;
	int target;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		if(dat.length > 0)
		{
			data.writeInt((Integer) dat[0]);
			data.writeInt((Integer) dat[1]);
		}
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		if(data.readableBytes() == 0)
			return this;
		username = data.readInt();
		target = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		UserListOpsEntry opsEntry = player == null ? null : player.getServer().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
		if(!MinestuckConfig.giveItems)
		{
			if(username == -1)
				ServerEditHandler.onPlayerExit(player);
			else if(!MinestuckConfig.privateComputers || IdentifierHandler.encode(player).getId() == this.username || opsEntry != null && opsEntry.getPermissionLevel() >= 2)
				ServerEditHandler.newServerEditor((EntityPlayerMP) player, IdentifierHandler.getById(username), IdentifierHandler.getById(target));
			return;
		}
		
		EntityPlayerMP playerMP = IdentifierHandler.getById(target).getPlayer();
		
		if(playerMP != null && (!MinestuckConfig.privateComputers || IdentifierHandler.getById(username).appliesTo(player)) || opsEntry != null && opsEntry.getPermissionLevel() >= 2)
		{
			SburbConnection c = SkaianetHandler.getClientConnection(IdentifierHandler.getById(target));
			if(c == null || c.getServerIdentifier().getId() != username || !(c.isMain() || SkaianetHandler.giveItems(IdentifierHandler.getById(target))))
				return;
			for(int i = 0; i < 5; i++)
				if(i == 4)
				{
					if(c.enteredGame())
						continue;
					ItemStack card = AlchemyRecipeHandler.createCard(SburbHandler.getEntryItem(c.getClientIdentifier()), true);
					if(!playerMP.inventory.hasItemStack(card))
						c.givenItems()[i] = playerMP.inventory.addItemStackToInventory(card) || c.givenItems()[i];
				} else
				{
					ItemStack machine = new ItemStack(MinestuckBlocks.sburbMachine, 1, i);
					if(i == 1 && !c.enteredGame())
						continue;
					if(!playerMP.inventory.hasItemStack(machine))
						c.givenItems()[i] = playerMP.inventory.addItemStackToInventory(machine) || c.givenItems()[i];
				}
			player.getServer().getPlayerList().syncPlayerInventory(playerMP);
		}
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}

}
