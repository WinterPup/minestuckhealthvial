package com.mraof.minestuck.modSupport;

import com.mraof.minestuck.util.CombinationRegistry;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TinkersConstructSupport extends ModSupport
{
	
	@Override
	public void registerRecipes() throws Exception
	{
		Block oreBush1 = ((Block) Class.forName("tconstruct.world.TinkerWorld").getField("oreBerry").get(null));
		Block oreBush2 = ((Block) Class.forName("tconstruct.world.TinkerWorld").getField("oreBerrySecond").get(null));
		
		String[] items1 = {"ingotIron", "ingotGold", "ingotCopper", "ingotTin"};
		
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 0), new GristSet(new GristType[]{GristType.Rust, GristType.Build, GristType.Amber}, new int[]{16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 1), new GristSet(new GristType[]{GristType.Gold, GristType.Build, GristType.Amber}, new int[]{16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 2), new GristSet(new GristType[]{GristType.Rust, GristType.Cobalt, GristType.Build, GristType.Amber}, new int[]{16, 3, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush1, 1, 3), new GristSet(new GristType[]{GristType.Rust, GristType.Caulk, GristType.Build, GristType.Amber}, new int[]{16, 8, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush2, 1, 0), new GristSet(new GristType[]{GristType.Rust, GristType.Chalk, GristType.Build, GristType.Amber}, new int[]{16, 6, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(oreBush2, 1, 1), new GristSet(new GristType[]{GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby, GristType.Build, GristType.Amber}, new int[]{8, 1, 4, 4, 1, 1}));
		
		for(int i = 0; i < items1.length; i++)
			CombinationRegistry.addCombination("treeLeaves", items1[i], CombinationRegistry.MODE_AND, new ItemStack(oreBush1, 1, i));
		
		CombinationRegistry.addCombination("treeLeaves", "ingotAluminium", CombinationRegistry.MODE_AND, new ItemStack(oreBush2, 1, 0));
		CombinationRegistry.addCombination("treeLeaves", Items.EXPERIENCE_BOTTLE, 0, CombinationRegistry.MODE_AND, new ItemStack(oreBush2, 1, 1));
	}
	
}
