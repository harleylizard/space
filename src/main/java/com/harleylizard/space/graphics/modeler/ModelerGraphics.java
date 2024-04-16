package com.harleylizard.space.graphics.modeler;

import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Shader;
import com.harleylizard.space.graphics.UniformBuffer;
import com.harleylizard.space.graphics.light.Lights;
import com.harleylizard.space.graphics.vertex.Layers;
import com.harleylizard.space.modeler.Modeler;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL41.glProgramUniform1i;
import static org.lwjgl.opengl.GL45.glNamedBufferData;
import static org.lwjgl.system.MemoryUtil.memCalloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class ModelerGraphics {
    private final ProgramPipeline pipeline = new ProgramPipeline.Builder()
            .useProgram(Shader.FRAGMENT, "shaders/model_fragment.glsl")
            .useProgram(Shader.VERTEX, "shaders/model_vertex.glsl")
            .useBuffer(Shader.FRAGMENT, "lightsBuffer", 0)
            .build();

    private final int fragment = pipeline.getProgram(Shader.FRAGMENT);

    private final int sizeLocation = glGetUniformLocation(fragment, "size");

    private final int lightsBuffer = pipeline.getBuffer("lightsBuffer");

    private final Lights lights = new Lights();

    private final Layers layers = new Layers();

    private final ModelerBackground background = ModelerBackground.of("modeler_background.block");

    private float angle;

    {
        background.upload(layers);
        layers.upload();
        lights.add(1.0F, 1.0F, 1.0F, 1.0F, 1.0F).move(-15.5F, -4.0F, -15.5F);
    }

    public void draw(Modeler modeler, Matrix4f projection, Matrix4f view, Matrix4f model) {
        view.identity();
        view.translate(0.0F, 0.0F, -7.0F);
        view.rotate((float) Math.toRadians(angle += 0.25F), 0.0F, 1.0F, 0.0F);
        view.translate(-15.5F, -4.0F, -15.5F);

        model.identity();

        UniformBuffer.uploadMatrices(projection, view, model);

        pipeline.bind();
        uploadLights();
        layers.draw();
        ProgramPipeline.unbind();
    }

    private void uploadLights() {
        var size = lights.size();
        glProgramUniform1i(fragment, sizeLocation, size);

        var buffer = memCalloc((8 * 4) * size);

        for (var light : lights) {
            var x = light.getX();
            var y = light.getY();
            var z = light.getZ();

            var r = light.getR();
            var g = light.getG();
            var b = light.getB();
            buffer.putFloat(x).putFloat(y).putFloat(z).putFloat(1.0F);
            buffer.putFloat(r).putFloat(g).putFloat(b).putFloat(1.0F);
        }
        buffer.position(0);

        glNamedBufferData(lightsBuffer, buffer, GL_STREAM_DRAW);
        memFree(buffer);
    }
}
