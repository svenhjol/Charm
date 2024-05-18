package svenhjol.charm.feature.note_blocks;

import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class NoteBlocksClient extends ClientFeature {
    public NoteBlocksClient(ClientLoader loader) {
        super(loader);
    }
}
