
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package dev.leanhe.minecraft.forge.cursedland.init;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;

public class CursedLandModTabs {
	public static CreativeModeTab TAB_CURSED_DIRT_TAB;

	public static void load() {
		TAB_CURSED_DIRT_TAB = new CreativeModeTab("tabcursed_dirt_tab") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(CursedLandModBlocks.CURSED_DIRT.get());
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
}
