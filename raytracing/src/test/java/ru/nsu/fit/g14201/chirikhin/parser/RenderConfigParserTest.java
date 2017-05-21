package ru.nsu.fit.g14201.chirikhin.parser;

import org.junit.Test;
import ru.nsu.fit.g14201.chirikhin.model.RenderSettings;
import ru.nsu.fit.g14201.chirikhin.model.RenderSettingsBuilder;
import ru.nsu.fit.g14201.chirikhin.model.SceneConfig;

import static org.junit.Assert.*;

public class RenderConfigParserTest {
    @Test
    public void getRenderSettings() throws Exception {
        String path = "C:\\Users\\userg\\Documents\\Programming\\Graphics\\g14201_Chirihin\\raytracing\\FIT_g14201_Chirikhin_Raytracing_Data\\render_config.txt";
        RenderSettings config = RenderConfigParser.getRenderSettings(path);
    }

}