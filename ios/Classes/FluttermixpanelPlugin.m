#import "FluttermixpanelPlugin.h"
#import <fluttermixpanel/fluttermixpanel-Swift.h>

@implementation FluttermixpanelPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFluttermixpanelPlugin registerWithRegistrar:registrar];
}
@end
