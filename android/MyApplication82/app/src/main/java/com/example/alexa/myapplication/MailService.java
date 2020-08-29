package com.example.alexa.myapplication;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by berenger on 27/04/18.
 */

public class MailService extends IntentService {
    public static final String MAIL = "mail content";
    Store store = null;

    public MailService(){
        super("Mail IntentService");
    }

    @Override
    public void onHandleIntent(Intent intent){
        Folder inbox = getMail();
        ArrayList<ArrayList> mailContent = getContent(inbox);
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> texts = new ArrayList<>();
        for (int i = 0; i<mailContent.size(); i++){
            subjects.add((String) mailContent.get(i).get(0));
            texts.add((String) mailContent.get(i).get(1));
        }
        sendMailContentToClient(subjects,texts);
    }

    public synchronized Folder getMail() {
        Properties props = new Properties();
        //IMAPS protocol
        props.setProperty("mail.store.protocol", "imaps");
        //Set host address
        props.setProperty("mail.imaps.host", "imaps.gmail.com");
        //Set specified port
        props.setProperty("mail.imaps.port", "993");
        //Using SSL
        props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imaps.socketFactory.fallback", "false");
        //Setting IMAP session
        Session imapSession = Session.getInstance(props);

        Message[] result = new Message[]{};
        Folder inbox = null;

        try {
            store = imapSession.getStore("imaps");
            //Connect to server by sending username = emaisel and password.
            store.connect("imap.gmail.com", "emaisel.pact21@gmail.com", "androidpact21");
            //Get all mails in Inbox Folder
            inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);

            inbox.close(false);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return inbox;
    }

    public ArrayList<ArrayList> getContent(Folder folder){
        ArrayList<ArrayList> content = new ArrayList<>();
        try {
            folder.open(Folder.READ_ONLY);
            int count = folder.getMessageCount();
            //Return result to array of message
            Message[] result = folder.getMessages();
            if (folder==null){
                System.out.println("dossier vide");
            }
            System.out.println(result);
            for (int i = 1; i <= count; i++ ) {
                Message message = folder.getMessage(i);
                String subject = message.getSubject();
                String text = (String) message.getContent();
                ArrayList<String> jzhe = new ArrayList<>();
                jzhe.add(subject);
                jzhe.add(text);
                content.add(jzhe);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally { //Should not forget to close all of it!
            try {
                folder.close(false);
                if (store != null && store.isConnected()) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private void sendMailContentToClient(ArrayList<String> subject, ArrayList<String> text){
        Intent intent = new Intent();
        intent.setAction(MAIL);
        intent.putStringArrayListExtra("subject",subject);
        intent.putStringArrayListExtra("text",text);
        System.out.println(subject);
        System.out.println(text);
        sendBroadcast(intent);
    }
}
