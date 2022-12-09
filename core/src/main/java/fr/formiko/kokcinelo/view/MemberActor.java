package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.model.MapItem;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;

public class MemberActor extends MapItemActor {
    private List<Action> listAction;
    private List<MemberActor> listMia;

    public MemberActor(String bodyTextureName, MapItem mapItem) {
        super(bodyTextureName, mapItem);
        listAction = new ArrayList<Action>();
        listMia = new ArrayList<MemberActor>();
    }

    public void addAvailableAction(Action action) { listAction.add(action); }
    public void addMember(MemberActor actor) {
        addActor(actor);
        listMia.add(actor);
    }

    public void startAction(int actionId) {
        if (listAction.size() > actionId && actionId >= 0) {
            addAction(listAction.get(actionId));
        }
        for (MemberActor ma : listMia) {
            ma.startAction(actionId);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (getDebug()) {
            batch.end();
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(VideoScreen.getCamera().combined);
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(new Color(0f, 0f, 1f, parentAlpha * 1f));
            MapItemActor parent = this;
            float xParent = 0;
            float yParent = 0;
            parent = (MapItemActor) parent.getParent();
            xParent += parent.getCenterX();
            yParent += parent.getCenterY();
            // while (parent.getParent() != null && parent.getParent() instanceof MapItemActor) {
            // parent = (MapItemActor) parent.getParent();
            // xParent += parent.getCenterX();
            // yParent += parent.getCenterY();
            // }
            shapeRenderer.circle(xParent + (getOriginX() - getWidth() / 2) / 2, yParent + (getOriginY() - getHeight() / 2) / 2, (float) 10);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL30.GL_BLEND);
            batch.begin();
        }
    }

    public void addToOrigin(float x, float y) { setOrigin(getCenterX() + x, getCenterY() + y); }
}
