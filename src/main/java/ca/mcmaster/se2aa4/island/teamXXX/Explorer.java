package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid {
    private final Logger logger = LogManager.getLogger();
    private DroneState droneState;
    private boolean foundGround = false;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}", info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        droneState = new DroneState();
        droneState.initializeDrone(direction, batteryLevel);
    }

    @Override
    public String takeDecision() {
        if (!foundGround) {
            logger.info("Scanning for ground...");
            return CreateCommand.newScanCommand().toString();
        } else {
            logger.info("Ground found, returning to base...");
            return CreateCommand.newStopCommand().toString();
        }
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n{}", response.toString(2));
        Integer cost = response.getInt("cost");
        droneState.updateBatteryLevel(cost);
        logger.info("Current battery level: {}", droneState.getBatteryLevel());

        if (response.has("extras") && response.getJSONObject("extras").has("biomes")) {
            String biome = response.getJSONObject("extras").getJSONArray("biomes").getString(0);
            if (!biome.equals("OCEAN")) {
                logger.info("Ground detected: {}", biome);
                foundGround = true;
            }
        }
    }

    @Override
    public String deliverFinalReport() {
        return "Mission Completed: Ground Detected and Returned to Base.";
    }
}