package com.mraof.minestuck.world.lands.decorator.structure;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;


public class RabbitHoleDecorator extends SimpleStructureDecorator
{
	public RabbitHoleDecorator(Biome... biome)
	{
		super(biome);
	}

	@Override
	public float getPriority()
	{
		return 0.5f;
	}

	@Override
	protected BlockPos generateStructure(World world, Random random, BlockPos pos, ChunkProviderLands provider)
	{

		IBlockState ground = provider.getSurfaceBlock();
		IBlockState bush = provider.blockRegistry.getBlockState("bush");
		IBlockState air = Blocks.AIR.getDefaultState();

		BlockPos newpos = pos;
		while (world.isAirBlock(newpos))
		{
			newpos = newpos.down();
		}
		while (!world.isAirBlock(newpos.up()))
		{
			newpos = newpos.up();
		}
		if (world.getBlockState(newpos) != ground)
		{
			if (world.getBlockState(newpos.down()) == ground)
			{
				newpos = newpos.down();
			}
			else
			{
				return null;
			}
		}

		boolean mirror;
		xCoord = newpos.getX();
		zCoord = newpos.getZ();
		yCoord = newpos.getY();
		//check wich way it should be facing
		if (world.getBlockState(new BlockPos(xCoord, yCoord - 1, zCoord)).getMaterial().isLiquid())
			return null;
		if (world.isAirBlock(newpos.north()))
		{
			rotation = false;
			mirror = false;
		}
		else if (world.isAirBlock(newpos.east()))
		{
			rotation = true;
			mirror = true;
		}
		else if (world.isAirBlock(newpos.south()))
		{
			rotation = false;
			mirror = true;
		}
		else if (world.isAirBlock(newpos.west()))
		{
			rotation = true;
			mirror = false;
		}
		else return null;

		if (mirror)
		{
			placeBlock(world, bush, 0, 0, 1);
			placeBlock(world, air, 0, 0, 0);
			placeBlock(world, air, 0, -1, 0);
			placeBlock(world, air, 0, -1, -1);
			placeBlock(world, MinestuckBlocks.rabbitSpawner.getDefaultState(), 0, -1, -2);
		}
		else
		{
			placeBlock(world, bush, 0, 0, -1);
			placeBlock(world, air, 0, 0, 0);
			placeBlock(world, air, 0, -1, 0);
			placeBlock(world, air, 0, -1, 1);
			placeBlock(world, MinestuckBlocks.rabbitSpawner.getDefaultState(), 0, -1, 2);
		}
		
		return null;
	}

	@Override
	public int getCount(Random random)
	{
		return 1;
	}
}

