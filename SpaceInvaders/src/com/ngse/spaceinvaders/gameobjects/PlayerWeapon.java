package com.ngse.spaceinvaders.gameobjects;

//this class is intended to be included in the Player class
//fire() will create new bullets and return a PlayerBullet[]
//, which is intended to be passed up to wherever the list of PlayerBullets is
//upgrade() and downgrade() will raise and lower the weapon's level respectively
//setMinumum() will reset the weapon to its weakest level and setMaximum() will set it to the strongest level
//getWeaponType() and setWeaponType() will return and set the weapon's current type, respectively

public class PlayerWeapon {
	//superficial change1
	
	
	//defines the number values of each weapon type
	//the order of the weapons' values is the order in which they will be obtained through upgrading
	//nothing can be less than BASIC or more than ULTIMATE
	public static final int BASIC = 0;
	public static final int THREE_WAY = 1;
	public static final int ULTIMATE = 2;
	
	//type is the integer specifying weapon type
	//it is automatically set to basic on initialization
	protected int type = BASIC;
	
	protected Player player;
	
	public PlayerWeapon(Player player) {
		this.player = player;
	}
	
	public void upgrade() {
		if (type < ULTIMATE) type++;
	}
	
	public void downgrade() {
		if (type > BASIC) type--;
	}
	
	//for resetting the weapon to its original state
	//may be used for when player is hit
	public void setMinimum() {
		type = BASIC;
	}
	
	//for setting the weapon to strongest state
	public void setMaximum() {
		type = ULTIMATE;
	}
	
	public void setWeaponType(int type) {
		if (!(type < BASIC || type > ULTIMATE)) this.type = type;
	}
	
	public int getWeaponType() {
		return type;
	}
	
	public PlayerBullet[] fire() {
		PlayerBullet[] bullets = null;
		switch (type) {
		case BASIC:
			bullets = fireBasic();
			break;
		case THREE_WAY:
			bullets = fireThreeWay();
			break;
		case ULTIMATE:
			bullets = fireUltimate();
			break;
		}
		
		return bullets;
	}
	
	
	protected PlayerBullet[] fireBasic() {
		PlayerBullet[] bullets = new PlayerBullet[1];
		double x = player.getX();
		double y = player.getY();
		int width = player.getImage().getWidth();
		
		bullets[0] = new PlayerBullet(x + width / 2, y, .5, Math.PI / 2);
		return bullets;
	}
	
	protected PlayerBullet[] fireThreeWay() {
		PlayerBullet[] bullets = new PlayerBullet[3];
		double x = player.getX();
		double y = player.getY();
		int width = player.getImage().getWidth();
		
		bullets[0] = new PlayerBullet(x + width / 2, y, .5, Math.PI / 2);
		bullets[1] = new PlayerBullet(x+width, y, .5, Math.PI / 4);
		bullets[2] = new PlayerBullet(x, y, .5, 3 * Math.PI / 4);
		return bullets;
	}

	protected PlayerBullet[] fireUltimate() {
		PlayerBullet[] bullets = new PlayerBullet[8];
		double x = player.getX();
		double y = player.getY();
		int width = player.getImage().getWidth();
		int height = player.getImage().getHeight();
		
		bullets[0] = new PlayerBullet(x + width / 2, y, .5, Math.PI / 2);
		bullets[1] = new PlayerBullet(x+width, y, .5, Math.PI / 4);
		bullets[2] = new PlayerBullet(x, y, .5, 3 * Math.PI / 4);
		bullets[3] = new PlayerBullet(x, y + height/2, .5, Math.PI);
		bullets[4] = new PlayerBullet(x, y + height, .5, -3 * Math.PI / 4);
		bullets[5] = new PlayerBullet(x + width, y + height / 2, .5, 0);
		bullets[6] = new PlayerBullet(x + width, y + height, .5, -Math.PI / 4);
		bullets[7] = new PlayerBullet(x + width / 2, y + height, .5, -Math.PI / 2);
		
		return bullets;
	}
}
