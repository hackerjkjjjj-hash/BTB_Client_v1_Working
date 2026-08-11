# BTB Bridge Protocol

BTB uses a small JSON payload for *read-only* player/world information.

Example:

```json
{
  "type": "player_state",
  "name": "Player",
  "dimension": "overworld",
  "x": 0,
  "y": 64,
  "z": 0,
  "gamemode": "survival",
  "world": "My World",
  "timestamp": 0
}
```

The Android client only displays/caches this information. It does not send
commands that change another player, and it does not patch Minecraft memory.

## Transport

The included server bridge exposes:

- `GET /health`
- `GET /state`

A local/private network can be used when a supported Bedrock Dedicated Server
is the source of the data.

For a normal Android Bedrock client, the game-side script must expose data
through a mechanism supported by that exact Bedrock environment. The project
does not pretend that a normal behavior pack can arbitrarily read Android
process memory.
