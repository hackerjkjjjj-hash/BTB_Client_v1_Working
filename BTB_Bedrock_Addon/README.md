# BTB Client Bedrock integration

Target: Minecraft Bedrock 1.26.x

The pack demonstrates the supported Bedrock Script API side. It can read
exposed player/world state such as player location and dimension.

Important limitation:
`@minecraft/server-net` is not a general client bridge; Microsoft documents
that it is for Bedrock Dedicated Server and does not function in the Minecraft
game client or Realms. Therefore this Android app does not pretend to receive
private process memory or unsupported client internals.

For a live Android <-> Bedrock bridge on a normal Bedrock client, the
integration must use a supported mechanism available to the exact target
version (for example, an explicit in-game UI/export or a dedicated-server
bridge). Private memory/injection is intentionally not used.
