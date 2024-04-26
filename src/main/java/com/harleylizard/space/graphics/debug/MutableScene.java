package com.harleylizard.space.graphics.debug;

import com.harleylizard.space.Resources;
import com.harleylizard.space.block.Block;
import com.harleylizard.space.block.Blocks;
import com.harleylizard.space.graphics.model.ModelReader;
import com.harleylizard.space.graphics.texture.ModelTextures;
import com.harleylizard.space.graphics.vertex.CullGetter;
import com.harleylizard.space.graphics.vertex.Layer;
import com.harleylizard.space.graphics.vertex.Layers;
import com.harleylizard.space.light.Light;
import com.harleylizard.space.light.LightSdf;
import com.harleylizard.space.math.Direction;
import org.joml.Matrix4fStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MutableScene implements CullGetter {
    public static MutableScene SCENE;

    private final List<Block> palette;
    private final int[] blocks;

    private boolean updated;

    private MutableScene(List<Block> palette, int[] blocks) {
        this.palette = palette;
        this.blocks = blocks;

        palette.add(Blocks.WHITE_LIGHT);
        blocks[indexOf(15, 4, 15)] = palette.indexOf(Blocks.WHITE_LIGHT);
    }

    public void draw(Layers layers, LightSdf sdf) {
        if (!updated) {
            sdf.clear();
            upload(layers, sdf);
            layers.upload();
            ModelTextures.bind(0);
            updated = true;
        }
    }

    public void upload(Layers layers, LightSdf sdf) {
        try {
            var stack = new Matrix4fStack(4);
            stack.pushMatrix();

            var size = blocks.length;
            for (var i = 0; i < size; i++) {
                var block = palette.get(blocks[i]);
                if (block == Blocks.AIR) {
                    continue;
                }
                var model = ModelReader.getModel(Blocks.REGISTRY, block);

                var color = model.getLight();
                if (color != 0) {
                    var x = i % 32;
                    var y = (i / 32) % 32;
                    var z = i / (32 * 32);

                    var light = Light.of(color);
                    light.move(x + 0.5F, y + 0.5F, z + 0.5F);
                    sdf.add(light);
                }
            }

            for (var i = 0; i < size; i++) {
                var block = palette.get(blocks[i]);
                if (block == Blocks.AIR) {
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
                stack.pushMatrix();
                if (model.isAmbient()) {
                    stack.translate(0.5F, 0.5F, 0.5F);

                    var l = (x + y + z) << 8 & 360;
                    var r = (float) Math.toRadians(l);
                    stack.rotate(r, 0.0F, 1.0F, 0.0F);

                    stack.translate(-0.5F, -0.5F, -0.5F);
                }
                for (var shape : model) {

                    shape.build(this, parameters, stack, x, y, z, model.isAmbient(), j);

                }
                stack.popMatrix();
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
        if (block == Blocks.AIR) {
            return false;
        }
        try {
            var model = ModelReader.getModel(Blocks.REGISTRY, block);
            return model.getLayer() == Layer.SOLID && !model.isAmbient();
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

    public Block getBlock(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x > 31 || y > 31 || z > 31) {
            return Blocks.AIR;
        }
        return palette.get(blocks[indexOf(x, y, z)]);
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
