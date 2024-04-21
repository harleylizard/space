package com.harleylizard.space;

import com.harleylizard.space.block.Block;
import com.harleylizard.space.block.Blocks;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ModelerTest {
    private static final Registry<Block> REGISTRY = Blocks.REGISTRY;

    private static final Map<String, String> MAP = createMap();

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

    private static Map<String, String> createMap() {
        var map = new HashMap<String, String>();
        map.put("minecraft:mycelium", REGISTRY.get(Blocks.MODELER_GRASS));
        map.put("minecraft:dirt", REGISTRY.get(Blocks.MODELER_DIRT));
        map.put("minecraft:coarse_dirt", REGISTRY.get(Blocks.MODELER_DIRT));
        map.put("minecraft:glowstone", REGISTRY.get(Blocks.MODELER_GLOW));
        map.put("minecraft:warped_stem", REGISTRY.get(Blocks.MODELER_STEM));
        map.put("minecraft:purple_concrete", REGISTRY.get(Blocks.MODELER_CAP));
        map.put("minecraft:air", REGISTRY.get(Blocks.MODELER_AIR));
        map.put("minecraft:short_grass", REGISTRY.get(Blocks.MODELER_WILD_GRASS));
        map.put("minecraft:crimson_fungus", REGISTRY.get(Blocks.MODELER_MUSHROOM));
        map.put("minecraft:smooth_stone_slab", REGISTRY.get(Blocks.MODELER_FLOOR));
        map.put("minecraft:weeping_vines", REGISTRY.get(Blocks.MODELER_AIR));
        map.put("minecraft:weeping_vines_plant", REGISTRY.get(Blocks.MODELER_AIR));
        map.put("minecraft:sea_lantern", REGISTRY.get(Blocks.MODELER_LIGHT));

        return Collections.unmodifiableMap(map);
    }
}
