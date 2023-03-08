package fr.formiko.kokcinelo.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

/**
 * {@summary Centralized place to load and store assets.}
 * 
 * @author Hydrolien
 * @version 1.1
 * @since 1.0
 */
public class Assets implements Disposable {
    private Map<String, SkeletonData> skeletonDataMap = new HashMap<String, SkeletonData>();
    private Set<TextureAtlas> texturesAtlasSet = new HashSet<TextureAtlas>();

    public Assets() {
        for (FileHandle child : Gdx.files.internal("images/Creatures/").list()) {
            if (child.isDirectory()) {
                loadAsset(child.name());
            }
        }
    }


    public SkeletonData getSkeletonData(String assetName) { return skeletonDataMap.get(assetName); }

    /**
     * Load assets for a given asset name.
     * 
     * @param assetName asset name
     */
    public void loadAsset(String assetName) {
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("images/Creatures/" + assetName + "/" + assetName + ".atlas"));
        texturesAtlasSet.add(textureAtlas);

        SkeletonJson json = new SkeletonJson(textureAtlas);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/Creatures/" + assetName + "/skeleton.json"));

        AnimationStateData animationData = new AnimationStateData(skeletonData);
        // Define default time between 2 animations
        animationData.setDefaultMix(0.1f);

        // setupState(ladybugStates, MapItem.State.run, skeletonData, "run", true);

        skeletonDataMap.put(assetName, skeletonData);
    }

    // private MapItemActor.StateView setupState(ObjectMap<MapItem.State, MapItemActor.StateView> map, MapItem.State state,
    // SkeletonData skeletonData, String name, boolean loop) {
    // MapItemActor.StateView stateView = new MapItemActor.StateView();
    // stateView.animation = skeletonData.findAnimation(name);
    // stateView.loop = loop;
    // map.put(state, stateView);
    // return stateView;
    // }

    /**
     * {@summary Dispose all thing that need to be disposed.}
     */
    @Override
    public void dispose() {
        for (TextureAtlas textureAtlas : texturesAtlasSet) {
            textureAtlas.dispose();
        }
    }
}
