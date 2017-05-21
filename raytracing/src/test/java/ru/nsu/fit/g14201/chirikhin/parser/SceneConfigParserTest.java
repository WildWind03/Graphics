package ru.nsu.fit.g14201.chirikhin.parser;

import com.sun.corba.se.impl.orb.ORBConfiguratorImpl;
import org.junit.Test;
import ru.nsu.fit.g14201.chirikhin.model.SceneConfig;

import java.io.File;

import static org.junit.Assert.*;

public class SceneConfigParserTest {
    @Test
    public void getSceneConfig() throws Exception {
        String path = "C:\\Users\\userg\\Documents\\Programming\\Graphics\\g14201_Chirihin\\raytracing\\FIT_g14201_Chirikhin_Raytracing_Data\\test_config.txt";
        SceneConfig config = SceneConfigParser.getSceneConfig(new File(path));
    }

}