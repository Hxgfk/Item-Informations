package com.hxgfk.iteminf;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ItemInformations.MODID)
public class ItemInformations
{
    public static final String MODID = "iteminf";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ItemInformations()
    {
        MinecraftForge.EVENT_BUS.addListener(ItemInformations::registryCommand);
    }

    public static void registryCommand(RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }
}
