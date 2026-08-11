package com.btbclient.app

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter

object WaypointUi {
    fun show(context: Context) {
        val points = DataStore.getWaypoints(context)
        if (points.isEmpty()) {
            AlertDialog.Builder(context)
                .setTitle("BTB MAP")
                .setMessage("No waypoints yet.\nUse MAP → Add Waypoint to create one.")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val labels = points.map { "${it.name}\n${it.coords}" }
        AlertDialog.Builder(context)
            .setTitle("BTB MAP • Waypoints")
            .setAdapter(
                ArrayAdapter(context, android.R.layout.simple_list_item_1, labels),
                null
            )
            .setNegativeButton("Close", null)
            .setNeutralButton("Clear All") { _, _ ->
                DataStore.clearWaypoints(context)
            }
            .show()
    }
}
