package fr.g123k.fluttermixpanel;

import android.telecom.Call;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;

import static fr.g123k.fluttermixpanel.Utils.mapToJSONObject;

public class MixPanelPeople {

    private final MixpanelAPI.People people;

    MixPanelPeople(MixpanelAPI.People people) {
        this.people = people;
    }

    void onMethodCall(MethodCall call, Result result) {
        String method = call.method.substring(call.method.indexOf("/") + 1);

        if ("append".equals(method)) {
            append(call, result);
        } else if ("clear_charges".equals(method)) {
            clearCharges(result);
        } else if ("delete_user".equals(method)) {
            deleteUser(result);
        } else if ("get_distinct_id".equals(method)) {
            getDistinctId(result);
        } else if ("join_experiment_if_available".equals(method)) {
            joinExperimentIfAvailable(result);
        } else if ("identify".equals(method)) {
            identify(call, result);
        } else if ("increment".equals(method)) {
            increment(call, result);
        } else if ("increment_map".equals(method)) {
            incrementMap(call, result);
        } else if ("set".equals(method)) {
            set(call, result);
        } else if ("set_json".equals(method)) {
            setJSONObject(call, result);
        } else if ("set_map".equals(method)) {
            setMap(call, result);
        } else if ("set_once".equals(method)) {
            setOnce(call, result);
        } else if ("set_once_json".equals(method)) {
            setOnceJSONObject(call, result);
        } else if ("set_once_map".equals(method)) {
            setOnceMap(call, result);
        } else if ("track_charge".equals(method)) {
            trackCharge(call, result);
        } else if ("merge".equals(method)) {
            merge(call, result);
        } else if ("remove".equals(method)) {
            remove(call, result);
        } else if ("union".equals(method)) {
            union(call, result);
        } else if ("unset".equals(method)) {
            unset(call, result);
        } else if ("with_identity".equals(method)) {
            withIdentity(call, result);
        } else {
            result.notImplemented();
        }
    }

    private void append(MethodCall call, Result result) {
        if (!call.hasArgument("name")) {
            result.error("ERROR", "Name argument is missing!", null);
            return;
        }

        people.append(call.argument("name").toString(), call.argument("value").toString());
        result.success(null);
    }

    private void clearCharges(Result result) {
        people.clearCharges();
        result.success(null);
    }

    private void deleteUser(Result result) {
        people.deleteUser();
        result.success(null);
    }

    private void getDistinctId(Result result) {
        result.success(people.getDistinctId());
    }

    private void identify(MethodCall call, Result result) {
        if (!call.hasArgument("distinct_id")) {
            result.error("ERROR", "The distinct id argument is missing!", null);
            return;
        }

        people.identify(call.argument("distinct_id").toString());
        result.success(null);
    }

    private void increment(MethodCall call, Result result) {
        if (!call.hasArgument("name")) {
            result.error("ERROR", "The name argument is missing!", null);
            return;
        } else if (!call.hasArgument("increment")) {
            result.error("ERROR", "The increment argument is missing!", null);
            return;
        }

        people.increment(call.argument("name").toString(), Integer.valueOf(call.argument("increment").toString()));
        result.success(null);
    }

    private void incrementMap(MethodCall call, Result result) {
        if (!call.hasArgument("properties")) {
            result.error("ERROR", "The map containing the properties is missing!", null);
            return;
        }

        try {
            people.increment((Map<String, Number>) call.argument("properties"));
            result.success(null);
        } catch (ClassCastException e) {
            result.error("ERROR", "Super properties map parameter should contain a map", null);
        }
    }

    private void joinExperimentIfAvailable(Result result) {
        people.joinExperimentIfAvailable();
        result.success(null);
    }

    private void merge(MethodCall call, Result result) {
        if (!call.hasArgument("name")) {
            result.error("ERROR", "The name argument is missing!", null);
            return;
        } else if (!call.hasArgument("updates_json")) {
            result.error("ERROR", "The JSON argument containing properties is missing!", null);
            return;
        }

        try {
            people.merge(call.argument("name").toString(), new JSONObject(call.argument("updates_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON containing the properties is not valid!", null);
        }
    }

    private void set(MethodCall call, Result result) {
        if (!call.hasArgument("property_name")) {
            result.error("ERROR", "The property name argument is missing!", null);
            return;
        } else if (!call.hasArgument("value")) {
            result.error("ERROR", "The value argument is missing!", null);
            return;
        }

        people.set(call.argument("property_name").toString(), call.argument("value"));
        result.success(null);
    }

    private void setJSONObject(MethodCall call, Result result) {
        if (!call.hasArgument("properties_json")) {
            result.error("ERROR", "The JSON argument containing the properties is missing!", null);
            return;
        }

        try {
            people.set(new JSONObject(call.argument("properties_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON containing the properties is not valid!", null);
        }
    }

    private void setMap(MethodCall call, Result result) {
        if (!call.hasArgument("properties_map")) {
            result.error("ERROR", "The map containing the properties is missing!", null);
            return;
        }

        try {
            people.setMap((Map<String, Object>) call.argument("properties_map"));
            result.success(null);
        } catch (ClassCastException e) {
            result.error("ERROR", "The super properties map parameter should contain a map", null);
        }
    }

    private void setOnce(MethodCall call, Result result) {
        if (!call.hasArgument("property_name")) {
            result.error("ERROR", "The property name argument is missing!", null);
            return;
        } else if (!call.hasArgument("value")) {
            result.error("ERROR", "The value argument is missing!", null);
            return;
        }

        people.setOnce(call.argument("property_name").toString(), call.argument("value"));
        result.success(null);
    }

    private void setOnceJSONObject(MethodCall call, Result result) {
        if (!call.hasArgument("properties_json")) {
            result.error("ERROR", "The JSON argument containing the properties is missing!", null);
            return;
        }

        try {
            people.setOnce(new JSONObject(call.argument("properties_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON containing the properties is not valid!", null);
        }
    }

    private void setOnceMap(MethodCall call, Result result) {
        if (!call.hasArgument("properties_map")) {
            result.error("ERROR", "The map containing the properties is missing!", null);
            return;
        }

        try {
            people.setOnceMap((Map<String, Object>) call.argument("properties_map"));
            result.success(null);
        } catch (ClassCastException e) {
            result.error("ERROR", "The super properties map parameter should contain a map", null);
        }
    }

    private void trackCharge(MethodCall call, Result result) {
        if (!call.hasArgument("properties_json")) {
            result.error("ERROR", "The JSON object argument containing the properties is missing!", null);
            return;
        } else if (!call.hasArgument("amount")) {
            result.error("ERROR", "The amount argument is missing!", null);
            return;
        }

        try {
            people.trackCharge(Double.valueOf(call.argument("amount").toString()),
                    new JSONObject(call.argument("properties_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON containing the properties is not valid!", null);
        }
    }

    private void remove(MethodCall call, Result result) {
        if (!call.hasArgument("name")) {
            result.error("ERROR", "The name argument is missing!", null);
            return;
        } else if (!call.hasArgument("value")) {
            result.error("ERROR", "The value argument is missing!", null);
            return;
        }

        people.remove(call.argument("name").toString(), call.argument("value"));
        result.success(null);
    }

    private void union(MethodCall call, Result result) {
        if (!call.hasArgument("name")) {
            result.error("ERROR", "The name argument is missing!", null);
            return;
        } else if (!call.hasArgument("value_json")) {
            result.error("ERROR", "The JSON Array argument containing the value is missing!", null);
            return;
        }

        try {
            people.union(call.argument("name").toString(),
                    new JSONArray(call.argument("value_json").toString()));
            result.success(null);
        } catch (JSONException e) {
            result.error("ERROR", "The JSON array containing the value is not valid!", null);
        }
    }

    private void unset(MethodCall call, Result result) {
        if (!call.hasArgument("name")) {
            result.error("ERROR", "The name argument is missing!", null);
            return;
        }

        people.unset(call.argument("name").toString());
        result.success(null);
    }

    private void withIdentity(MethodCall call, Result result) {
        if (!call.hasArgument("distinct_id")) {
            result.error("ERROR", "The name argument is missing!", null);
            return;
        }

        people.withIdentity(call.argument("distinct_id").toString());
        result.success(null);
    }

}
