package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.App;
import fr.formiko.kokcinelo.Controller;
import fr.formiko.kokcinelo.model.Creature;
import fr.formiko.kokcinelo.model.MapItem;
import fr.formiko.kokcinelo.tools.KProgressBar;
import fr.formiko.kokcinelo.tools.KTexture;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.AnimationStateListener;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.SkeletonActor;

/**
 * {@summary Actor that represent a MapItem.}
 * 
 * @author Hydrolien
 * @version 1.3
 * @since 0.1
 */
public class MapItemActor extends SkeletonActor {
    private static Map<String, TextureRegion> textureRegionMap;
    private @Null String textureName;
    private MapItem mapItem;
    // private static boolean showZone = false;
    private ShapeRenderer shapeRenderer;
    private KProgressBar progressBar;
    /**
     * {@summary Main constructor.}
     * 
     * @param textureName name of the texture to use to represent this Actor
     * @param mapItem     MapItem represent by this
     */
    public MapItemActor(@Null String textureName, MapItem mapItem) {
        this.textureName = textureName;
        this.mapItem = mapItem;

        if (textureName == null) {
            return;
        }

        if (textureRegionMap == null) {
            textureRegionMap = new HashMap<>();
        }
        if (!textureRegionMap.containsKey(textureName) && Gdx.files != null) {
            FileHandle file = Gdx.files.internal("images/Creatures/" + textureName + ".png");
            if (file.exists()) {
                textureRegionMap.put(textureName, new TextureRegion(new KTexture(file)));
            }
        }
        if (getTextureRegion() != null) {
            setSize(getTextureRegion().getRegionWidth(), getTextureRegion().getRegionHeight());
            setOrigin(Align.center);
        }

        if (Controller.getController() != null && Controller.getController().getAssets() != null
                && Controller.getController().getAssets().getSkeletonData(textureName) != null) {
            Skeleton skeleton = new Skeleton(Controller.getController().getAssets().getSkeletonData(textureName));

            skeleton.setPosition(getWidth() / 2, getHeight() / 2);
            SkeletonRenderer skeletonRenderer = new SkeletonRenderer(); // TODO maybe we can use a static one
            skeletonRenderer.setPremultipliedAlpha(true);

            AnimationStateData stateData = new AnimationStateData(Controller.getController().getAssets().getSkeletonData(textureName));
            // It do a mix between walk and default animation
            // stateData.setMix("walk", "default", 5f);
            // stateData.setMix("default", "walk", 2f);
            // stateData.setMix("hit", "hit", 1f);

            AnimationState animationState = new AnimationState(stateData);
            // different track index = animation are play at the same time, same track index = animation are play one after the other
            if (mapItem instanceof Creature) {
                try {
                    animationState.addAnimation(0, "walk", true, 0);
                } catch (IllegalArgumentException e) {
                    try {
                        animationState.addAnimation(0, "fly", true, 0);
                    } catch (IllegalArgumentException e2) {
                        App.log(2, "No walk or fly animation found for " + textureName + " skeleton");
                    }
                }
                animationState.addAnimation(1, "default", true, 0);
            }

            setRenderer(skeletonRenderer);
            setSkeleton(skeleton);
            setAnimationState(animationState);

            // addDelay();
        }
    }

    /**
     * The first animation will be play from a random time.
     * It is used so that all Creature of same type don't move at the same time.
     */
    private void addDelay() {
        // TODO not working as espected
        float delay = (float) Math.random() * getAnimationState().getCurrent(0).getAnimation().getDuration() / 4;
        getAnimationState().getCurrent(0).setAnimationStart(delay);
        getAnimationState().addListener(new AnimationStateListener() {
            @Override
            public void complete(TrackEntry entry) {
                getAnimationState().getCurrent(0).setAnimationStart(0);
                getAnimationState().removeListener(this);
            }
            @Override
            public void start(TrackEntry entry) {
                // Nothing to do
            }
            @Override
            public void interrupt(TrackEntry entry) {
                // Nothing to do
            }
            @Override
            public void end(TrackEntry entry) {
                // Nothing to do
            }
            @Override
            public void dispose(TrackEntry entry) {
                // Nothing to do
            }
            @Override
            public void event(TrackEntry entry, Event event) {
                // Nothing to do
            }
        });
    }
    /**
     * {@summary Secondary constructor with a null texture but a fix size.}
     * 
     * @param mapItem MapItem represent by this
     * @param width   width of the actor
     * @param height  height of the actor
     */
    public MapItemActor(MapItem mapItem, int width, int height) {
        this(null, mapItem);
        setSize(width, height);
        setOrigin(Align.center);
    }
    /**
     * {@summary Draw this actor texture and if showZone draw all debug info.}
     * Debug info are represent as circle for visionRadius &#38; hitRadius.
     * 
     * @param batch       batch were to draw
     * @param parentAlpha alpha of the parent to draw at same alpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getTextureRegion() == null && getSkeleton() == null) {
            return;
        }
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if (getSkeleton() != null) {
            drawSkeleton(batch, parentAlpha, color);
        } else {
            batch.draw(getTextureRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
                    getRotation());
        }

        if (mapItem instanceof Creature) {
            drawLifePoint(batch);
        }

        if (mapItem instanceof Creature c && getDebug() && c.getVisionRadius() > 0) {
            drawDebugCircles(batch, parentAlpha);
        }
    }

    /**
     * {@summary Draw life point of the Creature.}
     * 
     * @param batch batch were to draw
     */
    private void drawLifePoint(Batch batch) {
        Creature c = (Creature) mapItem;
        float mlp = c.getMaxLifePoints();
        if (mlp > 0) { // if is a Creature witch life point matter
            float lp = c.getLifePoints();
            if (progressBar == null) {
                progressBar = new KProgressBar((int) (mlp * 1.5f), -1, Color.GREEN, new Color(1, 0, 0, 0.5f), Color.BLACK);
            }
            progressBar.setProgress(lp / mlp);
            progressBar.setPosition(getCenterX() - progressBar.getWidth() / 2,
                    getCenterY() + mapItem.getHitRadius() + progressBar.getHeight());
            progressBar.draw(batch, 1f);
        }
    }

    /**
     * {@summary Draw debug circles for visionRadius and hitRadius.}
     * 
     * @param batch       batch were to draw
     * @param parentAlpha alpha of the parent to draw at same alpha
     */
    public void drawDebugCircles(Batch batch, float parentAlpha) {
        batch.end();
        Creature c = (Creature) mapItem;
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        if (GameScreen.getCamera() != null) {
            shapeRenderer.setProjectionMatrix(GameScreen.getCamera().combined);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(new Color(0f, 0f, 1f, parentAlpha * 1f));
            shapeRenderer.circle(getCenterX(), getCenterY(), c.getVisionRadius());
            shapeRenderer.setColor(new Color(1f, 0f, 0f, parentAlpha * 1f));
            shapeRenderer.circle(getCenterX(), getCenterY(), c.getHitRadius());
            shapeRenderer.end();
        }
        Gdx.gl.glDisable(GL30.GL_BLEND);
        batch.begin();
    }

    /**
     * {@summary Draw skeleton.}
     * 
     * @param batch       batch were to draw
     * @param parentAlpha alpha of the parent to draw at same alpha
     * @param color       color of the parent to draw at same color
     */
    public void drawSkeleton(Batch batch, float parentAlpha, Color color) {
        // blend need to be called before draw
        int blendSrc = batch.getBlendSrcFunc();
        int blendDst = batch.getBlendDstFunc();
        int blendSrcAlpha = batch.getBlendSrcFuncAlpha();
        int blendDstAlpha = batch.getBlendDstFuncAlpha();

        float oldAlpha = getSkeleton().getColor().a;
        getSkeleton().getColor().a *= parentAlpha;

        getSkeleton().setPosition(getCenterX(), getCenterY());
        getSkeleton().updateWorldTransform();
        getRenderer().draw(batch, getSkeleton());

        if (getResetBlendFunction())
            batch.setBlendFunctionSeparate(blendSrc, blendDst, blendSrcAlpha, blendDstAlpha);

        color.a = oldAlpha;
    }

    /**
     * {@summary Update this actor.}
     * It update skeleton to the same rotation and scale than the actor.
     * 
     * @param delta time since last update
     */
    @Override
    public void act(float delta) {
        if (getSkeleton() != null) {
            getSkeleton().findBone("root").setRotation(getRotation());
            getSkeleton().setScale(getScaleX(), getScaleY());
            if (mapItem instanceof Creature c && getAnimationState() != null && getAnimationState().getCurrent(0) != null) {
                getAnimationState().getCurrent(0).setTimeScale((c).getCurrentSpeed() * 0.3f * c.getAnimationSpeedMultiplier());
            }
            if (mapItem instanceof Creature && ((Creature) mapItem).getCurrentSpeed() > 0) {
                super.act(delta);
            }
        }
    }
    // // Override setter Is not enoth to update rotation and scale as it is done in act()
    // @Override
    // public void setRotation(float degrees) {
    // super.setRotation(degrees);
    // if (getSkeleton() != null) {
    // getSkeleton().findBone("root").setRotation(degrees);
    // }
    // }
    // @Override
    // public void setScale(float scaleX, float scaleY) {
    // super.setScale(getScaleX(), getScaleY());
    // if (getSkeleton() != null) {
    // getSkeleton().setScale(getScaleX(), getScaleY());
    // }
    // }
    /**
     * {@summary Standard toString that return important vars as String.}
     */
    @Override
    public String toString() {
        return "MapItemActor " + "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + ", " + getRotation() + ", "
                + getOriginX() + ", " + getOriginY() + ", " + getScaleX() + ", " + getScaleY() + "]";
    }

    // personaliseds functions -------------------------------------------------

    public void setZoom(float zoom) { setScale(zoom, zoom); }
    public float getZoom() { return getScaleX(); }
    /**
     * {@summary Set center loaction to a random loaction}
     * 
     * @param maxX max value of x
     * @param maxY max value of y
     */
    public void setRandomLoaction(float maxX, float maxY) {
        setCenterX((float) Math.random() * maxX);
        setCenterY((float) Math.random() * maxY);
    }
    public float getCenterX() { return getX() + getWidth() / 2; }
    public float getCenterY() { return getY() + getHeight() / 2; }
    public Vector2 getCenter() { return new Vector2(getCenterX(), getCenterY()); }
    public void setCenterX(float x) { setX(x - getWidth() / 2); }
    public void setCenterY(float y) { setY(y - getHeight() / 2); }
    /**
     * {@summary Set center location.}
     */
    public void setCenter(float x, float y) {
        setCenterX(x);
        setCenterY(y);
    }
    /**
     * {@summary Move in x &#38; y}
     * 
     * @param x x
     * @param y y
     */
    public void translate(float x, float y) {
        setX(getX() + x);
        setY(getY() + y);
    }
    /**
     * {@summary Move in the facing direction.}
     * 
     * @param distance distance to move
     */
    public void moveFront(float distance) {
        float facingAngle = getRotation() + 90;
        translate((float) (distance * Math.cos(Math.toRadians(facingAngle))), (float) (distance * Math.sin(Math.toRadians(facingAngle))));
    }

    /**
     * {@summary move Creature location between the rectangle 0,0,maxX,maxY if needed.}
     * 
     * @param maxX the max x for the creature
     * @param maxY the max y for the creature
     * @return true if Creature have been move
     */
    public boolean moveIn(float maxX, float maxY) {
        boolean haveMove = false;
        if (getCenterX() > maxX) {
            setCenterX(maxX);
            haveMove = true;
        } else if (getCenterX() < 0) {
            setCenterX(0f);
            haveMove = true;
        }
        if (getCenterY() > maxY) {
            setCenterY(maxY);
            haveMove = true;
        } else if (getCenterY() < 0) {
            setCenterY(0f);
            haveMove = true;
        }
        return haveMove;
    }

    /**
     * {@summary Animate the creature.}
     * 
     * @param animationName Name of the animation
     * @param animationId   Id of the animation. It can be the same as an other animation that need to stop before starting this one.
     */
    public void animate(String animationName, int animationId) {
        if (getAnimationState() != null) {
            App.log(0, "animate " + mapItem.getId() + " with " + animationName + " " + animationId);
            try {
                // If there is not a same animation already queued
                if (getAnimationState().getCurrent(animationId) == null || getAnimationState().getCurrent(animationId).getNext() == null) {
                    // Add new animation
                    TrackEntry te = getAnimationState().addAnimation(animationId, animationName, false, 0);
                    switch (animationName) { // Diferent speeds for some animations
                        case "hit":
                            te.setTimeScale(2f);
                            break;
                    }
                } else {
                    App.log(0, "Don't animate because there is already a queued event.");
                }
            } catch (IllegalArgumentException e) {
                // Some animation may not be available for some creatures
                App.log(0, "Animation " + animationName + " not available for " + mapItem.getId());
            }
        }
    }

    // private -----------------------------------------------------------------
    private TextureRegion getTextureRegion() { return textureRegionMap.get(textureName); }

    // private Pixmap getDefaultPixmap(int width, int height) {
    // Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
    // pixmap.setColor(new Color(1, 0, 1, 1));
    // pixmap.fillRectangle(0, 0, width, height);
    // return pixmap;
    // }

    /** Stores information needed by the view for a character state. */
    public static class StateView {
        Animation animation;
        boolean loop;
        // Controls the start frame when changing from another animation to this animation.
        ObjectFloatMap<Animation> startTimes = new ObjectFloatMap<>();
        float defaultStartTime;
    }
}
