package svenhjol.charm.module.portable_crafting.network;

import svenhjol.charm.network.ClientSender;
import svenhjol.charm.network.Id;

/**
 * Client sends empty message to ask server to open the crafting container.
 */
@Id("charm:open_crafting")
public class ClientSendOpenCrafting extends ClientSender {
}
