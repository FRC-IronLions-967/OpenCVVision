import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.wpi.cscore.VideoSource;

public class Init {
    private static String configFile = "/boot/frc.json";

    @SuppressWarnings("MemberName")
    public static class CameraConfig {
        public String name;
        public String path;
        public JsonObject config;
        public JsonElement streamConfig;
    }

    public static int team;
    public static boolean server;
    public static List<CameraConfig> cameraConfigs = new ArrayList<>();
    public static List<SwitchedCameraConfig> switchedCameraConfigs = new ArrayList<>();
    public static List<VideoSource> cameras = new ArrayList<>();

    public static boolean readCameraConfig(JsonObject config) {
        CameraConfig cam = new CameraConfig();

        // name
        JsonElement nameElement = config.get("name");
        if (nameElement == null) {
            parseError("could not read camera name");
            return false;
        }
        cam.name = nameElement.getAsString();

        // path
        JsonElement pathElement = config.get("path");
        if (pathElement == null) {
            parseError("camera '" + cam.name + "': could not read path");
            return false;
        }
        cam.path = pathElement.getAsString();

        // stream properties
        cam.streamConfig = config.get("stream");

        cam.config = config;

        cameraConfigs.add(cam);
        return true;
    }

    @SuppressWarnings("MemberName")
    public static class SwitchedCameraConfig {
        public String name;
        public String key;
    };

    public static void parseError(String str) {
        System.err.println("config error in '" + configFile + "': " + str);
    }

    public static boolean readSwitchedCameraConfig(JsonObject config) {
        SwitchedCameraConfig cam = new SwitchedCameraConfig();
        // name
        JsonElement nameElement = config.get("name");
        if (nameElement == null) {
            parseError("could not read switched camera name");
            return false;
        }
        cam.name = nameElement.getAsString();

        // path
        JsonElement keyElement = config.get("key");
        if (keyElement == null) {
            parseError("switched camera '" + cam.name + "': could not read key");
            return false;
        }
        cam.key = keyElement.getAsString();

        switchedCameraConfigs.add(cam);
        return true;
    }

    /**
     * Read configuration file.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public static boolean readConfig() {
        // parse file
        JsonElement top;
        try {
            top = new JsonParser().parse(Files.newBufferedReader(Paths.get(configFile)));
        } catch (IOException ex) {
            System.err.println("could not open '" + configFile + "': " + ex);
            return false;
        }

        // top level must be an object
        if (!top.isJsonObject()) {
            parseError("must be JSON object");
            return false;
        }
        JsonObject obj = top.getAsJsonObject();

        // team number
        JsonElement teamElement = obj.get("team");
        if (teamElement == null) {
            parseError("could not read team number");
            return false;
        }
        team = teamElement.getAsInt();

        // ntmode (optional)
        if (obj.has("ntmode")) {
            String str = obj.get("ntmode").getAsString();
            if ("client".equalsIgnoreCase(str)) {
                server = false;
            } else if ("server".equalsIgnoreCase(str)) {
                server = true;
            } else {
                parseError("could not understand ntmode value '" + str + "'");
            }
        }

        // cameras
        JsonElement camerasElement = obj.get("cameras");
        if (camerasElement == null) {
            parseError("could not read cameras");
            return false;
        }
        JsonArray cameras = camerasElement.getAsJsonArray();
        for (JsonElement camera : cameras) {
            if (!readCameraConfig(camera.getAsJsonObject())) {
                return false;
            }
        }

        if (obj.has("switched cameras")) {
            JsonArray switchedCameras = obj.get("switched cameras").getAsJsonArray();
            for (JsonElement camera : switchedCameras) {
                if (!readSwitchedCameraConfig(camera.getAsJsonObject())) {
                    return false;
                }
            }
        }

        return true;
    }
}