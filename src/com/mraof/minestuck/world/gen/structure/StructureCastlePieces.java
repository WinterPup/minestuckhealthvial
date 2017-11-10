package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;

public class StructureCastlePieces 
{

	public static StructureComponent getNextValidComponent(
			ComponentCastleStartPiece startPiece,
			List<ComponentCastlePiece> par2List, Random par2Random, int x, int y, int z, int par6, int componentType)
	{
		{
			ComponentCastlePiece newPiece = getNextComponent(startPiece, par2List, par2Random, x, y, z, par6, componentType);

			if (newPiece != null)
			{
				par2List.add(newPiece);
//				Debug.print(startPiece.pendingPieces);
//				Debug.print(newPiece);
				startPiece.pendingPieces.add(newPiece);
			}

			return newPiece;
		}
	}

	public static ComponentCastlePiece getNextComponent(ComponentCastleStartPiece startPiece, List<ComponentCastlePiece> par1List, Random par2Random, int x, int y, int z, int par6, int componentType) 
	{
		switch(componentType)
		{
		case 0:
			return ComponentCastleSolidPiece.findValidPlacement(par1List, startPiece, x, y, z, par6, componentType);
		case 1:
			return ComponentCastleWallPiece.findValidPlacement(par1List, startPiece, x, y, z, par6, componentType, false);
		case 2:
			return ComponentCastleRoomPiece.findValidPlacement(par1List, startPiece, x, y, z, par6, componentType);
		case 3:
			return ComponentCastleRoomPiece.createRandomRoom(par1List, startPiece, x, y, z, par6, componentType, par2Random);
		default:
			return null;
		}
	}
	
	public static void registerComponents()
	{
		MapGenStructureIO.registerStructureComponent(ComponentCastleStartPiece.class, "SkaiaCastleStart");
		MapGenStructureIO.registerStructureComponent(ComponentCastleLibraryPiece.class, "SkaiaCastleLibrary");
		MapGenStructureIO.registerStructureComponent(ComponentCastleRoomPiece.class, "SkaiaCastleRoom");
		MapGenStructureIO.registerStructureComponent(ComponentCastleSolidPiece.class, "SkaiaCastleSolid");
		MapGenStructureIO.registerStructureComponent(ComponentCastleStaircasePiece.class, "SkaiaCastleStaircase");
		MapGenStructureIO.registerStructureComponent(ComponentCastleWallPiece.class, "SkaiaCastleWall");
		MapGenStructureIO.registerStructureComponent(ComponentCastlePiece.class, "SkaiaCastleMain");
	}
	
}
