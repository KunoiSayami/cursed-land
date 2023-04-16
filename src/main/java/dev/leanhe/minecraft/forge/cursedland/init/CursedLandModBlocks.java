
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package dev.leanhe.minecraft.forge.cursedland.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;

import dev.leanhe.minecraft.forge.cursedland.block.SuperStrengthenedGlassBlock;
import dev.leanhe.minecraft.forge.cursedland.block.NoLightGlassBlock;
import dev.leanhe.minecraft.forge.cursedland.block.CursedDirtBlock;
import dev.leanhe.minecraft.forge.cursedland.CursedLandMod;

public class CursedLandModBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, CursedLandMod.MODID);
    public static final RegistryObject<Block> NO_LIGHT_GLASS = REGISTRY.register("no_light_glass", NoLightGlassBlock::new);
    public static final RegistryObject<Block> CURSED_DIRT = REGISTRY.register("cursed_dirt", CursedDirtBlock::new);
    public static final RegistryObject<Block> SUPER_STRENGTHENED_GLASS = REGISTRY.register("super_strengthened_glass", SuperStrengthenedGlassBlock::new);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSideHandler {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            NoLightGlassBlock.registerRenderLayer();
            SuperStrengthenedGlassBlock.registerRenderLayer();
        }
    }
}
