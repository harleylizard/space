package com.harleylizard.space.graphics.debug;

import com.harleylizard.space.Player;
import com.harleylizard.space.block.Blocks;
import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Shader;
import com.harleylizard.space.graphics.UniformBuffer;
import com.harleylizard.space.graphics.vertex.Layers;
import com.harleylizard.space.input.Mouse;
import com.harleylizard.space.light.LightSdf;
import com.harleylizard.space.modeler.Modeler;
import org.joml.Matrix4f;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL41.glProgramUniform1i;
import static org.lwjgl.opengl.GL45.glMapNamedBuffer;
import static org.lwjgl.opengl.GL45.glNamedBufferData;
import static org.lwjgl.system.MemoryUtil.memCalloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class DebugGraphics {
    private final ProgramPipeline pipeline = new ProgramPipeline.Builder()
            .useProgram(Shader.FRAGMENT, "shaders/model_fragment.glsl")
            .useProgram(Shader.VERTEX, "shaders/model_vertex.glsl")
            .useBuffer(Shader.FRAGMENT, "lightsBuffer", 0)
            .build();
    private final int fragment = pipeline.getProgram(Shader.FRAGMENT);
    private final int sizeLocation = glGetUniformLocation(fragment, "size");
    private final int lightsBuffer = pipeline.getBuffer("lightsBuffer");

    private final LightSdf sdf = new LightSdf();
    private final Layers layers = new Layers();
    private final MutableScene scene = MutableScene.of("modeler_background.block");

    private final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();

    {
        scene.setBlock(15, 2, 15, Blocks.MODELER_LIGHT);
    }

    public void draw(Mouse mouse, Player player, Modeler modeler, Matrix4f projection, Matrix4f view, Matrix4f model) {
        if (mouse.isPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
            var pos = player.getPosition();
            var x = (int) Math.floor(pos.x + 15.5F);
            var y = (int) Math.floor(pos.y + 2.5F);
            var z = (int) Math.floor(pos.z + 15.5F);
            scene.setBlock(x, y, z, Blocks.MODELER_LIGHT);
        }

        if (!queue.isEmpty()) {
            queue.poll().run();
        }

        scene.draw(layers, sdf, this::uploadLights);

        view.identity();
        view.rotate(player.getRotation());
        view.translate(-15.5F, -4.0F, -15.5F);

        var position = player.getPosition();
        view.translate(-position.x, -position.y, -position.z);

        model.identity();

        UniformBuffer.uploadMatrices(projection, view, model);

        pipeline.bind();
        layers.draw();
        ProgramPipeline.unbind();
    }

    private void uploadLights() {
        var size = sdf.getSize();
        glProgramUniform1i(fragment, sizeLocation, size);

        var buffer = memCalloc((8 * 4) * size);

        for (var light : sdf) {
            var x = light.getX();
            var y = light.getY();
            var z = light.getZ();

            var r = light.getR();
            var g = light.getG();
            var b = light.getB();
            var a = light.getA();
            buffer.putFloat(x).putFloat(y).putFloat(z).putFloat(1.0F);
            buffer.putFloat(r).putFloat(g).putFloat(b).putFloat(a);
        }
        buffer.position(0);

        glNamedBufferData(lightsBuffer, (8 * 4) * size, GL_DYNAMIC_DRAW);
        var mapped = glMapNamedBuffer(lightsBuffer, GL_READ_WRITE);
        if (mapped != null) {
            mapped.put(buffer);
        }
        glUnmapBuffer(lightsBuffer);
        memFree(buffer);
    }
}
