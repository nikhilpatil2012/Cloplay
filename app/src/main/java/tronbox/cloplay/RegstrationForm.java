package tronbox.cloplay;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class RegstrationForm extends Activity {

    EditText firstName, lastName, phone, homeAddress, emailAddress;
    ProgressDialog progressDialog;

    TextView title, subTitle;

    AlertDialog alertDialog;

    Button send;

    int Request_Mail = 1;
    String path;
    File beforeFile, afterFile;

    Typeface arial, babe;

    Multipart _multipart;

    private static final String name = "A1 Garage";
    private static final String username = "system@a1garage.com";
    private static final String password = "a1system12";

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regstration_form);

        title = (TextView)findViewById(R.id.cutomer_title);
        subTitle = (TextView)findViewById(R.id.cutomer_sub_title);

        arial = Typeface.createFromAsset(getAssets(), "arial.TTF");
        babe = Typeface.createFromAsset(getAssets(), "bebes.TTF");

        title.setTypeface(babe);
        subTitle.setTypeface(babe);

        _multipart = new MimeMultipart();

        storeImages();

        getActionBar().hide();

        firstName = (EditText)findViewById(R.id.first_name);
        firstName.setTypeface(babe);
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                if(charSequence.length()>0){
                    firstName.setTypeface(arial);
                }else {
                    firstName.setTypeface(babe);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lastName = (EditText)findViewById(R.id.last_name);
        lastName.setTypeface(babe);
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                if(charSequence.length()>0){
                    lastName.setTypeface(arial);
                }else {
                    lastName.setTypeface(babe);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phone = (EditText)findViewById(R.id.phone);
        phone.setTypeface(babe);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                if(charSequence.length()>0){
                    phone.setTypeface(arial);
                }else {
                    phone.setTypeface(babe);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        homeAddress = (EditText)findViewById(R.id.address);
        homeAddress.setTypeface(babe);
        homeAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                if(charSequence.length()>0){
                    homeAddress.setTypeface(arial);
                }else {
                    homeAddress.setTypeface(babe);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailAddress = (EditText)findViewById(R.id.email);
        emailAddress.setTypeface(babe);
        emailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                if(charSequence.length()>0){
                    emailAddress.setTypeface(arial);
                }else {
                    emailAddress.setTypeface(babe);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        send = (Button)findViewById(R.id.send);
        send.setTypeface(babe);


       /* firstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_NEXT){

                    lastName.requestFocus();

                    return true;
                }

                return true;
            }
        });

        lastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_NEXT) {

                    phone.requestFocus();

                    return true;
                }

                return true;
            }
        });

        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_NEXT){

                    emailAddress.requestFocus();

                    return true;
                }

                return true;
            }
        });

        emailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_NEXT){

                    homeAddress.requestFocus();

                    return true;
                }

                return true;
            }
        });


        homeAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE){


                    return true;
                }

                return false;
            }
        });*/



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if((firstName.getText().toString().length() > 0)&&(lastName.getText().toString().length() > 0) && (emailAddress.getText().toString().length() > 0)&&(phone.getText().toString().length() > 0) && (homeAddress.getText().toString().length() > 0)){

                    ArrayList<Uri> uris = new ArrayList<Uri>();
                    uris.add(Master.beforeImageUri);
                    uris.add(Master.afterImageUri);

                    sendMail(username, "Door App Registration", "User Name :- "+firstName.getText().toString()+" "+lastName.getText().toString()+"\n"+"Phone :- "+phone.getText().toString()+"\n"+"Email :- "+emailAddress.getText().toString()+"\n"+"Address :- "+homeAddress.getText().toString());


                    firstName.setText("");
                    lastName.setText("");
                    emailAddress.setText("");
                    phone.setText("");
                    homeAddress.setText("");



/*
                    Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    email.setType("text/plain");
                    //email.setType("image/png");
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jagat.xcube@gmail.com"}); // "jagat.xcube@gmail.com"
                    email.putExtra(Intent.EXTRA_SUBJECT, "Door App Registration"); // email subject
                    email.putExtra(Intent.EXTRA_TEXT, "User Name :- "+firstName.getText().toString()+" "+lastName.getText().toString()+"\n"+"Phone :- "+phone.getText().toString()+"\n"+"Email :- "+emailAddress.getText().toString()+"\n"+"Address :- "+homeAddress.getText().toString()); // email body
                    email.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                    startActivityForResult(email, Request_Mail);*/


                }else {
                    Toast.makeText(getApplicationContext(), "Please complete the form", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        if((firstName.getText().toString().length() > 0)&&(lastName.getText().toString().length() > 0) && (emailAddress.getText().toString().length() > 0)&&(phone.getText().toString().length() > 0) && (homeAddress.getText().toString().length() > 0)) {


            firstName.setText("");
            lastName.setText("");
            emailAddress.setText("");
            phone.setText("");
            homeAddress.setText("");


            Intent intent = new Intent(getApplicationContext(), TestingCamera.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        if((!Master.beforeImage.isRecycled()) && (!Master.afterImage.isRecycled()))
      {

            Master.beforeImage.recycle();
            Master.afterImage.recycle();
      }

            finish();
        }

        }

    public void clear(){


        Intent intent = new Intent(getApplicationContext(), TestingCamera.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        if(Master.beforeImage != null && Master.afterImage != null){

            if((!Master.beforeImage.isRecycled()) && (!Master.afterImage.isRecycled()))
            {

                Master.beforeImage.recycle();
                Master.afterImage.recycle();
            }

        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

          if(resultCode == RESULT_OK && requestCode == Request_Mail)
        {

            firstName.setText("");
            lastName.setText("");
            emailAddress.setText("");
            phone.setText("");
            homeAddress.setText("");

        }

    }

      public void storeImages()
    {
/*
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car2);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.car1);
*/


        File myDir =  getAlbumStorageDir("cloplay");
            myDir.mkdirs();

            String beforeImage = "before.jpg";
            String afterImage = "after.jpg";

            beforeFile = new File (myDir, beforeImage);
            afterFile = new File (myDir, afterImage);


        if (beforeFile.exists ()) beforeFile.delete ();
        if (afterFile.exists ()) afterFile.delete ();

        try {
                FileOutputStream out = new FileOutputStream(beforeFile);
                Master.beforeImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
                out.close();

                FileOutputStream out1 = new FileOutputStream(afterFile);
                Master.afterImage.compress(Bitmap.CompressFormat.JPEG, 100, out1);

           // bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, out);
               out1.flush();
                out1.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Master.beforeImageUri = Uri.fromFile(beforeFile);
            Master.afterImageUri = Uri.fromFile(afterFile);


        Log.w("StoredAfterFilePath", beforeFile.getPath());
        Log.w("StoredBeforeFilePath", afterFile.getPath());






        //}catch (IOException io){}
    }


    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("Errors", "Directory not created");
        }
        return file;
    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        Log.w("FilePath", beforeFile.getPath());

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, name));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress("TMello@a1garage.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);

        BodyPart messageBodyPart1 = new MimeBodyPart();
        BodyPart messageBodyPart2 = new MimeBodyPart();
        BodyPart messageBodyPart3 = new MimeBodyPart();



        messageBodyPart1.setDataHandler(new DataHandler(new FileDataSource(beforeFile.getPath())));
        messageBodyPart1.setFileName(beforeFile.getPath());

        messageBodyPart2.setDataHandler(new DataHandler(new FileDataSource(afterFile.getPath())));
        messageBodyPart2.setFileName(afterFile.getPath());

        messageBodyPart3.setText(messageBody);

        _multipart.addBodyPart(messageBodyPart1);
        _multipart.addBodyPart(messageBodyPart2);
        _multipart.addBodyPart(messageBodyPart3);



        message.setContent(_multipart);

        return message;
    }


    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtpout.secureserver.net");
        properties.put("mail.smtp.port", "3535");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

     class SendMailTask extends AsyncTask<Message, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(getApplicationContext(), "Sending Message", Toast.LENGTH_SHORT).show();

            alertDialog = makeDialog();
            alertDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplicationContext(), "Message Sent Successfully", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
            clear();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public AlertDialog makeDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.progress, null);

        alertDialogBuilder.setView(v);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setCancelable(false);

        return alertDialog;
    }


}
