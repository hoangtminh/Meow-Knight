package utils;

import static main.Game.SCALE;

import main.Game;

public class Constants {
    
    public static final float GRAVITY = 0.04f * Game.SCALE;
    public static final int ANI_SPEED = 20;

    public static class Projectiles {
        public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
        public static final int CANNON_BALL_WIDTH = (int) (CANNON_BALL_DEFAULT_WIDTH * Game.SCALE);
        public static final int CANNON_BALL_HEIGHT = (int) (CANNON_BALL_DEFAULT_HEIGHT * Game.SCALE);

        public static final float SPEED = 0.8f * Game.SCALE;
    }

    public static class UI {
        public static class Environment {
            public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
            public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
            
            public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
            public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE );

            public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
            public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
            
            public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
            public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class Buttons {
            public static final int BUTTON_WIDTH_DEFAULT = 140;
            public static final int BUTTON_HEIGHT_DEFAULT = 56;
            public static final int BUTTON_WIDTH = (int) (BUTTON_WIDTH_DEFAULT * Game.SCALE);
            public static final int BUTTON_HEIGHT = (int) (BUTTON_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButton {
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int) (42 * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);

        }
    }

    public static class Direction {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int RUNNING = 0;
        public static final int IDLE = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int DODGE = 4;
        public static final int ATTACK_1 = 5;
        public static final int ATTACK_2 = 6;
        public static final int HIT = 7;
        public static final int DEAD = 8;
        

        public static int GetSpriteAmount(int playerAction) {
            switch (playerAction) {
                case RUNNING:
                    return 8;
                case IDLE:
                    return 6;
                case JUMP:
                    return 6;
                case FALLING:
                    return 6;
                case HIT:
                    return 3; 
                case ATTACK_1:
                    return 4;
                case DODGE: 
                    return 8;
                case DEAD:
                    return 6;
                default:
                    return 1;
                
            }
        }
    }

    public static class EnemyConstant {
        public static final int CRABBY = 0;
        
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;

        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);

        public static final int CRABBY_DRAWOFFSET_X = (int) (26 * Game.SCALE);
        public static final int CRABBY_DRAWOFFSET_Y = (int) (10 * Game.SCALE);


        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case CRABBY:
                switch (enemyState) {
                    case IDLE:
                        return 9;
                    case RUNNING:
                        return 6;
                    case ATTACK:
                        return 7;
                    case HIT:
                        return 4;
                    case DEAD:
                    default:
                        return 5;
                }
            }
            return 0;
        }

        public static int getMaxHealth(int enemyType) {
            switch (enemyType) {
                case CRABBY:
                    return 20;
            
                default:
                    return 1;
            }
        }

        public static int getEnemyDamage(int enemyType) {
            switch (enemyType) {
                case CRABBY:
                    return 15;
            
                default:
                    return 0;
            }
        }
    }

    public static class ObjectsConstants {
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;

        public static final int RED_POTION_VALUE = 15;
        public static final int BLUE_POTION_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * SCALE);
        public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * SCALE);
        
        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * SCALE);
        public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * SCALE);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * SCALE);
        public static final int SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * SCALE);

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * SCALE);
        public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * SCALE);

        public static int GetSpriteAmount(int objType) {
            switch (objType) {
                case RED_POTION,BLUE_POTION:
                    return 7;
                case BARREL, BOX:
                    return 8;        
                case CANNON_LEFT, CANNON_RIGHT:
                    return 7;    
                default:
                    return 0;
            }
        }
    }
}
