#import <Foundation/Foundation.h>

@interface BCHelpers : NSObject

+ (char *)characterArrayFromHexString:(NSString *)hexString;
+ (BOOL)constantTimeCompare:(NSString *)reference toChallenger:(NSString *)challenger;
+ (NSData *)dataFromHexString:(NSString *)string;
+ (NSString *)hexStringFromData:(NSData *)data;

@end
