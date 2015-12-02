package com.ychstudio.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.ychstudio.gamesys.GameManager;

public class AStarPathFinding {
    private static final AStarPathFinding instance = new AStarPathFinding();

    // 0: walkable, 1: source, 2: target, 3: not walkable
    int[][] map; // should not modify this original map; instead create a
                 // temporary one for doing path-finding

    Node[][] nodeMap;

    private boolean isWall;

    private int mapWidth;
    private int mapHeight;

    private AStarPathFinding() {
    }

    public static AStarPathFinding getInstance() {
        return instance;
    }

    // only need to be called once the world is created
    public void init(World world, int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        map = new int[mapHeight][mapWidth];

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                isWall = false;
                world.QueryAABB(queryCallback, x + 0.2f, y + 0.2f, x + 0.8f, y + 0.8f);
                if (isWall) {
                    map[y][x] = 3;
                }
            }
        }
    }

    public Node findPath(Vector2 source, Vector2 target) {
        int[][] tmpMap = new int[mapHeight][mapWidth]; // tmp without modifying
                                                       // the original one

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                tmpMap[y][x] = map[y][x];
            }
        }

        nodeMap = new Node[mapHeight][mapWidth];

        return null;
    }

    public void printMap() {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[map.length - y - 1][x]) {
                    case 3:
                        System.out.print("X");
                        break;
                    case 2:
                        System.out.print("T");
                        break;
                    case 1:
                        System.out.print("S");
                        break;
                    case 0:
                    default:
                        System.out.print("O");
                        break;
                }
            }
            System.out.println("");
        }
    }

    private QueryCallback queryCallback = new QueryCallback() {

        @Override
        public boolean reportFixture(Fixture fixture) {
            if (fixture.getFilterData().categoryBits == GameManager.WALL_BIT) {
                isWall = true;
            }
            return false;
        }
    };

}
