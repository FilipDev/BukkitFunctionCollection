/*
 *  Created by Filip P. on 2/7/15 11:08 PM.
 */

package me.pauzen.alphacore.players.data.trackers;

import me.pauzen.alphacore.players.CorePlayer;
import me.pauzen.alphacore.players.data.MileStone;
import me.pauzen.alphacore.players.data.events.TrackerValueChangeEvent;
import me.pauzen.alphacore.points.TrackerDisplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tracker {

    private int value = 0;
    private String     id;
    private CorePlayer corePlayer;

    private boolean persistant = true;

    private Map<Integer, List<MileStone>> mileStones = new HashMap<>();

    public Tracker(String id, int initialValue) {
        this.id = id;
        this.value = initialValue;
    }

    public Tracker(CorePlayer corePlayer, String id, int initialValue) {
        this(id, initialValue);
        this.corePlayer = corePlayer;
    }

    public boolean isPersistant() {
        return persistant;
    }

    public void setPersistant(boolean persistant) {
        this.persistant = persistant;
    }

    public void addValue(int value) {
        setValue(getValue() + value);
    }

    public void subtractValue(int value) {
        setValue(getValue() - value);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        if (updateValue(value)) {
            this.value = value;
        }
    }

    public boolean updateValue(int newValue) {
        if (new TrackerValueChangeEvent(value, newValue, this).call().isCancelled()) {
            return false;
        }
        List<MileStone> milestones = getMilestones(newValue);

        if (milestones == null) {
            return true;
        }

        milestones.forEach(milestone -> milestone.onReach(this.corePlayer, this));

        return true;
    }

    public List<MileStone> checkAndGetMilestones(int value) {
        checkMilestoneListExists(value);

        return getMilestones(value);
    }

    public List<MileStone> getMilestones(int value) {
        return mileStones.get(value);
    }

    public void addTracker(CorePlayer corePlayer) {
        corePlayer.getTrackers().put(id, this);
    }

    private void checkMilestoneListExists(int milestone) {
        if (mileStones.get(milestone) == null) {
            mileStones.put(milestone, new ArrayList<>());
        }
    }

    public void addMilestone(MileStone milestone) {
        checkAndGetMilestones(milestone.getValue()).add(milestone);
    }

    public Tracker copy() {
        Tracker newTracker = new Tracker(this.corePlayer, this.id, this.value);
        newTracker.mileStones = this.mileStones;
        return newTracker;
    }

    public void display(CorePlayer corePlayer) {
        corePlayer.addAttribute("tracker_displayer", new TrackerDisplayer(corePlayer.getCurrentPlace(), corePlayer, this));
    }

    public String getId() {
        return id;
    }
}
