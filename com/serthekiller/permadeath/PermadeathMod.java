package com.serthekiller.permadeath;

import com.serthekiller.permadeath.Cambios.LifeOrbManager;
import com.serthekiller.permadeath.Cambios.MilkCurseHandler;
import com.serthekiller.permadeath.Cambios.mikecrack;
import com.serthekiller.permadeath.changes.DaySyncPayload;
import com.serthekiller.permadeath.commands.PermadeathCommands;
import com.serthekiller.permadeath.config.ServerSetup;
import com.serthekiller.permadeath.dimension.ModDimensions;
import com.serthekiller.permadeath.dimension.StormExpulsionHandler;
import com.serthekiller.permadeath.dimension.TheBeginningEventHandler;
import com.serthekiller.permadeath.handlers.ArmorBonusHandler;
import com.serthekiller.permadeath.handlers.DeathHandler;
import com.serthekiller.permadeath.handlers.EndPillarBedrockHandler;
import com.serthekiller.permadeath.handlers.PhaseManager;
import com.serthekiller.permadeath.items.HyperAppleDataManager;
import com.serthekiller.permadeath.items.LifeOrbLogicHandler;
import com.serthekiller.permadeath.items.ModArmorMaterials;
import com.serthekiller.permadeath.items.ModBlocks;
import com.serthekiller.permadeath.items.ModItems;
import com.serthekiller.permadeath.items.ModItemsGroups;
import com.serthekiller.permadeath.recipes.ModRecipes;
import com.serthekiller.permadeath.storm.PermadeathStorms;
import com.serthekiller.permadeath.storm.RandomStorm;
import com.serthekiller.permadeath.utils.CrystalRegenManager;
import com.serthekiller.permadeath.utils.DateManager;
import com.serthekiller.permadeath.utils.DayChangeHandler;
import com.serthekiller.permadeath.utils.MaxDayCondition;
import com.serthekiller.permadeath.utils.MinDayCondition;
import com.serthekiller.permadeath.utils.ModSounds;
import com.serthekiller.permadeath.utils.PermadeathUtils;
import com.serthekiller.permadeath.worldgen.ModPlacementModifiers;
import com.serthekiller.permadeath.worldgen.ModStructures;
import com.serthekiller.permadeath.worldgen.densityfunction.ModDensityFunctions;
import com.serthekiller.permadeath.worldgen.feature.ModFeatures;
import com.serthekiller.permadeath.worldgen.processor.ModStructureProcessors;
import com.serthekiller.permadeath.worldgen.structure.ModStructureTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_1937;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermadeathMod implements ModInitializer {
   public static final String MOD_ID = "permadeath";
   public static final Logger LOGGER = LoggerFactory.getLogger("permadeath");

   public void onInitialize() {
      LOGGER.info("===========================================");
      LOGGER.info("Inicializando Permadeath Mod v2.0");
      LOGGER.info("===========================================");
      ServerLifecycleEvents.SERVER_STARTED.register((ServerLifecycleEvents.ServerStarted)(server) -> {
         DateManager.init(server);
         mikecrack.init(server);
         long currentDay = DateManager.getCurrentRealDay();
         if (currentDay >= 40L) {
            LOGGER.info("Mundo en día {} detectado. Recargando recetas...", currentDay);
            server.method_29439(server.method_3836().method_29210());
         }

         if (currentDay >= 50L) {
            LOGGER.info("Mundo en día {} detectado. Recargando recetas...", currentDay);
            server.method_29439(server.method_3836().method_29210());
         }

         if (currentDay >= 60L) {
            LOGGER.info("Mundo en día {} detectado. Recargando recetas...", currentDay);
            server.method_29439(server.method_3836().method_29210());
         }

      });
      ModRecipes.register();
      PermadeathCommands.register();
      ServerSetup.init();
      DeathHandler.init();
      PhaseManager.init();
      PermadeathUtils.init();
      PermadeathStorms.init();
      RandomStorm.init();
      ModItems.register();
      ModArmorMaterials.init();
      ModItemsGroups.register();
      MinDayCondition.register();
      MaxDayCondition.register();
      ArmorBonusHandler.init();
      ModSounds.registerSounds();
      EndPillarBedrockHandler.register();
      DayChangeHandler.register();
      HyperAppleDataManager.register();
      LifeOrbLogicHandler.register();
      LifeOrbManager.init();
      ModBlocks.register();
      ModDensityFunctions.register();
      ModFeatures.register();
      ModStructureProcessors.register();
      ModStructureTypes.register();
      ModDimensions.register();
      ModStructures.register();
      TheBeginningEventHandler.register();
      MilkCurseHandler.register();
      StormExpulsionHandler.register();
      ModPlacementModifiers.register();
      mikecrack.registerSpawner();
      DaySyncPayload.register();
      ServerLifecycleEvents.SERVER_STOPPED.register((ServerLifecycleEvents.ServerStopped)(server) -> {
         LOGGER.info("Servidor detenido - Limpiando variables estáticas para evitar cruce de datos...");
         DateManager.reset();
         PermadeathUtils.clearRuntimeState();
         CrystalRegenManager.reset();
      });
      ServerTickEvents.END_WORLD_TICK.register((ServerTickEvents.EndWorldTick)(world) -> {
         if (world.method_27983() == class_1937.field_25181) {
            CrystalRegenManager.tick(world);
         }

      });
      LOGGER.info("===========================================");
      LOGGER.info("Permadeath mod pre-cargado correctamente");
      LOGGER.info("===========================================");
   }
}
