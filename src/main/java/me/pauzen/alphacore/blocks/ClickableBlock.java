/*
 *  Created by Filip P. on 3/22/15 9:24 PM.
 */

package me.pauzen.alphacore.blocks;

import me.pauzen.alphacore.inventory.misc.ClickType;
import me.pauzen.alphacore.players.CorePlayer;
import org.bukkit.Location;

public abstract class ClickableBlock {

    private Location location;

    public ClickableBlock(Location location) {
        this.location = location;
    }

    public abstract void onClick(ClickType clickType, CorePlayer corePlayer);

    public Location getLocation() {
        return location;
    }
}