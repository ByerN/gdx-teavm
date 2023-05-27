package com.github.xpenatan.gdx.examples.tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * @author xpenatan
 */
public class FreetypeDemo implements ApplicationListener {
    SpriteBatch batch;
    BitmapFont ftFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        FileHandle fontFile = Gdx.files.internal("data/Lato-Bold.ttf");

        System.out.println("Loading Font: " + fontFile.name());
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 15;

        ftFont = generator.generateFont(parameter);
        generator.dispose();
        ftFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        if(System.getProperties().contains("not_existing_property")) {
            JsonMapper mapper = JsonMapper.builder()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .build();
            try {
                String json = mapper.writeValueAsString(
                        new JacksonTest.A(List.of(
                                new JacksonTest.B(1),
                                new JacksonTest.B(2),
                                new JacksonTest.C(1)
                        ))
                );
                System.out.println("SERIALIZED: " + json);
                JacksonTest.A result = mapper.readValue(json, JacksonTest.A.class);
                System.out.println("DESERIALIZED: " + result);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        batch.begin();
        ftFont.draw(batch, "This is a test\nAnd another line\n()����$%&/!12390#", 100, 112);
// batch.disableBlending();
        batch.draw(ftFont.getRegion(), 300, 0);
// batch.enableBlending();
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
