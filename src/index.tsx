import { NativeModules } from 'react-native';

type ButtercryptType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Buttercrypt } = NativeModules;

export default Buttercrypt as ButtercryptType;
