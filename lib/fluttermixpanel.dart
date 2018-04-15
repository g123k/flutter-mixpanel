import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class Mixpanel {
  _Channel _channel;
  String token;
  MixpanelPeople _people;

  Mixpanel(this.token) : assert(token != null && token.trim().length > 0) {
    if (token == null || token.trim().length == 0) {
      throw new Exception('The token can not be empty!');
    }
    token = token;
    _channel = new _Channel(token);
    _people = new MixpanelPeople._init(_channel);
  }

  Future<void> track(String eventName) async {
    await _invokeMethod('track', args: {'event_name': eventName});
    return null;
  }

  Future<void> trackMap(
      String eventName, Map<String, Object> properties) async {
    await _invokeMethod('track_map',
        args: {'event_name': eventName, 'properties_map': properties});
    return null;
  }

  Future<void> trackJSON(String eventName, String jsonObject) async {
    _checkJson(jsonObject);

    await _invokeMethod('track_json',
        args: {'event_name': eventName, 'properties_json': jsonObject});
    return null;
  }

  Future<void> timeEvent(String eventName) async {
    await _invokeMethod('time_event', args: {'event_name': eventName});
    return null;
  }

  Future<void> identify(String distinctId) async {
    await _invokeMethod('identify', args: {'distinct_id': distinctId});
    return null;
  }

  Future<String> getDistinctId() async {
    String res = await _invokeMethod('get_distinct_id');
    return res;
  }

  Future<double> eventElapsedTime(String eventName) async {
    double res = await _invokeMethod('event_elapsed_time',
        args: {'event_name': eventName});
    return res;
  }

  Future<void> alias(String alias, String original) async {
    await _invokeMethod('alias', args: {'alias': alias, 'original': original});
    return null;
  }

  Future<void> registerSuperPropertiesJSON(
      String superPropertiesJSONObject) async {
    _checkJson(superPropertiesJSONObject);
    await _invokeMethod('register_super_properties_json',
        args: {'super_properties_json': superPropertiesJSONObject});
    return null;
  }

  Future<void> registerSuperPropertiesMap(
      Map<String, Object> superPropertiesMap) async {
    await _invokeMethod('register_super_properties_map',
        args: {'super_properties_map': superPropertiesMap});
    return null;
  }

  Future<void> registerSuperPropertiesOnceJSON(
      String superPropertiesJSONObject) async {
    _checkJson(superPropertiesJSONObject);
    await _invokeMethod('register_super_properties_once_json',
        args: {'super_properties_json': superPropertiesJSONObject});
    return null;
  }

  Future<void> registerSuperPropertiesOnceMap(
      Map<String, Object> superPropertiesMap) async {
    await _invokeMethod('register_super_properties_once_map',
        args: {'super_properties_map': superPropertiesMap});
    return null;
  }

  Future<void> unregisterSuperProperty(String superPropertyName) async {
    await _invokeMethod('unregister_super_property',
        args: {'prop': superPropertyName});
    return null;
  }

  Future<void> clearSuperProperties() async {
    await _invokeMethod('clear_super_properties');
    return null;
  }

  Future<void> reset() async {
    await _invokeMethod('reset');
    return null;
  }

  Future<void> flush() async {
    await _invokeMethod('flush');
    return null;
  }

  MixpanelPeople get people => _people;

  Future<dynamic> _invokeMethod(String method, {Map<String, Object> args}) =>
      _channel._invokeMethod(method, args: args);
}

class MixpanelPeople {
  final _Channel _channel;

  MixpanelPeople._init(this._channel);

  Future<void> append(String name, dynamic value) async {
    await _invokeMethod('append', args: {'name': name, 'value': value});
    return null;
  }

  Future<void> clearCharges() async {
    await _invokeMethod('clear_charges');
    return null;
  }

  Future<void> deleteUser() async {
    await _invokeMethod('delete_user');
    return null;
  }

  Future<String> getDistinctId() async {
    await _invokeMethod('get_distinct_id');
    return null;
  }

  Future<void> identify(String distinctId) async {
    await _invokeMethod('identify', args: {'distinct_id': distinctId});
    return null;
  }

  Future<void> increment(String name, double increment) async {
    await _invokeMethod('increment',
        args: {'name': name, 'increment': increment});
    return null;
  }

  Future<void> incrementMap(Map<String, num> properties) async {
    await _invokeMethod('increment_map', args: {'properties': properties});
    return null;
  }

  Future<void> joinExperimentIfAvailable() async {
    await _invokeMethod('join_experiment_if_available');
    return null;
  }

  Future<void> merge(String name, String updatesJSONObject) async {
    _checkJson(updatesJSONObject);
    await _invokeMethod('merge',
        args: {'name': name, 'updates_json': updatesJSONObject});
    return null;
  }

  Future<void> set(String propertyName, dynamic value) async {
    await _invokeMethod('set',
        args: {'property_name': propertyName, 'value': value});
    return null;
  }

  Future<void> setJSONObject(String propertiesJSONObject) async {
    _checkJson(propertiesJSONObject);

    await _invokeMethod('set_json',
        args: {'properties_json': propertiesJSONObject});
    return null;
  }

  Future<void> setMap(Map<String, dynamic> properties) async {
    await _invokeMethod('set_map', args: {'properties_map': properties});
    return null;
  }

  Future<void> setOnce(String propertyName, dynamic value) async {
    await _invokeMethod('set_once',
        args: {'property_name': propertyName, 'value': value});
    return null;
  }

  Future<void> setOnceJSONObject(String propertiesJSONObject) async {
    _checkJson(propertiesJSONObject);

    await _invokeMethod('set_once_json',
        args: {'properties_json': propertiesJSONObject});
    return null;
  }

  Future<void> setOnceMap(Map<String, dynamic> properties) async {
    await _invokeMethod('set_once_map', args: {'properties_map': properties});
    return null;
  }

  Future<void> trackCharge(double amount, String propertiesJSONObject) async {
    _checkJson(propertiesJSONObject);
    await _invokeMethod('track_charge',
        args: {'amount': amount, 'properties_json': propertiesJSONObject});
    return null;
  }

  Future<void> remove(String name, dynamic value) async {
    await _invokeMethod('remove', args: {'name': name, 'value': value});
    return null;
  }

  Future<void> union(String name, String valueJSONArray) async {
    _checkJson(valueJSONArray);
    await _invokeMethod('union',
        args: {'name': name, 'value_json': valueJSONArray});
    return null;
  }

  Future<void> unset(String name) async {
    await _invokeMethod('unset', args: {'name': name});
    return null;
  }

  Future<void> withIdentity(String distinctId) async {
    await _invokeMethod('with_identity', args: {'distinct_id': distinctId});
    return null;
  }

  Future<dynamic> _invokeMethod(String method, {Map<String, Object> args}) =>
      _channel._invokeMethod('people/$method', args: args);
}

class _Channel {
  final MethodChannel _channel;
  final String _token;

  _Channel(this._token)
      : _channel = const MethodChannel('g123k/fluttermixpanel');

  Future<dynamic> _invokeMethod(String method, {Map<String, Object> args}) {
    if (args == null) {
      args = {'token': _token};
    } else {
      args.putIfAbsent('token', () => _token);
    }
    return _channel.invokeMethod(method, args);
  }
}

bool _checkJson(String jsonStr) {
  try {
    json.decode(jsonStr);
    return true;
  } on Exception {
    throw new FormatException('Invalid JSON');
  }
}
