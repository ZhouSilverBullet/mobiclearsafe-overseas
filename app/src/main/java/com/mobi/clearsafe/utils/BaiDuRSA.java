
package com.mobi.clearsafe.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.mobi.clearsafe.app.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BaiDuRSA {

    private static final String TAG = "BaiDuRSA";

    private static final String PUBLIC_KEY_PATH = "rsa_public_key.pem";

    private static RSAPublicKey publicKey;

    private static Cipher rsaEnCipher;

    public static String encrypt(String content) {
        if (TextUtils.isEmpty(content) || getPublicKey() == null) {
            return null;
        }

        Log.d(TAG, "content:" + content);

        try {
            if (rsaEnCipher == null) {
                rsaEnCipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            }

            rsaEnCipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
            byte[] bytes = content.getBytes("UTF-8");
            Log.d(TAG, " publicKey.getModulus():" + publicKey.getModulus());
            int blockSize = publicKey.getModulus().bitLength() / 8;
            Log.d(TAG, " blockSize:" + publicKey.getModulus());
            int inputLen = bytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > blockSize) {
                    cache = rsaEnCipher.doFinal(bytes, offSet, blockSize);
                } else {
                    cache = rsaEnCipher.doFinal(bytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * blockSize;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            encryptedData = Base64.encode(encryptedData, Base64.URL_SAFE);
            String encryptedDataStr = new String(encryptedData, "UTF-8");
            return encryptedDataStr.replaceAll("\n", "");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey() {
        if (publicKey != null) {
            return publicKey;
        } else {
            // Read key from file
            String strKeyPEM = "";
            try {
                InputStream is = MyApplication.getContext().getAssets().open(PUBLIC_KEY_PATH);
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    strKeyPEM += line + "\n";
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(strKeyPEM)) {
                try {
                    strKeyPEM = strKeyPEM.replace("-----BEGIN PUBLIC KEY-----\n", "")
                            .replace("-----END PUBLIC KEY-----", "");
                    Log.d(TAG, "strKeyPEM:" + strKeyPEM);
                    byte[] encoded = Base64.decode(strKeyPEM, Base64.DEFAULT);
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
                    RSAPublicKey key = (RSAPublicKey) kf.generatePublic(keySpec);
                    publicKey = key;
                    return publicKey;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }
}
