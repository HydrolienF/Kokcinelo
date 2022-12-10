package fr.formiko.kokcinelo.view;

import fr.formiko.kokcinelo.model.MapItem;
import java.util.ArrayList;
import java.util.List;

public class MapItemActorAnimate extends MapItemActor {
    private List<MemberActor> listMia;
    private MapItem mapItem;
    private float speed;


    public MapItemActorAnimate(String bodyTextureName, MapItem mapItem) {
        super(bodyTextureName, mapItem);
        this.mapItem = mapItem;
        listMia = new ArrayList<MemberActor>();
        speed = 1f;
    }

    public MapItem getMapItem() { return mapItem; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) {
        for (MemberActor memberActor : listMia) {
            memberActor.setSpeedChange(this.speed - speed);
        }
        this.speed = speed;
    }

    public void addMember(MemberActor actor) {
        addActor(actor);
        listMia.add(actor);
    }

    public void startAction(int actionId) {
        for (MemberActor ma : listMia) {
            ma.startAction(actionId);
        }
    }

}
