package com.remote.remote2d.test.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Animation;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.entity.component.Component;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.particles.ParticleSystem;

public class ComponentPlayer extends Component {
	
	public String idleAnimation = "";
	public String walkAnimation = "";
	public String jumpAnimation = "";
	public String fallAnimation = "";
	public String landAnimation = "";
	public boolean spriteFacesRight = true;
	public boolean particleTest = false;
	
	private PlayerState state = PlayerState.IDLE;
	private FacingState facing = FacingState.RIGHT;
	private Animation currentAnimation;
	private int stateTimer = 0;
	
	private ParticleSystem testParticles;
	
	private Vector2 velocity = new Vector2(0,0);
	private Vector2 acceleration = new Vector2(0,2);
	private Vector2 maxVelocity = new Vector2(10,-1);
		
	public ComponentPlayer(Entity entity)
	{
		super(entity);
		
		testParticles = new ParticleSystem(Remote2D.getInstance().map);
	}

	@Override
	public void tick(int i, int j, int k) {
		PlayerState oldState = state;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			if(state == PlayerState.IDLE)
				state = PlayerState.WALK;
			facing = FacingState.LEFT;
			acceleration.x = -2;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			if(state == PlayerState.IDLE)
				state = PlayerState.WALK;
			facing = FacingState.RIGHT;
			acceleration.x = 2;
		}
		
		if(!Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			acceleration.x = 0;
			velocity.x = 0;
			if(state == PlayerState.WALK)
				state = PlayerState.IDLE;
		}
		velocity = velocity.add(acceleration);
		
		if(Math.abs(velocity.x) > maxVelocity.x && maxVelocity.x != -1)
		{
			if(velocity.x > 0)
				velocity.x = maxVelocity.x;
			else
				velocity.x = -maxVelocity.x;
		}
		if(Math.abs(velocity.y) > maxVelocity.y && maxVelocity.y != -1)
		{
			if(velocity.y > 0)
				velocity.y = maxVelocity.y;
			else
				velocity.y = -maxVelocity.y;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && (state == PlayerState.WALK || state == PlayerState.IDLE))
			velocity.y -= 20;
		
		Vector2 correction = Remote2D.getInstance().map.getCorrection(entity.pos.getColliderWithDim(entity.getDim()),new Vector2(velocity.getElements()));
		velocity = velocity.add(correction);
		
		//velocity = velocity.multiply(new Vector2DF(friction,friction));
		
		entity.pos = entity.pos.add(velocity);
		
		if(velocity.y > 0)
			state = PlayerState.FALL;
		else if(velocity.y < 0)
			state = PlayerState.JUMP;
		else if(state == PlayerState.FALL)
			state = PlayerState.LAND;
		//velocity.print();
		
		float right = Remote2D.getInstance().map.camera.x+Remote2D.getInstance().displayHandler.width;
		float left = Remote2D.getInstance().map.camera.x;
		if(entity.pos.x+entity.getDim().x > right)
			Remote2D.getInstance().map.camera.x += (entity.pos.x+entity.getDim().x)-right;
		if(entity.pos.x < left)
			Remote2D.getInstance().map.camera.x -= left-entity.pos.x;
		
		if(state == PlayerState.LAND)
		{
			if(stateTimer <= 0)
			{
				state = PlayerState.IDLE;
				stateTimer = 0;
			} else
				stateTimer--;
		}
		
		if(state != oldState)
		{
			currentAnimation = Remote2D.getInstance().artLoader.getAnimation(getPath());
			if(state == PlayerState.LAND && currentAnimation != null)
				stateTimer = (int) (currentAnimation.getFramelength()*currentAnimation.getFrames().x*currentAnimation.getFrames().y);
		}
		
		if(currentAnimation != null)
			currentAnimation.flippedX = spriteFacesRight ? (facing == FacingState.LEFT) : (facing == FacingState.RIGHT);
		
		
		testParticles.pos = new Vector2(i,j).add(Remote2D.getInstance().map.camera);
		if(particleTest)
			testParticles.tick(false);
			
	}
	
	public void updateAnimation()
	{
		currentAnimation = Remote2D.getInstance().artLoader.getAnimation(getPath());
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void onEntitySpawn() {
		updateAnimation();
	}
	
	public String getPath()
	{
		switch(state)
		{
		case IDLE:
			return idleAnimation;
		case WALK:
			return walkAnimation;
		case JUMP:
			return jumpAnimation;
		case FALL:
			return fallAnimation;
		case LAND:
			return landAnimation;
		}
		return "";
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(facing == (spriteFacesRight?FacingState.LEFT:FacingState.RIGHT)){
			GL11.glPushMatrix();
			GL11.glScalef(1, 1, 1);
			//GL11.glNormal3f(entity.pos.x+entity.getDim().x/2, entity.pos.y+entity.getDim().y/2, 1);
		}
		
		if(currentAnimation != null)
		{
			Vector2 posVec = new Vector2(0,0);
			posVec.x = entity.getPos(interpolation).x+entity.getDim().x/2-currentAnimation.getSpriteDim().x/2;
			posVec.y = entity.getPos(interpolation).y+entity.getDim().y/2-currentAnimation.getSpriteDim().y/2;
			currentAnimation.render(posVec, new Vector2(currentAnimation.getSpriteDim().getElements()));
		}
		
		if(facing == (spriteFacesRight?FacingState.LEFT:FacingState.RIGHT))
			GL11.glPopMatrix();
		
		if(particleTest)
			testParticles.render();
	}

	@Override
	public void apply() {
		currentAnimation = Remote2D.getInstance().artLoader.getAnimation(idleAnimation);
	}

	@Override
	public Component clone() {
		ComponentPlayer player = new ComponentPlayer(entity);
		player.idleAnimation = idleAnimation;
		player.walkAnimation = walkAnimation;
		player.jumpAnimation = jumpAnimation;
		player.fallAnimation = jumpAnimation;
		player.landAnimation = landAnimation;
		player.particleTest = particleTest;
		player.updateAnimation();
		return player;
	}
	
	enum PlayerState
	{
		IDLE, WALK, JUMP, FALL, LAND;
	}
	
	enum FacingState
	{
		LEFT, RIGHT;
	}

}
