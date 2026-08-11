#!/usr/bin/env python3
"""
BTB Bridge test server.

This is a read-only local bridge for testing the Android client.
It intentionally does NOT read Minecraft process memory.
Replace update_state() with data coming from a supported Bedrock server/API.
"""
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json, time

STATE = {
    "type": "player_state",
    "name": "Player",
    "dimension": "overworld",
    "x": None,
    "y": None,
    "z": None,
    "gamemode": "unknown",
    "world": "Unavailable",
    "timestamp": 0
}

def update_state(data):
    """Update only displayed state; no game-control commands are accepted."""
    global STATE
    allowed = {"name","dimension","x","y","z","gamemode","world"}
    STATE.update({k:v for k,v in data.items() if k in allowed})
    STATE["timestamp"] = int(time.time())

class Handler(BaseHTTPRequestHandler):
    def _json(self, code, payload):
        raw = json.dumps(payload).encode()
        self.send_response(code)
        self.send_header("Content-Type","application/json")
        self.send_header("Content-Length",str(len(raw)))
        self.send_header("Cache-Control","no-store")
        self.end_headers()
        self.wfile.write(raw)

    def do_GET(self):
        if self.path == "/health":
            self._json(200, {"ok": True, "bridge": "BTB"})
        elif self.path == "/state":
            self._json(200, STATE)
        else:
            self._json(404, {"ok": False})

    def log_message(self, fmt, *args):
        pass

if __name__ == "__main__":
    host = "127.0.0.1"
    port = 8765
    print(f"BTB Bridge listening on http://{host}:{port}")
    ThreadingHTTPServer((host, port), Handler).serve_forever()
