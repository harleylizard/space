package com.harleylizard.space.graphics.debug;

import com.harleylizard.space.Resources;
import com.harleylizard.space.block.Block;
import com.harleylizard.space.graphics.model.ModelReader;
import com.harleylizard.space.graphics.texture.ModelTextures;
import com.harleylizard.space.graphics.vertex.CullGetter;
import com.harleylizard.space.graphics.vertex.Layer;
import com.harleylizard.space.graphics.vertex.Layers;
import com.harleylizard.space.light.Light;
import com.harleylizard.space.light.LightSdf;
import com.harleylizard.space.math.Direction;
import com.harleylizard.space.block.Blocks;
import org.joml.Matrix4fStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MutableScene implements CullGetter {
    private final List<Block> palette;
    private final int[] blocks;

    private boolean updated;

    private MutableScene(List<Block> palette, int[] blocks) {
        this.palette = palette;
        this.blocks = blocks;
    }

    public void draw(Layers layers, LightSdf sdf, Runnable runnable) {
        if (!updated) {
            sdf.clear();
            upload(layers, sdf);
            runnable.run();
            layers.upload();
            ModelTextures.bind(0);
            updated = true;
        }
    }

    public void upload(Layers layers, LightSdf sdf) {
        try {
            var stack = new Matrix4fStack(3);
            stack.pushMatrix();

            var size = blocks.length;
            for (var i = 0; i < size; i++) {
                var block = palette.get(blocks[i]);
                if (block == Blocks.MODELER_AIR) {
                    continue;
                }

                var x = i % 32;
                var y = (i / 32) % 32;
                var z = i / (32 * 32);
                stack.identity();
                stack.translate(x, y, z);

                var model = ModelReader.getModel(Blocks.REGISTRY, block);

                var color = model.getLight();
                if (color != 0) {
                    var light = Light.of(color);
                    light.move(x + 0.5F, y + 0.5F, z + 0.5F);
                    sdf.add(light);
                }
            }

            for (var i = 0; i < size; i++) {
                var block = palette.get(blocks[i]);
                if (block == Blocks.MODELER_AIR) {
                    continue;
                }

                var x = i % 32;
                var y = (i / 32) % 32;
                var z = i / (32 * 32);
                stack.identity();
                stack.translate(x, y, z);

                var model = ModelReader.getModel(Blocks.REGISTRY, block);

                var parameters = layers.getVertexParameter(model.getLayer());

                var j = sdf.getColor(x, y, z);
                for (var shape : model) {
                    shape.build(this, parameters, stack, x, y, z, model.isAmbient(), j);
                }
            }
            stack.popMatrix();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldCull(int x, int y, int z, Direction direction) {
        var normal = direction.getNormal();
        x += normal.getX();
        y += normal.getY();
        z += normal.getZ();
        if (x < 0 || x > 32 || y < 0 || y > 32 || z < 0 || z > 32) {
            return false;
        }
        var block = palette.get(blocks[indexOf(x, y, z)]);
        if (block == Blocks.MODELER_AIR) {
            return false;
        }
        try {
            var model = ModelReader.getModel(Blocks.REGISTRY, block);
            return model.getLayer() == Layer.SOLID;
        } catch (IOException e) {
            return false;
        }
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (!palette.contains(block)) {
            palette.add(block);
        }
        var i = palette.indexOf(block);

        x = Math.floorMod(x, 32);
        y = Math.floorMod(y, 32);
        z = Math.floorMod(z, 32);
        blocks[indexOf(x, y, z)] = i;
        updated = false;
    }

    private static int indexOf(int x, int y, int z) {
        return (z * 32 * 32) + (y * 32) + x;
    }

    public static MutableScene of(String path) {
        try (var dataInput = new DataInputStream(Resources.get(path))) {
            var size = 32 * 32 * 32;
            var blocks = new int[size];

            for (var i = 0; i < size; i++) {
                blocks[i] = dataInput.readInt();
            }

            size = dataInput.readInt();
            var palette = new ArrayList<Block>(size);
            for (var i = 0; i < size; i++) {
                palette.add(Blocks.REGISTRY.get(dataInput.readUTF()));
            }
            return new MutableScene(palette, blocks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
