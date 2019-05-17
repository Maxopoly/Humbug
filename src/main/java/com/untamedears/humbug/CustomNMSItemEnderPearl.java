package com.untamedears.humbug;


import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.EnumInteractionResult;
import net.minecraft.server.v1_12_R1.InteractionResultWrapper;
import net.minecraft.server.v1_12_R1.ItemEnderPearl;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.SoundCategory;
import net.minecraft.server.v1_12_R1.SoundEffects;
import net.minecraft.server.v1_12_R1.StatisticList;
import net.minecraft.server.v1_12_R1.World;

public class CustomNMSItemEnderPearl extends ItemEnderPearl {
	private Config config;

	public CustomNMSItemEnderPearl(Config cfg) {
		super();
		this.config = cfg;
	}
  
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand)
	{
		ItemStack itemstack = entityhuman.b(enumhand);
		    
		if (!world.isClientSide)
		{
			double gravity = this.config.get("ender_pearl_gravity").getDouble();

			CustomNMSEntityEnderPearl entityenderpearl = new CustomNMSEntityEnderPearl(world, entityhuman, gravity);
			      
			entityenderpearl.a(entityhuman, entityhuman.pitch, entityhuman.yaw, 0.0F, 1.5F, 1.0F);

			if (!world.addEntity(entityenderpearl))
			{
				if ((entityhuman instanceof EntityPlayer)) {
					((EntityPlayer)entityhuman).getBukkitEntity().updateInventory();
				}

				return new InteractionResultWrapper<ItemStack>(EnumInteractionResult.FAIL, itemstack);
			}
		}
		    
		if (!entityhuman.abilities.canInstantlyBuild) {
			itemstack.subtract(1);
		}
		    
		world.a(null, entityhuman.locX, entityhuman.locY, entityhuman.locZ, SoundEffects.bn, SoundCategory.NEUTRAL, 0.5F, 0.4F / (j.nextFloat() * 0.4F + 0.8F));
		entityhuman.getCooldownTracker().a(this, 20);
		    
		entityhuman.b(StatisticList.b(this));
		return new InteractionResultWrapper<ItemStack>(EnumInteractionResult.SUCCESS, itemstack);
	}
}
