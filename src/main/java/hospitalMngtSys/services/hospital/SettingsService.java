package hospitalMngtSys.services.hospital;

import hospitalMngtSys.entities.SettingsEntity;
import hospitalMngtSys.repositories.hospital.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.text.html.HTML;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class SettingsService {

    @Autowired
    SettingsRepository settingsRepository;

    public void sendmail(String email,String name,String recipent,String id,int hospitalID) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("obianujunwanja@gmail.com", "rclzddfdfudosslg");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("obianujunwanja@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("Email Confirmation");
        String message= "";
        if(recipent.equals("patient")){
            String url = "http://localhost:4200/confirm/patient/"+id+"/"+hospitalID;
            message = "Dear "+name+"," +
                    "<p>You have just registered for a hospital account. Please confirm your email by clicking this <a href='"+
                    url +"'>link</a>.</p>" +
                    "<p>If you have not done so, then please contact Oasis Management Company.";
        }else if(recipent.equals("hospital")){
            String url = "http://localhost:4200/confirm/hospital/"+hospitalID;
            message = "Dear "+name+"," +
                    "<p>You have just created an account. Please confirm your email by clicking this <a href='"+
                    url +"'>link</a>.</p>" +
                    "<p>If you have not done so, then please contact Oasis Management Company.";
        }else if(recipent.equals("staff")){
            String url = "http://localhost:4200/confirm/staff/"+id+"/"+hospitalID;
            message = "Dear "+name+"," +
                    "<p>You have just been registered for a staff account with "+ settingsRepository.findByHospitalID(hospitalID).getName()+". Please confirm your email by clicking this <a href='"+
                    url +"'>link</a>.</p>" +
                    "<p>If you are not a staff member, then please contact Oasis Management Company.";
        }else if(recipent.equals("lab")){
            String url = "http://localhost:4200/record/" + id;
            message = "Dear "+name+"," +
                    "<p>Your test results are out. View them by clicking this <a href='"+
                    url +"'>link</a>.</p>" +
                    "<p>If you have not done so, then please contact Oasis Management Company.";
        }else if(recipent.equals("forgotHosp")){
            String url = "http://localhost:4200/changePassword/hospital/" + hospitalID;
            message = "Dear "+name+"," +
                    "<p>Your have requested to change your password. Click this <a href='"+
                    url +"'>link</a> to proceed.</p>" +
                    "<p>If you have not done so, then please contact Oasis Management Company.";
        }else if(recipent.equals("forgotStaff")){
            String url = "http://localhost:4200/changePassword/staff/" + id+"/"+hospitalID;
            message = "Dear "+name+"," +
            "<p>Your have requested to change your password. Click this <a href='"+
                    url +"'>link</a> to proceed.</p>" +
                    "<p>If you have not done so, then please contact Oasis Management Company.";
        }else if(recipent.equals("forgotPat")){
            String url = "http://localhost:4200/changePassword/patient/" + id+"/"+hospitalID;
            message = "Dear "+name+","+
                    "<p>Your have requested to change your password. Click this <a href='"+
                    url +"'>link</a> to proceed.</p>" +
                    "<p>If you have not done so, then please contact Oasis Management Company.";
        }
        msg.setContent(message, "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Tutorials point email", "text/html");

        Transport.send(msg);
    }

        public byte[] compressBytes(byte[] data) throws NullPointerException{
        Deflater deflater= new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream=new ByteArrayOutputStream(data.length);
        byte[] buffer=new byte[1024];
        while (!deflater.finished()){
            int count = deflater.deflate(buffer);
            outputStream.write(buffer,0,count);
        }
        try{
            outputStream.close();
        }catch (IOException e){

        }
        return outputStream.toByteArray();
    }

    public byte[] decompressBytes(byte[] data) throws NullPointerException{
        Inflater inflater=new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()){
                int count = inflater.inflate(buffer);
                outputStream.write(buffer,0,count);
            }
            outputStream.close();
        }catch (IOException ioException){

        }catch (DataFormatException e){

        }
        return outputStream.toByteArray();
    }

    public boolean emailExists(String email){
        SettingsEntity settings = settingsRepository.findByEmail(email);
        if(settings != null){
            return true;
        }else{
            return false;
        }
    }

}
