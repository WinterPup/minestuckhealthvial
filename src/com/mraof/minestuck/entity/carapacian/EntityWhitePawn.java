package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityWhitePawn extends EntityPawn
{
	public EntityWhitePawn(World world)
	{
		super(world);
	}
	public EntityWhitePawn(World world, int type)
	{
		super(world, type);
	}
	@Override
	public String getTexture() 
	{
		return "textures/mobs/prospitian_pawn.png";
	}

	@Override
	public EnumEntityKingdom getKingdom()
	{
		return EnumEntityKingdom.PROSPITIAN;
	}
}
