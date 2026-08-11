import { world, system } from "@minecraft/server";

function dimensionName(player) {
  const id = player.dimension?.id ?? "";
  if (id.endsWith("overworld")) return "Overworld";
  if (id.endsWith("nether")) return "Nether";
  if (id.endsWith("the_end")) return "The End";
  return id || "Unknown";
}

system.runInterval(() => {
  for (const player of world.getPlayers()) {
    const p = player.location;
    const x = Math.round(p.x);
    const y = Math.round(p.y);
    const z = Math.round(p.z);

    // This is real data from the Bedrock Script API.
    player.onScreenDisplay.setActionBar(
      `BTB  |  XYZ: ${x} ${y} ${z}  |  ${dimensionName(player)}`
    );
  }
}, 10);
