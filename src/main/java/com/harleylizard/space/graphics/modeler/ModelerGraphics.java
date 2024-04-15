package com.harleylizard.space.graphics.modeler;

import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Shader;
import com.harleylizard.space.graphics.light.Lights;
import com.harleylizard.space.modeler.Modeler;

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

    private final ModelerBackground background = ModelerBackground.of("modeler_background.block", lights);

    public void draw(Modeler modeler) {
        pipeline.bind();
        uploadLights();
        background.draw();
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
            buffer.putFloat(r).putFloat(g).putFloat(b).putFloat(0.25F);
        }
        buffer.position(0);

        glNamedBufferData(lightsBuffer, buffer, GL_STREAM_DRAW);
        memFree(buffer);
    }
}
