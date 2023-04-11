
package dev.leanhe.minecraft.forge.cursedland.command;

import dev.leanhe.minecraft.forge.cursedland.procedures.CursedDirtUpdateTickProcedure;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class CursedDirtCommandCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("curseddirt")
                .then(Commands.literal("toggle")
                        .then(Commands.literal("sky").executes(CursedDirtUpdateTickProcedure::toggleSkyCheck))
                        .then(Commands.literal("nearby").executes(CursedDirtUpdateTickProcedure::toggleReplaceBlock))
                        .executes(CursedDirtUpdateTickProcedure::getCommandStatus)
                ));

    }
}
