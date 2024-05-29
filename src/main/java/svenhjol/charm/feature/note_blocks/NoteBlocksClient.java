package svenhjol.charm.feature.note_blocks;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;

@Feature
public final class NoteBlocksClient extends ClientFeature {
    public NoteBlocksClient(ClientLoader loader) {
        super(loader);
    }
}
