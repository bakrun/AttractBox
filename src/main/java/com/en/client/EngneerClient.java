package com.en.client;

import com.en.AttractBox;
import com.en.AttractBoxScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class EngneerClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        HandledScreens.register(AttractBox.BOX_SCREEN_HANDLER, AttractBoxScreen::new);
    }

}
