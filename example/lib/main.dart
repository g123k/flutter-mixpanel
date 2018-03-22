import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttermixpanel/fluttermixpanel.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  initState() {
    super.initState();
    initPlatformState();
  }

  initPlatformState() async {
    MixpanelAPI mixPanel = new MixpanelAPI("<your token>");
    String distinctId = await mixPanel.people.getDistinctId();
    print(distinctId);

    await mixPanel.trackJSON("toto", '{"toto":"toto","titi":"titi"}');

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Plugin example app'),
        ),
        body: new Text('Hello world!'),
      ),
    );
  }
}
