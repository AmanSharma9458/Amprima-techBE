//package com.amprima.tech.service;
//
//import com.amprima.tech.entity.Lead;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    @Value("${lead.notification.recipients}")
//    private List<String> notificationRecipients;
//
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//
//    public void sendLeadNotification(Lead lead) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setFrom(fromEmail);
//            helper.setTo(notificationRecipients.toArray(new String[0]));
//            helper.setSubject("🎯 New Lead: " + lead.getName() + " - " + lead.getServiceInterest());
//
//            String emailBody = buildLeadEmailTemplate(lead);
//            helper.setText(emailBody, true);
//
//            mailSender.send(message);
//            log.info("Lead notification email sent successfully for lead ID: {}", lead.getId());
//
//        } catch (MessagingException e) {
//            log.error("Failed to send lead notification email", e);
//            throw new RuntimeException("Failed to send email notification", e);
//        }
//    }
//
//    private String buildLeadEmailTemplate(Lead lead) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
//        String formattedDate = lead.getCreatedAt().format(formatter);
//
//        return String.format("""
//            <!DOCTYPE html>
//            <html>
//            <head>
//                <style>
//                    body { font-family: 'Arial', sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
//                    .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
//                    .header { background: linear-gradient(135deg, #0A1F44 0%%, #1E40AF 100%%); color: white; padding: 30px; text-align: center; }
//                    .header h1 { margin: 0; font-size: 24px; }
//                    .content { padding: 30px; }
//                    .lead-info { background: #F8FAFC; border-left: 4px solid #1E40AF; padding: 15px; margin: 15px 0; border-radius: 4px; }
//                    .lead-info h3 { margin: 0 0 10px 0; color: #0A1F44; font-size: 16px; }
//                    .lead-info p { margin: 5px 0; color: #475569; }
//                    .label { font-weight: bold; color: #1E293B; }
//                    .footer { background: #F1F5F9; padding: 20px; text-align: center; font-size: 12px; color: #64748B; }
//                    .cta-button { display: inline-block; background: #1E40AF; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; margin: 20px 0; }
//                </style>
//            </head>
//            <body>
//                <div class="container">
//                    <div class="header">
//                        <h1>🎯 New Lead Received!</h1>
//                        <p style="margin: 10px 0 0 0; opacity: 0.9;">Amprima Tech - Lead Management</p>
//                    </div>
//
//                    <div class="content">
//                        <p style="font-size: 16px; color: #1E293B;">A new potential client has submitted an inquiry through your website.</p>
//
//                        <div class="lead-info">
//                            <h3>📋 Contact Information</h3>
//                            <p><span class="label">Name:</span> %s</p>
//                            <p><span class="label">Phone:</span> <a href="tel:%s">%s</a></p>
//                            <p><span class="label">Email:</span> <a href="mailto:%s">%s</a></p>
//                            <p><span class="label">Company:</span> %s</p>
//                        </div>
//
//                        <div class="lead-info">
//                            <h3>🎯 Project Details</h3>
//                            <p><span class="label">Service Interest:</span> %s</p>
//                            <p><span class="label">Source:</span> %s</p>
//                            <p><span class="label">Received:</span> %s</p>
//                        </div>
//
//                        <div class="lead-info">
//                            <h3>💬 Message</h3>
//                            <p style="line-height: 1.6;">%s</p>
//                        </div>
//
//                        <div class="lead-info">
//                            <h3>ℹ️ Additional Information</h3>
//                            <p><span class="label">How they found us:</span> %s</p>
//                            <p><span class="label">IP Address:</span> %s</p>
//                            <p><span class="label">Lead ID:</span> #%d</p>
//                        </div>
//
//                        <center>
//                            <a href="https://wa.me/%s" class="cta-button">WhatsApp This Lead</a>
//                        </center>
//
//                        <p style="margin-top: 20px; padding: 15px; background: #FEF9C3; border-radius: 6px; color: #854D0E;">
//                            <strong>⏰ Action Required:</strong> Respond within 24 hours for best conversion rates. Quick follow-up increases closing probability by 70%%.
//                        </p>
//                    </div>
//
//                    <div class="footer">
//                        <p><strong>Amprima Tech</strong> | Near Gurudwara, Pilkhuwa</p>
//                        <p>Phone: +91 7017383153 | Email: amanpkw11@gmail.com</p>
//                        <p style="margin-top: 10px; opacity: 0.7;">This is an automated notification from your lead management system.</p>
//                    </div>
//                </div>
//            </body>
//            </html>
//            """,
//                lead.getName(),
//                lead.getPhone(), lead.getPhone(),
//                lead.getEmail(), lead.getEmail(),
//                lead.getCompanyName() != null ? lead.getCompanyName() : "Not provided",
//                lead.getServiceInterest() != null ? lead.getServiceInterest() : "General Inquiry",
//                lead.getSource(),
//                formattedDate,
//                lead.getMessage(),
//                lead.getHearAboutUs() != null ? lead.getHearAboutUs() : "Not specified",
//                lead.getIpAddress() != null ? lead.getIpAddress() : "Unknown",
//                lead.getId(),
//                lead.getPhone().replace("+", "").replace(" ", "")
//        );
//    }
//}