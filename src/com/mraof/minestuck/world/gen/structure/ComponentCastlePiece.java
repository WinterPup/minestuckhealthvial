package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public abstract class ComponentCastlePiece extends StructureComponent 
{
	protected ComponentCastleStartPiece startPiece;
	protected int direction;
	//private List roomsLinkedToTheRoom = new LinkedList();
	public ComponentCastlePiece() {}
	protected ComponentCastlePiece(int par1, ComponentCastleStartPiece startPiece) 
	{
		super(par1);
//		this.boundingBox = new StructureBoundingBox(0, 0, 0, 256, 7, 256);
		this.startPiece = startPiece;
//		this.coordBaseMode = 0;
		this.direction = 0;
	}

	protected StructureComponent getNextComponentNormal(
			ComponentCastleStartPiece castleStartPiece, List<ComponentCastlePiece> components, Random random, int xShift, int zShift, boolean par6)
	{
		return getNextComponentNormal(castleStartPiece, components, random, xShift, 0, zShift, par6);
	}

	protected StructureComponent getNextComponentNormal(
			ComponentCastleStartPiece castleStartPiece, List<ComponentCastlePiece> components, Random random, int xShift, int yShift, int zShift, boolean par6)
	{
			return this.getNextComponent(castleStartPiece, components, random, this.boundingBox.minX + xShift, this.boundingBox.minY + yShift, this.boundingBox.minZ + zShift, this.direction, this.getComponentType(), par6);
	}

	protected StructureComponent getNextComponent(
			ComponentCastleStartPiece castleStartPiece, List<ComponentCastlePiece> par2List, Random par3Random, int i, int j, int k, int coordBaseMode, int componentType, boolean par6)
	{
		return StructureCastlePieces.getNextValidComponent(castleStartPiece, par2List, par3Random, i, j, k, coordBaseMode, componentType);
	}
	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	protected int getAverageGroundLevel(World par1World, StructureBoundingBox par2StructureBoundingBox)
	{
		int var3 = 0;
		int var4 = 0;

		for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
		{
			for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6)
			{
				if (par2StructureBoundingBox.isVecInside(new BlockPos(var6, 64, var5)))	//isVecInside
				{
					var3 += par1World.getTopSolidOrLiquidBlock(new BlockPos(var6, 0, var5)).getY();
					++var4;
				}
			}
		}
		
		if (var4 == 0)
		{
			return -1;
		}
		else
		{
			if(var3 / var4 - 8 < 255)
				return var3 / var4;
			else
				return 255 - 8;
		}
	}
	
	protected void fillWithAlternatingBlocks(World world, StructureBoundingBox structureboundingbox, int x1, int y1, int z1, int x2, int y2, int z2, IBlockState block1, IBlockState block2, boolean b)
	{
		for (int y = y1; y <= y2; ++y)
		{
			for (int x = x1; x <= x2; ++x)
			{
				for (int z = z1; z <= z2; ++z)
				{
					if(((x + y + z) % 2 == 0) ^ b)
					{
//						Debug.print("Placing block at " + x + " " + y + " " + z + " " + blockID + " " + metadata1);
						this.setBlockState(world, block1, x, y, z, structureboundingbox);	//placeBlockAtCurrentPosition
					}
					else
					{
						this.setBlockState(world, block2, x, y, z, structureboundingbox);
//						Debug.print("Placing block at " + x + " " + y + " " + z + " " + blockID2 + " " + metadata2);
					}
				}
			}
		}
	}
	protected int getAverageGroundLevel(World world)
	{
		return this.getAverageGroundLevel(world, this.boundingBox);
	}
	
	protected IBlockState getChessBlockState(int meta)
	{
		return MinestuckBlocks.chessTile.getStateFromMeta(meta);
	}
	
}
