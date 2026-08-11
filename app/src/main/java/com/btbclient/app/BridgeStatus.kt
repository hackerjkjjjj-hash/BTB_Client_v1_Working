package com.btbclient.app

object BridgeStatus {
    @Volatile var latest: BedrockState = BedrockState()

    fun refresh(client: BridgeClient = BridgeClient(), onChanged: (BedrockState) -> Unit = {}) {
        client.poll {
            latest = it
            onChanged(it)
        }
    }
}
