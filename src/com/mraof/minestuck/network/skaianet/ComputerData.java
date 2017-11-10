package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ComputerData
{
	int x, y, z;
	int dimension;
	PlayerIdentifier owner;
	@SideOnly(Side.CLIENT)
	private int ownerId;
	
	public static ComputerData createData(TileEntityComputer te)
	{
		if(!te.getWorld().isRemote)
			return new ComputerData(te.owner, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), te.getWorld().provider.getDimension());
		else return new ComputerData(te.ownerId, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), te.getWorld().provider.getDimension());
	}
	
	private ComputerData(int ownerId, int x, int y, int z, int dimension)
	{
		this.ownerId = ownerId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}
	
	public ComputerData(PlayerIdentifier owner, int x, int y, int z, int dimension)
	{
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}
	
	ComputerData()
	{}
	
	ComputerData read(NBTTagCompound nbt)
	{
		owner = IdentifierHandler.load(nbt, "name");
		x = nbt.getInteger("x");
		y = nbt.getInteger("y");
		z = nbt.getInteger("z");
		dimension = nbt.getInteger("dim");
		return this;
	}
	
	NBTTagCompound write()
	{
		NBTTagCompound c = new NBTTagCompound();
		owner.saveToNBT(c, "name");
		c.setInteger("x", x);
		c.setInteger("y", y);
		c.setInteger("z", z);
		c.setInteger("dim", dimension);
		return c;
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	public int getZ() {return z;}
	public int getDimension() {return dimension;}
	public PlayerIdentifier getOwner() {return owner;}
	@SideOnly(Side.CLIENT) public int getOwnerId() {return ownerId;}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof ComputerData)
		{
			ComputerData otherData = (ComputerData) obj;
			return this.owner.equals(otherData.owner) && this.x == otherData.x && this.y == otherData.y && this.z == otherData.z && this.dimension == otherData.dimension;
		}
		return false;
	}
	
}