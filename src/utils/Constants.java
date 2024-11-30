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
        
        public static final int SPAWN = 100;

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
                    return 6;
                case DEAD:
                    return 6;
                default:
                    return 1;
            }
        }
    }

    public static class EnemyConstant {
        public static final int BOXING = 0;
        public static final int SWORD = 1;
        public static final int ARCHER = 2;
        public static final int AXE = 3;
        
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int DOGGO_HEIGHT_DEFAULT = 128;
        public static final int DOGGO_HEIGHT = (int) (DOGGO_HEIGHT_DEFAULT / 4.0f * Game.SCALE);

        public static final int DOGGO_DRAWOFFSET_X = (int) (0 * Game.SCALE);
        public static final int DOGGO_DRAWOFFSET_Y = (int) (0 * Game.SCALE);

        public static int GetDoggoWidth(int enemyType) {
            switch (enemyType) {
                case AXE:
                    return 184;
                case ARCHER, SWORD, BOXING:
                default:
                    return 128;
            }
        }

        public static int CalculateDoggoWidth(int enemyType) {
            return (int) (GetDoggoWidth(enemyType) / 4.0f * Game.SCALE);
        }


        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyState) {
                case IDLE, RUNNING:
                    return 8;
                case HIT:
                    return 6;
                case DEAD:
                    return 10;
                case ATTACK:
                    switch (enemyType) {
                        case ARCHER, AXE:
                            return 8;
                        case BOXING:
                            return 11;
                        case SWORD:
                            return 10;
                        default:
                            break;
                    }
                default:
                    break;
            }
            return 0;
        }

        public static int getMaxHealth(int enemyType) {
            switch (enemyType) {
                case ARCHER:
                    return 8;
                case BOXING, AXE:
                    return 24;
                case SWORD:
                    return 15;
                default:
                    return 1;
            }
        }

        public static int getEnemyDamage(int enemyType) {
            switch (enemyType) {
                case ARCHER, SWORD:
                    return 15;
                case BOXING:
                    return 10;
                case AXE:
                    return 25;
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
