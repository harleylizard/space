package com.harleylizard.space.graphics;

import com.harleylizard.space.Resources;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;

import java.io.IOException;

import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.opengl.GL45.glCreateProgramPipelines;

public final class ProgramPipeline {
    private final int pipeline;

    private final Object2IntMap<Shader> programs;
    private final Object2IntMap<String> buffers;

    private ProgramPipeline(int pipeline, Object2IntMap<Shader> programs, Object2IntMap<String> buffers) {
        this.pipeline = pipeline;
        this.programs = programs;
        this.buffers = buffers;
    }

    public void bind() {
        glBindProgramPipeline(pipeline);
    }

    public static void unbind() {
        glBindProgramPipeline(0);
    }

    public static final class Builder {
        private final int pipeline = glCreateProgramPipelines();

        private final Object2IntMap<Shader> programs = new Object2IntArrayMap<>();
        private final Object2IntMap<String> buffers = new Object2IntArrayMap<>();

        public Builder useProgram(Shader shader, String path) {
            try {
                var program = glCreateShaderProgramv(shader.getType(), Resources.readString(path));
                glUseProgramStages(pipeline, shader.getBit(), program);

                programs.put(shader, program);
                return this;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public ProgramPipeline build() {
            glValidateProgramPipeline(pipeline);
            return new ProgramPipeline(pipeline, Object2IntMaps.unmodifiable(programs), Object2IntMaps.unmodifiable(buffers));
        }
    }
}
