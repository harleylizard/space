package com.harleylizard.space.graphics.modeler;

import com.harleylizard.space.Resources;
import com.harleylizard.space.block.Block;
import com.harleylizard.space.graphics.model.ModelReader;
import com.harleylizard.space.graphics.vertex.Layers;
import com.harleylizard.space.modeler.ModelerBlocks;
import org.joml.Matrix4fStack;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ModelerBackground {
    private final List<Block> palette;
    private final int[] blocks;

    private ModelerBackground(List<Block> palette, int[] blocks) {
        this.palette = palette;
        this.blocks = blocks;
    }

    public void upload(Layers layers) {
        var stack = new Matrix4fStack(3);
        stack.pushMatrix();
        for (int i = 0; i < blocks.length; i++) {
            var block = palette.get(blocks[i]);
            if (block == ModelerBlocks.MODELER_AIR) {
                continue;
            }

            var x = i % 32;
            var y = (i / 32) % 32;
            var z = i / (32 * 32);
            stack.identity();
            stack.translate(x, y, z);

            try {
                var model = ModelReader.getModel(ModelerBlocks.REGISTRY, block);

                var parameters = layers.getVertexParameter(model.getLayer());
                for (var shape : model) {
                    shape.build(parameters, stack);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        stack.popMatrix();
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
