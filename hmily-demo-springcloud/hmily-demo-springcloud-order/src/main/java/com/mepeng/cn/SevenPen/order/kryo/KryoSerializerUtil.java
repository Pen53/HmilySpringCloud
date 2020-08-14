package com.mepeng.cn.SevenPen.order.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mepeng.cn.SevenPen.order.kryo.ObjectSerializer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class KryoSerializerUtil implements ObjectSerializer {
    public KryoSerializerUtil(){
    }
    public byte[] serialize(final Object obj) throws RuntimeException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Throwable var4 = null;

            byte[] bytes;
            try {
                Output output = new Output(outputStream);
                Throwable var6 = null;

                try {
                    Kryo kryo = new Kryo();
                    kryo.writeObject(output, obj);
                    bytes = output.toBytes();
                    output.flush();
                } catch (Throwable var31) {
                    var6 = var31;
                    throw var31;
                } finally {
                    if (output != null) {
                        if (var6 != null) {
                            try {
                                output.close();
                            } catch (Throwable var30) {
                                var6.addSuppressed(var30);
                            }
                        } else {
                            output.close();
                        }
                    }

                }
            } catch (Throwable var33) {
                var4 = var33;
                throw var33;
            } finally {
                if (outputStream != null) {
                    if (var4 != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable var29) {
                            var4.addSuppressed(var29);
                        }
                    } else {
                        outputStream.close();
                    }
                }

            }

            return bytes;
        } catch (IOException var35) {
            throw new RuntimeException("kryo serialize error" + var35.getMessage());
        }
    }

    public <T> T deSerialize(final byte[] param, final Class<T> clazz) throws RuntimeException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(param);
            Throwable var5 = null;

            Object object;
            try {
                Kryo kryo = new Kryo();
                Input input = new Input(inputStream);
                object = kryo.readObject(input, clazz);
                input.close();
            } catch (Throwable var16) {
                var5 = var16;
                throw var16;
            } finally {
                if (inputStream != null) {
                    if (var5 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var15) {
                            var5.addSuppressed(var15);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }

            return (T) object;
        } catch (IOException var18) {
            throw new RuntimeException("kryo deSerialize error" + var18.getMessage());
        }
    }
}
