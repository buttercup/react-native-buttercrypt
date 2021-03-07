import { NativeModules } from 'react-native';

type ButtercryptType = {
  deriveKeyFromPassword(
    password: string,
    salt: string,
    rounds: number
  ): Promise<string>;
  generateSaltWithLength(length: number): Promise<string>;
  generateIV(): Promise<string>;
  encryptText(
    text: string,
    key: string,
    salt: string,
    hmacHexKey: string
  ): Promise<string>;
  decryptText(
    encryptedText: string,
    key: string,
    ivHex: string,
    salt: string,
    hmacHexKey: string,
    hmacHex: string
  ): Promise<string>;
};

const { Buttercrypt } = NativeModules;

export default Buttercrypt as ButtercryptType;
