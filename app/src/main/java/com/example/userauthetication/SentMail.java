package com.example.userauthetication;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mailjet.client.errors.MailjetException;
// import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class SentMail {

    public static double randnumber;
    public static  int ran = 0;

    public static class SendMailTask extends AsyncTask<Void, Void, Void> {
        private final String receiver;
        private final Context context;

        public SendMailTask(String receiver, Context context) {
            this.receiver = receiver;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                int number = 152222;
                // apikey is a 32 char length from jetMail
                //removing the apikey and apiSecret for security reasons
                //apiSecret is also a 32 char string
                //create a new account and paste the details here for apikey and apiSecret
                ClientOptions options = ClientOptions.builder()
                        .apiKey("")
                        .apiSecretKey("")
                        .build();

                MailjetClient client = new MailjetClient(options);

                MailjetRequest request = new MailjetRequest(Emailv31.resource)
                        .property(Emailv31.MESSAGES, new JSONArray()
                                .put(new JSONObject()
                                        .put(Emailv31.Message.FROM, new JSONObject()
                                                .put("Email", "christyalexgamer@gmail.com")
                                                .put("Name", "Me"))
                                        .put(Emailv31.Message.TO, new JSONArray()
                                                .put(new JSONObject()
                                                        .put("Email", receiver)
                                                        .put("Name", "You")))
                                        .put(Emailv31.Message.SUBJECT, "My first Mailjet Email!")
                                        .put(Emailv31.Message.TEXTPART, "Greetings from Mailjet!")
                                        .put(Emailv31.Message.HTMLPART, "<h3>Dear passenger 1, welcome to <a href=\"https://www.mailjet.com/\">Mailjet</a>!</h3><br />May the delivery force be with you!")
                                        .put(Emailv31.Message.HTMLPART, "<h2> your OTP is : " + getRandomNumber() + "</h2")));

                MailjetResponse response = client.post(request);

                // Handle the response if needed
                Log.d("TAG", "Request send to  mailjet server: " + response);

            } catch (MailjetException | JSONException e) {
                e.printStackTrace();
                // Handle the exception or propagate it to the caller
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Perform any UI updates after the background task completes
            super.onPostExecute(aVoid);
            // For example, show a Toast to indicate that the email was sent
            Toast.makeText(context, "Mail sent successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getRandomNumber()
    {
        int temp=0;

        while(String.valueOf(temp).length()!=6)
        {
            randnumber = Math.floor(Math.random() * 1000000);
            ran=(int)randnumber;
            temp=ran;
            Log.d("TAG","OTP temp =>"+String.valueOf(ran));

        }
        return String.valueOf(ran);

    }
}