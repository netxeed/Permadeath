package com.serthekiller.permadeath.worldgen.structure;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.serthekiller.permadeath.worldgen.densityfunction.UniformIslandsDensityFunction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_2902;
import net.minecraft.class_2960;
import net.minecraft.class_3195;
import net.minecraft.class_3778;
import net.minecraft.class_3785;
import net.minecraft.class_5434;
import net.minecraft.class_5868;
import net.minecraft.class_6122;
import net.minecraft.class_6880;
import net.minecraft.class_6910;
import net.minecraft.class_7151;
import net.minecraft.class_8889;
import net.minecraft.class_8891;
import net.minecraft.class_9778;
import net.minecraft.class_9822;
import net.minecraft.class_2902.class_2903;
import org.slf4j.Logger;

public class UniformIslandJigsawStructure extends class_3195 {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final boolean DEBUG_LOG = true;
   private static final int SEARCH_RADIUS = 48;
   private static final int SEARCH_STEP = 16;
   private static final int FOOTPRINT_HALF = 28;
   private static final int FOOTPRINT_STEP = 4;
   private static final int FLOOR_OFFSET = 1;
   private static final double MIN_EDGE_MARGIN = 0.2;
   private final class_6880<class_3785> startPool;
   private final Optional<class_2960> startJigsawName;
   private final int maxDepth;
   private final class_6122 startHeight;
   private final boolean useExpansionHack;
   private final Optional<class_2902.class_2903> projectStartToHeightmap;
   private final int maxDistanceFromCenter;
   private final List<class_8889> poolAliases;
   private final class_9778 dimensionPadding;
   private final class_9822 liquidSettings;
   public static final MapCodec<UniformIslandJigsawStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(method_42697(instance), class_3785.field_24954.fieldOf("start_pool").forGetter((s) -> s.startPool), class_2960.field_25139.optionalFieldOf("start_jigsaw_name").forGetter((s) -> s.startJigsawName), Codec.intRange(0, 20).fieldOf("size").forGetter((s) -> s.maxDepth), class_6122.field_31540.fieldOf("start_height").forGetter((s) -> s.startHeight), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((s) -> s.useExpansionHack), class_2903.field_24772.optionalFieldOf("project_start_to_heightmap").forGetter((s) -> s.projectStartToHeightmap), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((s) -> s.maxDistanceFromCenter), Codec.list(class_8889.field_46825).optionalFieldOf("pool_aliases", List.of()).forGetter((s) -> s.poolAliases), class_9778.field_51952.optionalFieldOf("dimension_padding", class_5434.field_51911).forGetter((s) -> s.dimensionPadding), class_9822.field_52239.optionalFieldOf("liquid_settings", class_5434.field_52235).forGetter((s) -> s.liquidSettings)).apply(instance, UniformIslandJigsawStructure::new)).validate(UniformIslandJigsawStructure::verifyRange);

   public UniformIslandJigsawStructure(class_3195.class_7302 structureSettings, class_6880<class_3785> startPool, Optional<class_2960> startJigsawName, int maxDepth, class_6122 startHeight, boolean useExpansionHack, Optional<class_2902.class_2903> projectStartToHeightmap, int maxDistanceFromCenter, List<class_8889> poolAliases, class_9778 dimensionPadding, class_9822 liquidSettings) {
      super(structureSettings);
      this.startPool = startPool;
      this.startJigsawName = startJigsawName;
      this.maxDepth = maxDepth;
      this.startHeight = startHeight;
      this.useExpansionHack = useExpansionHack;
      this.projectStartToHeightmap = projectStartToHeightmap;
      this.maxDistanceFromCenter = maxDistanceFromCenter;
      this.poolAliases = poolAliases;
      this.dimensionPadding = dimensionPadding;
      this.liquidSettings = liquidSettings;
   }

   private static DataResult<UniformIslandJigsawStructure> verifyRange(UniformIslandJigsawStructure s) {
      byte var10000;
      switch (s.method_42701()) {
         case field_28922:
            var10000 = 0;
            break;
         case field_28923:
         case field_38431:
         case field_38432:
         case field_51413:
            var10000 = 12;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      int padding = var10000;
      return s.maxDistanceFromCenter + padding > 128 ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 128") : DataResult.success(s);
   }

   public Optional<class_3195.class_7150> method_38676(class_3195.class_7149 context) {
      class_1923 cp = context.comp_568();
      int sampleY = this.startHeight.method_35391(context.comp_566(), new class_5868(context.comp_562(), context.comp_569()));
      int floorY = sampleY - 1;
      List<Candidate> valid = new ArrayList();
      int tried = 0;
      double bestScore = Double.NEGATIVE_INFINITY;

      for(int ox = -48; ox <= 48; ox += 16) {
         for(int oz = -48; oz <= 48; oz += 16) {
            int cx = cp.method_33940() + ox;
            int cz = cp.method_33942() + oz;
            ++tried;
            double score = this.footprintScore(cx, cz, floorY);
            bestScore = Math.max(bestScore, score);
            if (score >= 0.2) {
               valid.add(new Candidate(cx, cz, score));
            }
         }
      }

      valid.sort(Comparator.comparingDouble(Candidate::score).reversed());

      for(Candidate c : valid) {
         class_2338 pos = new class_2338(c.x(), sampleY, c.z());
         Optional<class_3195.class_7150> stub = class_3778.method_30419(context, this.startPool, this.startJigsawName, this.maxDepth, pos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter, class_8891.create(this.poolAliases, pos, context.comp_567()), this.dimensionPadding, this.liquidSettings);
         if (stub.isPresent()) {
            LOGGER.info("[UniformIsland] chunk {} -> GENERADA en ({}, {}, {}) score={} ({} de {} candidatos validos)", new Object[]{cp, c.x(), sampleY, c.z(), String.format("%.3f", c.score()), valid.size(), tried});
            return stub;
         }
      }

      LOGGER.info("[UniformIsland] chunk {} -> NADA: {} candidatos, {} validos, mejor score={}, sampleY={}", new Object[]{cp, tried, valid.size(), String.format("%.3f", bestScore), sampleY});
      return Optional.empty();
   }

   private double footprintScore(int cx, int cz, int floorY) {
      double min = Double.POSITIVE_INFINITY;

      for(int dx = -28; dx <= 28; dx += 4) {
         for(int dz = -28; dz <= 28; dz += 4) {
            double d = this.density(cx + dx, floorY, cz + dz);
            if (d < min) {
               min = d;
               if (d <= (double)0.0F) {
                  return d;
               }
            }
         }
      }

      return min;
   }

   private double density(int x, int y, int z) {
      return UniformIslandsDensityFunction.INSTANCE.method_40464(new SimpleFunctionContext(x, y, z));
   }

   public class_7151<?> method_41618() {
      return ModStructureTypes.UNIFORM_ISLAND_JIGSAW;
   }

   private static record Candidate(int x, int z, double score) {
   }

   private static record SimpleFunctionContext(int x, int y, int z) implements class_6910.class_6912 {
      public int comp_371() {
         return this.x;
      }

      public int comp_372() {
         return this.y;
      }

      public int comp_373() {
         return this.z;
      }
   }
}
