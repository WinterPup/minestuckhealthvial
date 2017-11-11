package com.mraof.minestuck.event;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.GuiColorSelector;
import com.mraof.minestuck.client.gui.playerStats.GuiDataChecker;
import com.mraof.minestuck.client.gui.playerStats.GuiEcheladder;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Used to track mixed client sided events.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	
	@SubscribeEvent
	public void onConnectedToServer(ClientConnectedToServerEvent event)	//Reset all static client-side data here
	{
		GuiPlayerStats.normalTab = GuiPlayerStats.NormalGuiType.CAPTCHA_DECK;
		GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.DEPLOY_LIST;
		ContainerEditmode.clientScroll = 0;
		CaptchaDeckHandler.clientSideModus = null;
		MinestuckPlayerData.title = null;
		MinestuckPlayerData.rung = -1;
		ColorCollector.playerColor = -1;
		ColorCollector.displaySelectionGui = false;
		GuiDataChecker.activeComponent = null;
		GuiEcheladder.lastRung = -1;
		GuiEcheladder.animatedRung = 0;
		SkaiaClient.clear();
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START)
		{
			if(ColorCollector.displaySelectionGui && Minecraft.getMinecraft().currentScreen == null)
			{
				ColorCollector.displaySelectionGui = false;
				if(MinestuckConfig.loginColorSelector)
					Minecraft.getMinecraft().displayGuiScreen(new GuiColorSelector(true));
			}
			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void addCustomTooltip(ItemTooltipEvent event)
	{
		//Add config check
		{
			ItemStack stack = event.getItemStack();
			if(stack.getItem().getRegistryName().getResourceDomain().equals(Minestuck.class.getAnnotation(Mod.class).modid()))
			{
				String name = stack.getUnlocalizedName() + ".tooltip";
				if(I18n.canTranslate(name))
					event.getToolTip().add(1, I18n.translateToLocal(name));
			}
		}
	}
	
	//Made by Rose at 5 AM 11/10/2017
	//Renders the health vial above the player's head.
	@SubscribeEvent
	public void handlerRenderLiving(RenderLivingEvent.Pre event){
		if(event.getEntity() != null && event.getEntity() instanceof EntityPlayer) {
			
			updateHealthVialPlayer(event);
			updateHealthVialBar(event);
			
		}
		
	}
	
	//Updates the health vial accordingly.
	public float updateHealthVialPlayer(RenderLivingEvent.Pre event) {
		
		float currentHealth = event.getEntity().getHealth();
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
	        
	        
	   	EntityPlayer localPlayer = Minecraft.getMinecraft().player;
		double x = event.getEntity().posX - localPlayer.posX;
		double y = event.getEntity().posY - localPlayer.posY;
		double z = event.getEntity().posZ - localPlayer.posZ;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minestuck", "textures/gui/healthvialbackground.png"));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0F, 200.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F - event.getRenderer().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		buffer.pos(-1.10D, 2.25D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos(1.10D, 2.25D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos(1.10D, 2.75D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos(-1.10D, 2.75D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GL11.glPopMatrix();
		
		return currentHealth;
		
	}
	
	public float updateHealthVialBar(RenderLivingEvent.Pre event) {
		
		float currentHealth = event.getEntity().getHealth();
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
	        
	        
	   	EntityPlayer localPlayer = Minecraft.getMinecraft().player;
	   	double healthPercentage = localPlayer.getHealth() / localPlayer.getMaxHealth();
		double x = event.getEntity().posX - localPlayer.posX;
		double y = event.getEntity().posY - localPlayer.posY;
		double z = event.getEntity().posZ - localPlayer.posZ;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minestuck", "textures/gui/healthvial.png"));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 100.0F, 100.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F - event.getRenderer().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		buffer.pos(healthPercentage - 1.0D, 1.75D, 0.1D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos(healthPercentage, 1.25D, 0.1D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos(healthPercentage, 1.25D, 0.1D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		buffer.pos(healthPercentage - 1.0D, 1.75D, 0.1D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GL11.glPopMatrix();
		
		return currentHealth;
	}
	
}
