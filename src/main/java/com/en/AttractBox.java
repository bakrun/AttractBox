package com.en;

import com.en.block.AttractBoxBlock;
import com.en.entity.AttractBoxEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttractBox implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "attractbox";
    public static final Logger LOGGER = LoggerFactory.getLogger("attractbox");

	public static final Block AttractBoxBlock;
	public static final Item AttractBoxItem;
	public static final BlockEntityType<com.en.entity.AttractBoxEntity> Attract_BoxEntity;
	public static final ScreenHandlerType<AttratctBoxScreenHandler> BOX_SCREEN_HANDLER = new ScreenHandlerType<>(AttratctBoxScreenHandler::new, FeatureSet.empty());

	public static final Identifier ATTRACT_BOX = Identifier.of(MOD_ID, "attract_box");
	static {
		AttractBoxBlock = Registry.register(Registries.BLOCK,ATTRACT_BOX,new AttractBoxBlock(AbstractBlock.Settings.copy(Blocks.CHEST)));
		AttractBoxItem = Registry.register(Registries.ITEM,ATTRACT_BOX,new BlockItem(AttractBoxBlock,new Item.Settings()));
		Attract_BoxEntity = Registry.register(Registries.BLOCK_ENTITY_TYPE,ATTRACT_BOX, BlockEntityType.Builder.create(AttractBoxEntity::new,AttractBoxBlock).build(null));
		Registry.register(Registries.SCREEN_HANDLER,ATTRACT_BOX,BOX_SCREEN_HANDLER);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE)
				.register((itemGroup) -> itemGroup.add(AttractBox.AttractBoxItem));
	}


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}