package entities;

import static utilz.constants.Directions.DOWN;
import static utilz.constants.Directions.LEFT;
import static utilz.constants.Directions.RIGHT;
import static utilz.constants.Directions.UP;
//import static utilz.constants.PlayerConstants.GetSpriteAmount;
import static utilz.constants.PlayerConstants.IDLE;
import static utilz.constants.PlayerConstants.RUNNING;
import static utilz.constants.PlayerConstants.JUMP;
import static utilz.constants.PlayerConstants.FALLING;
import static utilz.HelpMethods.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity{
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 20;
	private int playerAction = IDLE;
	private boolean moving = false, jumping = false;
	private boolean left, up, right, down, jump;
	private float playerSpeed = 2.0f;
	private int[][] lvlData;
	private float xDrawOffset = 10 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;
	
	// Jumping / Gravity//
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollison = 0.5f * Game.SCALE;
	private boolean inAir = false; 
	 
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y+100, 20 * Game.SCALE, 28 * Game.SCALE);
	}
	 
	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
	}

	public void render(Graphics g) {
		g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
		drawHitbox(g);
		
	}
	
	

	private void updateAnimationTick() {
		
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
		if (aniIndex >= 4) {
			aniIndex = 0;
			jumping = false;
		}
		}
	}
	
	private void setAnimation() {
		int startAni = playerAction;
		
		if(moving)
			playerAction = RUNNING;
		else 
			playerAction = IDLE;
		
		if(inAir) {
			if(airSpeed < 0)
				playerAction = JUMP;
			else 
				playerAction = FALLING;
		}
		
		if(jumping)
			playerAction = JUMP;
		
		if(startAni != playerAction)
			resetAniTick();
		
		}
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		
		if(jump)
			jump();
		if (!left && !right && !inAir)
			return;
		
		float xSpeed = 0;
		
		if (left) 
			xSpeed = -playerSpeed;
		
		if (right)
			xSpeed = playerSpeed;
		
		if(!inAir)
			if(!IsEntityOnFloor(hitbox, lvlData)) 
				inAir = true;
		
		if(inAir) {
			
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			}else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollison;
				updateXPos(xSpeed);

			}
			
		}else {
			updateXPos(xSpeed);
		moving = true;	
		}
		
	}
	
	private void jump() {
		if(inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x +=xSpeed;
		}else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
	}
	private void loadAnimations() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			animations = new BufferedImage[4][4];
			for(int j = 0; j < animations.length; j++)
				for(int i = 0; i < animations[j].length; i++) {
					animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
				}
			
	
		
	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
	}
	
	public void resetdirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
		
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}	
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
		
	
	
}
