package com.et.testapplication;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView1);

		MyApplication myApplication = (MyApplication) getApplication();
		textView.setText(myApplication.getName());

		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			android.content.pm.Signature[] signatures = packageInfo.signatures;
			if (signatures != null && signatures.length > 0) {
				String md5 = DigestUtils.md5(signatures[0].toCharsString());
				String md52 = DigestUtils.md5(new String(signatures[0].toByteArray(), "utf-8"));
				Log.i("MD5", md5);
				PublicKey publicKey = getPublicKey(signatures[0]);
				String strPk = publicKey.toString();
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(1024); // 1024位长度，目前1024位也会给破解，最好设置2048，
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate(); // 私钥
			PublicKey publicKey = keyPair.getPublic(); // 公钥
			String plaintext = "This is the message being signed";

			// Compute signature
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initSign(privateKey); // 用私钥签名
			instance.update((plaintext).getBytes()); //原文
			byte[] signature = instance.sign(); // 用sha1散列算法生成摘要并用私钥进行数字签名返回 （数字签名就是私钥加密原文的摘要）

			// Compute digest
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			byte[] digest = sha1.digest((plaintext).getBytes());

			// Encrypt digest
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] cipherText = cipher.doFinal(digest);

			// Display results
			System.out.println("Input data: " + plaintext);
			System.out.println("Digest: " + bytes2String(digest));
			System.out.println("Cipher text: " + bytes2String(cipherText)); //奇怪，Cipher text和Signature的输出不一样，难道Cipher text先sha1再RSA私钥加密两步操作和SHA1withRSA一步出来的结果会不一样。
			System.out.println("Signature: " + bytes2String(signature));
			/*************************用公钥验证数字签名***********************/
			instance.initVerify(publicKey); // 用公钥验证
			instance.update(plaintext.getBytes()); //原文
			if (instance.verify(signature)) { // 验证是否和数字签名一致
				Log.i("Verification", "Signature verification success.");
			} else {
				Log.i("Verification", "Signature verification fail.");
			}
			/*********************私钥加密，公钥解密*************************/
			Cipher encrypt = Cipher.getInstance("RSA");
			encrypt.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptData = encrypt.doFinal(plaintext.getBytes()); //私钥加密字符串
			
			Cipher decrypt = Cipher.getInstance("RSA");
			decrypt.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] decryptData = decrypt.doFinal(encryptData); //公钥解密字符串
			String decryptSt = new String(decryptData);
			Log.i("public key decrypt", decryptSt);
			/*********************公钥加密，私钥解密*************************/
			Cipher encrypt2 = Cipher.getInstance("RSA");
			encrypt2.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptData2 = encrypt.doFinal(plaintext.getBytes()); //公钥加密字符串
			
			Cipher decrypt2 = Cipher.getInstance("RSA");
			decrypt2.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptData2 = decrypt.doFinal(encryptData2); //公钥解密字符串
			String decryptSt2 = new String(decryptData2);
			Log.i("private key decrypt", decryptSt2);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PublicKey getPublicKey(android.content.pm.Signature signature) throws CertificateException {
		final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
		final ByteArrayInputStream bais = new ByteArrayInputStream(signature.toByteArray());
		final Certificate cert = certFactory.generateCertificate(bais);
		return cert.getPublicKey();
	}

	public static String bytes2String(byte[] bytes) {
		StringBuilder string = new StringBuilder();
		for (byte b : bytes) {
			String hexString = Integer.toHexString(0x00FF & b);
			string.append(hexString.length() == 1 ? "0" + hexString : hexString);
		}
		return string.toString();
	}
}
