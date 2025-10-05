package com.asteristired.soulstice;

import com.asteristired.soulstice.BlockEntities.ModBlockEntities;
import com.asteristired.soulstice.Blocks.ModBlocks;
import com.asteristired.soulstice.Items.ModItems;
import com.asteristired.soulstice.ServerEvents.AfterDeath;
import com.asteristired.soulstice.StatusEffects.ModStatusEffects;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Soulstice implements ModInitializer {

    public static final String MOD_ID = "soulstice";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {

        // initialise modules
        ModStatusEffects.Initalise();
        ModItems.Initialise();
        ModItems.AddToCreativeMenu();
        ModBlocks.Initalise();
        ModBlockEntities.Initalise();

        AfterDeath.Initalise();

        LOGGER.info("Server Initalised");
    }
}
