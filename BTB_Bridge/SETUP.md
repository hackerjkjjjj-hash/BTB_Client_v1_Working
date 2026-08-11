# BTB Bridge setup

## 1. Test the bridge

On a machine that can run Python:

```bash
cd BTB_Bridge/server
python3 btb_bridge.py
```

Test:

```bash
curl http://127.0.0.1:8765/health
curl http://127.0.0.1:8765/state
```

## 2. Android

`BridgeClient.kt` polls `/state` and converts it into `BedrockState`.

The dashboard can display:

- player name
- XYZ
- dimension
- gamemode
- world
- bridge connected/disconnected

## 3. Important limitation

For an ordinary Minecraft Bedrock Android client, a behavior pack cannot
magically open another Android process's private memory. The bridge must receive
data from a supported Bedrock-side source. This project therefore separates the
UI/transport layer from the Bedrock data source.

Do not add memory patching, process injection, or commands intended to affect
other players.
