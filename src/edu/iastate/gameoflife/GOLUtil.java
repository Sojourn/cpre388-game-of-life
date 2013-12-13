package edu.iastate.gameoflife;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.MediaStore.Files;

public class GOLUtil {
	private static final String width_tag = "width";
	private static final String height_tag = "height";
	private static final String cells_tag = "cells";

	public static JSONObject serialize(GOLModel model) {
		try {
			JSONArray cells = new JSONArray();
			for (int y = 0; y < model.getHeight(); y++) {
				for (int x = 0; x < model.getWidth(); x++) {
					cells.put(model.getCell(x, y));
				}
			}

			JSONObject value = new JSONObject();
			value.put(width_tag, model.getWidth());
			value.put(height_tag, model.getHeight());
			value.put(cells_tag, cells);
			return value;
		} catch (JSONException e) {
			return null;
		}
	}

	public static GOLModel deserialize(JSONObject value) {
		try {
			int width = value.getInt(width_tag);
			int height = value.getInt(height_tag);

			JSONArray cells = value.getJSONArray(cells_tag);
			if (cells.length() != width * height)
				return null;

			GOLModel model = new GOLModel(width, height);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					model.setCell(x, y, cells.getBoolean(y * width + x));
				}
			}

			return model;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static String readFile(File file) {
		
		try {
			Scanner in = new Scanner(file);
			return in.useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			return null;
		}
	}
}
