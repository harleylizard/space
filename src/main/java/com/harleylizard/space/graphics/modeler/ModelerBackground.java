package com.harleylizard.space.graphics.modeler;

import com.harleylizard.space.Resources;
import com.harleylizard.space.block.Block;
import com.harleylizard.space.graphics.light.Light;
import com.harleylizard.space.graphics.light.LightSignedDistanceField;
import com.harleylizard.space.graphics.light.Lights;
import com.harleylizard.space.graphics.model.ModelReader;
import com.harleylizard.space.graphics.vertex.CullGetter;
import com.harleylizard.space.graphics.vertex.Layer;
import com.harleylizard.space.graphics.vertex.Layers;
import com.harleylizard.space.math.Direction;
import com.harleylizard.space.modeler.ModelerBlocks;
import org.joml.Matrix4fStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ModelerBackground implements CullGetter {
    private final List<Block> palette;
    private final int[] blocks;

    private ModelerBackground(List<Block> palette, int[] blocks) {
        this.palette = palette;
        this.blocks = blocks;
    }

    public void upload(Layers layers, Lights lights) {
        try {
            var stack = new Matrix4fStack(3);
            stack.pushMatrix();

            var size = blocks.length;
            for (var i = 0; i < size; i++) {
                var block = palette.get(blocks[i]);
                if (block == ModelerBlocks.MODELER_AIR) {
                    continue;
                }

                var x = i % 32;
                var y = (i / 32) % 32;
                var z = i / (32 * 32);
                stack.identity();
                stack.translate(x, y, z);

                var model = ModelReader.getModel(ModelerBlocks.REGISTRY, block);

                var color = model.getLight();
                if (color != 0) {
                    var light = lights.add(color);
                    light.move(x + 0.5F, y + 0.5F, z + 0.5F);
                }
            }

            var sdf = LightSignedDistanceField.createFrom(lights);
            for (var i = 0; i < size; i++) {
                var block = palette.get(blocks[i]);
                if (block == ModelerBlocks.MODELER_AIR) {
                    continue;
                }

                var x = i % 32;
                var y = (i / 32) % 32;
                var z = i / (32 * 32);
                stack.identity();
                stack.translate(x, y, z);

                var model = ModelReader.getModel(ModelerBlocks.REGISTRY, block);

                var parameters = layers.getVertexParameter(model.getLayer());

                var j = sdf.getLight(lights, x, y, z);
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
        if (block == ModelerBlocks.MODELER_AIR) {
            return false;
        }
        try {
            var model = ModelReader.getModel(ModelerBlocks.REGISTRY, block);
            return model.getLayer() == Layer.SOLID;
        } catch (IOException e) {
            return false;
        }
    }

    private static int indexOf(int x, int y, int z) {
        return (z * 32 * 32) + (y * 32) + x;
    }

    public static ModelerBackground of(String path) {
        try (var dataInput = new DataInputStream(Resources.get(path))) {
            var size = 32 * 32 * 32;
            var blocks = new int[size];

            for (var i = 0; i < size; i++) {
                blocks[i] = dataInput.readInt();
            }

            size = dataInput.readInt();
            var palette = new ArrayList<Block>(size);
            for (var i = 0; i < size; i++) {
                palette.add(ModelerBlocks.REGISTRY.get(dataInput.readUTF()));
            }
            return new ModelerBackground(palette, blocks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
