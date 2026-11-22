package com.serthekiller.permadeath.Cambios;

import java.util.Random;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.class_1299;
import net.minecraft.class_1548;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_2902.class_2903;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MikecrackSpawner {
   private static final Logger LOGGER = LoggerFactory.getLogger("MikecrackSpawner");
   private static final int TICK_INTERVAL = 100;
   private static final int SPAWNS_PER_PLAYER = 5;
   private static final int RADIUS = 24;
   private static final int MIN_DIST = 8;
   private static final double SURFACE_RATIO = 0.4;
   private static final int Y_SEARCH_ATTEMPTS = 24;
   private static int tickCounter = 0;
   private static final Random RNG = new Random();
   private static int dbgTried = 0;
   private static int dbgNoColumnSpot = 0;
   private static int dbgFailFeet = 0;
   private static int dbgFailHead = 0;
   private static int dbgFailGround = 0;
   private static int dbgFailEntity = 0;
   private static int dbgSuccess = 0;

   public static void register() {
      ServerTickEvents.END_SERVER_TICK.register(MikecrackSpawner::onTick);
      LOGGER.info("MikecrackSpawner registrado.");
   }

   private static void onTick(MinecraftServer server) {
      if (mikecrack.isEnabled()) {
         if (++tickCounter >= 100) {
            tickCounter = 0;
            class_3218 overworld = server.method_3847(class_1937.field_25179);
            if (overworld != null) {
               for(class_3222 player : overworld.method_18456()) {
                  spawnCreepersNearPlayer(overworld, player);
               }

            }
         }
      }
   }

   private static void spawnCreepersNearPlayer(class_3218 level, class_3222 player) {
      class_2338 origin = player.method_24515();
      int spawned = 0;

      for(int attempt = 0; attempt < 30 && spawned < 5; ++attempt) {
         int dx = RNG.nextInt(49) - 24;
         int dz = RNG.nextInt(49) - 24;
         if (Math.abs(dx) >= 8 || Math.abs(dz) >= 8) {
            int x = origin.method_10263() + dx;
            int z = origin.method_10260() + dz;
            class_2338 candidate;
            if (RNG.nextDouble() < 0.4) {
               class_2338 column = new class_2338(x, 0, z);
               candidate = level.method_8598(class_2903.field_13202, column);
            } else {
               candidate = findValidSpotInColumn(level, x, z, origin.method_10264());
               if (candidate == null) {
                  ++dbgNoColumnSpot;
                  continue;
               }
            }

            if (trySpawn(level, candidate)) {
               ++spawned;
            }
         }
      }

      logAndResetDebug();
   }

   private static class_2338 findValidSpotInColumn(class_3218 level, int x, int z, int playerY) {
      int minY = level.method_31607() + 1;
      int topY = level.method_8598(class_2903.field_13202, new class_2338(x, 0, z)).method_10264();
      int maxY = Math.min(level.method_31600() - 1, Math.max(topY, playerY + 10));
      if (maxY <= minY) {
         return null;
      } else {
         for(int i = 0; i < 24; ++i) {
            int y = minY + RNG.nextInt(maxY - minY);
            class_2338 pos = new class_2338(x, y, z);
            if (level.method_24794(pos) && level.method_24794(pos.method_10084())) {
               class_2680 stateAt = level.method_8320(pos);
               class_2680 stateAbove = level.method_8320(pos.method_10084());
               class_2680 stateBelow = level.method_8320(pos.method_10074());
               boolean feetOk = isPassable(level, pos, stateAt);
               boolean headOk = isPassable(level, pos.method_10084(), stateAbove);
               boolean groundOk = !stateBelow.method_26215() || !stateBelow.method_26227().method_15769();
               if (feetOk && headOk && groundOk) {
                  return pos;
               }
            }
         }

         return null;
      }
   }

   private static boolean trySpawn(class_3218 level, class_2338 pos) {
      ++dbgTried;
      if (level.method_24794(pos) && level.method_24794(pos.method_10084())) {
         class_2680 stateAt = level.method_8320(pos);
         class_2680 stateAbove = level.method_8320(pos.method_10084());
         class_2680 stateBelow = level.method_8320(pos.method_10074());
         boolean feetOk = isPassable(level, pos, stateAt);
         boolean headOk = isPassable(level, pos.method_10084(), stateAbove);
         boolean groundOk = !stateBelow.method_26215() || !stateBelow.method_26227().method_15769();
         if (!feetOk) {
            ++dbgFailFeet;
            return false;
         } else if (!headOk) {
            ++dbgFailHead;
            return false;
         } else if (!groundOk) {
            ++dbgFailGround;
            return false;
         } else {
            double spawnY = (double)pos.method_10264();
            class_265 groundShape = stateBelow.method_26220(level, pos.method_10074());
            if (!groundShape.method_1110()) {
               spawnY = (double)pos.method_10074().method_10264() + groundShape.method_1105(class_2351.field_11052);
            }

            class_1548 creeper = new class_1548(class_1299.field_6046, level);
            creeper.method_5808((double)pos.method_10263() + (double)0.5F, spawnY, (double)pos.method_10260() + (double)0.5F, RNG.nextFloat() * 360.0F, 0.0F);
            boolean ok = level.method_8649(creeper);
            if (ok) {
               ++dbgSuccess;
            } else {
               ++dbgFailEntity;
            }

            return ok;
         }
      } else {
         return false;
      }
   }

   private static boolean isPassable(class_3218 level, class_2338 pos, class_2680 state) {
      if (state.method_26215()) {
         return true;
      } else if (!state.method_26227().method_15769()) {
         return true;
      } else {
         return state.method_45474() ? true : state.method_26220(level, pos).method_1110();
      }
   }

   private static void logAndResetDebug() {
      if (dbgTried != 0 || dbgNoColumnSpot != 0) {
         LOGGER.info("Mikecrack ciclo -> intentados:{} exito:{} failFeet:{} failHead:{} failGround:{} failEntity:{} sinBolsaAire:{}", new Object[]{dbgTried, dbgSuccess, dbgFailFeet, dbgFailHead, dbgFailGround, dbgFailEntity, dbgNoColumnSpot});
         dbgTried = 0;
         dbgNoColumnSpot = 0;
         dbgFailFeet = 0;
         dbgFailHead = 0;
         dbgFailGround = 0;
         dbgFailEntity = 0;
         dbgSuccess = 0;
      }
   }
}
