import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import Buttercrypt from 'react-native-buttercrypt';

export default function App() {
  const [generatedIV, setGeneratedIV] = React.useState<string>();
  const [generatedKey, setGeneratedKey] = React.useState<string>();
  const [generatedSalt, setGeneratedSalt] = React.useState<string>();
  const [encryptedStr, setEncryptedStr] = React.useState<string>();
  const [decryptedStr, setDecryptedStr] = React.useState<string>();

  React.useEffect(() => {
    const run = async () => {
      const iv = await Buttercrypt.generateIV();
      setGeneratedIV(iv);
      const derivedKey = await Buttercrypt.deriveKeyFromPassword(
        'test',
        'test',
        10
      );
      setGeneratedKey(derivedKey);
      const key = derivedKey.slice(0, 64);
      const hmacHexKey = derivedKey.slice(64, 128);
      const salt = await Buttercrypt.generateSaltWithLength(20);
      setGeneratedSalt(salt);

      const enc = await Buttercrypt.encryptText('hello', key, salt, hmacHexKey);
      setEncryptedStr(enc);
      const [payload, hmacHex, ivHex, saltHex] = enc.split('|');
      const dec = await Buttercrypt.decryptText(
        payload,
        key,
        ivHex,
        saltHex,
        hmacHexKey,
        hmacHex
      );
      setDecryptedStr(dec);
    };

    run();
  }, []);

  return (
    <View style={styles.container}>
      <Text>IV: {generatedIV}</Text>
      <Text>Key: {generatedKey}</Text>
      <Text>Salt: {generatedSalt}</Text>
      <Text>Encrypted: {encryptedStr}</Text>
      <Text>Decrypted: {decryptedStr}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
