package managers;

import models.Playground;
import models.Owner;

import java.util.ArrayList;
import java.util.List;

public class PlaygroundManager {
    private static PlaygroundManager instance;
    private List<Playground> playgrounds;

    private PlaygroundManager() {
        playgrounds = new ArrayList<>();
    }

    public static PlaygroundManager getInstance() {
        if (instance == null) {
            instance = new PlaygroundManager();
        }
        return instance;
    }

    public void addPlayground(Playground pg) {
        playgrounds.add(pg);
    }

    public List<Playground> getPlaygrounds() {
        return playgrounds;
    }

    public List<Playground> getPlaygroundsByOwner(Owner owner) {
        List<Playground> result = new ArrayList<>();
        for (Playground pg : playgrounds) {
            if (pg.getOwner().getUsername().equals(owner.getUsername())) {
                result.add(pg);
            }
        }
        return result;
    }
}