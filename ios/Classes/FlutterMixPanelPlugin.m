#import "FlutterMixPanelPlugin.h"
#import <fluttermixpanel/fluttermixpanel-Swift.h>

@implementation FlutterMixPanelPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFluttermixpanelPlugin registerWithRegistrar:registrar];
}
@end
