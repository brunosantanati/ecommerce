package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();
        var service = new KafkaService(EmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                //USANDO METHOD REFERENCE
                emailService::parse);

                //USANDO LAMBDA
                //c -> emailService.parse(c)); //doc sobre method reference: https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html

                //USANDO CLASSE ANONIMA
                /*new ConsumerFunction() {
                    @Override
                    public void consume(ConsumerRecord<String, String> c) {
                        emailService.parse(c);
                    }
                });*/
                service.run();
    }

    private void parse(ConsumerRecord<String, String> record){
        System.out.println("--------------------------------------------");
        System.out.println("Sending email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //ignoring
            e.printStackTrace();
        }
        System.out.println("Email sent");
    }
}
