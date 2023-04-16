
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package dev.leanhe.minecraft.forge.cursedland.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;

import dev.leanhe.minecraft.forge.cursedland.CursedLandMod;

public class CursedLandModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, CursedLandMod.MODID);
	public static final RegistryObject<Item> NO_LIGHT_GLASS = block(CursedLandModBlocks.NO_LIGHT_GLASS, CursedLandModTabs.TAB_CURSED_DIRT_TAB);
	public static final RegistryObject<Item> CURSED_DIRT = block(CursedLandModBlocks.CURSED_DIRT, CursedLandModTabs.TAB_CURSED_DIRT_TAB);
	public static final RegistryObject<Item> SUPER_STRENGTHENED_GLASS = block(CursedLandModBlocks.SUPER_STRENGTHENED_GLASS, CursedLandModTabs.TAB_CURSED_DIRT_TAB);

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}
