package com.example.democode3.features.learning.ui.registry;

import com.example.democode3.features.learning.model.LessonStep;
import com.example.democode3.features.learning.ui.renderer.LessonStepRenderer;

import java.util.HashMap;
import java.util.Map;

public class StepRegistry {

    private final Map<String,
            LessonStepRenderer> renderers =
            new HashMap<>();

    public void register(

            String type,

            LessonStepRenderer renderer
    ) {

        renderers.put(
                type,
                renderer
        );
    }

    public void render(
            LessonStep step
    ) {

        LessonStepRenderer renderer =
                renderers.get(
                        step.type
                );

        if (renderer == null) {

            return;
        }

        renderer.render(step);
    }
}