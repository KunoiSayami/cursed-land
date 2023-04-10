
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package dev.leanhe.minecraft.forge.cursedland.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import dev.leanhe.minecraft.forge.cursedland.block.CursedDirtBlock;
import dev.leanhe.minecraft.forge.cursedland.CursedLandMod;

public class CursedLandModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, CursedLandMod.MODID);
	public static final RegistryObject<Block> CURSED_DIRT = REGISTRY.register("cursed_dirt", CursedDirtBlock::new);
}
