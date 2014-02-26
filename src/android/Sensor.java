package com.okolabo.cordova.sensor;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.hardware.*;

import java.util.*;


public class Sensor extends CordovaPlugin implements SensorEventListener{

    public static final String GET_ALL_SENSORS = "poolAllSensors";
    public static final String GET_SPEC_SENSORS = "nativeAction";
    SensorManager mSensorManager;

    private float mPressure = 0;
    
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {

        @SuppressWarnings("deprecation")
        Context ctx = this.cordova.getActivity();
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_NORMAL);
        
//        cordova.getThreadPool().execute(new Runnable(){
//        	public void run(){
//        		while(mPressure == 0) {
//        			if (mPressure > 0) {
//        				mSensorManager.unregisterListener(Sensormatic.this);
//                		JSONObject result = new JSONObject();
//                		try {
//    						result.put("pressure", mPressure);
//    	            		callbackContext.success(result);
//    					} catch (JSONException e) {
//    						e.printStackTrace();
//    					}
//        			}
//        		}
//        	}
//        });
		while(mPressure == 0) {
			if (mPressure > 0) {
				mSensorManager.unregisterListener(Sensormatic.this);
        		JSONObject result = new JSONObject();
        		try {
					result.put("pressure", mPressure);
            		callbackContext.success(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
        return true;
        
//        if (GET_ALL_SENSORS.equals(action)) {
//
//            JSONArray rtnJSON = poolAllSensors();
//
//            if (rtnJSON == null) {
//        		return false;
//            }
//            callbackContext.success(rtnJSON);
//            return true;
//
//        } else if (GET_SPEC_SENSORS.equals(action)) {
//    		return false;
//        } else {
//    		return false;
//        }
    }

//    @Override
//    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
//
//        @SuppressWarnings("deprecation")
//        Context ctx = this.cordova.getActivity(); //This is not deprecated. I promise..! (See note #1 below)
//        Sensors = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
//
//        if (GET_ALL_SENSORS.equals(action)) {
//
//            JSONArray rtnJSON = poolAllSensors();
//
//            if (rtnJSON == null) {
//        		return false;
//            }
//            callbackContext.success(rtnJSON);
//            return true;
//
//        } else if (GET_SPEC_SENSORS.equals(action)) {
//    		return false;
//        } else {
//    		return false;
//        }
//    }
    
    /*  This gets a list of Sensors from the SensorManager and returns a JSONObject
     *  containing them.
     */
    public JSONArray poolAllSensors() {
        List<Sensor> SensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		/* Loop through all sensor objects and create a JSON object */
        JSONArray rtnJSON = new JSONArray();
        for (Sensor s : SensorList) {
            JSONObject o = new JSONObject();

            try {
                o.put("vendor", s.getVendor());
                o.put("name", s.getName());
                o.put("type", checkType(s.getType()));
                o.put("version", s.getVersion());
                o.put("maxRange", s.getMaximumRange());
                //o.put( "minDelay",		s.getMinDelay() 		);
                o.put("power", s.getPower());
                o.put("resolution", s.getResolution());

                rtnJSON.put(o);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        } //EOF for() loop

        return rtnJSON;
    }


    /* Just a really long switch() in all honesty. It compares the returned
     *  type of a Sensor - and then checks it against the CONSTANT values
     *  contained in the Sensor class. A string containing the sensor type
     *  will be returned; or "Unknown" if no constant matches the value.
     */
    @SuppressWarnings("deprecation")
    public static String checkType(int type) {

        switch (type) {

            case Sensor.TYPE_ACCELEROMETER:
                return "Accelerometer";

            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "Ambient Temperature";

            case Sensor.TYPE_LIGHT:
                return "Light";

            case Sensor.TYPE_GRAVITY:
                return "Gravity";

            case Sensor.TYPE_GYROSCOPE:
                return "Gyroscope";

            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "Linear Acceleration";

            case Sensor.TYPE_MAGNETIC_FIELD:
                return "Magnetic Field";

            case Sensor.TYPE_PRESSURE:
                return "Pressure";

            case Sensor.TYPE_PROXIMITY:
                return "Proximity";

            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "Relative Humidity";

            case Sensor.TYPE_ROTATION_VECTOR:
                return "Rotation Vector";

			/* These are deprecated - however, they are required as of Android 4.1 to correctly
			 * identify certain forms of sensor. Yes. Deprecated but even required by the official
			 * SDK, Emulator and latest Android image... You heard it here folks; Google is so
			 * hipster that they even rock out deprecated constants.
			 */
            case Sensor.TYPE_ORIENTATION:
                return "Orientation";

            case Sensor.TYPE_TEMPERATURE:
                return "Temperature";

            default:
                return "Unknown";

        }
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		mPressure = event.values[0];
	}

}

/* Notes:
 * 1) cordova.getContext() is NOT deprecated; this is an issue with the Apache
 *  Cordova/PhoneGap codebase that they have yet to fix.
 *  (src: http://simonmacdonald.blogspot.fr/2012/07/phonegap-android-plugins-sometimes-we.html)
 */