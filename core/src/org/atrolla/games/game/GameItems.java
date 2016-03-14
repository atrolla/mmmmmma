package org.atrolla.games.game;

import org.atrolla.games.items.Item;

import java.util.*;

public class GameItems {

    private final Map<Integer, List<Item>> timeItems;

    public GameItems() {
        this.timeItems = new HashMap<>();
    }

    public void unregisterItem(Item item, int time) {
        timeItems.entrySet().stream().forEach(listEntry -> {
            if (!listEntry.getKey().equals(time)) {
                listEntry.getValue().removeIf(i -> i.equals(item));
            }
        });
    }

    public void registerItem(Item item, int time) {
        if (!timeItems.containsKey(time)) {
            timeItems.put(time, new ArrayList<>());
        }
        final List<Item> items = timeItems.get(time);
        items.add(item);
    }

    public void registerItem(List<Item> items, int time) {
        if (!timeItems.containsKey(time)) {
            timeItems.put(time, new ArrayList<>());
        }
        final List<Item> itemss = timeItems.get(time);
        itemss.addAll(items);
    }

    public List<Item> getEvents(int time) {
        return timeItems.getOrDefault(time, Collections.emptyList());
    }

    public void update(int time) {
        timeItems.remove(time - 1);
    }
}
