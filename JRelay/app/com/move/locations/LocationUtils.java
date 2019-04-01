package com.move.locations;

import com.data.shared.Location;

public class LocationUtils {

	public static boolean IntersectsRadius(Location location, Location location2, float radius) {
        return (location.distanceTo(location2) <= radius);
    }
	 public static boolean IntersectsRect(Location location, Location location2, float width) {
		 return location.x - width >= location2.x
	                && location.x + width <= location2.x
	                && location.y + width <= location2.y
	                && location.y - width >= location2.y;
	    }
	 public static double GetAngleDifferenceDegrees(Location location, Location locationA, Location locationB) {
	        double angleA = Math.atan2((locationA.y - location.y), (locationA.x - location.x));
	        double angleB = Math.atan2((locationB.y - location.y), (locationB.x - location.x));
	        double diffRadians = (angleA - angleB);
	        return Math.abs((diffRadians * (180 / Math.PI)));
	    }
	 public static Location Add(Location location, Location locationToAdd) {
	        Location loc = ((Location)(location));
	        loc.x = (loc.x + locationToAdd.x);
	        loc.y = (loc.y + locationToAdd.y);
	        return loc;
	    }
	 public static Location subtract(Location location, Location locationToAdd) {
	        Location loc = ((Location)(location));
	        loc.x = (loc.x - locationToAdd.x);
	        loc.y = (loc.y - locationToAdd.y);
	        return loc;
	    }
	 public static Location Scale(Location location, float factor) {
	        Location loc = ((Location)(location));
	        loc.x = (loc.x * factor);
	        loc.y = (loc.y * factor);
	        return loc;
	    }
	   
}
