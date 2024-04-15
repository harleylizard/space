package com.harleylizard.space.graphics.modeler;

import com.harleylizard.space.graphics.ProgramPipeline;
import com.harleylizard.space.graphics.Shader;
import com.harleylizard.space.modeler.Modeler;

public final class ModelerGraphics {
    private final ProgramPipeline pipeline = new ProgramPipeline.Builder()
            .useProgram(Shader.FRAGMENT, "shaders/model_fragment.glsl")
            .useProgram(Shader.VERTEX, "shaders/model_vertex.glsl")
            .build();

    private final ModelerBackground background = ModelerBackground.of("modeler_background.block");

    public void draw(Modeler modeler) {
        pipeline.bind();
        background.draw();
        ProgramPipeline.unbind();
    }
}
