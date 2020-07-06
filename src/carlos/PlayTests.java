package carlos;

import engine.core.MarioGame;
import engine.core.MarioResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PlayTests {
    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
        }
        return content;
    }

    public static void main(String[] args) {
        MarioGame game = new MarioGame();

        printResults(game.runGame(new agents.carlosTello.Agent("MarioDatasetSmallJump"), getLevel("./levels/carlosTello/plainLevel.txt"), 5, 0, true));
        printResults(game.runGame(new agents.carlosTello.Agent("MarioDatasetHighJump"), getLevel("./levels/carlosTello/plainLevel.txt"), 5, 0, true));
        printResults(game.runGame(new agents.carlosTello.Agent("MarioDatasetSmallBlock"), getLevel("./levels/carlosTello/jumpBlockLevel.txt"), 20, 0, true));
        printResults(game.runGame(new agents.carlosTello.Agent("MarioDatasetEnemies"), getLevel("./levels/carlosTello/jumpEnemyLevel.txt"), 20, 0, true));
        printResults(game.runGame(new agents.carlosTello.Agent("MarioDatasetHole"), getLevel("./levels/carlosTello/jumpHoleLevel.txt"), 20, 0, true));
        printResults(game.runGame(new agents.carlosTello.Agent("MarioDatasetComplete"), getLevel("./levels/carlosTello/completeSimpleTrainLevel.txt"), 20, 0, true));
        printResults(game.runGame(new agents.carlosTello.Agent("MarioDatasetComplete"), getLevel("levels/original/lvl-1.txt"), 20, 0, true));
//
//        printResults(game.runGame(new agents.carlosTello.Recorder(new agents.robinBaumgarten.Agent(),"MarioRecordedDataset"), getLevel("levels/original/lvl-1.txt"), 20, 0, true));
//        printResults(game.runGame(new agents.carlosTello.LegacyAgent("MarioRecordedDataset"), getLevel("levels/original/lvl-1.txt"), 20, 0, true));

//        printResults(game.playGame(getLevel("levels/original/lvl-1.txt"), 200, 0));
    }
}
