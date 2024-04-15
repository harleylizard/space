package com.harleylizard.space;

import com.harleylizard.space.block.Block;
import com.harleylizard.space.modeler.ModelerBlocks;
import com.harleylizard.space.registry.Registry;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;
import org.junit.jupiter.api.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

public final class ModelerTest {
    private static final Registry<Block> REGISTRY = ModelerBlocks.REGISTRY;

    private static final Map<String, String> MAP = Map.of(
            "minecraft:mycelium", REGISTRY.get(ModelerBlocks.MODELER_GRASS),
            "minecraft:dirt", REGISTRY.get(ModelerBlocks.MODELER_DIRT),
            "minecraft:coarse_dirt", REGISTRY.get(ModelerBlocks.MODELER_DIRT),
            "minecraft:glowstone", REGISTRY.get(ModelerBlocks.MODELER_GLOW),
            "minecraft:warped_stem", REGISTRY.get(ModelerBlocks.MODELER_STEM),
            "minecraft:purple_concrete", REGISTRY.get(ModelerBlocks.MODELER_CAP),
            "minecraft:air", REGISTRY.get(ModelerBlocks.MODELER_AIR),
            "minecraft:short_grass", REGISTRY.get(ModelerBlocks.MODELER_WILD_GRASS),
            "minecraft:crimson_fungus", REGISTRY.get(ModelerBlocks.MODELER_MUSHROOM),
            "minecraft:smooth_stone_slab", REGISTRY.get(ModelerBlocks.MODELER_FLOOR)
    );

    @Test
    public void map() throws IOException {
        var nbtPath = Paths.get(".cache", "background.nbt");
        var binaryPath = Paths.get(".cache", "modeler_background.block");

        try (var inputStream = Files.newInputStream(nbtPath); var dataOutput = new DataOutputStream(Files.newOutputStream(binaryPath))) {
            var compoundTag = (CompoundTag) new NBTDeserializer().fromStream(inputStream).getTag();

            var size = compoundTag.getListTag("size");
            var w = ((IntTag) size.get(0)).asInt();
            var h = ((IntTag) size.get(1)).asInt();
            var d = ((IntTag) size.get(2)).asInt();

            int[] ints = new int[w * h * d];
            Arrays.fill(ints, -1);

            var blocks = (ListTag<CompoundTag>) compoundTag.getListTag("blocks");
            for (var tag : blocks) {
                var pos = tag.getListTag("pos");
                var x = ((IntTag) pos.get(0)).asInt();
                var y = ((IntTag) pos.get(1)).asInt();
                var z = ((IntTag) pos.get(2)).asInt();

                var state = tag.getInt("state");

                ints[indexOf(x, y, z)] = state;
            }

            for (var i : ints) {
                dataOutput.writeInt(i);
            }

            var palette = (ListTag<CompoundTag>) compoundTag.getListTag("palette");
            dataOutput.writeInt(palette.size());

            for (var tag : palette) {
                var name = tag.getString("Name");
                dataOutput.writeUTF(MAP.get(name));
            }
            dataOutput.flush();
        }
    }

    private int indexOf(int x, int y, int z) {
        return (z * 32 * 32) + (y * 32) + x;
    }
}
