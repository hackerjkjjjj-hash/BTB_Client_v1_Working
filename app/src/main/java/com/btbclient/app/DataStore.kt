package com.btbclient.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object DataStore {
    private const val PREF = "btb_client"
    private const val WAYPOINTS = "waypoints"

    data class Waypoint(val name: String, val coords: String)

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun addWaypoint(context: Context, waypoint: Waypoint) {
        val array = JSONArray(prefs(context).getString(WAYPOINTS, "[]"))
        array.put(JSONObject().apply {
            put("name", waypoint.name)
            put("coords", waypoint.coords)
        })
        prefs(context).edit().putString(WAYPOINTS, array.toString()).apply()
    }

    fun getWaypoints(context: Context): List<Waypoint> {
        val array = JSONArray(prefs(context).getString(WAYPOINTS, "[]"))
        val result = mutableListOf<Waypoint>()
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            result.add(
                Waypoint(
                    item.optString("name", "Waypoint"),
                    item.optString("coords", "Manual")
                )
            )
        }
        return result
    }

    fun clearWaypoints(context: Context) {
        prefs(context).edit().remove(WAYPOINTS).apply()
    }

    fun setOverlayOpacity(context: Context, value: Int) {
        prefs(context).edit().putInt("opacity", value.coerceIn(30,100)).apply()
    }

    fun getOverlayOpacity(context: Context): Int =
        prefs(context).getInt("opacity", 90)
}
