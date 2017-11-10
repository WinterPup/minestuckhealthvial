package com.mraof.minestuck.tracker;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;

public class ConnectionListener
{
	
	@SubscribeEvent
	public void onServerConnectionCreated(FMLNetworkEvent.ServerConnectionFromClientEvent event)
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.LANDREGISTER);
		
		MinestuckChannelHandler.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DISPATCHER);
		MinestuckChannelHandler.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(event.getManager().channel().attr(NetworkDispatcher.FML_DISPATCHER).get());
		MinestuckChannelHandler.channels.get(Side.SERVER).writeOutbound(packet);
	}
	
	@SubscribeEvent
	public void onClientConnectionClosed(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		Debug.debug("Disconnecting from server. Unregistering land dimensions...");
		if(!Minestuck.isServerRunning)
			MinestuckDimensionHandler.unregisterDimensions();
	}
	
}
