package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;  // Player's position on the screen (X-coordinate)
    public final int screenY;   // Player's position on the screen (Y-coordinate)
    int haskey=0;  // counter for collected keys


    public Player(GamePanel gp, KeyHandler keyH){
        this.gp=gp;
        this.keyH=keyH;

        // calculate player screen position based on screen size and tile size
        screenX=gp.screenWidth/2-(gp.tileSize/2);
        screenY=gp.screenHeight/2-(gp.tileSize/2);

        solidArea=new Rectangle();

        //  define collision area dimension
        solidArea.x=8;
        solidArea.y=16;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;
        solidArea.width=32;
        solidArea.height=32;

        setDefaultValue();
        getPlayerImage();
    }

    public void setDefaultValue(){
        worldX=gp.tileSize*23;
        worldY=gp.tileSize*21;
        speed=4;
        direction="down";

    }


    public  void getPlayerImage(){
        try{
            up1= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_up_1.png")));
            up2= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_up_2.png")));
            down1= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_down_1.png")));
            down2= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_down_2.png")));
            left1= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_left_1.png")));
            left2= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_left_2.png")));
            right1= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_right_1.png")));
            right2= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/src/main/resources/player/boy_right_2.png")));

//            up1=ImageIO.read(getClass().getResourceAsStream(""));
//            up2=ImageIO.read(getClass().getResourceAsStream("resource/src/player/boy_up_2.png"));
//            down1=ImageIO.read(getClass().getResourceAsStream("resource/src/player/boy_down_2.png"));
//            down2=ImageIO.read(getClass().getResourceAsStream("resource/src/player/boy_down_2.png"));
//            left1=ImageIO.read(getClass().getResourceAsStream("resource/src/player/boy_left_1.png"));
//            left2=ImageIO.read(getClass().getResourceAsStream("resource/src/player/boy_left_2.png"));
//            right1=ImageIO.read(getClass().getResourceAsStream("resource/src/player/boy_right_1.png"));
//            right2=ImageIO.read(getClass().getResourceAsStream("resource/src/player/boy_right_2.png"));
        }
        catch (IOException e){

            e.printStackTrace();
        }
    }

    public void update(){

//        Check if any movement keys are pressed

        if (keyH.upPressed ||keyH.rightPressed||keyH.leftPressed|| keyH.downPressed ){

            if (keyH.upPressed){
                direction="up";
            } else if (keyH.downPressed) {
                direction="down";
            } else if (keyH.leftPressed) {
                direction="left";
            } else if (keyH.rightPressed) {
                direction="right";
            }

            collision=false;
            gp.collisionChecker.checkTile(this);  // Check for tile collisions


            //Check for interactions with objects
        int objIndex=  gp.collisionChecker.checkObject(this,true);

          pickUpobjectIndex(objIndex);  // handle Object Interaction

            //Move the player if no collision is detected

            if (collision==false){
                switch (direction){
                    case "up":
                        worldY-=speed;
                        break;
                    case "down":
                        worldY+=speed;
                        break;
                    case "left":
                        worldX-=speed;
                        break;
                    case "right":
                        worldX+=speed;
                        break;


                }
            }
            spriteCounter++;
            if(spriteCounter>12){
                if (spriteNum==1){
                    spriteNum=2;
                } else if (spriteNum==2) {
                    spriteNum=1;
                }
                spriteCounter=0;
            }
        }

    }

    private void pickUpobjectIndex(int objIndex) {

        //  Handle interactions with objects in the game
        if (objIndex!=999){
            String objectName=gp.obj[objIndex].name;

            switch (objectName){
                case "Key":
                    haskey++;
                    gp.obj[objIndex]=null;
                    System.out.println("Key"+haskey);
                    break;
                case "Door":
                    if (haskey>0){
                        gp.obj[objIndex]=null;
                        haskey--;
                    }
                    System.out.println("Door"+haskey);
                    break;
                case "Boots":
                    speed+=2;
                    gp.obj[objIndex]=null;
                    break;
            }
        }
    }

    BufferedImage image=null;

    public void draw(Graphics2D g2){
//        g2.setColor(Color.white);
//        g2.fillRect(x,y, gp.tileSize,gp.tileSize);


        switch (direction){
            case "up":
                if (spriteNum==1){
                    image=up1;
                }
                if (spriteNum==2){
                    image=up2;
                }

                break;
            case "down":
                if (spriteNum==1){
                    image=down1;
                }
                if (spriteNum==2){
                    image=down2;
                }
                break;
            case "left":
                if (spriteNum==1){
                    image=left1;
                }
                if (spriteNum==2){
                    image=left2;
                }

                break;
            case "right":
                if (spriteNum==1){
                    image=right1;
                }
                if (spriteNum==2){
                    image=right2;
                }

                break;

        }

        g2.drawImage(image,screenX,screenY,gp.tileSize,gp.tileSize,null);

    }

}
