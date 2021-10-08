package xyz.verylonely.cryptostorage.activities;

import static xyz.verylonely.cryptostorage.activities.MainActivity.FLAG_SECURE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import xyz.verylonely.CryptoSDK.Base58;
import xyz.verylonely.CryptoSDK.EncryptDecrypt;
import xyz.verylonely.CryptoSDK.HashUtil;
import xyz.verylonely.CryptoSDK.KeyGeneratorUtil;
import xyz.verylonely.cryptostorage.R;
import xyz.verylonely.cryptostorage.StartType;

public class LoginActivity extends AppCompatActivity {

    private EditText password, passwordAgain;
    private Button login, generateKeys;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(FLAG_SECURE, FLAG_SECURE);

        setContentView(R.layout.login_activity);

        password = findViewById(R.id.password);
        passwordAgain = findViewById(R.id.password_again);
        login = findViewById(R.id.loginBtn);
        generateKeys = findViewById(R.id.genKeyBtn);

        if(MainActivity.getInstance().startType == StartType.FIRST_START)
        {
            login.setVisibility(View.GONE);
        }else{
            passwordAgain.setVisibility(View.GONE);
            generateKeys.setVisibility(View.GONE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences pref = getSharedPreferences("app_keystore", MODE_PRIVATE);
                String encryptedMasterKey =  pref.getString("master_key", null);
                String salt = pref.getString("salt_master_key", null);

                if(encryptedMasterKey != null)
                {

                    try {
                        SecretKey passwordKeyString = KeyGeneratorUtil.getKeyFromPassword(String.valueOf(password.getText()), salt);

                        byte[] decryptedMasterKey = Base58.decode(EncryptDecrypt.DecryptString(encryptedMasterKey, passwordKeyString));

                        if (decryptedMasterKey != null)
                        {
                            MainActivity.getInstance().setKey(decryptedMasterKey);
                            MainActivity.getInstance().start();

                            finish();
                        }


                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                        password.setText("");
                        Toast.makeText(MainActivity.getInstance(), R.string.error_decryption, Toast.LENGTH_LONG).show();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        generateKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(password.getText()).isEmpty())
                {
                    Toast.makeText(MainActivity.getInstance(), R.string.password_cant_be_empty, Toast.LENGTH_LONG).show();
                }
                else if(!String.valueOf(password.getText()).equals(String.valueOf(passwordAgain.getText())))
                {
                    Toast.makeText(MainActivity.getInstance(), R.string.password_not_match, Toast.LENGTH_LONG).show();
                }
                else {
                    SecretKey key;

                    try {
                        key = KeyGeneratorUtil.GenerateKey();

                        String salt = Base58.encode(EncryptDecrypt.generateIv());

                        SecretKey passwordKey = KeyGeneratorUtil.getKeyFromPassword(String.valueOf(password.getText()), salt);

                        String encryptedMasterKey = EncryptDecrypt.EncryptString(passwordKey, Base58.encode(key.getEncoded()));

                        SharedPreferences pref = getSharedPreferences("app_keystore",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("master_key", encryptedMasterKey);
                        editor.putString("salt_master_key", salt);
                        editor.commit();

                        MainActivity.getInstance().setKey(key.getEncoded());

                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(MainActivity.getInstance(), R.string.key_generated, Toast.LENGTH_SHORT).show();

                    MainActivity.getInstance().start();

                    finish();


                }

            }
        });
    }
}
