package fr.g123k.fluttermixpanel;

import android.app.Activity;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterNativeView;

/**
 * FlutterMixPanelPlugin
 */
public class FlutterMixPanelPlugin implements MethodCallHandler {
    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "g123k/fluttermixpanel");
        FlutterMixPanelPlugin plugin = new FlutterMixPanelPlugin(registrar.activity());
        registrar.addViewDestroyListener(plugin.viewDestroyListener);
        channel.setMethodCallHandler(plugin);
    }

    private final Activity activity;
    private final PluginRegistry.ViewDestroyListener viewDestroyListener;

    private MixpanelAPI mixPanel;
    private MixPanelPeople people;

    private FlutterMixPanelPlugin(Activity activity) {
        this.activity = activity;
        this.viewDestroyListener = new PluginRegistry.ViewDestroyListener() {

            @Override
            public boolean onViewDestroy(FlutterNativeView flutterNativeView) {
                if (mixPanel != null) {
                    mixPanel.flush();
                }
                return true;
            }
        };
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (mixPanel == null && call.hasArgument("token")) {
            initMixPanel(call.argument("token").toString());
        }

        if (mixPanel == null) {
            result.error("NOT_INITIALIZED", "Please initialized the Mixpanel API first with a valid token", null);
            return;
        }

        if (call.method.startsWith("people/")) {
            people.onMethodCall(call, result);
        } else if (call.method.equals("track")) {
            track(call, result);
        } else if (call.method.equals("track_map")) {
            trackMap(call, result);
        } else if (call.method.equals("track_json")) {
            trackJSON(call, result);
        } else if (call.method.equals("timeEvent")) {
            timeEvent(call, result);
        } else if (call.method.equals("identify")) {
            identify(call, result);
        } else if (call.method.equals("get_distinct_id")) {
            getDistinctId(result);
        } else if (call.method.equals("event_elapsed_time")) {
            eventElapsedTime(call, result);
        } else if (call.method.equals("alias")) {
            alias(call, result);
        } else if (call.method.equals("register_super_properties_json")) {
            registerSuperPropertiesJSON(call, result);
        } else if (call.method.equals("register_super_properties_map")) {
            registerSuperPropertiesMap(call, result);
        } else if (call.method.equals("register_super_properties_once_json")) {
            registerSuperPropertiesOnceJSON(call, result);
        } else if (call.method.equals("register_super_properties_once_map")) {
            registerSuperPropertiesOnceMap(call, result);
        } else if (call.method.equals("unregister_super_property")) {
            unregisterSuperProperty(call, result);
        } else if (call.method.equals("clear_super_properties")) {
            clearSuperProperties(result);
        } else if (call.method.equals("reset")) {
            reset(result);
        } else if (call.method.equals("flush")) {
            flush(result);
        } else {
            result.notImplemented();
        }
    }

    private void initMixPanel(String token) {
        mixPanel = MixpanelAPI.getInstance(activity, token);
        people = new MixPanelPeople(mixPanel.getPeople());
    }

    private void track(MethodCall call, Result result) {
        if (!call.hasArgument("event_name")) {
            result.error("ERROR", "The event name argument is missing!", null);
            return;
        }

        mixPanel.track(call.argument("event_name").toString());
        result.success(null);
    }

    private void trackMap(MethodCall call, Result result) {
        if (!call.hasArgument("event_name")) {
            result.error("ERROR", "The event name argument is missing!", null);
            return;
        } else if (!call.hasArgument("properties_map")) {
            result.error("ERROR", "The properties map argument is missing!", null);
            return;
        }

        try {
            //noinspection unchecked
            mixPanel.trackMap(call.argument("event_name").toString(), (Map<String, Object>) call.argument("properties_map"));
            result.success(null);
        } catch (ClassCastException e) {
            result.error("ERROR", "The props parameter should contain a map", null);
        }
    }

    private void trackJSON(MethodCall call, Result result) {
        if (!call.hasArgument("event_name")) {
            result.error("ERROR", "The event name argument is missing!", null);
            return;
        } else if (!call.hasArgument("properties_json")) {
            result.error("ERROR", "THE properties JSON argument is missing!", null);
            return;
        }

        try {
            //noinspection unchecked
            mixPanel.track(call.argument("event_name").toString(), new JSONObject(call.argument("properties_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON containing the properties is not valid!", null);
        }
    }

    private void timeEvent(MethodCall call, Result result) {
        if (!call.hasArgument("event_name")) {
            result.error("ERROR", "The event name argument is missing!", null);
        }

        mixPanel.timeEvent(call.argument("event_name").toString());
        result.success(null);
    }

    private void identify(MethodCall call, Result result) {
        if (!call.hasArgument("distinct_id")) {
            result.error("ERROR", "The distinct id argument is missing!", null);
        }

        mixPanel.identify(call.argument("distinct_id").toString());
        result.success(null);
    }

    private void getDistinctId(Result result) {
        result.success(mixPanel.getDistinctId());
    }

    private void eventElapsedTime(MethodCall call, Result result) {
        if (!call.hasArgument("event_name")) {
            result.error("ERROR", "The event name argument is missing!", null);
        }

        result.success(mixPanel.eventElapsedTime(call.argument("event_name").toString()));
    }

    private void alias(MethodCall call, Result result) {
        if (!call.hasArgument("alias") && !call.hasArgument("original")) {
            result.error("ERROR", "Alias and/or original is/are missing", null);
        }

        mixPanel.alias(call.argument("alias").toString(), call.argument("original").toString());
        result.success(null);
    }

    private void registerSuperPropertiesJSON(MethodCall call, Result result) {
        if (!call.hasArgument("super_properties_json")) {
            result.error("ERROR", "The super properties json argument is missing!", null);
            return;
        }

        try {
            //noinspection unchecked
            mixPanel.registerSuperProperties(new JSONObject(call.argument("super_properties_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON containing the super properties is not valid!", null);
        }
    }

    private void registerSuperPropertiesMap(MethodCall call, Result result) {
        if (!call.hasArgument("super_properties_map")) {
            result.error("ERROR", "The super properties map argument is missing!", null);
            return;
        }

        try {
            //noinspection unchecked
            mixPanel.registerSuperPropertiesMap((Map<String, Object>) call.argument("super_properties_map"));
            result.success(null);
        } catch (ClassCastException e) {
            result.error("ERROR", "The super properties map parameter should contain a map", null);
        }
    }

    private void registerSuperPropertiesOnceJSON(MethodCall call, Result result) {
        if (!call.hasArgument("super_properties_json")) {
            result.error("ERROR", "The super properties JSON argument is missing!", null);
            return;
        }

        try {
            //noinspection unchecked
            mixPanel.registerSuperPropertiesOnce(new JSONObject(call.argument("super_properties_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON containing the super properties is not valid!", null);
        }
    }

    private void registerSuperPropertiesOnceMap(MethodCall call, Result result) {
        if (!call.hasArgument("super_properties_map")) {
            result.error("ERROR", "Super properties map argument is missing!", null);
            return;
        }

        try {
            //noinspection unchecked
            mixPanel.registerSuperPropertiesOnceMap((Map<String, Object>) call.argument("super_properties_map"));
            result.success(null);
        } catch (ClassCastException e) {
            result.error("ERROR", "The super properties map parameter should contain a map", null);
        }
    }

    private void unregisterSuperProperty(MethodCall call, Result result) {
        if (!call.hasArgument("prop")) {
            result.error("ERROR", "The super property argument is missing!", null);
        }

        mixPanel.unregisterSuperProperty(call.argument("prop").toString());
        result.success(null);
    }

    private void clearSuperProperties(Result result) {
        mixPanel.clearSuperProperties();
        result.success(null);
    }

    private void reset(Result result) {
        mixPanel.reset();
        result.success(null);
    }

    private void flush(Result result) {
        mixPanel.flush();
        result.success(null);
    }

}
