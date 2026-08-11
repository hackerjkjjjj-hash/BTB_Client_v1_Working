# BTB Client

BTB Client is a Minecraft Bedrock utility companion.

## Included

- BTB dashboard overlay
- GUI / MAP / ASD BUTTON / F5 / Controls / Screenshot / Settings / About entry points
- Local waypoint storage
- Bedrock add-on sample showing real player XYZ and dimension data through the supported Script API
- GitHub Actions Android build

## Important

The Android app does not scan or inject into Minecraft memory. Android cannot
legitimately obtain all private Bedrock process data through a normal public API.
Bedrock-side data must come through supported add-on/script mechanisms.

## GitHub build

Push the project to GitHub, then open:
Actions -> Build BTB Client -> Run workflow

The resulting APK is uploaded under Artifacts as `BTB-Client-debug`.


## v2 utilities

MAP now supports persistent local waypoints. Long-press the MAP button to view
saved waypoints. This is local Android data until a Bedrock data bridge supplies
real coordinates.


## v4 — Read-only BTB Bridge

Added `BTB_Bridge/` and Android `BridgeClient.kt`.

The bridge is read-only and designed for exposed Bedrock player/world state.
It does not read private Android process memory and does not include cheat or
other-player manipulation features.
