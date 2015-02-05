package tronbox.cloplay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
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

public class ClientMailCheckActivity extends Activity {
    private static final String name = "A1 Garage";
    private static final String username = "system@a1garage.com";
    private static final String password = "a1system12";
    private EditText emailEdit;
    private EditText subjectEdit;
    private EditText messageEdit;
    File beforeFile, afterFile;
    private Multipart _multipart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _multipart = new MimeMultipart();

/*
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart*//*
*/
/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
*/

        emailEdit = (EditText) findViewById(R.id.email);
        subjectEdit = (EditText) findViewById(R.id.subject);
        messageEdit = (EditText) findViewById(R.id.message);
        Button sendButton = (Button) findViewById(R.id.send);

        storeImages();

        Log.w("FilePath", beforeFile.getPath());

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMail("system@a1garage.com", "dddd", "hello");

                /*
                String email = emailEdit.getText().toString();
                String subject = subjectEdit.getText().toString();
                String message = messageEdit.getText().toString();

                sendMail(email,subject,message);
*/

            }
        });

    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        Log.w("FilePath", beforeFile.getPath());

        try {
            Message message = createMessage(email, subject, messageBody, session, beforeFile.getPath());
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session, String filename) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("system@a1garage.com", "Nikhil Patil"));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress("patilnikhil9@gmail.com"));
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

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ClientMailCheckActivity.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
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

    public void storeImages()
    {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car2);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.car1);


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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            FileOutputStream out1 = new FileOutputStream(afterFile);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, out1);
            out1.flush();
            out1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

            /*Master.beforeImageUri = Uri.fromFile(beforeFile);
            Master.afterImageUri = Uri.fromFile(afterFile);*/


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

}
